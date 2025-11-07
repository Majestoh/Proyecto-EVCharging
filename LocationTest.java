import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas de implementación de la clase {@link Location}.
 * Proporciona tests unitarios para los métodos {@code distance()} y {@code nextLocation()}.
 *
 * @author Pablo Carrasco Caballero
 * @version 2025
 */
public class LocationTest {
    // --- Objetos ---
    private Location locReferencia;
    private Location p0, p1;
    private Location paso1, paso2, paso3, paso4;

    // --- Constructor ---
    public LocationTest() {

    }

    /**
     * Preparamos el entorno de prueba.
     * Se llama ANTES de cada módulo de prueba (@Test)
     * Aquí creamos los objetos Location que usaremos.
     */
    @Before
    public void setUp() {
        locReferencia = new Location(5, 5);

        p0 = new Location(0, 0);
        p1 = new Location(1, 5);

        paso1 = new Location(1, 1);
        paso2 = new Location(1, 2);
        paso3 = new Location(1, 3);
        paso4 = new Location(1, 4);
    }

    /**
     * Limpiamos el entorno de prueba.
     * Se llama DESPUÉS de cada módulo de prueba (@Test).
     * No es realmente necesario.
     */
    @After
    public void tearDown() {

    }

    /**
     * Probamos el módulo {@code distance} de la clase {@link Location}.
     * Comprobamos las distancias desde una localización central a puntos
     * circundantes y ejemplos señalados.
     */
    @Test
    public void testDistance() {
        // --- Casos basados en las pautas ---
        // Ejemplo 1. p0(0, 0) a p1(1, 5) debe ser 5.
        assertEquals(5, p0.distance(p1));

        // Ejemplo 2. p2(6, 6) a p3(5, 2) debe ser 4.
        Location p2 = new Location(6, 6);
        Location p3 = new Location(5, 2);
        assertEquals(6, p2.distance(p3));

        // --- Pruebas adicionales ---
        // Prueba 1. Distancia a sí mismo (debería ser 0).
        assertEquals(0, locReferencia.distance(new Location(5,5)));

        // Prueba 2. Distancia en línea recta del eje X.
        assertEquals(3, locReferencia.distance(new Location(5, 8)));

        // Prueba 3. Distancia en línea recta del eje Y.
        assertEquals(4, locReferencia.distance(new Location(1,5)));

        // Prueba 4. Distancia diagonal (máx: diffX).
        assertEquals(4, locReferencia.distance(new Location(9,7)));

        // Prueba 5. Distancia diagonal (máx: diffY).
        assertEquals(5, locReferencia.distance(new Location(8,0)));
    }

    /**
     * Probamos el módulo {@code nextLocation} cuando el destino es adyacente
     * El resultado debería ser la propia localización destino.
     */
    @Test
    public void testAdjacentLocations() {
        Location destArribaIzq = new Location(4, 4);
        Location destArriba = new Location(4, 5);
        Location destArribaDer = new Location(4, 6);
        Location destIzq = new Location(5,4);
        Location destDer = new Location(5,6);
        Location destAbajoIzq = new Location(6,4);
        Location destAbajo = new Location(6,5);
        Location destAbajoDer = new Location(6,6);

        assertEquals(destArribaIzq, locReferencia.nextLocation(destArribaIzq));
        assertEquals(destArriba, locReferencia.nextLocation(destArriba));
        assertEquals(destArribaDer, locReferencia.nextLocation(destArribaDer));
        assertEquals(destIzq, locReferencia.nextLocation(destIzq));
        assertEquals(destDer, locReferencia.nextLocation(destDer));
        assertEquals(destAbajoIzq, locReferencia.nextLocation(destAbajoIzq));
        assertEquals(destAbajo, locReferencia.nextLocation(destAbajo));
        assertEquals(destAbajoDer, locReferencia.nextLocation(destAbajoDer));
    }

    /**
     * Probamos el módulo {@code nextLocation} cuando el destino no es adyacente.
     */
    @Test
    public void testNonAdjacentLocations() {
        // 1. Primer paso: de (0,0) a (1,5) -> debe ser (1,1) [cite: 43]
        Location next = p0.nextLocation(p1);
        assertEquals(paso1, next); // Compara (1,1)

        // (x igual, y distinta -> se mueve recto)

        // 2. Segundo paso: de (1,1) a (1,5) -> debe ser (1,2) [cite: 48]
        next = next.nextLocation(p1);
        assertEquals(paso2, next); // Compara (1,2)

        // 3. Tercer paso: de (1,2) a (1,5) -> debe ser (1,3) [cite: 48]
        next = next.nextLocation(p1);
        assertEquals(paso3, next); // Compara (1,3)

        // 4. Cuarto paso: de (1,3) a (1,5) -> debe ser (1,4) [cite: 48]
        next = next.nextLocation(p1);
        assertEquals(paso4, next); // Compara (1,4)

        // 5. Quinto paso: de (1,4) a (1,5) -> debe ser (1,5) (adyacente)
        next = next.nextLocation(p1);
        assertEquals(p1, next); // Compara (1,5)

        // 6. Estando en el destino: de (1,5) a (1,5) -> debe ser (1,5) (no se mueve)
        next = next.nextLocation(p1);
        assertEquals(p1, next); // Compara (1,5)
    }
}