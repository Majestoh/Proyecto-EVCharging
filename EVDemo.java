import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Proporciona una demo simple y un entorno de simulación para el
 * modelo de Vehículos Eléctricos (VE) y Estaciones de Carga.
 * Configura el entorno y ejecuta la simulación durante un número fijo de pasos.
 *
 * Escenarios:
 * <ul>
 *     <li> Demo SIMPLE (demo=DemoType.SIMPLE): se crean 2 vehículos.</li>
 *     <li> Demo MEDIUM (demo=DemoType.MEDIUM): se crean 5 vehículos.</li>
 *     <li> Demo ADVANCED (demo=DemoType.ADVANCED): se crean 8 vehículos.</li>
 * </ul>
 *
 * @author Pablo Carrasco Caballero
 * @version 2025
 */
public class EVDemo {
    public static final int MAXX = 20;
    public static final int MAXY = 20;

    public static final int MAXSTEPS = 50;

    private EVCompany compania;

    /**
     * Los vehículos de la simulación son los vehículos eléctricos de la compañía.
     * @see ElectricVehicle
     */
    private List<ElectricVehicle> vehiculos;

    /**
     * Las estaciones de carga disponibles en la ciudad.
     * @see ChargingStation
     */
    private List<ChargingStation> estaciones;

    /**
     * Constante para seleccionar el escenario de la demo.
     * Utiliza la enumeración {@link DemoType}.
     */
    private static final DemoType DEMO = DemoType.ADVANCED;

    /**
     * Constructor para objetos de la clase EVDemo.
     * Inicializa la compañía y las listas de vehículos y estaciones.
     * Finalmente, llama a {@code reset()} para crear la configuración inicial.
     */
    public EVDemo() {
        this.compania = new EVCompany();

        this.vehiculos = new ArrayList<>();
        this.estaciones = new ArrayList<>();

        reset();
    }

    /**
     * Ejecuta la demo durante un número fijo de pasos (MAXSTEPS).
     * En cada paso, todos los vehículos realizan su acción.
     * Al finalizar, muestra la información final.
     */
    public void run() {
        for(int step = 0; step < MAXSTEPS; step++) {
            step(step);
        }

        showFinalInfo();
    }

    /**
     * Ejecuta UN paso de la demo, pidiendo a todos los vehículos que actúen.
     * La actuación se hace en orden creciente de matrícula.
     * @param step El número de paso o turno actual de la simulación.
     */
    public void step(int step) {
        for (ElectricVehicle vehiculo : this.vehiculos) {
            vehiculo.act(step);
        }

        for (ElectricVehicle vehiculo : this.vehiculos) {
            System.out.println(vehiculo.getStepInfo(step));
        }
    }

    /**
     * Reinicia la demo a su punto de partida.
     * Limpia los vehículos y estaciones existentes, resetea la compañía
     * y vuelve a crear la configuración inicial.
     */
    public void reset() {
        this.vehiculos.clear();
        this.estaciones.clear();

        this.compania.reset();

        createElectricVehicles();
        createStations();
        createChargers();
        configureRoutes();
        showInitialInfo();
    }

    /**
     * Crea los {@link ElectricVehicle}s según el escenario DEMO,
     * les asigna localizaciones de inicio/destino y los añade a la compañía.
     * Al final, ordena la lista de vehículos por matrícula.
     */
    private void createElectricVehicles() {
        Location [] locations = {new Location(10, 13), new Location(8, 4), new Location(8, 4), new Location(5, 10), new Location(1, 1), new Location(2, 2), new Location (11, 13), new Location(14, 16)};
        Location [] targetLocations = {new Location(1, 1), new Location(19, 19), new Location(12, 17), new Location(4, 4), new Location(1, 10), new Location(5, 5), new Location(8, 7), new Location(19, 19)};

        for (int i = 0; i < DEMO.getNumVehiclesToCreate(); i++) {
            ElectricVehicle ev = new ElectricVehicle(compania, locations[i], targetLocations[i], ("EV"+i), (i+"CCC"), (i+1)*15);

            this.vehiculos.add(ev);

            this.compania.addElectricVehicle(ev);
        }

        this.vehiculos.sort( (v1, v2) ->
                v1.getMatricula().compareTo(v2.getMatricula())
        );
    }

    /**
     * Crea las {@link ChargingStation}s predefinidas y las añade a la compañía.
     * Al final, ordena la lista de estaciones por ID.
     */
    private void createStations() {
        // Localizaciones de las 4 estaciones
        Location [] locations = {new Location(10,5), new Location(10,11), new Location(14,16), new Location(8,4)};

        // Creamos el número de estaciones que indique la DEMO (siempre 4)
        for (int i = 0; i < DEMO.getNumStationsToCreate(); i++){
            // Creamos la estación
            ChargingStation nuevaEstacion = new ChargingStation("Cáceres", "CC0" + i, locations[i]);

            // La añadimos a nuestra lista de demo
            this.estaciones.add(nuevaEstacion);

            // La AÑADIMOS a la compañía (requisito fuente 168)
            this.compania.addChargingStation(nuevaEstacion);
        }

        // ORDENAMOS la lista de estaciones por ID (requisito fuente 162)
        // Usamos el comparador que ya hemos creado
        this.estaciones.sort( (est1, est2) ->
                est1.getId().compareTo(est2.getId())
        );
    }

