/**
 * Modela una localización en una ciudad de cuadrícula bidimensional
 * usando coordenadas (x, y).
 *
 * @author Pablo Carrasco Caballero
 * @version 2025
 */
public class Location {
    // --- Atributos ---
    private int x;
    private int y;

    // --- Constructor ---

    /**
     * Modela una localización.
     *
     * @param x La coordenada x. Debe ser no-negativa.
     * @param y La coordenada y. Debe ser no-negativa.
     *
     * @throws IllegalArgumentException Si alguna coordenada fuera neativa.
     */
    public Location(int x, int y) {
        if (x < 0) {
            throw new IllegalArgumentException("Coordenada X negativa: " + x);
        }
        if (y < 0) {
            throw new IllegalArgumentException("Coordenada Y negativa: " + y);
        }

        this.x = x;
        this.y = y;
    }

    // --- Métodos de lógica de movimiento ---

    /**
     * Genera la siguiente localización en la ruta hacia el destino.
     * Sigue la lógica de movimiento.
     * 1. Si amabs coordenadas son diferentes, se mueve en diagonal.
     * 2. Si una coord. coincide, se mueve recto.
     *
     * @param destination La {@link Location} a la que queremos llegar.
     * @return La {@link Location} del siguiente paso, un paso más cerca.
     */
    public Location nextLocation(Location destination) {
        Location destino = destination;

        int proximaX = this.x;
        int proximaY = this.y;

        if (destino.getX() > this.x) {
            proximaX++;
        } else if (destino.getX() < this.x) {
            proximaX--;
        }

        if (destino.getY() > this.y) {
            proximaY++;
        } else if (destino.getY() < this.y) {
            proximaY--;
        }

        return new Location(proximaX, proximaY);
    }

    /**
     * Calcula la distancia a una localización destino.
     *
     * @param destination La {@link Location} destino.
     * @return El número de pasos requeridos.
     */
    public int distance(Location destination) {
        Location destino = destination;

        int diffX = Math.abs(this.x - destino.getX());

        int diffY = Math.abs(this.y - destino.getY());

        return Math.max(diffX, diffY);
    }

    // --- Métodos estándar ---

    /**
     * Compara esta localización con otro objeto para ver si son iguales.
     *
     * @param other El objeto a comparar.
     * @return {@code true} si el otro objeto es una Location y tiene las mismas coordenadas, {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object other) {
        Object otro = other;

        if (otro instanceof Location otraLocalizacion) {
            return this.x == otraLocalizacion.getX() && this.y == otraLocalizacion.getY();
        }

        return false;
    }

    /**
     * Devuelve una representación en String de la localización.
     *
     * @return un String en el formato x-y.
     */
    @Override
    public String toString() {
        return this.x + "-" + this.y;
    }

    /**
     * Genera un código hash único para la localización.
     * Esencial para el correcto funcionamiento.
     *
     * @return Un código hash para la localización.
     */
    @Override
    public int hashCode() {
        return (y << 16) + x;
    }

    // --- Getters ---

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}