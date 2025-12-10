public class UltraFastCharger extends Charger {

    public UltraFastCharger(String id, int velocidad, float tarifa) {
        super(id, velocidad, tarifa);
    }

    @Override
    protected boolean esCompatible(ElectricVehicle vehiculo) {
        // Solo compatible con PREMIUM
        return vehiculo.getTipo() == VehicleTier.PREMIUM;
    }

    /**
     * Aplica un recargo del 10% en el coste total.
     */
    @Override
    protected float calcularCoste(int kwh) {
        float costeBase = super.calcularCoste(kwh);
        return costeBase * 1.10f; // Recargo del 10%
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.US,
            "(UltraFastCharger: %s, %dkwh, %.1f€, %d, %.2f€)",
            this.id, this.velocidadCarga, this.tarifaCarga, this.getNumerEVRecharged(), this.cantidadRecaudada);
    }
}