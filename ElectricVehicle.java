import java.util.List;

/**
 * Clase abstracta que modela los elementos comunes de cualquier Vehículo Eléctrico (VE).
 * <p>
 * Implementa la lógica básica de movimiento y gestión de batería, pero delega
 * en las subclases las decisiones específicas de negocio (como la elección
 * de cargador o la política de notificaciones) mediante métodos abstractos.
 * </p>
 * 
 * @author Pablo Carrasco Caballero
 * @version 2025.11.25
 */
public abstract class ElectricVehicle {
    // --- Atributos Comunes ---
    protected EVCompany compania;
    protected String matricula;
    protected String nombre;
    
    protected Location localizacion;
    protected Location localizacionDestinoFinal;
    protected Location localizacionRecarga;
    
    protected int capacidadBateria;
    protected int nivelBateria;
    
    protected int contadorInactividad;
    protected int contadorRecargas;
    protected float costeTotalRecargas;
    protected int kwhTotalesCargados;
    
    protected int turnoLlegada;
    protected boolean haLlegadoAlDestino;
    
    // --- Constructor ---
    public ElectricVehicle(EVCompany company, Location location, Location targetLocation, String name, String plate, int batteryCapacity) {
        if(company == null || location == null || targetLocation == null) {
            throw new NullPointerException("La compañía y las localizaciones no pueden ser nulas.");
        }
        this.compania = company;
        this.localizacion = location;
        this.localizacionDestinoFinal = targetLocation;
        this.nombre = name;
        this.matricula = plate;
        this.capacidadBateria = batteryCapacity;
        
        // Inicialización de estado
        this.nivelBateria = this.capacidadBateria;
        this.contadorInactividad = 0;
        this.contadorRecargas = 0;
        this.costeTotalRecargas = 0.0f;
        this.kwhTotalesCargados = 0;
        this.localizacionRecarga = null;
        this.haLlegadoAlDestino = false;
        this.turnoLlegada = -1;
    }
    
    // -- Métodos abstractos (polimorfismo puro) ---
    
    /**
     * Devuelve el tipo de vehículo (enum) para comprobar su compatibilidad con cargadores.
     */
    public abstract VehicleTier getTipo();
    
    /**
     * Define la estrategia para elegir la mejor estación de recarga.
     * Cada hijo implementará su propia lógica (más cercana, más barata, más rápida...).
     * 
     * @return La estación elegida, o null si no hay ninguna válida.
     */
    protected abstract ChargingStation seleccionarMejorEstacion(List<ChargingStation> estaciones);
    
    /**
     * Define si el vehículo debe notificar a la compañía tras recargar.
     * 
     * @param cargador El cargador donde se ha realizado la carga.
     */
    protected abstract void notificarRecargaACompania(Charger cargador);
    
    // --- Lógica de movimiento y simulación ---
    
    /**
     * Realiza una acción en la simulación (un turno).
     * <p>
     * La lógica base es moverse un paso y comprobar si se ha llegado.
     * Nota: PriorityEV sobreescribirá este método para moverse dos veces.
     * </p>
     */
    public void act(int step) {
        if(haLlegadoAlDestino) {
            incrementIdleCount();
            return;
        }
        
        // Intentar moverse un paso.
        boolean seHaMovido = intentarMoverse();
        
        if(!seHaMovido) {
            return;
        }
        
        // Comprobar dónde estamos.
        comprobarLlegada(step);
    }
    
    /**
     * Ejecuta la lógica de movimiento de un solo paso.
     * 
     * @return true si el vehículo se movió, false si se quedó quieto.
     */
    protected boolean intentarMoverse() {
        // 1. Verificar batería mínima (1 paso = 5 kwh)
        if(!enoughBattery(1)) {
            return false;
        }
        
        // 2. Obtener destino inmediato.
        Location destinoActual = getDestinoActual();
        
        // 3. Si ya estamos ahí, no nos movemos.
        if(localizacion.equals(destinoActual)) {
            // Caso especial: atascado en estación sin poder ir al destino final.
            if(localizacionRecarga == null && !enoughBattery(localizacion.distance(localizacionDestinoFinal))) {
                incrementIdleCount();
            }
            return false;
        }
        
        // 4. Moverse
        Location siguiente = localizacion.nextLocation(destinoActual);
        setLocation(siguiente);
        reduceBatteryLevel();
        return true;
    }
    
    /**
     * Comprueba si la ubicación actual es un destino y actúa en consecuencia.
     */
    protected void comprobarLlegada(int step) {
        // Caso A. Destino final
        if(localizacion.equals(localizacionDestinoFinal)) {
            haLlegadoAlDestino = true;
            turnoLlegada = step;
            System.out.println(String.format("(step: %d - %s at target destination ********)",
                step, this.getStepPrefix()));
        }
        // Caso B. Estación de recarga
        else if(localizacion.equals(localizacionRecarga)) {
            recharge(step);
        }
    }
    
