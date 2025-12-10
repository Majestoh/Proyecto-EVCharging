import java.util.List;

/**
 * Vehículo de alta gama (premium).
 * <p>
 * Solo puede usar cargadores ultra-rápidos y siempre busca el que ofrezca
 * la mayor velocidad de carga posible dentro de su alcance.
 * </p>
 * 
 * @author Pablo Carrasco Caballero
 * @version 2025.11.18
 */
public class PremiumEV extends ElectricVehicle {
    
    public PremiumEV(EVCompany company, Location loc, Location target, String name, String plate, int capacity) {
        super(company, loc, target, name, plate, capacity);
    }
    
    @Override
    public VehicleTier getTipo() {
        return VehicleTier.PREMIUM;
    }
    
    @Override
    protected void notificarRecargaACompania(Charger cargador) {
        compania.notificarRecarga(this, cargador);
    }
    
    @Override
    protected ChargingStation seleccionarMejorEstacion(List<ChargingStation> estaciones) {
        // ESTRATEGIA: BUSCAR EL CARGADOR ULTRA-RÁPIDO CON MAYOR VELOCIDAD
        ChargingStation mejorEstacion = null;
        int maxVelocidad = -1;
        
        for (ChargingStation estacion : estaciones) {
            // IGNORAR LA ESTACIÓN ACTUAL PARA EVITAR BUCLES
            if (localizacion.equals(estacion.getLocation())) continue;
            
            // SOLO CONSIDERAMOS SI TENEMOS BATERÍA SUFICIENTE
            if (enoughBattery(localizacion.distance(estacion.getLocation()))) {
                
                // BUSCAMOS DENTRO DE LA ESTACIÓN SI TIENE CARGADORES
                for (Charger ch : estacion.getChargers()) {
                    // VERIFICAMOS SI ES ULTRAFAST USANDO COMPATIBILIDAD
                    if (ch instanceof UltraFastCharger) {
                        if (ch.getVelocidadCarga() > maxVelocidad) {
                            maxVelocidad = ch.getVelocidadCarga();
                            mejorEstacion = estacion;
                        }
                        
                        // EN CASO DE EMPATE EN VELOCIDAD, NOS QUEDAMOS CON EL PRIMERO.
                        // ASÍ RESPETAMOS EL ORDEN DE ESTACIONES Y CARGADORES
                    }
                }
            }
        }
        
        return mejorEstacion;
    }
}