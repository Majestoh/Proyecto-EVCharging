import java.util.*;

/**
 * Modela la compañía de vehículos eléctricos.
 * <p>
 * Aplica el patrón <b>Singleton</b> para garantizar una única instancia.
 * Gestiona la flota de vehículos, las estaciones y el registro centralizado
 * de notificaciones de carga.
 * </p>
 * 
 * @author Pablo Carrasco Caballero
 * @version 10.12.2025
 */
public class EVCompany {
    
    // --- Patrón SINGLETON ---
    private static EVCompany instance;
    
    // --- Atributos ---
    public static final int COSTE_MOVIMIENTO_KWH = 5;
    
    private String nombre;
    private List<ElectricVehicle> vehiculosSuscritos;
    private List<ChargingStation> estaciones;
    
    /**
     * Registro de notificaciones de carga.
     * CLAVE: Charger (ordenados por ID).
     * VALOR: set de ElectricVehicle (ordenados por inserción, sin duplicados).
     */
    private Map<Charger, Set<ElectricVehicle>> registroCargas;
    
    // --- Constructor privado (SINGLETON) ---
    private EVCompany() {
        this.nombre = "Compania EVCharging Caceres";
        this.vehiculosSuscritos = new ArrayList<>();
        this.estaciones = new ArrayList<>();
        
        // TreeMap con un Comparator para ordenar los cargadores por ID.
        this.registroCargas = new TreeMap<>((c1, c2) -> c1.getId().compareTo(c2.getId()));
    }
    
    /**
     * Devuelve la única instancia de la compañía (SINGLETON).
     * @return La instancia de EVCompany.
     */
    public static synchronized EVCompany getInstance() {
        if (instance == null) {
            instance = new EVCompany();
        }
        return instance;
    }
    
    // --- Métodos de gestión (PÚBLICOS) ---
    public String getName() {
        return this.nombre;
    }
    
    public List<ElectricVehicle> getVehicles() {
        return Collections.unmodifiableList(this.vehiculosSuscritos);
    }
    
    public void addElectricVehicle(ElectricVehicle vehicle) {
        if (vehicle != null) {
            this.vehiculosSuscritos.add(vehicle);
        }
    }
    
    public void addChargingStation(ChargingStation station) {
        if (station != null) {
            this.estaciones.add(station);
        }
    }
    
    /**
     * Busca un estación por su ID. 
     * Implementamos aquí un uso de Iterator.
     */
    public ChargingStation getChargingStation(String id) {
        Iterator<ChargingStation> it = this.estaciones.iterator();
        while (it.hasNext()) {
            ChargingStation estacion = it.next();
            if (estacion.getId().equals(id)) {
                return estacion;
            }
        }
        
        return null;
    }
    
    /**
     * Busca una estación por su localización.
     * Implementamos aquí el otro uso de Iterator.
     */
    public ChargingStation getChargingStation(Location location) {
        Iterator<ChargingStation> it = this.estaciones.iterator();
        while (it.hasNext()) {
            ChargingStation estacion = it.next();
            if (estacion.getLocation().equals(location)) {
                return estacion;
            }
        }
        
        return null;
    }
    
    public List<ChargingStation> getCityStations() {
        return Collections.unmodifiableList(this.estaciones);
    }
    
    public int getNumberOfStations() {
        return this.estaciones.size();
    }
    
    /**
     * Reinicia el estado de la compañía.
     */
    public void reset() {
        this.vehiculosSuscritos.clear();
        this.estaciones.clear();
        this.registroCargas.clear();
    }
    
    // --- Gestión de notificaciones ---
    
    /**
     * Registra una notificación de carga de un vehículo.
     * Solo se guardan si el vehículo notifica (STANDARD, PREMIUM, VTC)
     * 
     * @param vehiculo El vehículo que ha cargado.
     * @param cargador El cargador utilizado.
     */
    public void notificarRecarga(ElectricVehicle vehiculo, Charger cargador) {
        // Si el cargador no está en el mapa, lo añadimos con un nuevo Set vacío.
        if (!registroCargas.containsKey(cargador)) {
            // LinkedHashSet mantiene el orden de inserción cronológico.
            registroCargas.put(cargador, new LinkedHashSet<>());
        }
        
        // Añadimos el vehículo al set del cargador.
        // Al ser un Set, si el vehículo ya estaba, NO se duplica.
        registroCargas.get(cargador).add(vehiculo);
    }
    
    /**
     * Muestra la información de la compañía (el registro de cargas).
     * Seguimos el formato mostrado en el ANEXO I.
     */
    public void showCompanyInfo() {
        System.out.println("(--------------)");
        System.out.println("( Company Info )");
        System.out.println("(--------------)");
        System.out.println("(EVCompany: " + this.nombre + ")");
        
        // Iteramos sobre el mapa.
        for (Map.Entry<Charger, Set<ElectricVehicle>> entrada : registroCargas.entrySet()) {
            Charger cargador = entrada.getKey();
            Set<ElectricVehicle> vehiculos = entrada.getValue();
            
            // Si el cargador NO tiene notificaciones, NO se muestra.
            if (!vehiculos.isEmpty()) {
                // MOSTRAR CARGADOR
                System.out.println(cargador.toString());
                
                // MOSTRAR VEHÍCULOS
                for (ElectricVehicle ev : vehiculos) {
                    // Usamos toString() o getInitialFinalInfo() según convenga.
                    // Aquí utilizaremos la segunda opción.
                    System.out.println(ev.getInitialFinalInfo());
                }
            }
        }
    }
}