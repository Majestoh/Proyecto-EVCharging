import java.util.List;

/**
 * Modela los elementos comunes de un EV que opera
 * dentro de la simulación, moviéndose hacia un destino y recargando
 * cuando es necesario.
 *
 * @author Pablo Carrasco Caballero
 * @version 2025
 */
public class ElectricVehicle {
    // --- Atributos ---
    private EVCompany compania;
    private String matricula;
    private String nombre;
    private Location localizacion;
    private Location localizacionDestinoFinal;
    private Location localizacionRecarga;
    private int capacidadBateria;
    private int nivelBateria;
    private int contadorInactividad;
    private int contadorRecargas;
    private float costeTotalRecargas;
    private int kwhTotalesCargados;
    private int turnoLlegada;
    private boolean haLlegadoAlDestino;
    private Location location;

    // --- Constructor ---

    /** Constructor de la clase ElectricVehicle.
     *
     * @param company La compañía del EV. No puede ser nulo.
     * @param location La {@link Location} inicial. No puede ser nulo.
     * @param targetLocation La {@link Location} destino FINAL. No puede ser nulo.
     * @param name El nombre del vehículo.
     * @param plate La matrícula del vehículo.
     * @param batteryCapacity La capacidad máxima de la batería.
     * @throws NullPointerException Si company, location, o targetLocation son nulos.
     */
    public ElectricVehicle(EVCompany company, Location location, Location targetLocation, String name, String plate, int batteryCapacity) {
        EVCompany compania = company;
        Location localizacionInicial = location;
        Location destinoFinal = targetLocation;
        String nombre = name;
        String matricula = plate;
        int capacidad = batteryCapacity;

        if (compania == null || localizacionInicial == null ||destinoFinal == null) {
            throw new NullPointerException("La compañía y/o las localizaciones no pueden ser nulas.");
        }

        this.compania = compania;
        this.localizacion = localizacionInicial;
        this.localizacionDestinoFinal = destinoFinal;
        this.nombre = nombre;
        this.matricula = matricula;
        this.capacidadBateria = capacidad;

        this.nivelBateria = this.capacidadBateria;
        this.contadorInactividad = 0;
        this.contadorRecargas = 0;
        this.costeTotalRecargas = 0.0f;
        this.kwhTotalesCargados = 0;

        this.localizacionRecarga = null;
        this.haLlegadoAlDestino = false;
        this.turnoLlegada = -1;
    }

    // --- Getters ---

    /**
     * Obtiene la localización actual del vehículo.
     *
     * @return Dónde está el vehículo actualmente.
     */
    public Location getLocation() {
        return this.localizacion;
    }

    /**
     * Obtiene la localización del DESTINO FINAL.
     *
     * @return Hacia dónde se dirige el vehículo en última instancia.
     */
    public Location getTargetLocation() {
        return this.localizacionDestinoFinal;
    }

    /**
     * Obtiene la localización de RECARGA temporal.
     *
     * @return La {@link Location} de la próxima {@link ChargingStation} a visitar, o
     * {@code null} si no hay recarga planeada.
     */
    public Location getRechargingLocation() {
        return this.localizacionRecarga;
    }

    /**
     * Obtiene el turno de la simulación en que el vehículo llegó
     * a su destino final.
     */
    public int getArrivingStep() {
        return this.turnoLlegada;
    }

    /**
     * Devuelve el contador de turnos que este vehículo ha estado inactivo.
     *
     * @return El número de pasos inactivo (idle).
     */
    public int getIdleCount() {
        return this.contadorInactividad;
    }

    /**
     * Devuelve el número de recargas realizadas.
     *
     * @return El contador total de recargas.
     */
    public int getChargesCount() {
        return this.contadorRecargas;
    }

    /**
     * Devuelve la matrícula del vehículo.
     *
     * @return El String de la matrícula (ej. "0CCC").
     */
    public String getMatricula() {
        return this.matricula;
    }

    /**
     * Devuelve el destino *actual* al que se dirige el vehículo.
     * Si hay una parada de recarga planificada, devuelve esa.
     * Si no, devuelve el destino final.
     *
     * @return La {@link Location} del objetivo inmediato.
     */
    private Location getDestinoActual() {
        if (this.localizacionRecarga != null) {
            return this.localizacionRecarga;
        } else {
            return this.localizacionDestinoFinal;
        }
    }

    // --- Setters ---

