import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas unitarias para la clase {@link Charger}.
 * Requisito extra (fuente 254).
 *
 * @author IA para docencia
 * @version 2025.11.04
 */
public class ChargerTest
{
    // --- Fixtures (fuente 264) ---
    // Objetos que usaremos en las pruebas
    
    private Charger cargadorPrueba;
    private ElectricVehicle evPrueba;
    
    /**
     * Constructor por defecto para la clase de prueba ChargerTest
     */
    public ChargerTest()
    {
    }

    /**
     * Prepara el entorno de prueba (Fixtures).
     * Se llama ANTES de cada método de prueba (@Test).
     */
    @Before
    public void setUp()
    {
        // 1. Crear un cargador para la prueba
        // (ID: "Test_01", Velocidad: 50kwh, Tarifa: 0.5 €/kwh)
        cargadorPrueba = new Charger("Test_01", 50, 0.5f);
        
        // 2. Crear un EV (necesita objetos auxiliares)
        EVCompany company = new EVCompany();
        Location loc1 = new Location(0, 0);
        Location loc2 = new Location(5, 5);
        // (Capacidad de 100kwh)
        evPrueba = new ElectricVehicle(company, loc1, loc2, "TestEV", "1234ABC", 100);
    }

    /**
     * Limpia el entorno de prueba.
     * Se llama DESPUÉS de cada método de prueba (@Test).
     */
    @After
    public void tearDown()
    {
        // Ponemos los objetos a null para liberar memoria
        cargadorPrueba = null;
        evPrueba = null;
    }

    /**
     * Prueba el método 'recharge' de la clase Charger (fuente 255).
     * Comprueba:
     * 1. Que el coste devuelto sea correcto.
     * 2. Que la cantidad recaudada por el cargador se actualice.
     * 3. Que el vehículo se añada a la lista de vehículos recargados.
     */
    @Test
    public void testRecharge()
    {
        // --- 1. Definición del escenario ---
        // Vamos a simular una recarga de 30 kwh.
        int kwhARecargar = 30;
        
        // Coste esperado: 30 kwh * 0.5 €/kwh = 15.0 €
        float costeEsperado = 15.0f;

        // --- 2. Ejecución ---
        // Llamamos al método que queremos probar
        float costeReal = cargadorPrueba.recharge(evPrueba, kwhARecargar);

        // --- 3. Comprobación (Aserciones) ---
        
        // Aserción 1: ¿Es el coste devuelto el esperado?
        // Usamos un 'delta' (0.01) para comparar floats
        assertEquals(costeEsperado, costeReal, 0.01);

        // Aserción 2: ¿Se ha actualizado la cantidad recaudada?
        assertEquals(costeEsperado, cargadorPrueba.getCantidadRecaudada(), 0.01);
        
        // Aserción 3: ¿Se ha contado el vehículo?
        assertEquals(1, cargadorPrueba.getNumerEVRecharged());
    }
}