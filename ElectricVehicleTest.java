import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas para ElectricVehicle y sus subclases.
 * Verifica creación, cálculo de batería y estrategias de selección de estación.
 *
 * @author DP classes (Modificado por IA para docencia)
 * @version 2025.11.18
 */
public class ElectricVehicleTest
{
    private EVCompany company;
    private ChargingStation estBarata, estRapida;
    private Location start, end;

    @Before
    public void setUp()
    {
        company = EVCompany.getInstance();
        company.reset();
        
        start = new Location(0,0);
        end = new Location(10,10); // Distancia 10
        
        // Estación 1: Barata pero Lenta (en (5,5))
        estBarata = new ChargingStation("Caceres", "LOW_COST", new Location(5,5));
        estBarata.addCharger(new StandardCharger("CH1", 20, 0.1f)); // Tarifa 0.1
        estBarata.addCharger(new SolarCharger("CH2", 20, 0.1f));    // Tarifa 0.1
        
        // Estación 2: Rápida pero Cara (en (6,6))
        estRapida = new ChargingStation("Caceres", "FAST", new Location(6,6));
        estRapida.addCharger(new UltraFastCharger("CH3", 200, 0.9f)); // Vel 200
        
        company.addChargingStation(estBarata);
        company.addChargingStation(estRapida);
    }

    /**
     * Prueba la inicialización correcta de campos.
     */
    @Test
    public void testCreation()
    {
        ElectricVehicle ev = new StandardEV(company, start, end, "Test", "0000", 50);
        assertEquals(50, ev.capacidadBateria); // Acceso paquete/protected para test
        assertEquals(0, ev.getChargesCount());
        assertEquals(start, ev.getLocation());
    }

    /**
     * Prueba el cálculo de batería suficiente.
     * Distancia (0,0) a (10,10) = 10 pasos * 5 kwh = 50 kwh necesarios.
     */
    @Test
    public void testEnoughBattery()
    {
        ElectricVehicle ev = new StandardEV(company, start, end, "Test", "0000", 60);
        // Con 60kwh (lleno), debería tener suficiente para 10 pasos (50kwh)
        assertTrue(ev.enoughBattery(10));
        
        // Vaciamos batería a 40kwh
        ev.setNivelBateria(40);
        assertFalse(ev.enoughBattery(10));
    }

    /**
     * Prueba la estrategia de VtcEV: Debe elegir la estación más BARATA.
     */
    @Test
    public void testVtcStrategy()
    {
        // VTC tiene poca batería, necesita cargar
        ElectricVehicle vtc = new VtcEV(company, start, end, "VTC", "VTC1", 20); // 20kwh no llega a 50kwh
        
        // Forzamos cálculo de ruta
        vtc.calculateRoute();
        
        // Debería haber elegido la estación BARATA (estBarata), no la rápida
        assertNotNull(vtc.localizacionRecarga);
        assertEquals(estBarata.getLocation(), vtc.localizacionRecarga);
    }

    /**
     * Prueba la estrategia de PremiumEV: Debe elegir la estación más RÁPIDA.
     */
    @Test
    public void testPremiumStrategy()
    {
        // Premium con poca batería
        ElectricVehicle prem = new PremiumEV(company, start, end, "Prem", "PRM1", 20);
        
        prem.calculateRoute();
        
        // Debería haber elegido la estación RÁPIDA (estRapida)
        assertNotNull(prem.localizacionRecarga);
        assertEquals(estRapida.getLocation(), prem.localizacionRecarga);
    }
}