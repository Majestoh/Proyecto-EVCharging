/**
 * Define los niveles o tipos de vehículos eléctricos en la simulación.
 * Se utiliza principalmente para determinar las reglas de acceso y compatibilidad
 * con los diferentes tipos de cargadores.
 * 
 * @author Pablo Carrasco Caballero
 * @version 2025.11.25
 */
public enum VehicleTier {
    /** Vehículos de emergencia (ej. ambulancias) */
    PRIORITY,
    
    /** Vehículos estándar (comportamiento base) */
    STANDARD,
    
    /** Vehículos de transporte con conductor (VTC) */
    VTC,
    
    /** Vehículos de alta gama */
    PREMIUM,
    
    /** Vehículos de reparto (no descrito en la entrega final pero incluido) */
    DELIVERY;
    
    /**
     * Devuelve el número total de tipos de vehículos definidos.
     * <p>La clase EVDEmo usa este método para asignar tipos de vehículos
     * de forma rotatoria (usando el operador módulo %) al crear la flota.</p>
     * 
     * @return La cantidad de constantes definidas en esta numeración.
     */
    public static int numTiers() {
        // .values() es un método estáticos que tienen todas las enumeraciones en Java.
        // Devuelve un array que contiene todas las constantes (PRIORITY, STANDARD, ...).
        // .length nos da el tamaño de ese array.
        return VehicleTier.values().length;
    }
}