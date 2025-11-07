import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Modela la operación de una compañía de vehículos eléctricos (VE).
 * Gestiona una flota de {@link ElectricVehicle}s (vehículos eléctricos) y una
 * red de {@link ChargingStation}s (estaciones de recarga).
 * @author Pablo Carrasco Caballero
 * @version 2025
 */
public class EVCompany {
    // --- Atributos ---
    public static final int COSTE_MOVIMIENTO_KWH = 5;
    private String nombre;
    private List<ElectricVehicle> vehiculosSuscritos;
    private List<ChargingStation> estaciones;

    // --- Constructor ---

    /**
     * Constructor para objetos de la clase EVCompany.
     * Inicializa el nombre de la compañía y las listas de vehículos
     * y estaciones.
     */
    public EVCompany() {
        this.nombre = "EVCharging Cáceres";

        // Inicializamos las listas como ArrayLists vacíos.
        // Para evitar un 'NullPointerException' cuando
        // intentemos añadir vehículos o estaciones.
        this.vehiculosSuscritos = new ArrayList<>();
        this.estaciones = new ArrayList<>();
    }

    // --- Métodos Públicos ---

    /**
     * Devuelve el nombre de la compañía.
     * @return El nombre de la compañía.
     */
    public String getName() {
        return this.nombre;
    }

    /**
     * Devuelve una lista NO MODIFICABLE de todos los vehículos suscritos.
     * Se devuelve como 'no modificable' para proteger la lista interna de la compañía.
     *
     * @return Una lista no modificable de {@link ElectricVehicle}s.
     */
    public List<ElectricVehicle> getVehicles() {
        return Collections.unmodifiableList(this.vehiculosSuscritos);
    }

    /**
     * Añade un {@link ElectricVehicle} a la flota de la compañía.
     * @param vehicle El vehículo eléctrico a añadir.
     */
    public void addElectricVehicle(ElectricVehicle vehicle) {
        ElectricVehicle vehiculo = vehicle;

        if (vehiculo != null) {
            this.vehiculosSuscritos.add(vehiculo);
        }
    }

    /**
     * Añade una {@link ChargingStation} a la red de compañía.
     * @param station La estación de recarga a añadir.
     */
    public void addChargingStation(ChargingStation station) {
        ChargingStation estacion = station;

        if (estacion != null) {
            this.estaciones.add(estacion);
        }
    }

    /**
     * Recupera una {@link ChargingStation} usando su ID único.
     *
     * @param location La {@link Location} de la estación a buscar.
     * @return La {@link ChargingStation} en la localización coincidente, o {@code null} si no se encuentra.
     */
    public ChargingStation getChargingStation(Location location) {
        Location localizacion = location;

        Iterator<ChargingStation> it = this.estaciones.iterator();

        while (it.hasNext()) {
            ChargingStation estacion = it.next();

            if (estacion.getLocation().equals(localizacion)) {
                return estacion;
            }
        }

        return null;
    }

    /**
     * Recupera una {@link ChargingStation} usando su {@link Location}.
     *
     * @param id El ID (String) de la estación a buscar.
     * @return La {@link ChargingStation} con el ID coincidente, o {@code null} si no se encuentra.
     */
    public ChargingStation getChargingStation(String id) {
        Iterator<ChargingStation> it = this.estaciones.iterator();

        while (it.hasNext()) {
            ChargingStation estacion = it.next();

            if (estacion.getId().equals(id)) {
                return estacion;
            }
        }

        return null;
    }

    /**
     * Devuelve una lista NO MODIFICABLE de todas las estaciones de recarga gestionadas.
     *
     * @return Una lista no modificable de {@link ChargingStation}s.
     */
    public List<ChargingStation> getCityStations() {
        return Collections.unmodifiableList(this.estaciones);
    }

    /**
     * Devuelve el número total de estaciones de recarga gestionadas.
     *
     * @return El número total de estaciones.
     */
    public int getNumberOfStations() {
        return this.estaciones.size();
    }

    /**
     * Limpia todos los vehículos y estaciones gestionados, reiniciando la compañía a un estado
     * vacío.
     */
    public void reset() {
        this.vehiculosSuscritos.clear();
        this.estaciones.clear();
    }
}