    // --- Lógica de recarga ---
    public void recharge(int step) {
        ChargingStation estacion = compania.getChargingStation(localizacion);
        if(estacion == null) {
            return;
        }
        
        // Aquí usamos el polimorfismo implícito: el cargador comprobará si son compatibles.
        Charger cargador = estacion.getFreeCharger(); 
        
        if(cargador != null) {
            cargador.setEstaLibre(false);
            int kwhNecesarios = capacidadBateria - nivelBateria;
            
            if(kwhNecesarios > 0) {
                float coste = cargador.recharge(this, kwhNecesarios);
                
                if(coste >= 0) {
                    incrementCharges();
                    incrementChargesCost(coste);
                    kwhTotalesCargados += kwhNecesarios;
                    nivelBateria = capacidadBateria;
                    
                    notificarRecargaACompania(cargador);
                    
                    System.out.println(String.format(java.util.Locale.US,
                        "(step: %d - %s recharges: %dkwh at %s with cost: %.2f% ********)",
                        step, this.getStepPrefix(), kwhNecesarios, cargador.getClass().getSimpleName() + ": " + cargador.getId(), coste));
                }
            }
            
            localizacionRecarga = null;
            calculateRoute();
            cargador.setEstaLibre(true);
        }
    }
    
    // --- Lógica de ruta ---
    public void calculateRoute() {
        int distanciaDestino = localizacion.distance(localizacionDestinoFinal);
        
        if(enoughBattery(distanciaDestino)) {
            localizacionRecarga = null;
        } else {
            ChargingStation mejorEstacion = seleccionarMejorEstacion(compania.getCityStations());
            
            if(mejorEstacion != null) {
                localizacionRecarga = mejorEstacion.getLocation();
            } else {
                localizacionRecarga = null;
            }
        }
    }
    
    // --- Métodos auxiliares ---
    protected Location getDestinoActual() {
        return (localizacionRecarga != null) ? localizacionRecarga : localizacionDestinoFinal;
    }
    
    public boolean enoughBattery(int distancia) {
        return nivelBateria >= (distancia * EVCompany.COSTE_MOVIMIENTO_KWH);
    }
    
    public void reduceBatteryLevel() {
        nivelBateria = Math.max(0, nivelBateria - EVCompany.COSTE_MOVIMIENTO_KWH);
    }
    
    // --- Getters, setters y tostring ---
    public Location getLocation() { return localizacion; }
    
    public void setLocation(Location location) {
        if (location == null) {
            throw new NullPointerException("La localización no puede ser nula.");
        }
        this.localizacion = location;
    }
    
    /**
     * Devuelve el número total de recargas realizadas.
     */
    public int getChargesCount() {
        return this.contadorRecargas;
    }
    
    /**
     * Establece manualmente el nivel de la batería.
     * Exclusivo para facilitar las pruebas.
     * 
     * @param nivel El nuevo nivel de batería.
     */
    public void setNivelBateria(int nivel) {
        if (nivel >= 0 && nivel <= this.capacidadBateria) {
            this.nivelBateria = nivel;
        } else if (nivel > this.capacidadBateria) {
            this.nivelBateria = this.capacidadBateria;
        } else {
            this.nivelBateria = 0;
        }
    }
    
    public String getMatricula() { return matricula; }
    public int getArrivingStep() { return turnoLlegada; }
    public void incrementIdleCount() { contadorInactividad++; }
    public void incrementCharges() { contadorRecargas++; }
    public void incrementChargesCost(float c) { costeTotalRecargas += c; }
    
    protected String getStepPrefix() {
        return this.getClass().getSimpleName() + ": " + matricula;
    }
    
    @Override
    public String toString() {
        String locRecarga = (localizacionRecarga != null) ? localizacionRecarga.toString(): "";
        String ruta = (locRecarga.isEmpty()) ? localizacionDestinoFinal.toString() : locRecarga + ", " + localizacionDestinoFinal;
        
        return String.format(java.util.Locale.US,
            "%s: %s, %s, %dkwh, %d, %.2f€, %d, %s, %s",
            this.getClass().getSimpleName(), 
            nombre, matricula, capacidadBateria, nivelBateria,
            contadorRecargas, costeTotalRecargas, contadorInactividad,
            localizacion, ruta
        );
    }
    
    public String getInitialFinalInfo() {
        return "(" + this.toString() + ")";
    }
    
    public String getStepInfo(int step) {
        return String.format("(step: %d - %s)", step, this.toString());
    }
}