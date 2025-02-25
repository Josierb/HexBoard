package comp20050.hexboard;

import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

public class Hexagon {
    private int q, r, s; // Cube coordinates
    private Polygon hexShape;
    private Color owner; // Null if empty, otherwise RED or BLUE
    private Map<String, Hexagon> neighbors; // Stores references to neighbor hexes

    public Hexagon(int q, int r, int s, Polygon shape) {
        this.q = q;
        this.r = r;
        this.s = s;
        this.hexShape = shape;
        this.owner = null;
        this.neighbors = new HashMap<>();
    }

    public void setOwner(Color owner) {
        this.owner = owner;
        hexShape.setFill(owner);
    }

    public boolean isEmpty() {
        return owner == null;
    }

    public void addNeighbor(String direction, Hexagon neighbor) {
        neighbors.put(direction, neighbor);
    }

    public Hexagon getNeighbor(String direction) {
        return neighbors.get(direction);
    }

    public int getX() { return q; }
    public int getY() { return r; }
    public int getZ() { return s; }
    public Polygon getHexShape() { return hexShape; }
}