    /**
     * Crea 4 {@link Charger} (cargadores) para CADA {@link ChargingStation}.
     * El orden de los cargadores se gestiona automáticamente dentro de
     * la clase ChargingStation (en su método 'addCharger').
     * (fuente 169, 174)
     */
    private void createChargers() {

        // Recorremos CADA estación que hemos creado
        for (ChargingStation station : this.estaciones){

            // Creamos el número de cargadores que indique la DEMO (siempre 4)
            for (int i = 0; i < DEMO.getNumChargersToCreate(); i++){
                // (ID, Velocidad(kwh), Tarifa(€/kwh))
                Charger nuevoCargador = new Charger(station.getId() + "_00" + i, ((i+1)*20), ((i+1)*0.20f));

                // Añadimos el cargador a la estación
                station.addCharger(nuevoCargador);
            }
            // NOTA: La ordenación de los cargadores (fuente 174)
            // se realiza DENTRO del método 'station.addCharger()',
            // tal como lo implementamos en ChargingStation.java.
            // No es necesario hacer nada más aquí.
        }
    }


    /**
     * Pide a cada {@link ElectricVehicle} que calcule su ruta inicial.
     * Esto determina si necesitan una parada de recarga intermedia
     * antes de empezar a moverse.
     */
    private void configureRoutes() {
        // Recorremos todos los vehículos
        for (ElectricVehicle vehiculo : this.vehiculos) {
            // Cada vehículo calcula si necesita ir a una estación
            // o puede ir directo a su destino final.
            vehiculo.calculateRoute();
        }
    }

    /**
     * Muestra la información INICIAL de la simulación por consola.
     * Sigue el formato del Anexo I (fuente 170-175, 297).
     * Muestra la compañía, los vehículos (ordenados por matrícula)
     * y las estaciones (ordenadas por ID) con sus cargadores (ordenados por criterios).
     */
    private void showInitialInfo() {
        System.out.println("( "+compania.getName()+" )");
        System.out.println("(-------------------)");
        System.out.println("( Electric Vehicles )");
        System.out.println("(-------------------)");

        // Recorremos los vehículos (ya están ordenados por matrícula, fuente 172)
        for (ElectricVehicle vehiculo : this.vehiculos) {
            System.out.println(vehiculo.getInitialFinalInfo());
        }

        System.out.println("(-------------------)");
        System.out.println("( Charging Stations )");
        System.out.println("(-------------------)");

        // Recorremos las estaciones (ya están ordenadas por ID, fuente 173)
        for (ChargingStation estacion : this.estaciones) {
            // Imprimimos la línea de la estación
            System.out.println(estacion.toString());

            // Recorremos sus cargadores (ya están ordenados por la estación, fuente 174)
            for (Charger cargador : estacion.getChargers()) {
                // Imprimimos la línea del cargador
                System.out.println(cargador.toString());
            }
        }

        System.out.println("(------------------)");
        System.out.println("( Simulation start )");
        System.out.println("(------------------)");
    }

    /**
     * Muestra la información FINAL de la simulación por consola.
     * Sigue el formato del Anexo I (fuente 185-188, 485).
     * Muestra los vehículos (ordenados por turno de llegada)
     * y las estaciones (ordenadas por nº de recargas) con sus cargadores
     * y los vehículos que recargaron en ellos.
     */
    private void showFinalInfo() {

        System.out.println("(-------------------)");
        System.out.println("( Final information )");
        System.out.println("(-------------------)");

        System.out.println("(-------------------)");
        System.out.println("( Electric Vehicles )");
        System.out.println("(-------------------)");

        // ORDENAMOS los vehículos por turno de llegada (requisito fuente 186)
        // Usamos una expresión Lambda para el comparador (Extra, fuente 245)
        this.vehiculos.sort( (v1, v2) -> {
            // "Si un vehículo no ha llegado, es el último" (fuente 524)
            // Asignamos un valor "infinito" si getArrivingStep() es -1
            int turno1 = (v1.getArrivingStep() == -1) ? Integer.MAX_VALUE : v1.getArrivingStep();
            int turno2 = (v2.getArrivingStep() == -1) ? Integer.MAX_VALUE : v2.getArrivingStep();

            // Criterio 1: Ordenar por turno de llegada
            if (turno1 != turno2) {
                return Integer.compare(turno1, turno2); // Orden creciente
            }

            // Desempate: Ordenar por matrícula (creciente) (fuente 186)
            return v1.getMatricula().compareTo(v2.getMatricula());
        });

        // Imprimimos los vehículos ya ordenados
        for (ElectricVehicle vehiculo : this.vehiculos) {
            System.out.println(vehiculo.getInitialFinalInfo());
        }

        System.out.println("(-------------------)");
        System.out.println("( Charging Stations )");
        System.out.println("(-------------------)");

        // ORDENAMOS las estaciones por número de recargas (requisito fuente 187)
        // Usamos el comparador que ya hemos creado (Extra, fuente 246)
        this.estaciones.sort(new ComparatorChargingStationNumberRecharged());

        // Imprimimos las estaciones ya ordenadas
        for (ChargingStation estacion : this.estaciones) {
            // Usamos el método getCompleteInfo() que diseñamos en ChargingStation
            // para mostrar la info de la estación, sus cargadores, y los
            // vehículos de cada cargador (requisito fuente 188)
            System.out.println(estacion.getCompleteInfo());
        }
    }


    /**
     * El punto de entrada principal para ejecutar la simulación.
     * Crea una instancia de {@code EVDemo} e inicia la simulación.
     */
    public static void main() {
        EVDemo demo = new EVDemo();
        demo.run();
    }
}
