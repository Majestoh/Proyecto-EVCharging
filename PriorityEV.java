import java.util.List;

public class PriorityEV extends ElectricVehicle  {
    
    public PriorityEV(EVCompany company, Location loc, Location target, String name, String plate, int capacity) {
        super(company, loc, target, name, plate, capacity);
    }
    
    @Override
    public VehicleTier getTipo() {
        return VehicleTier.PRIORITY;
    }
    
    @Override
    protected void notificarRecargaACompania(Charger cargador) {
        // NO NOTIFICA SEGÚN REQUISITOS.
    }
    
    @Override
    public void act(int step) {
        // COMPORTAMIENTO ESPECIAL: puede moverse hasta 2 veces.
        if (super.haLlegadoAlDestino) {
            super.incrementIdleCount();
            return;
        }
        
        // PRIMER SALTO
        boolean seMovio = super.intentarMoverse();
        if (seMovio) {
            super.comprobarLlegada(step);
            
            // Si tras el primer salto NO HEMOS LLEGADO NI RECARGADO, damos un segundo salto.
            if (!super.haLlegadoAlDestino && localizacionRecarga != null && !localizacion.equals(localizacionRecarga)) {
                // SEGUNDO SALTO
                boolean seMovio2 = super.intentarMoverse();
                if (seMovio2) {
                    super.comprobarLlegada(step);
                }
            }
        }
    }
    
    @Override
    protected ChargingStation seleccionarMejorEstacion(List<ChargingStation> estaciones) {
        // ESTRATEGIA: MÁS CERCANA AL DESTINO FINAL
        ChargingStation mejor = null;
        int minD = Integer.MAX_VALUE;
        
        for(ChargingStation st : estaciones) {
            if (localizacion.equals(st.getLocation())) continue;
            
            if (enoughBattery(localizacion.distance(st.getLocation()))) {
                int distAlFinal = st.getLocation().distance(localizacionDestinoFinal);
                if (distAlFinal < minD) {
                    minD = distAlFinal;
                    mejor = st;
                }
            }
        }
        
        return mejor;
    }
}