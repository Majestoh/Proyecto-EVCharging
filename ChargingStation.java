import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 * Modela una estación de carga de vehículos eléctricos.
 * Gestiona una colección de cargadores que se mantiene siempre ordenada
 * por velocidad (desc), tarifa (asc) e ID (asc).
 * 
 * @author Pablo Carrasco Caballero
 * @version 10.12.2025
 */
public class ChargingStation {
    // --- Atributos ---
    private String id;
    private String ciudad;
    private Location localizacion;
    private List<Charger> cargadores;
    
    // --- Constructor ---
    public ChargingStation(String city, String id, Location location) {
        // VERIFICACIONES DE NULIDAD BÁSICAS
        if (city == null || id == null || location == null) {
            throw new NullPointerException("Los parámetros de la estación no pueden ser nulos.");
        }
        
        this.id = id;
        this.ciudad = city;
        this.localizacion = location;
        this.cargadores = new ArrayList<>();
    }
    
    // --- Métodos de consulta ---
    public String getId() {
        return this.id;
    }
    
    public Location getLocation() {
        return this.localizacion;
    }
    
    /**
     * Devuelve la lista de cargadores de la estación (solo lectura).
     * Esencial para que los vehículos Premium o VTC puedan buscar su cargador ideal.
     */
    public List<Charger> getChargers() {
        // Devolvemos una vista no modificable para proteger la encapsulación.
        return Collections.unmodifiableList(this.cargadores);
    }
    
    public Charger getFreeCharger() {
        for (Charger cargador : this.cargadores) {
            if (cargador.estaLibre()) {
                return cargador;
            }
        }
        return null;
    }
    
    // --- Métodos de gestión ---
    public void setLocation(Location location) {
        if (location == null) {
            throw new NullPointerException("La localización no puede ser nula.");
        }
        this.localizacion = location;
    }
    
    /**
     * Añade un nuevo cargador a la estación y reordena la lista.
     * Criterios de ordenación según lo estipulado:
     * 1. Velocidad de carga (decreciente)
     * 2. Tarifa (creciente)
     * 3. ID (creciente)
     */
    public void addCharger(Charger charger) {
        if (charger != null) {
            this.cargadores.add(charger);
            
            // Reordenar la lista cada vez que añadimos uno.
            this.cargadores.sort((c1, c2) -> {
                // 1. Velocidad (mayor a menor)
                int compVelocidad = Integer.compare(c2.getVelocidadCarga(), c1.getVelocidadCarga());
                if (compVelocidad != 0) {
                    return compVelocidad;
                }
                
                // 2. Tarifa (menor a mayor)
                int compTarifa = Float.compare(c1.getTarifaCarga(), c2.getTarifaCarga());
                if (compTarifa != 0) {
                    return compTarifa;
                }
                
                // 3. ID (alfábetico o númerico creciente)
                return c1.getId().compareTo(c2.getId());
            });
        }   
    }
    
    public int getNumerEVRecharged() {
        int totalRecargas = 0;
        for (Charger cargador : this.cargadores) {
            totalRecargas += cargador.getNumerEVRecharged();
        }
        return totalRecargas;
    }
    
    // --- Información ---
    
    @Override
    public String toString() {
        // Formato: (ChargingStation: CC00, Cáceres, 0, 5-5)
        return String.format("(ChargingStation: %s, %s, %d, %s)",
            this.id,
            this.ciudad,
            this.getNumerEVRecharged(),
            this.localizacion.toString());
    }
    
    public String getCompleteInfo() {
        StringBuilder info = new StringBuilder();
        info.append(this.toString());
        // Delegamos en el toString() polimórfico de cada cargador.
        for (Charger cargador : this.cargadores) {
            info.append("\n");
            info.append(cargador.getCompleteInfo());
        }
        return info.toString();
    }
}