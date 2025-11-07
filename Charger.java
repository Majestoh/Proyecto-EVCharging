import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Modela una unidad de cargador dentro de una {@link ChargingStation}.
 * Rastrea sus capacidades de carga, tarifa, estado y vehículos que ha recargado.
 *
 * @author Pablo Carrasco Caballero
 * @version 2025
 */
public class Charger {
    // --- Atributos ---
    private String id;
    private int velocidadCarga;
    private float tarifaCarga;
    private List<ElectricVehicle> vehiculosRecargados;
    private float cantidadRecaudada;
    private boolean estaLibre;

    // --- Constructor ---

    /**
     * Constructor para objetos de la clase Charger.
     *
     * @param id El identificador único del cargador.
     * @param speed La velocidad máxima de carga en kWh.
     * @param fee El coste por kWh.
     */
    public Charger(String id, int speed, float fee) {
        String idCargador = id;
        int velocidad = speed;
        float tarifa = fee;

        this.id = idCargador;
        this.velocidadCarga = velocidad;
        this.tarifaCarga = tarifa;

        this.vehiculosRecargados = new ArrayList<>();
        this.cantidadRecaudada = 0.0f;
        this.estaLibre = true;
    }

    // --- Getters ---

    /**
     * Devuelve el ID del cargador.
     *
     * @return El 'id' del cargador.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Devuelve la velocidad de carga del cargador.
     *
     * @return La velocidad de carga en kWh.
     */
    public int getVelocidadCarga() {
        return this.velocidadCarga;
    }

    /**
     * Devuelve la tarifa de carga del cargador.
     *
     * @return La tarifa de carga en €/kWh.
     */
    public float getTarifaCarga() {
        return this.tarifaCarga;
    }

    /**
     * Devuelve la cantidad total recaudada por este cargador.
     *
     * @return La cantidad recaudada en €.
     */
    public float getCantidadRecaudada() {
        return this.cantidadRecaudada;
    }

    /**
     * Devuelve el número total de vehículos que han recargado aquí.
     *
     * @return El número de vehículos en la lista 'vehiculosRecargados'.
     */
    public int getNumerEVRecharged() {
        return this.vehiculosRecargados.size();
    }

    /**
     * Comprueba si el cargador está libre.
     *
     * @return {@code true} si está libre {@code false} si está ocupado.
     */
    public boolean estaLibre() {
        return this.estaLibre;
    }

    // --- Setters ---

    /**
     * Establece el estado del cargador.
     * La clase ElectricVehicle usará esto al empezar y terminar una carga.
     *
     * @param estado {@code true} para marcar como libre, {@code false} para marcar como ocupado.
     */
    public void setEstaLibre(boolean estado) {
        this.estaLibre = estado;
    }

    /**
     * Añade un {@link ElectricVehicle} a la lista de vehículos recargados.
     *
     * @param vehicle El vehículo eléctrico.
     */
    public void addEvRecharged(ElectricVehicle vehicle) {
        ElectricVehicle vehiculo = vehicle;

        if (vehiculo != null) {
            this.vehiculosRecargados.add(vehiculo);
        }
    }

    /**
     * Actualiza la cantidad total recaudada.
     *
     * @param cantidad El coste de la recarga recién realizada.
     */
    private void actualizarCantidadRecaudada(float cantidad) {
        this.cantidadRecaudada += cantidad;
    }

    // --- Métodos ppales. ---

    /**
     * Simula el proceso de redcarga para un {@link ElectricVehicle}.
     * Calcula el coste, actualiza la cantidad recuadada y registra el vehículo como recargado.
     *
     * @param vehicle El vehículo a recargar.
     * @param kwsRecharging La cantidad de kWh a recargar.
     *
     * @return El coste total de esta operación.
     */
    public float recharge(ElectricVehicle vehicle, int kwsRecharging) {
        ElectricVehicle vehiculo = vehicle;
        int kwhARecargar = kwsRecharging;

        float costeRecarga = kwhARecargar * this.tarifaCarga;

        this.actualizarCantidadRecaudada(costeRecarga);

        this.addEvRecharged(vehiculo);

        return costeRecarga;
    }

    // --- Métodos de información ---

    /**
     * Devuelve una representación en String del cargador.
     * Formato: (Charger: ID, Velocidad, Tarifa, VehiculosRecargados, CantidadRecuadada)
     *
     * @return Una cadena formateada con la información del cargador.
     */
    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "(Charger, %s, %dkwh, %.1f€, %d, %.1f€)",
                this.id,
                this.velocidadCarga,
                this.tarifaCarga,
                this.getNumerEVRecharged(),
                this.cantidadRecaudada);
    }

    /**
     * Devuelve uan representación COMPLETA en String del cargador.
     * Incluye la información del propio cargador y la información de CADA vehículo que ha
     * recargado en él.
     *
     * @return Una cadena de varias líneas con la info. completa del cargador.
     */
    public String getCompleteInfo() {
        StringBuilder info = new StringBuilder();

        info.append(this.toString());

        for (ElectricVehicle vehiculo : this.vehiculosRecargados) {
            info.append("\n");

            info.append(vehiculo.getInitialFinalInfo());
        }

        return info.toString();
    }
}
