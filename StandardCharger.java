public class StandardCharger extends Charger {
   public StandardCharger(String id, int velocidad, float tarifa) {
       super(id, velocidad, tarifa);
   }
   
   @Override
   protected boolean esCompatible(ElectricVehicle vehiculo) {
       // Compatible con STANDARD y VTC
       VehicleTier tipo = vehiculo.getTipo();
       return tipo == VehicleTier.STANDARD || tipo == VehicleTier.VTC;
   }
   
   @Override
   public String toString() {
       return String.format(java.util.Locale.US,
           "(StandardCharger: %s, %dkwh, %.1f€, %d, %.2f€)",
           this.id, this.velocidadCarga, this.tarifaCarga, this.getNumerEVRecharged(), this.cantidadRecaudada);
   }
}