/**
 * Fábrica para la creación de cargadores (patrón Factory).
 * <p>
 * Encapsula la lógica de instalación de las subclases de {@link Charger}.
 * </p>
 * 
 * @author Pablo Carrasco Caballero
 * @version 10.12.2025
 */
public class ChargerFactory {
    
    /**
     * Tipos de cargadores disponibles para la creación.
     */
    public enum ChargerType {
        STANDARD, SOLAR, PRIORITY, ULTRAFAST
    }
    
    /**
     * Crea una instancia de un cargador según el tipo especificado.
     * 
     * @param tipo El tipo de cargador.
     * @param id Identificador del cargador.
     * @param velocidad Velocidad de carga.
     * @param tarifa Tarifa de carga.
     * @return Una instancia de la subclase correspondiente de Charger.
     */
    public static Charger createCharger(ChargerType tipo, String id, int velocidad, float tarifa) {
        switch (tipo) {
            case SOLAR:
                return new SolarCharger(id, velocidad, tarifa);
            case ULTRAFAST:
                return new UltraFastCharger(id, velocidad, tarifa);
            case PRIORITY:
                return new PriorityCharger(id, velocidad, tarifa);
            case STANDARD:
            default:
                return new StandardCharger(id, velocidad, tarifa);
        }
    }
}