package comp20050.hexboard;

import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

public class HexGridTest {
    private HexGrid grid;
    private Hexagon hex1;
    private Hexagon hex2;
    private Hexagon hex3;
    private Polygon shape1;
    private Polygon shape2;

    @BeforeEach
    public void setUp() {
        grid = new HexGrid();
        shape1 = new Polygon();
        shape2 = new Polygon();

        hex1 = new Hexagon(0, 0, 0, shape1);
        hex2 = new Hexagon(1, -1, 0, shape2);
        hex3 = new Hexagon(0, 1, -1, new Polygon());
    }

    @Test
    public void testAddAndGetHex() {
        grid.addHex(hex1);
        Hexagon retrieved = grid.getHex(0, 0, 0);

        assertNotNull(retrieved);
        assertEquals(0, retrieved.getX());
        assertEquals(0, retrieved.getY());
        assertEquals(0, retrieved.getZ());
        assertEquals(shape1, retrieved.getHexShape());
    }

    @Test
    public void testGetHexByShape() {
        grid.addHex(hex1);
        grid.addHex(hex2);

        assertEquals(hex1, grid.getHexByShape(shape1));
        assertEquals(hex2, grid.getHexByShape(shape2));
        assertNull(grid.getHexByShape(new Polygon()));
    }

    @Test
    public void testLinkNeighborsCreatesProperConnections() {
        grid.addHex(hex1);
        grid.addHex(hex2);
        grid.addHex(hex3);
        grid.linkNeighbors();

        Map<String, Hexagon> hex1Neighbors = hex1.getNeighbors();
        assertEquals(2, hex1Neighbors.size());
        assertTrue(hex1Neighbors.containsKey("1,-1,0"));
        assertTrue(hex1Neighbors.containsKey("0,1,-1"));

        Map<String, Hexagon> hex2Neighbors = hex2.getNeighbors();
        assertEquals(1, hex2Neighbors.size());
        assertTrue(hex2Neighbors.containsKey("0,0,0"));
    }


    @Test
    public void testHexagonOwnershipAffectsGrid() {
        hex1.setOwner(Color.RED);
        grid.addHex(hex1);

        Hexagon retrieved = grid.getHex(0, 0, 0);
        assertEquals(Color.RED, retrieved.getOwner());
        assertFalse(retrieved.isEmpty());
    }

    @Test
    public void testIsValidMove() {
        // Current implementation always returns false
        assertFalse(grid.isValidMove(0, 0, 0));

        // Test with actual hexagons
        grid.addHex(hex1);
        assertFalse(grid.isValidMove(0, 0, 0));
        assertFalse(grid.isValidMove(1, -1, 0));
    }

    @Test
    public void testEdgeCases() {
        // Test null values
        assertThrows(NullPointerException.class, () -> grid.addHex(null));

        // Test getting non-existent hexagon
        assertNull(grid.getHex(Integer.MAX_VALUE, Integer.MIN_VALUE, 0));

        // Test with hexagon that has null shape (should now throw exception)
        assertThrows(IllegalArgumentException.class, () -> {
            new Hexagon(2, -1, -1, null);
        });
    }

    @Test
    public void testCoordinateSystemConsistency() {
        // Verify that q+r+s always equals 0 for valid hex coordinates
        Hexagon invalidHex = new Hexagon(1, 1, 1, new Polygon());
        grid.addHex(invalidHex);

        // The grid still stores it, but it won't have proper neighbors
        assertNotNull(grid.getHex(1, 1, 1));
        grid.linkNeighbors();
        assertTrue(invalidHex.getNeighbors().isEmpty());
    }
}