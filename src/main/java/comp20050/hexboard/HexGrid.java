package comp20050.hexboard;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.shape.Polygon;


public class HexGrid {
    private Map<String, Hexagon> hexMap = new HashMap<>(); // Stores hexagons by "x,y,z" keys

    public void addHex(Hexagon hex) {
        String key = hex.getX() + "," + hex.getY() + "," + hex.getZ();
        hexMap.put(key, hex);
    }

    public Hexagon getHex(int x, int y, int z) {
        String key = x + "," + y + "," + z;
        return hexMap.getOrDefault(key, null);
    }

    public Hexagon getHexByShape(Polygon shape) {
        for (Hexagon hex : hexMap.values()) {  // Iterate through all hexagons in the grid
            if (hex.getHexShape() == shape) {  // Compare shapes
                return hex;
            }
        }
        return null; // Return null if no match is found
    }

    //NOT RIGHT
    public boolean isValidMove(int x, int y, int z) {
        return false;
    }

    public void linkNeighbors() {
        int[][] directions = {
                {1, -1, 0}, {-1, 1, 0}, {0, 1, -1}, {0, -1, 1}, {1, 0, -1}, {-1, 0, 1}
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
