/**
 * Fábrica para la creación de vehículos eléctricos (patrón Factory)
 * <p>
 * Encapsula la lógica de instanciación de las subclases de {@link ElectricVehicle}
 * permitiendo que el cliente (EVDemo) no depende de las clases concretas.
 * </p>
 * 
 * @author Pablo Carrasco Caballero
 * @version 10.12.2025
 */
public class VehicleFactory {
    
    /**
     * Crea una instancia de un vehículo eléctrico según el tipo especificado.
     * 
     * @param tipo El tipo de vehículo (VehicleTier).
     * @param company La compañía a la que pertenece.
     * @param loc Ubicación inicial.
     * @param target Ubicación destino.
     * @param name Nombre del vehículo.
     * @param plate Matrícula.
     * @param capacity Capacidad de la batería.
     * @return Una instancia de la subclase correspondiente de ElectricVehicle.
     */
    public static ElectricVehicle createVehicle(VehicleTier tipo, EVCompany company, Location loc, Location target, String name, String plate, int capacity) {
        switch (tipo) {
            case PRIORITY:
                return new PriorityEV(company, loc, target, name, plate, capacity);
            case VTC:
                return new VtcEV(company, loc, target, name, plate, capacity);
            case PREMIUM:
                return new PremiumEV(company, loc, target, name, plate, capacity);
            case STANDARD:
            default:
                return new StandardEV(company, loc, target, name, plate, capacity);
        }
    }
}