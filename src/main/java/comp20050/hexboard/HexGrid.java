package comp20050.hexboard;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.shape.Polygon;

/**
 * Manages the collection of Hexagon tiles in a hexagonal grid.
 * Provides methods to add, retrieve, and link hexagons by cube coordinates.
 */
public class HexGrid {
    private Map<String, Hexagon> hexMap = new HashMap<>(); // Stores hexagons by "x,y,z" keys

    /**
     * Adds a hexagon to the grid using its cube coordinates as a key.
     * @param hex The Hexagon to be added
     */
    public void addHex(Hexagon hex) {
        if (hex == null) {
            throw new NullPointerException("hex is null");
        }
        String key = hex.getX() + "," + hex.getY() + "," + hex.getZ();
        hexMap.put(key, hex);
    }

    /**
     * Retrieves a hexagon from the grid by its cube coordinates.
     * @param x q-coordinate
     * @param y r-coordinate
     * @param z s-coordinate
     * @return The Hexagon at the given location or null if not present or out of bounds
     */
    public Hexagon getHex(int x, int y, int z) {
        if (x > 6 || x < -6 || y > 6 || y < -6 || z > 6 || z < -6) {
            return null;
        }
        String key = x + "," + y + "," + z;
        return hexMap.getOrDefault(key, null);
    }

    /**
     * Finds the Hexagon associated with a given JavaFX Polygon shape.
     * Used for mapping clicks to logical grid tiles.
     * @param shape The JavaFX Polygon
     * @return The matching Hexagon, or null if not found
     */
    public Hexagon getHexByShape(Polygon shape) {
        for (Hexagon hex : hexMap.values()) {
            if (hex.getHexShape() == shape) {
                return hex;
            }
        }
        return null;
    }

    // Placeholder: This method is unimplemented.
    public boolean isValidMove(int x, int y, int z) {
        return false;
    }

    /**
     * Gets the number of hexagons in the grid.
     * @return Total hex count
     */
    public int getHexCount() {
        return hexMap.size();
    }

    /**
     * Links all neighboring hexagons based on cube coordinate directions.
     * Six possible directions are checked for each hex.
     */
    public void linkNeighbors() {
        int[][] directions = {
                {1, -1, 0}, {-1, 1, 0},
                {0, 1, -1}, {0, -1, 1},
                {1, 0, -1}, {-1, 0, 1}
        };

        for (Hexagon hex : hexMap.values()) {
            for (int[] dir : directions) {
                int nx = hex.getX() + dir[0];
                int ny = hex.getY() + dir[1];
                int nz = hex.getZ() + dir[2];

                Hexagon neighbor = getHex(nx, ny, nz);
                if (neighbor != null) {
                    hex.addNeighbor(nx + "," + ny + "," + nz, neighbor);
                }
            }
        }
    }
}
