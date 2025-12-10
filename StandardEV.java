import java.util.List;

public class StandardEV extends ElectricVehicle {
    
    public StandardEV(EVCompany company, Location loc, Location target, String name, String plate, int capacity) {
        super(company, loc, target, name, plate, capacity);
    }
    
    @Override
    public VehicleTier getTipo() {
        return VehicleTier.STANDARD;
    }
    
    @Override
    protected void notificarRecargaACompania(Charger cargador) {
        // Sí notifica (REQUISITO)
        compania.notificarRecarga(this, cargador); // Este método está en EVCompany.
    }
    
    @Override
    protected ChargingStation seleccionarMejorEstacion(List<ChargingStation> estaciones) {
        // STANDARD: la más cercana (mínima dist. total)
        // SOLO compatible con cargadores STANDARD (verificamos con Charger.esCompatible de forma implícita
        // al intentar recargar, pero aquí filtramos para la ruta).
        
        // El requisito de "solo puede cargar en Standard" se asegura en el método recharge().
        // Aquí buscamos la estación óptima.
        // NOTA: asumimos que tendrá cargadores.
        
        ChargingStation mejor = null;
        int minDist = Integer.MAX_VALUE;
        
        for (ChargingStation st : estaciones) {
            if (localizacion.equals(st.getLocation())) continue; // Ignoramos actual.
            
            int dist = localizacion.distance(st.getLocation()) + st.getLocation().distance(localizacionDestinoFinal);
            if (enoughBattery(localizacion.distance(st.getLocation())) && dist < minDist) {
                minDist = dist;
                mejor = st;
            }
        }
        
        return mejor;
    }
}