    /**
     * Establece el nivel de batería actual.
     * Lo usamos principalmente para las pruebas unitarias solicitadas.
     * @param nivel El nuevo nivel de batería en kWh.
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
    
    /**
     * Establece la localización actual del vehículo.
     *
     * @param location Dónde está. No puede ser nulo.
     * @throws NullPointerException Si la localización es nula.
     */
    public void setLocation(Location location) {
        if (location == null) {
            throw new NullPointerException("La localización de destino no puede ser nula.");
        }
        this.localizacion = location;
    }

    /**
     * Establece la localización del DESTINO FINAL.
     *
     * @param location Hacia dónde ir. No debe ser nulo.
     * @throws NullPointerException Si la localización es nula.
     */
    public void setTargetLocation(Location location) {
        if (location == null) {
            throw new NullPointerException("La localización de destino no puede ser nula.");
        }
        this.localizacionDestinoFinal = location;
    }

    // --- Lógica de batería y ruta ---

    /**
     * Comprueba si el nivel de batería actual es suficiente para cubrir
     * una distancia determinada.
     * Coste: 5 kWh por cada paso.
     *
     * @param distanceToTargetLocation La distancia a comprobar.
     * @return {@code true} si la batería es suficiente, {@code false} en caso contrario.
     */
    public boolean enoughBattery(int distanceToTargetLocation) {
        int distancia = distanceToTargetLocation;

        int costeEnergiaNecesario = distancia * EVCompany.COSTE_MOVIMIENTO_KWH;

        return this.nivelBateria >= costeEnergiaNecesario;
    }

    /**
     * Reduce el nivel de batería por el coste de un solo paso de movimiento.
     * Asegura que la batería no baje de cero.
     */
    public void reduceBatteryLevel() {
        this.nivelBateria -= EVCompany.COSTE_MOVIMIENTO_KWH;

        if (this.nivelBateria < 0) {
            this.nivelBateria = 0;
        }
    }

    /**
     * Calcula la ruta óptima para el vehículo.
     * Si no hay batería suficiente para el destino final, intenta encontrar
     * una {@link ChargingStation} intermedia y la establece como {@code localizacionRecarga}.
     */
    public void calculateRoute() {
        int distanciaAlDestinoFinal = this.location.distance(this.localizacionDestinoFinal);

        if (enoughBattery(distanciaAlDestinoFinal)) {
            this.localizacionRecarga = null;
        } else {
            this.calculateRechargingPosition();
        }
    }

    /**
     * Busca la estación de recarga óptima para una parada intermedia.
     * ÓPTIMA = aquella que minimiza (dist(actual, estacion) + dist(estacion, destino_final))
     * CONDICIÓN = el vehículo DEBE tener batería para llegar a la estación.
     */
    public void calculateRechargingPosition() {
        List<ChargingStation> estaciones = this.compania.getCityStations();

        ChargingStation mejorEstacion = null;
        int minimaDistanciaTotal = Integer.MAX_VALUE;

        for (ChargingStation estacion : estaciones) {
            Location locEstacion = estacion.getLocation();

            // --- Criterio 1. ¿Podemos llegar a la estación?
            int distActualAEstacion = this.location.distance(locEstacion);

            if (enoughBattery(distActualAEstacion)) {

                // --- Criterio 2. Si puedo llegar, ¿minimiza la ruta total?
                int distEstacionADestinoFinal = locEstacion.distance(this.localizacionDestinoFinal);

                int distanciaTotal = distActualAEstacion + distEstacionADestinoFinal;

                if (distanciaTotal < minimaDistanciaTotal) {
                    minimaDistanciaTotal = distanciaTotal;
                    mejorEstacion = estacion;
                }
            }
        }

        // --- Asignamos la ruta ---
        if (mejorEstacion != null) {
            this.localizacionRecarga = mejorEstacion.getLocation();
        } else {
            this.localizacionRecarga = null;
        }
    }

    /**
     * Comprueba si el vehículo tiene una parada planificada.
     *
     * @return true si localizacionRecarga no es nula.
     */
    public boolean hasRechargingLocation() {
        return this.localizacionRecarga != null;
    }

    /**
     * Calcula la distancia desde la pos. actual hasta el destino inmediato.
     *
     * @return La distancia (Chebyshev) al objetivo actual.
     */
    public int distanceToTheTargetLocation() {
        Location destinoActual = getDestinoActual();
        return this.location.distance(destinoActual);
    }

    /**
     * Incrementa el contador de pasos que este vehículo ha estado inactivo.
     */
    public void incrementIdleCount() {
        this.contadorInactividad++;
    }

