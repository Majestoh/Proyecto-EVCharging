public class PriorityCharger extends Charger {
    public PriorityCharger(String id, int velocidad, float tarifa) {
        super(id, velocidad, tarifa);
    }
    
    @Override
    protected boolean esCompatible(ElectricVehicle vehiculo) {
        // Solo compatible con PRIORITY
        return vehiculo.getTipo() == VehicleTier.PRIORITY;
    }
    
    @Override
    public String toString() {
        return String.format(java.util.Locale.US,
            "(PriorityCharger: %s, %dkwh, %.1f€, %d, %.2f€)",
            this.id, this.velocidadCarga, this.tarifaCarga, this.getNumerEVRecharged(), this.cantidadRecaudada);
    }
}