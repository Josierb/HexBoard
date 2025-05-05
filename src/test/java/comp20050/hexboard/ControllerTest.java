package comp20050.hexboard;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.HashSet;
import java.util.Set;

public class ControllerTest {

    @Test
    void testIsValidMove_allowsFirstMove() {
        Controller controller = new Controller();

        Polygon dummyPolygon = new Polygon();
        Hexagon dummyHex = new Hexagon(0, 0, 0, dummyPolygon);

        // No neighbors, no owner
        boolean isValid = controller.isValidMove(dummyHex, Color.BLUE);

        assertTrue(isValid, "Should allow first placement if hex is empty and has no neighbors.");
    }

    @Test
    void testFindConnectedGroupSize_singleHex() {
        Polygon p = new Polygon();
        Hexagon hex = new Hexagon(0, 0, 0, p);
        hex.setOwner(Color.ORANGE);

        Set<Hexagon> visited = new HashSet<>();
        int size = Controller.findConnectedGroupSize(hex, Color.ORANGE, visited);

        assertEquals(1, size, "A single colored hex should count as a group of 1.");
    }

    @Test
    void testFindConnectedGroupSize_twoConnectedHexes() {
        Polygon p1 = new Polygon();
        Polygon p2 = new Polygon();

        Hexagon h1 = new Hexagon(0, 0, 0, p1);
        Hexagon h2 = new Hexagon(1, -1, 0, p2);

        h1.setOwner(Color.BLUE);
        h2.setOwner(Color.BLUE);

        h1.getNeighbors().put("E", h2);  // Arbitrary direction
        h2.getNeighbors().put("W", h1);

        Set<Hexagon> visited = new HashSet<>();
        int size = Controller.findConnectedGroupSize(h1, Color.BLUE, visited);

        assertEquals(2, size, "Two connected hexes of the same color should be a group of 2.");
    }
}
