package comp20050.hexboard;

import java.util.HashMap;
import java.util.Map;

public class HexGrid {
    private Map<String, Hexagon> hexMap = new HashMap<>(); // Stores hexagons by "x,y,z" keys

    public void addHex(Hexagon hex) {
        hexMap.put(hex.getX() + "," + hex.getY() + "," + hex.getZ(), hex);
    }

    public Hexagon getHex(int x, int y, int z) {
        return hexMap.get(x + "," + y + "," + z);
    }

    //NOT RIGHT
    public boolean isValidMove(int x, int y, int z) {

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
