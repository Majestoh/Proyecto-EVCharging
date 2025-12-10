import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas para la jerarquía Charger.
 * Prueba compatibilidad, cálculo de costes (descuentos/recargos) y métricas.
 *
 * @author DP classes (Modificado por IA para docencia)
 * @version 2025.11.18
 */
public class ChargerTest
{
    // Cargadores
    private Charger standardCharger;
    private Charger solarCharger;
    private Charger ultraFastCharger;
    private Charger priorityCharger;
    
    // Vehículos (Mocks o reales simplificados para probar compatibilidad)
    private ElectricVehicle standardEV;
    private ElectricVehicle vtcEV;
    private ElectricVehicle premiumEV;
    private ElectricVehicle priorityEV;
    
    private EVCompany company;

    @Before
    public void setUp()
    {
        // Necesitamos la compañía (Singleton) y ubicaciones dummy
        company = EVCompany.getInstance();
        company.reset();
        Location loc = new Location(0,0);
        
        // Inicializamos Cargadores (Velocidad 100, Tarifa 1.0€ para facilitar cálculos)
        standardCharger = new StandardCharger("STD_01", 100, 1.0f);
        solarCharger = new SolarCharger("SOL_01", 100, 1.0f);
        ultraFastCharger = new UltraFastCharger("ULT_01", 100, 1.0f);
        priorityCharger = new PriorityCharger("PRI_01", 100, 1.0f);
        
        // Inicializamos Vehículos
        standardEV = new StandardEV(company, loc, loc, "Std", "0000", 100);
        vtcEV = new VtcEV(company, loc, loc, "Vtc", "1111", 100);
        premiumEV = new PremiumEV(company, loc, loc, "Prm", "2222", 100);
        priorityEV = new PriorityEV(company, loc, loc, "Pri", "3333", 100);
    }

    /**
     * Prueba la compatibilidad de vehículos con cargadores [Requisito Extra].
     */
    @Test
    public void testCompatibility()
    {
        // StandardCharger: Acepta Standard y VTC
        assertNotEquals(-1.0f, standardCharger.recharge(standardEV, 10), 0.01);
        assertNotEquals(-1.0f, standardCharger.recharge(vtcEV, 10), 0.01);
        assertEquals(-1.0f, standardCharger.recharge(premiumEV, 10), 0.01); // No compatible
        
        // PriorityCharger: Solo Priority
        assertEquals(-1.0f, priorityCharger.recharge(standardEV, 10), 0.01);
        assertNotEquals(-1.0f, priorityCharger.recharge(priorityEV, 10), 0.01);
        
        // SolarCharger: Solo VTC
        assertEquals(-1.0f, solarCharger.recharge(standardEV, 10), 0.01);
        assertNotEquals(-1.0f, solarCharger.recharge(vtcEV, 10), 0.01);
        
        // UltraFastCharger: Solo Premium
        assertEquals(-1.0f, ultraFastCharger.recharge(vtcEV, 10), 0.01);
        assertNotEquals(-1.0f, ultraFastCharger.recharge(premiumEV, 10), 0.01);
    }

    /**
     * Prueba el cálculo de costes incluyendo descuentos y recargos [Requisito Extra].
     * Base: 10 kwh * 1.0€ tarifa = 10.0€
     */
    @Test
    public void testRechargeCostCalculation()
    {
        int kwh = 10;
        
        // 1. Standard: Coste base (10.0€)
        float costeStd = standardCharger.recharge(standardEV, kwh);
        assertEquals(10.0f, costeStd, 0.01);
        
        // 2. Solar: Descuento 10% (9.0€)
        float costeSolar = solarCharger.recharge(vtcEV, kwh);
        assertEquals(9.0f, costeSolar, 0.01);
        
        // 3. UltraFast: Recargo 10% (11.0€)
        float costeUltra = ultraFastCharger.recharge(premiumEV, kwh);
        assertEquals(11.0f, costeUltra, 0.01);
        
        // 4. Priority: Coste base (10.0€)
        float costePri = priorityCharger.recharge(priorityEV, kwh);
        assertEquals(10.0f, costePri, 0.01);
    }
    
    /**
     * Prueba que las métricas (dinero recaudado y lista de vehículos) se actualicen.
     */
    @Test
    public void testRechargeMetrics()
    {
        standardCharger.recharge(standardEV, 10); // Coste 10.0
        standardCharger.recharge(vtcEV, 5);       // Coste 5.0
        
        assertEquals(15.0f, standardCharger.getCantidadRecaudada(), 0.01);
        assertEquals(2, standardCharger.getNumerEVRecharged());
    }
}

