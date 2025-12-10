import java.util.List;

/**
 * Vehículo de Transporte con Conductor (VTC).
 * <p>
 * Puede usar cargadores STANDARD o SOLAR. Su prioridad es la economía:
 * siempre busca el cargador con la tarifa más baja posible dentro de su alcance.
 * </p>
 * 
 * @author Pablo Carrasco Caballero
 * @version 10.12.2025
 */
public class VtcEV extends ElectricVehicle {
    
    public VtcEV(EVCompany company, Location loc, Location target, String name, String plate, int capacity) {
        super(company, loc, target, name, plate, capacity);
    }
    
    @Override
    public VehicleTier getTipo() {
        return VehicleTier.VTC;
    }
    
    @Override
    protected void notificarRecargaACompania(Charger cargador) {
        compania.notificarRecarga(this, cargador);
    }
    
    @Override
    protected ChargingStation seleccionarMejorEstacion(List<ChargingStation> estaciones) {
        // ESTRATEGIA: BUSCAR EL CARGADOR MÁS BARATO
        ChargingStation mejorEstacion = null;
        float minTarifa = Float.MAX_VALUE;
        
        for (ChargingStation estacion : estaciones) {
            if (localizacion.equals(estacion.getLocation())) continue;
            
            if (enoughBattery(localizacion.distance(estacion.getLocation()))) {
                
                for (Charger ch : estacion.getChargers()) {
                    // VERIFICAMOS SI ES STANDARD O SOLAR
                    if (ch instanceof StandardCharger || ch instanceof SolarCharger) {
                        if (ch.getTarifaCarga() < minTarifa) {
                            minTarifa = ch.getTarifaCarga();
                            mejorEstacion = estacion;
                        }
                        // EN CASO DE EMPATE DEBERÍAMOS SELECCIONAR EL PRIMERO SEGÚN ORDEN
                        // AL ITERAR EN ORDEN, EL PRIMERO QUE ENCONTRAMOS SE QUEDA (CON < ESTRICTO)
                    }
                }
            }
        }
        
        return mejorEstacion;
    }
}