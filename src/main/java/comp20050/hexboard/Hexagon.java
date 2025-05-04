package comp20050.hexboard;

import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a single hexagon tile in the game grid, using cube coordinates (q, r, s).
 * Each hexagon has an owner (null if unclaimed), a shape (Polygon), and a set of neighbors.
 */
public class Hexagon {
    private int q, r, s; // Cube coordinates
    private Polygon hexShape;
    private Color owner; // Null if empty, otherwise RED or BLUE
    private Map<String, Hexagon> neighbors; // Stores references to neighbor hexes

    /**
     * Constructs a Hexagon with given cube coordinates and a Polygon shape.
     * @param q The q coordinate
     * @param r The r coordinate
     * @param s The s coordinate
     * @param shape The visual representation as a Polygon
     */
    public Hexagon(int q, int r, int s, Polygon shape) {
        if (shape == null) {
            throw new IllegalArgumentException("Hexagon shape cannot be null");
        }
        this.q = q;
        this.r = r;
        this.s = s;
        this.hexShape = shape;
        this.owner = null;
        this.neighbors = new HashMap<>();
    }

    /**
     * Sets the owner of this hexagon and updates its visual color.
     * @param owner The player's color (BLUE or ORANGE)
     */
    public void setOwner(Color owner) {
        this.owner = owner;
        hexShape.setFill(owner);
    }

    /**
     * Gets the current owner of the hexagon.
     * @return The color of the owner, or null if unclaimed
     */
    public Color getOwner() {
        return this.owner;
    }

    /**
     * Checks if the hexagon is unclaimed.
     * @return true if no owner is set, false otherwise
     */
    public boolean isEmpty() {
        return owner == null;
    }

    /**
     * Adds a neighboring hexagon in a specified direction.
     * @param direction The direction label (e.g., "1,-1,0")
     * @param neighbor The neighboring hexagon to link
     */
    public void addNeighbor(String direction, Hexagon neighbor) {
        neighbors.put(direction, neighbor);
    }

    /**
     * Retrieves all neighbors of this hexagon.
     * @return A map of direction strings to neighboring Hexagon objects
     */
    public Map<String, Hexagon> getNeighbors() {
        return neighbors;
    }

    public int getX() { return q; }
    public int getY() { return r; }
    public int getZ() { return s; }
    public Polygon getHexShape() { return hexShape; }
}
