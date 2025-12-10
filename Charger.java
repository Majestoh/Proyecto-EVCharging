import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que modela un cargador genérico.
 * <p>
 * Implementa el patrón de diseño <b>Template Method</b> en el método {@link
 * #recharge},
 * definiendo el esqueleto del algoritmo de carga y delegando los detalles específicos
 * (compatibilidad y cálculo de costes) a las subclases.
 * </p>
 * 
 * @author Pablo Carrasco Caballero
 * @version 2025.11.25
 */
public abstract class Charger {
    // --- Atributos Comunes ---
    
    protected String id;
    protected int velocidadCarga;
    protected float tarifaCarga;
    protected List<ElectricVehicle> vehiculosRecargados;
    protected float cantidadRecaudada;
    protected boolean estaLibre;
    
    // --- Constructor ---
    
    public Charger(String id, int velocidadCarga, float tarifaCarga) {
        this.id = id;
        this.velocidadCarga = velocidadCarga;
        this.tarifaCarga = tarifaCarga;
        this.vehiculosRecargados = new ArrayList<>();
        this.cantidadRecaudada = 0.0f;
        this.estaLibre = true;
    }
    
    // -- Patrón Template Method (método plantilla) ---
    
    /**
     * Realiza el proceso de carga de un vehículo siguiendo un algoritmo definido.
     * (patrón template method)
     * <ol>
     * <li>Comprueba la compatibilidad (paso abstracto).</li>
     * <li>Calcula el coste (paso extensible).</li>
     * <li>Actualiza métricas (paso concreto).</li>
     * </ol>
     * 
     * @param vehiculo El vehiculo que solicita la carga.
     * @param kwhARecargar La cantidad de energía a suministrar.
     * @return El coste de la carga si es exitosa, o -1.0f si no es compatible.
     */
    public final float recharge(ElectricVehicle vehiculo, int kwhARecargar) {
        // Paso 1: comprobar compatibilidad (definido por las subclases)
        if(!esCompatible(vehiculo)) {
            // Si no es compatible, no realizamos la recarga.
            return -1.0f; // Código de error o coste 0.
        }
        
        // Paso 2: calcular el precio de la carga (puede ser sobreescrito por las subclases)
        float coste = calcularCoste(kwhARecargar);
        
        // Paso 3: actualizar métricas internas (común para todos)
        actualizarMetricas(vehiculo, coste);
        
        return coste;
    }
    
    // --- Métodos abstractos y hooks (para los hijos) ---
    
    /**
     * Comprueba si este cargador es compatible con el tipo de vehículo dado.
     * Cada tipo de cargador define sus propias reglas de compatibilidad.
     * 
     * @param vehiculo El vehiculo a comprobar.
     * @return true si es compatible, false en caso contrario.
     */
    protected abstract boolean esCompatible(ElectricVehicle vehiculo);
    
    /**
     * Calcula el coste de la carga.
     * La implementación base es (kwh * tarifa), pero las subclases pueden
     * aplicar descuentos o recargos (override)
     * 
     * @param kwh Cantidad de energía.
     * @return Precio final en euros.
     */
    protected float calcularCoste(int kwh) {
        return kwh * this.tarifaCarga;
    }
    
    // --- Métodos internos ---
    
    /**
     * Actualiza el registro de vehículos y la recaudación.
     */
    private void actualizarMetricas(ElectricVehicle vehiculo, float coste) {
        this.cantidadRecaudada += coste;
        
        if(vehiculo != null) {
            this.vehiculosRecargados.add(vehiculo);
        }
    }
    
    // -- Getters y setters comunes ---
    
    public String getId() { return id; }
    public int getVelocidadCarga() { return velocidadCarga; }
    public float getTarifaCarga() { return tarifaCarga; }
    public boolean estaLibre() { return estaLibre; }
    public void setEstaLibre(boolean estaLibre) { this.estaLibre = estaLibre; }
    public int getNumerEVRecharged() { return this.vehiculosRecargados.size(); }
    public float getCantidadRecaudada() { return cantidadRecaudada; }
    
    // --- Métodos de información (toString) ---
    
    /**
     * Devuelve la información completa del cargador y su historial.
     */
    public String getCompleteInfo() {
        StringBuilder info = new StringBuilder();
        info.append(this.toString());
        for(ElectricVehicle vehiculo : this.vehiculosRecargados) {
            info.append("\n");
            info.append(vehiculo.getInitialFinalInfo());
        }
        return info.toString();
    }
    
    // El toString será implementado/usado por las subclases, pero podemos dejar uno base
    // o forzar a que lo implementen si queremos que aparezca el nombre de la clase (ej: StandardCharger).
    // Para simplificar, lo dejamos aquí y usaremos getClass().getSimpleName() o similar en
    // los hijos.
    @Override
    public abstract String toString();
}