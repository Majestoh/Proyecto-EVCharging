import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas unitarias para la clase {@link ElectricVehicle}.
 * Requisitos extra (fuente 256-260).
 *
 * @author IA para docencia
 * @version 2025.11.04
 */
public class ElectricVehicleTest
{
    // --- Fixtures (fuente 264) ---
    private EVCompany company;
    private Location locInicio;
    private Location locDestinoLejano; // Destino que requiere recarga
    private Location locDestinoCercano; // Destino que NO requiere recarga
    private Location locEstacion;
    private ChargingStation estacionPrueba;
    private ElectricVehicle evPrueba;

    /**
     * Constructor por defecto
     */
    public ElectricVehicleTest()
    {
    }

    /**
     * Prepara el entorno de prueba (Fixtures).
     * Se llama ANTES de cada método de prueba (@Test).
     */
    @Before
    public void setUp()
    {
        // 1. Crear compañía y localizaciones
        company = new EVCompany();
        locInicio = new Location(0, 0);
        locDestinoCercano = new Location(5, 5); // Distancia = 5 pasos
        locDestinoLejano = new Location(10, 10); // Distancia = 10 pasos
        locEstacion = new Location(2, 2);       // Distancia = 2 pasos

        // 2. Crear y configurar la estación
        estacionPrueba = new ChargingStation("Caceres", "ST-01", locEstacion);
        estacionPrueba.addCharger(new Charger("ST-01_01", 80, 0.8f));
        company.addChargingStation(estacionPrueba); // Añadir la estación a la compañía

        // 3. Crear el vehículo
        // Capacidad: 80 kwh
        evPrueba = new ElectricVehicle(company, locInicio, locDestinoLejano, "TestEV", "1234ABC", 80);
        
        // 4. Establecer un nivel de batería inicial para las pruebas
        // (50 kwh)
        evPrueba.setNivelBateria(50);
    }

    /**
     * Limpia el entorno de prueba.
     * Se llama DESPUÉS de cada método de prueba (@Test).
     */
    @After
    public void tearDown()
    {
        company = null;
        locInicio = null;
        locDestinoLejano = null;
        locDestinoCercano = null;
        locEstacion = null;
        estacionPrueba = null;
        evPrueba = null;
    }

    /**
     * Prueba la creación (constructor) del ElectricVehicle (fuente 257).
     * Comprueba que los campos iniciales se establecen correctamente.
     */
    @Test
    public void testCreation()
    {
        // (Usamos los objetos creados en setUp)
        assertNotNull(evPrueba.getLocation());
        assertEquals(locInicio, evPrueba.getLocation());
        assertEquals(locDestinoLejano, evPrueba.getTargetLocation());
        assertEquals(0, evPrueba.getChargesCount());
        assertEquals(0, evPrueba.getIdleCount());
        
        // Comprueba que la batería inicial se puso a 50 (en setUp)
        // evPrueba.toString() -> "... 80kwh, 50kwh, ..."
        assertTrue(evPrueba.toString().contains("80kwh, 50kwh"));
    }
    
    /**
     * Prueba el método 'enoughBattery' (fuente 258).
     */
    @Test
    public void testEnoughBattery()
    {
        // Batería actual = 50 kwh (de setUp)
        // Coste movimiento = 5 kwh/paso (de EVCompany)
        
        // 1. Prueba destino cercano (Distancia 5)
        // Coste necesario = 5 pasos * 5 kwh/paso = 25 kwh
        // Batería (50) >= Coste (25) -> Debería ser TRUE
        int distCercana = evPrueba.getLocation().distance(locDestinoCercano); // 5
        assertTrue(evPrueba.enoughBattery(distCercana));

        // 2. Prueba destino lejano (Distancia 10)
        // Coste necesario = 10 pasos * 5 kwh/paso = 50 kwh
        // Batería (50) >= Coste (50) -> Debería ser TRUE (justo)
        int distLejana = evPrueba.getLocation().distance(locDestinoLejano); // 10
        assertTrue(evPrueba.enoughBattery(distLejana));
        
        // 3. Prueba destino muy lejano (Distancia 11)
        // Coste necesario = 11 pasos * 5 kwh/paso = 55 kwh
        // Batería (50) < Coste (55) -> Debería ser FALSE
        assertFalse(evPrueba.enoughBattery(11));
    }

    /**
     * Prueba el método 'calculateRoute' (fuente 259).
     */
    @Test
    public void testCalculateRoute()
    {
        // --- Escenario 1: Batería suficiente ---
        // Batería = 50 kwh. Destino Lejano (10,10) requiere 50 kwh.
        // Tiene batería suficiente (justa).
        evPrueba.calculateRoute();
        // El destino de recarga debería ser NULL
        assertNull(evPrueba.getRechargingLocation());

        // --- Escenario 2: Batería insuficiente ---
        // Bajamos la batería a 49 kwh.
        evPrueba.setNivelBateria(49);
        // Ahora, el destino Lejano (10,10) requiere 50 kwh (no tiene).
        // Debe buscar una estación.
        // La estación (2,2) requiere 2 pasos * 5 kwh/paso = 10 kwh.
        // Tiene 49 kwh, así que puede llegar a la estación.
        
        evPrueba.calculateRoute();
        
        // El destino de recarga NO debería ser null
        assertNotNull(evPrueba.getRechargingLocation());
        // El destino de recarga debe ser la localización de la estación
        assertEquals(locEstacion, evPrueba.getRechargingLocation());
    }
    
    /**
     * Prueba el método 'recharge' de ElectricVehicle (fuente 260).
     * (Este prueba la lógica de EV, no la de Charger).
     */
    @Test
    public void testRecharge()
    {
        // 1. Mover el vehículo a la estación
        evPrueba.setLocation(locEstacion);
        
        // 2. Bajar la batería a 30 kwh (Capacidad es 80)
        evPrueba.setNivelBateria(30);
        
        // 3. Llamar a recharge (que es parte de 'act', pero lo llamamos directo)
        // El método recharge() interno de EV es privado, así que
        // simulamos el paso 'act' estando en la estación.
        
        // evPrueba.recharge(1); // Llamamos al método recharge(step)
        
        // Corrección: El método recharge(step) SÍ es público.
        evPrueba.recharge(1); // Simula la recarga en el paso 1

        // 4. Comprobar resultados
        // Aserción 1: La batería debe estar llena (80 kwh)
        // Usamos el toString() para verificar el nivel de batería
        assertTrue(evPrueba.toString().contains("80kwh, 80kwh"));
        
        // Aserción 2: El contador de recargas debe ser 1
        assertEquals(1, evPrueba.getChargesCount());
        
        // Aserción 3: El coste debe haberse actualizado (50kwh * 0.8€ = 40.0€)
        assertTrue(evPrueba.toString().contains("1, 40.0€"));
    }
}