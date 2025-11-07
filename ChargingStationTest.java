import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

/**
 * Pruebas unitarias para la clase {@link ChargingStation}.
 * Requisito extra (fuente 261).
 *
 * @author IA para docencia
 * @version 2025.11.04
 */
public class ChargingStationTest
{
    // --- Fixtures (fuente 264) ---
    private ChargingStation estacionPrueba;
    private Charger cargadorLento;   // 20kwh, 0.2€, ID: ST_02
    private Charger cargadorRapido;  // 80kwh, 0.8€, ID: ST_01
    private Charger cargadorMedio;   // 50kwh, 0.5€, ID: ST_03
    
    /**
     * Constructor por defecto
     */
    public ChargingStationTest()
    {
    }

    /**
     * Prepara el entorno de prueba (Fixtures).
     * Se llama ANTES de cada método de prueba (@Test).
     */
    @Before
    public void setUp()
    {
        estacionPrueba = new ChargingStation("Caceres", "Test-ST", new Location(1, 1));
        
        // IDs invertidos a propósito para probar la ordenación
        cargadorRapido = new Charger("ST_01", 80, 0.8f);
        cargadorLento = new Charger("ST_02", 20, 0.2f);
        cargadorMedio = new Charger("ST_03", 50, 0.5f);
    }

    /**
     * Limpia el entorno de prueba.
     * Se llama DESPUÉS de cada método de prueba (@Test).
     */
    @After
    public void tearDown()
    {
        estacionPrueba = null;
        cargadorLento = null;
        cargadorRapido = null;
        cargadorMedio = null;
    }

    /**
     * Prueba el método 'addCharger' (fuente 262).
     * Comprueba que los cargadores se añaden y, lo más importante,
     * que la lista interna se mantiene ordenada según los criterios del proyecto:
     * 1. Velocidad (Decreciente)
     * 2. Tarifa (Creciente)
     * 3. ID (Creciente)
     */
    @Test
    public void testAddCharger()
    {
        // 1. Añadir los cargadores en orden aleatorio
        estacionPrueba.addCharger(cargadorLento);
        estacionPrueba.addCharger(cargadorRapido);
        estacionPrueba.addCharger(cargadorMedio);

        // 2. Obtener la lista de cargadores (que debe estar ordenada)
        List<Charger> cargadores = estacionPrueba.getChargers();

        // 3. Comprobar el orden
        // Orden esperado (según criterios):
        // 1º: Rápido (80 kwh)
        // 2º: Medio (50 kwh)
        // 3º: Lento (20 kwh)
        
        // Aserción 1: El tamaño de la lista es 3
        assertEquals(3, cargadores.size());
        
        // Aserción 2: El primer cargador es el 'cargadorRapido'
        assertEquals(cargadorRapido, cargadores.get(0));
        
        // Aserción 3: El segundo cargador es el 'cargadorMedio'
        assertEquals(cargadorMedio, cargadores.get(1));
        
        // Aserción 4: El tercer cargador es el 'cargadorLento'
        assertEquals(cargadorLento, cargadores.get(2));
    }
}