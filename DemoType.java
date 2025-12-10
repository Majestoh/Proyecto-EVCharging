/**
 * Enumeración que define los tipos de escenarios de demostración preconfigurados
 * para la simulación. Cada tipo especifica el número de vehículos, estaciones
 * y cargadores que se crearán.
 *
 * @author Pablo Carrasco Caballero
 * @version 2025.11.25
 */
public enum DemoType
{
    /** Escenario NANO: 2 vehículos, 2 estaciones, 3 cargadores/estación. */
    NANO("DEMO NANO", 2, 2, 3),
    
    /** Escenario SIMPLE: 4 vehículos, 5 estaciones, 4 cargadores/estación. */
    SIMPLE ("DEMO SIMPLE", 4, 5, 4),
    
    /** Escenario MEDIO: 7 vehículos, 5 estaciones, 4 cargadores/estación. */
    MEDIUM ("DEMO MEDIO", 7, 5, 4),
    
    /** Escenario AVANZADO: 10 vehículos, 5 estaciones, 4 cargadores/estación. */
    ADVANCED ("DEMO AVANZADA", 10, 5, 4);
    
    // --- ATRIBUTOS PRIVADOS ---

    /** Nombre descriptivo del escenario. */
    private String nombre;
    
    /** Número de {@link ElectricVehicle}s a crear. */
    private int numVehiculosACrear;
    
    /** Número de {@link ChargingStation}s a crear. */
    private int numEstacionesACrear;
    
    /** Número de {@link Charger}s a crear por estación. */
    private int numCargadoresACrear;
    
    /**
     * Constructor para los tipos de demo.
     *
     * @param nombre Nombre descriptivo.
     * @param numVehiculos Número de vehículos.
     * @param numEstaciones Número de estaciones.
     * @param numCargadores Número de cargadores por estación.
     */
    DemoType(String nombre, int numVehiculos, int numEstaciones, int numCargadores){
        this.nombre = nombre;
        this.numVehiculosACrear = numVehiculos;
        this.numEstacionesACrear = numEstaciones;
        this.numCargadoresACrear = numCargadores;
    }
    
    // --- MÉTODOS PÚBLICOS DE CONSULTA ---
    
    /**
     * Devuelve el nombre descriptivo de la demo.
     * @return El nombre de la demo (ej. "DEMO SIMPLE").
     */
    public String getName(){
        return this.nombre;
    }
    
    /**
     * Devuelve el número de vehículos que se crearán en este escenario.
     * @return El número de vehículos.
     */
    public int getNumVehiclesToCreate(){
        return this.numVehiculosACrear;
    }
    
    /**
     * Devuelve el número de estaciones de carga que se crearán en este escenario.
     * @return El número de estaciones.
     */
    public int getNumStationsToCreate(){
        return this.numEstacionesACrear;
    }
    
    /**
     * Devuelve el número de cargadores que se crearán en CADA estación.
     * @return El número de cargadores por estación.
     */
    public int getNumChargersToCreate(){
        return this.numCargadoresACrear;
    }
}