/**
 * Enumeración que define los escenarios de la simulación.
 * Define la cantidad de vehículos, estaciones y cargadores para
 * cada nivel de la demo (SIMPLE, MEDIUM, ADVANCED).
 *
 * @author Pablo Carrasco Caballero
 * @version 2025
 */
public enum DemoType {

    // --- Valores de la Enumeración ---

    /** Escenario simple: 2 vehículos, 4 estaciones, 4 cargadores/estación */
    SIMPLE(2, 4, 4),

    /** Escenario medio: 5 vehículos, 4 estaciones, 4 cargadores/estación */
    MEDIUM(5, 4, 4),

    /** Escenario avanzado: 8 vehículos, 4 estaciones, 4 cargadores/estación */
    ADVANCED(8, 4, 4);

    // --- Atributos de cada valor ---

    private final int numVehiculos;
    private final int numEstaciones;
    private final int numCargadores;

    /**
     * Constructor privado para cada valor de la enumeración.
     * @param vehiculos Número de vehículos a crear.
     * @param estaciones Número de estaciones a crear.
     * @param cargadores Número de cargadores por estación.
     */
    DemoType(int vehiculos, int estaciones, int cargadores) {
        this.numVehiculos = vehiculos;
        this.numEstaciones = estaciones;
        this.numCargadores = cargadores;
    }

    // --- Métodos de consulta ---

    /**
     * @return El número de vehículos a crear para este escenario.
     */
    public int getNumVehiclesToCreate() {
        return this.numVehiculos;
    }

    /**
     * @return El número de estaciones a crear para este escenario.
     */
    public int getNumStationsToCreate() {
        return this.numEstaciones;
    }

    /**
     * @return El número de cargadores a crear POR ESTACIÓN.
     */
    public int getNumChargersToCreate() {
        return this.numCargadores;
    }
}