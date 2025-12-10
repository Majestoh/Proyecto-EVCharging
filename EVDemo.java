import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.*; // NECESARIO PARA EL MANEJO DE FICHEROS

/**
 * Clase principal que configura y ejecuta la simulación.
 * 
 * @author Pablo Carrasco Caballero
 * @version 10.12.2025
 */
public class EVDemo {
    
    // CONSTANTES DE CONFIGURACIÓN
    public static final int MAXX = 20;
    public static final int MAXY = 20;
    public static final int MAXSTEPS = 50;
    
    // COMPONENTES PRINCIPALES
    private EVCompany company;
    private List<ElectricVehicle> vehicles;
    private List<ChargingStation> stations;
    
    // SELECCIÓN DEL ESCENARIO
    private static final DemoType DEMO = DemoType.ADVANCED;
    
    /**
     * Constructor. Inicializa la simulación y resetea el estado.
     */
    public EVDemo() {
        // Usamos el patrón Singleton para obtener la compañía.
        this.company = EVCompany.getInstance();
        this.vehicles = new ArrayList<>();
        this.stations = new ArrayList<>();
        reset();
    }
    
    /**
     * Ejecuta el bucle principal de la simulación.
     * EXTRA: ESCRITURA EN FICHERO
     * 
     * Este método redirige la salida estándar para que todo lo que se imprima por consola
     * se guarde también en el fichero simulation_output.txt
     */
    public void run() {
        // 1. GUARDAMOS LA REFERENCIA A LA CONSOLA ORIGINAL PARA RESTAURARLA LUEGO
        PrintStream consolaOriginal = System.out;
        PrintStream ficheroStream = null;
        
        try {
            // 2. CREAMOS EL FICHERO DE SALIDA
            ficheroStream = new PrintStream(new FileOutputStream("simulation_output.txt"));
            
            // 3. ACTIVAMOS EL STREAM DUAL (CONSOLA + FICHERO)
            DualPrintStream dualStream = new DualPrintStream(consolaOriginal, ficheroStream);
            System.setOut(dualStream);
            
            // --- INICIO DE LA SIMULACIÓN ---
            for (int step = 0; step < MAXSTEPS; step++) {
                step(step);
            }
            showFinalInfo();
            
            // --- FIN DE LA SIMULACIÓN
        } catch (FileNotFoundException e) {
            // SI FALLA EL FICHERO, AVISAMOS PERO INTENTAMOS SEGUIR POR CONSOLA
            System.setOut(consolaOriginal);
            System.err.println("Error: no se pudo crear el fichero de salida.");
            e.printStackTrace();
        } finally {
            // 4. IMPORTANTE: RESTAURAR SIEMPRE LA CONSOLA ORIGINAL AL TERMINAR
            System.setOut(consolaOriginal);
            if (ficheroStream != null) {
                ficheroStream.close();
            }
            System.out.println("Simulación finaliza. Salida guardada en simulation_output.txt");
        }
    }
    
    /**
     * Ejecuta un único paso de la simulación.
     */
    public void step(int step) {
        
        // 1. FASE DE ACTUACIÓN (POLIMORFISMO: CADA VEHÍCULO ACTÚA SEGÚN SU TIPO)
        for (ElectricVehicle vehicle : this.vehicles) {
            vehicle.act(step);
        }
        
        // 2. FASE DE REPORTE
        for (ElectricVehicle vehicle : this.vehicles) {
            System.out.println(vehicle.getStepInfo(step));
        }
    }
    
    /**
     * Reinicia y reconfigura todo el entorno.
     */
    public void reset() {
        this.vehicles.clear();
        this.stations.clear();
        this.company.reset();
        
        createElectricVehicles();
        createStations();
        createChargers();
        configureRoutes();
        showInitialInfo();
    }
    
    /**
     * Crea los vehículos usando las subclases apropiadas.
     */
    private void createElectricVehicles() {
       Location [] locations = {new Location(1,1), new Location(1,1), new Location(1,19), new Location(1,19), 
                                new Location(19,1), new Location(19,1), new Location(10,19), new Location(19,10),
                                new Location(10,10), new Location(10,10)};

        Location [] targetLocations = {new Location(20,20), new Location(20,20), new Location(19,1), new Location(19,1), 
                                       new Location(1,19), new Location(1,19), new Location(19,10), new Location(10,19),
                                       new Location(10,20), new Location(20,10)};
                                        
        for (int i = 0; i < DEMO.getNumVehiclesToCreate(); i++){
            // Lógica de rotación de tipos
            VehicleTier[] tiposDisponibles = {VehicleTier.STANDARD, VehicleTier.PRIORITY, VehicleTier.VTC, VehicleTier.PREMIUM};
            VehicleTier tipo = tiposDisponibles[i % tiposDisponibles.length];
            
            String nombre = "EV" + i;
            String matricula = i + "CCC";
            int capacidadBateria = (i + 1) * (20 - i);
            
            // --- USO DEL PATRÓN FACTORY ---
            ElectricVehicle ev = VehicleFactory.createVehicle(tipo, company, locations[i], targetLocations[i], nombre, matricula, capacidadBateria);
            
            this.vehicles.add(ev);
            this.company.addElectricVehicle(ev);
        }
        
        this.vehicles.sort((v1, v2) -> v1.getMatricula().compareTo(v2.getMatricula()));
    }
    
