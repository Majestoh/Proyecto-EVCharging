import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

/**
 * Clase de pruebas para ChargingStation.
 * Verifica la adición y ordenación correcta de cargadores.
 *
 * @author Pablo Carrasco Caballero
 * @version 10.12.2025
 */
public class ChargingStationTest
{
    private ChargingStation estacion;
    private Charger c1, c2, c3;

    @Before
    public void setUp()
    {
        estacion = new ChargingStation("Caceres", "CC01", new Location(5,5));
        
        // Creamos cargadores con diferentes atributos para probar la ordenación
        // Criterios: Velocidad DESC, Tarifa ASC, ID ASC
        
        c1 = new StandardCharger("CH_LOW_SPEED", 20, 0.5f);
        c2 = new StandardCharger("CH_HIGH_SPEED", 100, 0.5f);
        c3 = new StandardCharger("CH_MID_SPEED", 50, 0.2f);
    }

    /**
     * Prueba que al añadir cargadores, la lista se mantiene ordenada según criterios.
     */
    @Test
    public void testAddChargerSorting()
    {
        estacion.addCharger(c1); // Vel 20
        estacion.addCharger(c2); // Vel 100 (Debería ir primero)
        estacion.addCharger(c3); // Vel 50 (Debería ir segundo)
        
        List<Charger> lista = estacion.getChargers();
        
        assertEquals(3, lista.size());
        
        // 1º: c2 (Vel 100)
        assertEquals("CH_HIGH_SPEED", lista.get(0).getId());
        
        // 2º: c3 (Vel 50)
        assertEquals("CH_MID_SPEED", lista.get(1).getId());
        
        // 3º: c1 (Vel 20)
        assertEquals("CH_LOW_SPEED", lista.get(2).getId());
    }
    
    @Test
    public void testGetFreeCharger() {
        estacion.addCharger(c1);
        c1.setEstaLibre(false); // Ocupamos el único cargador
        assertNull(estacion.getFreeCharger());
        
        c1.setEstaLibre(true);
        assertNotNull(estacion.getFreeCharger());
    }
}