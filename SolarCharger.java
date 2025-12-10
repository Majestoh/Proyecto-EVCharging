public class SolarCharger extends Charger {
    public SolarCharger(String id, int velocidad, float tarifa) {
        super(id, velocidad, tarifa);
    }
    
    @Override
    protected boolean esCompatible(ElectricVehicle vehiculo) {
        // Solo compatible con VTC
        return vehiculo.getTipo() == VehicleTier.VTC;
    }
    
    /**
     * Aplica un descuento del 10% en el coste total.
     */
    @Override
    protected float calcularCoste(int kwh) {
        float costeBase = super.calcularCoste(kwh);
        return costeBase * 0.90f; // Descuento del 10%.
    }
    
    @Override
    public String toString() {
        return String.format(java.util.Locale.US,
            "(SolarCharger: %s, %dkwh, %.1f€, %d, %.2f€)",
            this.id, this.velocidadCarga, this.tarifaCarga, this.getNumerEVRecharged(), this.cantidadRecaudada);
    }
}