    /**
     * Crea las estaciones de carga.
     */
    private void createStations() {
        Location [] locations = {new Location(5,5), new Location(15,15), new Location(5,15), new Location(15,5), new Location(10,10)};
                                
        for (int i = 0; i < DEMO.getNumStationsToCreate(); i++){
            // Usamos "Caceres" sin tilde
            ChargingStation nuevaEstacion = new ChargingStation("Caceres", "CC0" + i, locations[i]);
            this.stations.add(nuevaEstacion);
            this.company.addChargingStation(nuevaEstacion);
        }
        
        // Ordenar estaciones por ID
        this.stations.sort((s1, s2) -> s1.getId().compareTo(s2.getId()));
    }

    /**
     * Crea y distribuye los diferentes tipos de cargadores.
     */
    private void createChargers() {  
        List<ChargingStation> cityStations = company.getCityStations();
        
        int j = 0;
        for (ChargingStation station : cityStations){
            for (int i = 0; i < DEMO.getNumChargersToCreate(); i++){
                
                String idCargador = station.getId() + "_00" + i;
                int velocidad = (i + j + 1) * 20;
                float tarifa = (i + 1) * 0.20f;
                
                // Determinamos el tipo de cargador
                int numCargadores = DEMO.getNumChargersToCreate();
                int numEstaciones = DEMO.getNumStationsToCreate();
                ChargerFactory.ChargerType tipoCargador;

                if (i % numCargadores == (j % numEstaciones - 1)) {
                    tipoCargador = ChargerFactory.ChargerType.SOLAR;
                }    
                else if (i % numCargadores == (j % numEstaciones)) {
                    tipoCargador = ChargerFactory.ChargerType.ULTRAFAST;
                } 
                else if (i % numCargadores == (j % numEstaciones) + 1) {
                    tipoCargador = ChargerFactory.ChargerType.PRIORITY;
                }    
                else {
                    tipoCargador = ChargerFactory.ChargerType.STANDARD;
                }
                
                // --- USO DEL PATRÓN FACTORY ---
                Charger ch = ChargerFactory.createCharger(tipoCargador, idCargador, velocidad, tarifa);
                
                station.addCharger(ch);
            }
            j++;
        }  
    }
    
    private void configureRoutes() {
        for (ElectricVehicle vehiculo : this.vehicles) {
            vehiculo.calculateRoute();
        }
    }
    
    private void showInitialInfo() {
        System.out.println("( Compania EVCharging Caceres )");
        System.out.println("(-------------------)");
        System.out.println("( Electric Vehicles )");
        System.out.println("(-------------------)");
        
        for (ElectricVehicle vehiculo : this.vehicles) {
            System.out.println(vehiculo.getInitialFinalInfo());
        }

        System.out.println("(-------------------)");
        System.out.println("( Charging Stations )");
        System.out.println("(-------------------)");
       
        for (ChargingStation estacion : this.stations) {
            System.out.println(estacion.toString()); 
            // Mostramos los cargadores (usarán su toString específico: StandardCharger, etc.)
            for (Charger cargador : estacion.getChargers()) {
                System.out.println(cargador.toString());
            }
        }
        
        System.out.println("(------------------)");
        System.out.println("( Simulation start )");
        System.out.println("(------------------)");
    }

    private void showFinalInfo() {
        System.out.println("(-------------------)");
        System.out.println("( Final information )");        
        System.out.println("(-------------------)");

        System.out.println("(-------------------)");
        System.out.println("( Electric Vehicles )");
        System.out.println("(-------------------)");
        
        // Ordenar vehículos: Turno llegada (asc) -> Matrícula (asc)
        this.vehicles.sort((v1, v2) -> {
            int turno1 = (v1.getArrivingStep() == -1) ? Integer.MAX_VALUE : v1.getArrivingStep();
            int turno2 = (v2.getArrivingStep() == -1) ? Integer.MAX_VALUE : v2.getArrivingStep();
            
            if (turno1 != turno2) return Integer.compare(turno1, turno2);
            return v1.getMatricula().compareTo(v2.getMatricula());
        });
        
        for (ElectricVehicle vehiculo : this.vehicles) {
            System.out.println(vehiculo.getInitialFinalInfo());
        }

        System.out.println("(-------------------)");
        System.out.println("( Charging Stations )");
        System.out.println("(-------------------)");
       
        // Ordenar estaciones: Nº recargas (desc) -> ID (asc)
        this.stations.sort((s1, s2) -> {
            int recargas1 = s1.getNumerEVRecharged();
            int recargas2 = s2.getNumerEVRecharged();
            if (recargas1 != recargas2) return Integer.compare(recargas2, recargas1);
            return s1.getId().compareTo(s2.getId());
        });
        
        for (ChargingStation estacion : this.stations) {
            System.out.println(estacion.getCompleteInfo());
        }
        
        // --- NUEVO REQUISITO: Mostrar info de la compañía (notificaciones) ---
        company.showCompanyInfo();
    }
    
    public static void main() {
        EVDemo demo = new EVDemo();
        demo.run();
    }
    
    // --- CLASE INTERNA PARA EL STREAM DUAL ---
    /**
     * Clase auxiliar que redirige la salida a dos flujos simultáneamente:
     * la consola original y un fichero.
     */
    private static class DualPrintStream extends PrintStream {
        private final PrintStream segundoStream;
        
        public DualPrintStream(PrintStream principal, PrintStream segundo) {
            super(principal);
            this.segundoStream = segundo;
        }
        
        @Override
        public void write(byte[] buf, int off, int len) {
            super.write(buf, off, len); // ESCRIBE EN CONSOLA
            segundoStream.write(buf, off, len); // ESCRIBE EN FICHERO
        }
        
        @Override
        public void flush() {
            super.flush();
            segundoStream.flush();
        }
        
        @Override
        public void close() {
            super.close();
            segundoStream.close();
        }
    }
}