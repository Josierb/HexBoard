package comp20050.hexboard;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HexagonTest {

    @Test
    void testHexagonCreation() {
        Polygon shape = new Polygon();
        Hexagon hex = new Hexagon(1, 2, 3, shape);


        assertEquals(1, hex.getX(), "X coordinate should be 1");
        assertEquals(2, hex.getY(), "Y coordinate should be 2");
        assertEquals(3, hex.getZ(), "Z coordinate should be 3");


        assertNull(hex.getOwner(), "Owner should be null when hexagon is created");
    }

    @Test
    void testSetOwner() {
        Polygon shape = new Polygon();
        Hexagon hex = new Hexagon(1, 2, 3, shape);


        hex.setOwner(Color.RED);

        assertEquals(Color.RED, hex.getOwner(), "Owner should be RED");

        assertEquals(Color.RED, shape.getFill(), "Shape's fill color should be RED");
    }

    @Test
    void testIsEmpty() {
        Polygon shape = new Polygon();
        Hexagon hex = new Hexagon(1, 2, 3, shape);


        assertTrue(hex.isEmpty(), "Hexagon should be empty");

        hex.setOwner(Color.BLUE);
        assertFalse(hex.isEmpty(), "Hexagon should not be empty when owned");
    }

    @Test
    void testAddAndGetNeighbors() {
        Polygon shape = new Polygon();
        Hexagon hex = new Hexagon(1, 2, 3, shape);
        Hexagon neighbor = new Hexagon(4, 5, 6, shape);

        hex.addNeighbor("north", neighbor);

        Map<String, Hexagon> neighbors = hex.getNeighbors();
        assertNotNull(neighbors, "Neighbors map should not be null");
        assertTrue(neighbors.containsKey("north"), "Should contain the 'north' neighbor");
        assertEquals(neighbor, neighbors.get("north"), "The neighbor should be the one we added");
    }
}