    /**
     * Incrementa el contador de recargas.
     */
    public void incrementCharges() {
        this.contadorRecargas++;
    }

    /**
     * Añade un coste a la suma total de costes de recarga.
     * @param cost El coste de la última recarga.
     */
    public void incrementChargesCost(float cost) {
        float coste = cost;
        this.costeTotalRecargas += coste;
    }

    // --- Acciones principales SIMULACIÓN ---

    /**
     * Simula la recarga del vehículo CUANDO LLEGA a la estación.
     *
     * @param step El turno actual de la simulación.
     * @return {@code true} si la redcarga fue exitosa, {@code false} si no.
     */
    public void recharge(int step) {
        ChargingStation estacion = this.compania.getChargingStation(this.location);
        Charger cargador = estacion.getFreeCharger();

        if (cargador != null) {
            cargador.setEstaLibre(false);

            int kwhNecesarios = this.capacidadBateria - this.nivelBateria;

            float costeRecarga = cargador.recharge(this, kwhNecesarios);

            this.incrementCharges();
            this.incrementChargesCost(costeRecarga);
            this.kwhTotalesCargados += kwhNecesarios;

            this.nivelBateria = this.capacidadBateria;

            System.out.println(String.format(java.util.Locale.US, "(step: %d - ElectricVehicle: %s recharges: %dkwh at charges: %s with cost: %.1f€********)",
                    step,
                    this.matricula,
                    kwhNecesarios,
                    cargador.getId(),
                    costeRecarga));

            this.localizacionRecarga = null;
            this.calculateRoute();

            cargador.setEstaLibre(true);
        }
    }

    /**
     * Realiza una acción en la simulación.
     * Mueve el vehículo un paso hacia su destino
     * o gestiona la llegada/recarga si ya está en el destino.
     *
     * @param step El turno actual de la simulación.
     */
    public void act(int step) {
        if (this.haLlegadoAlDestino) {
            this.incrementIdleCount();
            return;
        }

        Location destinoActual = getDestinoActual();

        if (this.location.equals(destinoActual)) {
            if (this.location.equals(this.localizacionDestinoFinal)) {
                this.haLlegadoAlDestino = true;
                this.turnoLlegada = step;

                System.out.println(String.format("(step: %d - ElectricVehicle: %s at target destination ********)",
                        step,
                        this.matricula));

                this.incrementIdleCount();;
            }
            else if (this.location.equals(this.localizacionRecarga)) {
                this.recharge(step);
            }
        }

        else {
            Location siguientePosicion = this.location.nextLocation(destinoActual);

            this.setLocation(siguientePosicion);

            this.reduceBatteryLevel();
        }
    }

    // --- Métodos de info. ---

    /**
     * Devuelve una representación detallada de un vehículo.
     * Usado para la salida por consola en cada paso.
     *
     * @return Un String formateado con todos los datos del vehículo.
     */
    @Override
    public String toString() {
        String nombre = this.nombre;
        String matricula = this.matricula;
        String capBat = this.capacidadBateria + "kwh";
        String nivBat = this.nivelBateria + "kwh";
        int numRecargas = this.contadorRecargas;
        float costeRecargas = this.costeTotalRecargas;
        int inactividad = this.contadorInactividad;
        String locActual = this.localizacion.toString();

        String locRecargaStr = (this.localizacionRecarga != null) ? this.localizacionRecarga.toString() : "";
        String locFinalStr = this.localizacionDestinoFinal.toString();

        String rutaStr = (locRecargaStr.isEmpty()) ? locFinalStr : (locRecargaStr + ", " + locFinalStr);

        return String.format(java.util.Locale.US, "ElectricVehicle: %s, %s, %s, %s, %d, %.1f€, %d, %s, %s",
                nombre,
                matricula,
                capBat,
                nivBat,
                numRecargas,
                costeRecargas,
                inactividad,
                locActual,
                rutaStr
        );
    }

    /**
     * Genera una cadena con los detalles del vehículo, prefijada con el
     * número de paso actual.
     *
     * @param step El paso actual.
     * @return Una cadena formateada para el log de la simulación.
     */
    public String getStepInfo(int step) {
        return String.format("(step: %d - %s)",
                step,
                this.toString());
    }

    /**
     * Genera una cadena del estado inicial o final del vehículo para
     * los resúmenes.
     *
     * @return La salida de {@link #toString()} envuelta en paréntesis.
     */
    public String getInitialFinalInfo() {
        return String.format("(%s)",
                this.toString());
    }
}