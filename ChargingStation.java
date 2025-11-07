import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Modela una estación de carga de vehículos eléctricos.
 * Una estación de carga contiene múltiples unidades {@link Charger} (cargadores)
 * @author Pablo Carrasco Caballero
 * @version 2025
 */
public class ChargingStation {
    // --- Atributos ---
    private String id;
    private String ciudad;
    private Location localizacion;
    private List<Charger> cargadores;

    // --- Constructor ---

    /**
     * Constructor para objetos de la clase ChargingStation.
     *
     * @param city La ciudad donde está la estacio´n.
     * @param id El identificador único de esa estación.
     * @param location La {@link Location} de la estación.
     */
    public ChargingStation(String city, String id, Location location) {
        String ciudadEstacion = city;
        String idEstacion = id;
        Location localizacionEstacion = location;

        this.id = idEstacion;
        this.ciudad = ciudadEstacion;
        this.localizacion = localizacionEstacion;

        this.cargadores = new ArrayList<>();
    }

    // --- Getters ---

    /**
     * Devuelve el identificador único de la estación.
     *
     * @return El 'id' de la estación.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Devuelve la localiización de la estación.
     *
     * @return El objeto {@link Location} de la estación.
     */
    public Location getLocation() {
        return this.localizacion;
    }

    /**
     * Devuelve una lista NO MODIFICABLE de todos los cargadores de la estación.
     * La lista devuelta respeta el orden.
     *
     * @return Una lista no modificable de {@link Charger}s.
     */
    public List<Charger> getChargers() {
        return Collections.unmodifiableList(this.cargadores);
    }

    /**
     * Busca y devuelve el primer {@link Charger} que esté liber.
     * La búsqueda respeta el orden de la colección.
     *
     * @return Un {@link Charger} libre, o {@code null} si no se encuentra ninguno libre.
     */
    public Charger getFreeCharger() {
        for (Charger cargador : this.cargadores) {
            if (cargador.estaLibre()) {
                return cargador;
            }
        }

        return null;
    }

    // --- Setters ---

    /**
     * Establece la localización actual de la estación de carga.
     *
     * @param location La nueva localización. No debe ser nula.
     * @throws NullPointerException Si la localización es nula.
     */
    public void setLocation(Location location) {
        Location nuevaLocalizacion = location;

        if (nuevaLocalizacion == null) {
            throw new NullPointerException("La localización no puede ser nula.");
        }
        this.localizacion = nuevaLocalizacion;
    }

    /**
     * Añade un nuevo {@link Charger} a la estación.
     * IMPORTANTE: después de añadir, este módulo RE-ORDENA la lista interna.
     *
     * @param charger El nuevo cargador a añadir.
     */
    public void addCharger(Charger charger) {
        Charger nuevoCargador = charger;

        if (nuevoCargador != null) {
            this.cargadores.add(nuevoCargador);

            this.cargadores.sort((c1,c2) -> {
                int velocidad1 = c1.getVelocidadCarga();
                int velocidad2 = c2.getVelocidadCarga();
                float tarifa1 = c1.getTarifaCarga();
                float tarifa2 = c2.getTarifaCarga();

                if (velocidad1 != velocidad2) {
                    return Integer.compare(velocidad2, velocidad1);
                }

                if (tarifa1 != tarifa2) {
                    return Float.compare(tarifa1, tarifa2);
                }

                return c1.getId().compareTo(c2.getId());
            });
        }
    }

    // --- Métodos de info. y estado ---

    /**
     * Calcula el número total de vehículos recargados en TODOS los cargadores
     * de esta estación.
     *
     * @return El número total de recargas.
     */
    public int getNumerEVRecharged() {
        int totalRecargas = 0;

        for (Charger cargador : this.cargadores) {
            totalRecargas += cargador.getNumerEVRecharged();
        }

        return totalRecargas;
    }

    /**
     * Devuelve una cadena con la información formateada de la estación.
     * Formato: (ChargingStation: ID, Ciudad, VehiculosRecargados, Localización)
     *
     * @return un String con la representación de la estación.
     */
    @Override
    public String toString() {
        return String.format("(ChargingStation: %s, %s, %d, %s)",
                this.id,
                this.ciudad,
                this.getNumerEVRecharged(),
                this.localizacion.toString());
    }

    /**
     * Devuelve una cadena con la información FINAL completa de la estación,
     * incluyendo los detalles de TODOS sus cargadores.
     *
     * @return Una cadena de varias líneas con la información completa.
     */
    public String getCompleteInfo() {
        StringBuilder info = new StringBuilder();

        info.append(this.toString());

        for (Charger cargador : this.cargadores) {
            info.append("\n");

            info.append(cargador.getCompleteInfo());
        }

        return info.toString();
    }

    /**
     * Muesta un resumen de información final.
     *
     * @return Un String con la representación de la estación.
     * @deprecated Considerar usar {@link #toString()} o {@link #getCompleteInfo()} en su lugar.
     */
    public String showFinalInfo() {
        return this.toString();
    }
}