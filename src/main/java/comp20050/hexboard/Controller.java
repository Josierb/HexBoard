package comp20050.hexboard;

import java.net.URL;
import java.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Controller {

    // Set up Grid for storing Hexagon objects
    private HexGrid hexGrid = new HexGrid();
    // Set up Map for linking the FXML Polygons to Hexagon objects
    private Map<Polygon, Hexagon> polygonToHexMap = new HashMap<>();

    // Boolean for tracking which players turn it is
    private boolean isBlueTurn = true;

    // Set up for audio used
    private AudioClip stonePlacement;
    private MediaPlayer bgMusicPlayer;

    // Set up colours used
    Color hexNavy = Color.web("#010437");
    Color tronBlue = Color.web("#08F7FE"); // Tron Neon Blue
    Color tronOrange = Color.web("#FF8000"); // Tron Neon Orange

    // Set up Label for displaying who's turn it is
    @FXML
    private Label turnlabel;

    // Method for associating the polygon and hexagon object
    @FXML
    void getHexID(MouseEvent event) {
        Polygon hexagon = (Polygon) event.getSource();
        Hexagon clickedHex = hexGrid.getHexByShape(hexagon);

        if (clickedHex == null) {
            System.out.println("Hexagon not found!");
            return;
        }

        // Call makeMove method with associated Hexagon and Polygon
        makeMove(clickedHex, hexagon);
    }

    // Method for making a move
    private void makeMove(Hexagon clickedHex, Polygon hexagon) {
        // Determine the current player's color based on the turn
        Color currentColor = isBlueTurn ? tronBlue : tronOrange;

        // If the hexagon is unclaimed, allow the move
        if (hexagon.getFill().equals(Color.DARKMAGENTA)) {
            hexagon.setFill(currentColor);
            clickedHex.setOwner(currentColor);

            // Update the turn indicator
            if (isBlueTurn) {
                turnlabel.setText("Orange's Turn");
                turnlabel.setTextFill(tronOrange);
            } else {
                turnlabel.setText("Blue's Turn");
                turnlabel.setTextFill(tronBlue);
            }

            // Play sound effect and switch turn
            stonePlacement.play();
            isBlueTurn = !isBlueTurn;
        }
    }

    // Method for features called when hexagon is hovered on
    @FXML
    void onHexHover(MouseEvent event) {

        Polygon hexagon = (Polygon) event.getSource();
        Hexagon hoveredHex = hexGrid.getHexByShape(hexagon); // Get the Hexagon object

        if (hoveredHex == null) {
            System.out.println("Hovered hex not found!");
            return;
        }

        Color currentColor = isBlueTurn ? tronBlue : tronOrange;

        if(isValidMove(hexagon, hoveredHex, currentColor)) {
            // Change color when hovered
            if (hexagon.getFill().equals(hexNavy)) {
                hexagon.setFill(Color.DARKMAGENTA);
            }
        }

    }

    boolean isValidMove(Polygon hexagon, Hexagon hoveredHex, Color currentColor) {
        // Check each neighbor of the clicked hexagon
        List<Hexagon> neighbors = getHexNeighbors(hoveredHex);
        for (Hexagon neighbor : neighbors) {
            // If any neighbor's fill color is the same as currentColor, block the move.
            if (neighbor.getHexShape().getFill().equals(currentColor)) {
                System.out.println("Invalid move! Cannot place next to a hexagon of the same color.");
                return false;
            }
        }
        return true;
    }

    // Method for creating List of all neighbours of a hexagon
    private List<Hexagon> getHexNeighbors(Hexagon hex) {
        // List for storing neighbours
        List<Hexagon> neighbors = new ArrayList<>();
        if (hex != null) {
            // Add all neighbor Hexagon objects from the hex's neighbors map
            neighbors.addAll(hex.getNeighbors().values());
        }
        // Return the list of all neighbours
        return neighbors;
    }





    //added this Mar 25
    private boolean isCaptureMove(Hexagon clickedHex) {
        //set current color
        Color currentColor = clickedHex.getOwner();
        // Get the opponent's color
        Color opponentColor = (currentColor.equals(tronBlue)) ? tronOrange : tronBlue;

       //find group of adjacent hex with current color
        Set<Hexagon> connectedGroup = findConnectedGroup(clickedHex, currentColor, new HashSet<>());

        boolean captureMove = false;


        return captureMove;
    }

    /**
     * Finds all connected hexagons of the same color starting from a given hexagon
     * @param startHex The hexagon to start checking from
     * @param targetColor The color to match (should match startHex's color)
     * @param visited Set to track visited hexagons (pass empty HashSet initially)
     * @return Set of all connected hexagons with matching color
     */
    public static Set<Hexagon> findConnectedGroup(Hexagon startHex, Color targetColor, Set<Hexagon> visited) {
        //return length
        Set<Hexagon> connectedGroup = new HashSet<>();

        // Base cases:
        // 1. Hex is null
        // 2. Hex is empty (no owner)
        // 3. Hex color doesn't match target
        // 4. Hex already visited
        if (startHex == null || startHex.isEmpty() ||
                !targetColor.equals(startHex.getOwner()) ||
                visited.contains(startHex)) {
            return connectedGroup;
        }

        // Add current hex to group and mark as visited
        connectedGroup.add(startHex);
        visited.add(startHex);

        // Recursively check all neighbors using the Hexagon class's neighbor map
        for (Hexagon neighbor : startHex.getNeighbors().values()) {
            connectedGroup.addAll(findConnectedGroup(neighbor, targetColor, visited));
        }

        return connectedGroup;
    }


    public void testAndPrintConnectedGroup() {
        // Create a simple hexagon grid for testing
        Hexagon centerHex = new Hexagon(0, 0, 0, new Polygon());
        Hexagon northHex = new Hexagon(0, -1, 1, new Polygon());
        Hexagon northeastHex = new Hexagon(1, -1, 0, new Polygon());

        // Set up connections
        centerHex.addNeighbor("N", northHex);
        centerHex.addNeighbor("NE", northeastHex);
        northHex.addNeighbor("S", centerHex);
        northeastHex.addNeighbor("SW", centerHex);

        // Set colors - make a small blue group
        centerHex.setOwner(tronBlue);
        northHex.setOwner(tronBlue);
        northeastHex.setOwner(tronOrange); // Different color

        // Test the group
        System.out.println("Testing connected group from center hex:");
        Set<Hexagon> connectedGroup = findConnectedGroup(centerHex, tronBlue, new HashSet<>());

        // Print results
        System.out.println("Found " + connectedGroup.size() + " connected hexes:");
        for (Hexagon hex : connectedGroup) {
            System.out.println("Hex at (" + hex.getX() + "," + hex.getY() + "," + hex.getZ() + ")");
        }

        // Expected: Should find centerHex and northHex (2 hexes)
        // northeastHex should not be included (different color)
        assert connectedGroup.size() == 2 : "Should find 2 connected blue hexes";
    }






    // Button for quitting
    @FXML
    private Button quitbutton;

    // Method for quitting game when quitbutton is clicked
    @FXML
    void quit(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void exitHexHover(MouseEvent event) {
        Polygon hexagon = (Polygon) event.getSource();
        if (hexagon.getFill().equals(Color.DARKMAGENTA)) {
            hexagon.setFill(hexNavy);
        }
    }


    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="hex1"
    private Polygon hex1; // Value injected by FXMLLoader

    @FXML // fx:id="hex10"
    private Polygon hex10; // Value injected by FXMLLoader

    @FXML // fx:id="hex100"
    private Polygon hex100; // Value injected by FXMLLoader

    @FXML // fx:id="hex101"
    private Polygon hex101; // Value injected by FXMLLoader

    @FXML // fx:id="hex102"
    private Polygon hex102; // Value injected by FXMLLoader

    @FXML // fx:id="hex103"
    private Polygon hex103; // Value injected by FXMLLoader

    @FXML // fx:id="hex104"
    private Polygon hex104; // Value injected by FXMLLoader

    @FXML // fx:id="hex105"
    private Polygon hex105; // Value injected by FXMLLoader

    @FXML // fx:id="hex106"
    private Polygon hex106; // Value injected by FXMLLoader

    @FXML // fx:id="hex107"
    private Polygon hex107; // Value injected by FXMLLoader

    @FXML // fx:id="hex108"
    private Polygon hex108; // Value injected by FXMLLoader

    @FXML // fx:id="hex109"
    private Polygon hex109; // Value injected by FXMLLoader

    @FXML // fx:id="hex11"
    private Polygon hex11; // Value injected by FXMLLoader

    @FXML // fx:id="hex110"
    private Polygon hex110; // Value injected by FXMLLoader

    @FXML // fx:id="hex111"
    private Polygon hex111; // Value injected by FXMLLoader

    @FXML // fx:id="hex112"
    private Polygon hex112; // Value injected by FXMLLoader

    @FXML // fx:id="hex113"
    private Polygon hex113; // Value injected by FXMLLoader

    @FXML // fx:id="hex114"
    private Polygon hex114; // Value injected by FXMLLoader

    @FXML // fx:id="hex115"
    private Polygon hex115; // Value injected by FXMLLoader

    @FXML // fx:id="hex116"
    private Polygon hex116; // Value injected by FXMLLoader

    @FXML // fx:id="hex117"
    private Polygon hex117; // Value injected by FXMLLoader

    @FXML // fx:id="hex118"
    private Polygon hex118; // Value injected by FXMLLoader

    @FXML // fx:id="hex119"
    private Polygon hex119; // Value injected by FXMLLoader

    @FXML // fx:id="hex12"
    private Polygon hex12; // Value injected by FXMLLoader

    @FXML // fx:id="hex120"
    private Polygon hex120; // Value injected by FXMLLoader

    @FXML // fx:id="hex121"
    private Polygon hex121; // Value injected by FXMLLoader

    @FXML // fx:id="hex122"
    private Polygon hex122; // Value injected by FXMLLoader

    @FXML // fx:id="hex123"
    private Polygon hex123; // Value injected by FXMLLoader

    @FXML // fx:id="hex124"
    private Polygon hex124; // Value injected by FXMLLoader

    @FXML // fx:id="hex125"
    private Polygon hex125; // Value injected by FXMLLoader

    @FXML // fx:id="hex126"
    private Polygon hex126; // Value injected by FXMLLoader

    @FXML // fx:id="hex127"
    private Polygon hex127; // Value injected by FXMLLoader

    @FXML // fx:id="hex13"
    private Polygon hex13; // Value injected by FXMLLoader

    @FXML // fx:id="hex14"
    private Polygon hex14; // Value injected by FXMLLoader

    @FXML // fx:id="hex15"
    private Polygon hex15; // Value injected by FXMLLoader

    @FXML // fx:id="hex16"
    private Polygon hex16; // Value injected by FXMLLoader

    @FXML // fx:id="hex17"
    private Polygon hex17; // Value injected by FXMLLoader

    @FXML // fx:id="hex18"
    private Polygon hex18; // Value injected by FXMLLoader

    @FXML // fx:id="hex19"
    private Polygon hex19; // Value injected by FXMLLoader

    @FXML // fx:id="hex2"
    private Polygon hex2; // Value injected by FXMLLoader

    @FXML // fx:id="hex20"
    private Polygon hex20; // Value injected by FXMLLoader

    @FXML // fx:id="hex21"
    private Polygon hex21; // Value injected by FXMLLoader

    @FXML // fx:id="hex22"
    private Polygon hex22; // Value injected by FXMLLoader

    @FXML // fx:id="hex23"
    private Polygon hex23; // Value injected by FXMLLoader

    @FXML // fx:id="hex24"
    private Polygon hex24; // Value injected by FXMLLoader

    @FXML // fx:id="hex25"
    private Polygon hex25; // Value injected by FXMLLoader

    @FXML // fx:id="hex26"
    private Polygon hex26; // Value injected by FXMLLoader

    @FXML // fx:id="hex27"
    private Polygon hex27; // Value injected by FXMLLoader

    @FXML // fx:id="hex28"
    private Polygon hex28; // Value injected by FXMLLoader

    @FXML // fx:id="hex29"
    private Polygon hex29; // Value injected by FXMLLoader

    @FXML // fx:id="hex3"
    private Polygon hex3; // Value injected by FXMLLoader

    @FXML // fx:id="hex30"
    private Polygon hex30; // Value injected by FXMLLoader

    @FXML // fx:id="hex31"
    private Polygon hex31; // Value injected by FXMLLoader

    @FXML // fx:id="hex32"
    private Polygon hex32; // Value injected by FXMLLoader

    @FXML // fx:id="hex33"
    private Polygon hex33; // Value injected by FXMLLoader

    @FXML // fx:id="hex34"
    private Polygon hex34; // Value injected by FXMLLoader

    @FXML // fx:id="hex35"
    private Polygon hex35; // Value injected by FXMLLoader

    @FXML // fx:id="hex36"
    private Polygon hex36; // Value injected by FXMLLoader

    @FXML // fx:id="hex37"
    private Polygon hex37; // Value injected by FXMLLoader

    @FXML // fx:id="hex38"
    private Polygon hex38; // Value injected by FXMLLoader

    @FXML // fx:id="hex39"
    private Polygon hex39; // Value injected by FXMLLoader

    @FXML // fx:id="hex4"
    private Polygon hex4; // Value injected by FXMLLoader

    @FXML // fx:id="hex40"
    private Polygon hex40; // Value injected by FXMLLoader

    @FXML // fx:id="hex41"
    private Polygon hex41; // Value injected by FXMLLoader

    @FXML // fx:id="hex42"
    private Polygon hex42; // Value injected by FXMLLoader

    @FXML // fx:id="hex43"
    private Polygon hex43; // Value injected by FXMLLoader

    @FXML // fx:id="hex44"
    private Polygon hex44; // Value injected by FXMLLoader

    @FXML // fx:id="hex45"
    private Polygon hex45; // Value injected by FXMLLoader

    @FXML // fx:id="hex46"
    private Polygon hex46; // Value injected by FXMLLoader

    @FXML // fx:id="hex47"
    private Polygon hex47; // Value injected by FXMLLoader

    @FXML // fx:id="hex48"
    private Polygon hex48; // Value injected by FXMLLoader

    @FXML // fx:id="hex49"
    private Polygon hex49; // Value injected by FXMLLoader

    @FXML // fx:id="hex5"
    private Polygon hex5; // Value injected by FXMLLoader

    @FXML // fx:id="hex50"
    private Polygon hex50; // Value injected by FXMLLoader

    @FXML // fx:id="hex51"
    private Polygon hex51; // Value injected by FXMLLoader

    @FXML // fx:id="hex52"
    private Polygon hex52; // Value injected by FXMLLoader

    @FXML // fx:id="hex53"
    private Polygon hex53; // Value injected by FXMLLoader

    @FXML // fx:id="hex54"
    private Polygon hex54; // Value injected by FXMLLoader

    @FXML // fx:id="hex55"
    private Polygon hex55; // Value injected by FXMLLoader

    @FXML // fx:id="hex56"
    private Polygon hex56; // Value injected by FXMLLoader

    @FXML // fx:id="hex57"
    private Polygon hex57; // Value injected by FXMLLoader

    @FXML // fx:id="hex58"
    private Polygon hex58; // Value injected by FXMLLoader

    @FXML // fx:id="hex59"
    private Polygon hex59; // Value injected by FXMLLoader

    @FXML // fx:id="hex6"
    private Polygon hex6; // Value injected by FXMLLoader

    @FXML // fx:id="hex60"
    private Polygon hex60; // Value injected by FXMLLoader

    @FXML // fx:id="hex61"
    private Polygon hex61; // Value injected by FXMLLoader

    @FXML // fx:id="hex62"
    private Polygon hex62; // Value injected by FXMLLoader

    @FXML // fx:id="hex63"
    private Polygon hex63; // Value injected by FXMLLoader

    @FXML // fx:id="hex64"
    private Polygon hex64; // Value injected by FXMLLoader

    @FXML // fx:id="hex65"
    private Polygon hex65; // Value injected by FXMLLoader

    @FXML // fx:id="hex66"
    private Polygon hex66; // Value injected by FXMLLoader

    @FXML // fx:id="hex67"
    private Polygon hex67; // Value injected by FXMLLoader

    @FXML // fx:id="hex68"
    private Polygon hex68; // Value injected by FXMLLoader

    @FXML // fx:id="hex69"
    private Polygon hex69; // Value injected by FXMLLoader

    @FXML // fx:id="hex7"
    private Polygon hex7; // Value injected by FXMLLoader

    @FXML // fx:id="hex70"
    private Polygon hex70; // Value injected by FXMLLoader

    @FXML // fx:id="hex71"
    private Polygon hex71; // Value injected by FXMLLoader

    @FXML // fx:id="hex72"
    private Polygon hex72; // Value injected by FXMLLoader

    @FXML // fx:id="hex73"
    private Polygon hex73; // Value injected by FXMLLoader

    @FXML // fx:id="hex74"
    private Polygon hex74; // Value injected by FXMLLoader

    @FXML // fx:id="hex75"
    private Polygon hex75; // Value injected by FXMLLoader

    @FXML // fx:id="hex76"
    private Polygon hex76; // Value injected by FXMLLoader

    @FXML // fx:id="hex77"
    private Polygon hex77; // Value injected by FXMLLoader

    @FXML // fx:id="hex78"
    private Polygon hex78; // Value injected by FXMLLoader

    @FXML // fx:id="hex79"
    private Polygon hex79; // Value injected by FXMLLoader

    @FXML // fx:id="hex8"
    private Polygon hex8; // Value injected by FXMLLoader

    @FXML // fx:id="hex80"
    private Polygon hex80; // Value injected by FXMLLoader

    @FXML // fx:id="hex81"
    private Polygon hex81; // Value injected by FXMLLoader

    @FXML // fx:id="hex82"
    private Polygon hex82; // Value injected by FXMLLoader

    @FXML // fx:id="hex83"
    private Polygon hex83; // Value injected by FXMLLoader

    @FXML // fx:id="hex84"
    private Polygon hex84; // Value injected by FXMLLoader

    @FXML // fx:id="hex85"
    private Polygon hex85; // Value injected by FXMLLoader

    @FXML // fx:id="hex86"
    private Polygon hex86; // Value injected by FXMLLoader

    @FXML // fx:id="hex87"
    private Polygon hex87; // Value injected by FXMLLoader

    @FXML // fx:id="hex88"
    private Polygon hex88; // Value injected by FXMLLoader

    @FXML // fx:id="hex89"
    private Polygon hex89; // Value injected by FXMLLoader

    @FXML // fx:id="hex9"
    private Polygon hex9; // Value injected by FXMLLoader

    @FXML // fx:id="hex90"
    private Polygon hex90; // Value injected by FXMLLoader

    @FXML // fx:id="hex91"
    private Polygon hex91; // Value injected by FXMLLoader

    @FXML // fx:id="hex92"
    private Polygon hex92; // Value injected by FXMLLoader

    @FXML // fx:id="hex93"
    private Polygon hex93; // Value injected by FXMLLoader

    @FXML // fx:id="hex94"
    private Polygon hex94; // Value injected by FXMLLoader

    @FXML // fx:id="hex95"
    private Polygon hex95; // Value injected by FXMLLoader

    @FXML // fx:id="hex96"
    private Polygon hex96; // Value injected by FXMLLoader

    @FXML // fx:id="hex97"
    private Polygon hex97; // Value injected by FXMLLoader

    @FXML // fx:id="hex98"
    private Polygon hex98; // Value injected by FXMLLoader

    @FXML // fx:id="hex99"
    private Polygon hex99; // Value injected by FXMLLoader

    private void setupHexGrid ()
    {
        // Center hexagon
        Hexagon h1 = new Hexagon(0, 0, 0, hex1);  // Center

// First layer (radius = 1)
        Hexagon h2 = new Hexagon(0, -1, 1, hex2);  // Top
        Hexagon h3 = new Hexagon(1, -1, 0, hex3);
        Hexagon h4 = new Hexagon(1, 0, -1, hex4);
        Hexagon h5 = new Hexagon(0, 1, -1, hex5);
        Hexagon h6 = new Hexagon(-1, 1, 0, hex6);
        Hexagon h7 = new Hexagon(-1, 0, 1, hex7);

// Second layer (radius = 2)
        Hexagon h8  = new Hexagon(0, -2, 2, hex8);  // Top (above Hex2)
        Hexagon h9  = new Hexagon(1, -2, 1, hex9);
        Hexagon h10 = new Hexagon(2, -2, 0, hex10);
        Hexagon h11 = new Hexagon(2, -1, -1, hex11);
        Hexagon h12 = new Hexagon(2, 0, -2, hex12);
        Hexagon h13 = new Hexagon(1, 1, -2, hex13);
        Hexagon h14 = new Hexagon(0, 2, -2, hex14);
        Hexagon h15 = new Hexagon(-1, 2, -1, hex15);
        Hexagon h16 = new Hexagon(-2, 2, 0, hex16);
        Hexagon h17 = new Hexagon(-2, 1, 1, hex17);
        Hexagon h18 = new Hexagon(-2, 0, 2, hex18);
        Hexagon h19 = new Hexagon(-1, -1, 2, hex19);

// Third layer (radius = 3)
        Hexagon h20 = new Hexagon(0, -3, 3, hex20);  // Top (above Hex8)
        Hexagon h21 = new Hexagon(1, -3, 2, hex21);
        Hexagon h22 = new Hexagon(2, -3, 1, hex22);
        Hexagon h23 = new Hexagon(3, -3, 0, hex23);
        Hexagon h24 = new Hexagon(3, -2, -1, hex24);
        Hexagon h25 = new Hexagon(3, -1, -2, hex25);
        Hexagon h26 = new Hexagon(3, 0, -3, hex26);
        Hexagon h27 = new Hexagon(2, 1, -3, hex27);
        Hexagon h28 = new Hexagon(1, 2, -3, hex28);
        Hexagon h29 = new Hexagon(0, 3, -3, hex29);
        Hexagon h30 = new Hexagon(-1, 3, -2, hex30);
        Hexagon h31 = new Hexagon(-2, 3, -1, hex31);
        Hexagon h32 = new Hexagon(-3, 3, 0, hex32);
        Hexagon h33 = new Hexagon(-3, 2, 1, hex33);
        Hexagon h34 = new Hexagon(-3, 1, 2, hex34);
        Hexagon h35 = new Hexagon(-3, 0, 3, hex35);
        Hexagon h36 = new Hexagon(-2, -1, 3, hex36);
        Hexagon h37 = new Hexagon(-1, -2, 3, hex37);

// Fourth layer (radius = 4)
        Hexagon h38 = new Hexagon(0, -4, 4, hex38);  // Top (above Hex20)
        Hexagon h39 = new Hexagon(1, -4, 3, hex39);
        Hexagon h40 = new Hexagon(2, -4, 2, hex40);
        Hexagon h41 = new Hexagon(3, -4, 1, hex41);
        Hexagon h42 = new Hexagon(4, -4, 0, hex42);
        Hexagon h43 = new Hexagon(4, -3, -1, hex43);
        Hexagon h44 = new Hexagon(4, -2, -2, hex44);
        Hexagon h45 = new Hexagon(4, -1, -3, hex45);
        Hexagon h46 = new Hexagon(4, 0, -4, hex46);
        Hexagon h47 = new Hexagon(3, 1, -4, hex47);
        Hexagon h48 = new Hexagon(2, 2, -4, hex48);
        Hexagon h49 = new Hexagon(1, 3, -4, hex49);
        Hexagon h50 = new Hexagon(0, 4, -4, hex50);  // Bottom (below Hex29)
        Hexagon h51 = new Hexagon(-1, 4, -3, hex51);
        Hexagon h52 = new Hexagon(-2, 4, -2, hex52);
        Hexagon h53 = new Hexagon(-3, 4, -1, hex53);
        Hexagon h54 = new Hexagon(-4, 4, 0, hex54);
        Hexagon h55 = new Hexagon(-4, 3, 1, hex55);
        Hexagon h56 = new Hexagon(-4, 2, 2, hex56);
        Hexagon h57 = new Hexagon(-4, 1, 3, hex57);
        Hexagon h58 = new Hexagon(-4, 0, 4, hex58);
        Hexagon h59 = new Hexagon(-3, -1, 4, hex59);
        Hexagon h60 = new Hexagon(-2, -2, 4, hex60);
        Hexagon h61 = new Hexagon(-1, -3, 4, hex61);

// Fifth layer (radius = 5)


        Hexagon h62 = new Hexagon(0, -5, 5, hex62);  // top
        Hexagon h63 = new Hexagon(1, -5, 4, hex63);
        Hexagon h64 = new Hexagon(2, -5, 3, hex64);
        Hexagon h65 = new Hexagon(3, -5, 2, hex65);
        Hexagon h66 = new Hexagon(4, -5, 1, hex66);
        Hexagon h67 = new Hexagon(5, -5, 0, hex67);  // top right corner
        Hexagon h68 = new Hexagon(5, -4, -1, hex68);
        Hexagon h69 = new Hexagon(5, -3, -2, hex69);
        Hexagon h70 = new Hexagon(5, -2, -3, hex70);
        Hexagon h71 = new Hexagon(5, -1, -4, hex71);
        Hexagon h72 = new Hexagon(5, 0, -5, hex72);  // bottom right corner
        Hexagon h73 = new Hexagon(4, 1, -5, hex73);
        Hexagon h74 = new Hexagon(3, 2, -5, hex74);
        Hexagon h75 = new Hexagon(2, 3, -5, hex75);
        Hexagon h76 = new Hexagon(1, 4, -5, hex76);
        Hexagon h77 = new Hexagon(0, 5, -5, hex77); // bottom
        Hexagon h78 = new Hexagon(-1, 5, -4, hex78);
        Hexagon h79 = new Hexagon(-2, 5, -3, hex79);
        Hexagon h80 = new Hexagon(-3, 5, -2, hex80);
        Hexagon h81 = new Hexagon(-4, 5, -1, hex81);
        Hexagon h82 = new Hexagon(-5, 5, 0, hex82); // bottom left corner
        Hexagon h83 = new Hexagon(-5, 4, 1, hex83);
        Hexagon h84 = new Hexagon(-5, 3, 2, hex84);
        Hexagon h85 = new Hexagon(-5, 2, 3, hex85);
        Hexagon h86 = new Hexagon(-5, 1, 4, hex86);
        Hexagon h87 = new Hexagon(-5, 0, 5, hex87); // top left corner
        Hexagon h88 = new Hexagon(-4, -1, 5, hex88);
        Hexagon h89 = new Hexagon(-3, -2, 5, hex89);
        Hexagon h90 = new Hexagon(-2, -3, 5, hex90);
        Hexagon h91 = new Hexagon(-1, -4, 5, hex91);

// Sixth layer (radius = 6)

        Hexagon h92 = new Hexagon(0, -6, 6, hex92);  // Top
        Hexagon h93 = new Hexagon(1, -6, 5, hex93);
        Hexagon h94 = new Hexagon(2, -6, 4, hex94);
        Hexagon h95 = new Hexagon(3, -6, 3, hex95);
        Hexagon h96 = new Hexagon(4, -6, 2, hex96);
        Hexagon h97 = new Hexagon(5, -6, 1, hex97);
        Hexagon h98 = new Hexagon(6, -6, 0, hex98);  // Top right corner
        Hexagon h99 = new Hexagon(6, -5, -1, hex99);
        Hexagon h100 = new Hexagon(6, -4, -2, hex100);
        Hexagon h101 = new Hexagon(6, -3, -3, hex101);
        Hexagon h102 = new Hexagon(6, -2, -4, hex102);
        Hexagon h103 = new Hexagon(6, -1, -5, hex103);
        Hexagon h104 = new Hexagon(6, 0, -6, hex104);  // Bottom right corner
        Hexagon h105 = new Hexagon(5, 1, -6, hex105);
        Hexagon h106 = new Hexagon(4, 2, -6, hex106);
        Hexagon h107 = new Hexagon(3, 3, -6, hex107);
        Hexagon h108 = new Hexagon(2, 4, -6, hex108);
        Hexagon h109 = new Hexagon(1, 5, -6, hex109);
        Hexagon h110 = new Hexagon(0, 6, -6, hex110);  // Bottom
        Hexagon h111 = new Hexagon(-1, 6, -5, hex111);
        Hexagon h112 = new Hexagon(-2, 6, -4, hex112);
        Hexagon h113 = new Hexagon(-3, 6, -3, hex113);
        Hexagon h114 = new Hexagon(-4, 6, -2, hex114);
        Hexagon h115 = new Hexagon(-5, 6, -1, hex115);
        Hexagon h116 = new Hexagon(-6, 6, 0, hex116);  // Bottom left corner
        Hexagon h117 = new Hexagon(-6, 5, 1, hex117);
        Hexagon h118 = new Hexagon(-6, 4, 2, hex118);
        Hexagon h119 = new Hexagon(-6, 3, 3, hex119);
        Hexagon h120 = new Hexagon(-6, 2, 4, hex120);
        Hexagon h121 = new Hexagon(-6, 1, 5, hex121);
        Hexagon h122 = new Hexagon(-6, 0, 6, hex122);  // Top left corner
        Hexagon h123 = new Hexagon(-5, -1, 6, hex123);
        Hexagon h124 = new Hexagon(-4, -2, 6, hex124);
        Hexagon h125 = new Hexagon(-3, -3, 6, hex125);
        Hexagon h126 = new Hexagon(-2, -4, 6, hex126);
        Hexagon h127 = new Hexagon(-1, -5, 6, hex127);

        // adding hexagons to the grid itself

        hexGrid.addHex(h1);
        hexGrid.addHex(h2);
        hexGrid.addHex(h3);
        hexGrid.addHex(h4);
        hexGrid.addHex(h5);
        hexGrid.addHex(h6);
        hexGrid.addHex(h7);
        hexGrid.addHex(h8);
        hexGrid.addHex(h9);
        hexGrid.addHex(h10);
        hexGrid.addHex(h11);
        hexGrid.addHex(h12);
        hexGrid.addHex(h13);
        hexGrid.addHex(h14);
        hexGrid.addHex(h15);
        hexGrid.addHex(h16);
        hexGrid.addHex(h17);
        hexGrid.addHex(h18);
        hexGrid.addHex(h19);
        hexGrid.addHex(h20);
        hexGrid.addHex(h21);
        hexGrid.addHex(h22);
        hexGrid.addHex(h23);
        hexGrid.addHex(h24);
        hexGrid.addHex(h25);
        hexGrid.addHex(h26);
        hexGrid.addHex(h27);
        hexGrid.addHex(h28);
        hexGrid.addHex(h29);
        hexGrid.addHex(h30);
        hexGrid.addHex(h31);
        hexGrid.addHex(h32);
        hexGrid.addHex(h33);
        hexGrid.addHex(h34);
        hexGrid.addHex(h35);
        hexGrid.addHex(h36);
        hexGrid.addHex(h37);
        hexGrid.addHex(h38);
        hexGrid.addHex(h39);
        hexGrid.addHex(h40);
        hexGrid.addHex(h41);
        hexGrid.addHex(h42);
        hexGrid.addHex(h43);
        hexGrid.addHex(h44);
        hexGrid.addHex(h45);
        hexGrid.addHex(h46);
        hexGrid.addHex(h47);
        hexGrid.addHex(h48);
        hexGrid.addHex(h49);
        hexGrid.addHex(h50);
        hexGrid.addHex(h51);
        hexGrid.addHex(h52);
        hexGrid.addHex(h53);
        hexGrid.addHex(h54);
        hexGrid.addHex(h55);
        hexGrid.addHex(h56);
        hexGrid.addHex(h57);
        hexGrid.addHex(h58);
        hexGrid.addHex(h59);
        hexGrid.addHex(h60);
        hexGrid.addHex(h61);
        hexGrid.addHex(h62);
        hexGrid.addHex(h63);
        hexGrid.addHex(h64);
        hexGrid.addHex(h65);
        hexGrid.addHex(h66);
        hexGrid.addHex(h67);
        hexGrid.addHex(h68);
        hexGrid.addHex(h69);
        hexGrid.addHex(h70);
        hexGrid.addHex(h71);
        hexGrid.addHex(h72);
        hexGrid.addHex(h73);
        hexGrid.addHex(h74);
        hexGrid.addHex(h75);
        hexGrid.addHex(h76);
        hexGrid.addHex(h77);
        hexGrid.addHex(h78);
        hexGrid.addHex(h79);
        hexGrid.addHex(h80);
        hexGrid.addHex(h81);
        hexGrid.addHex(h82);
        hexGrid.addHex(h83);
        hexGrid.addHex(h84);
        hexGrid.addHex(h85);
        hexGrid.addHex(h86);
        hexGrid.addHex(h87);
        hexGrid.addHex(h88);
        hexGrid.addHex(h89);
        hexGrid.addHex(h90);
        hexGrid.addHex(h91);
        hexGrid.addHex(h92);
        hexGrid.addHex(h93);
        hexGrid.addHex(h94);
        hexGrid.addHex(h95);
        hexGrid.addHex(h96);
        hexGrid.addHex(h97);
        hexGrid.addHex(h98);
        hexGrid.addHex(h99);
        hexGrid.addHex(h100);
        hexGrid.addHex(h101);
        hexGrid.addHex(h102);
        hexGrid.addHex(h103);
        hexGrid.addHex(h104);
        hexGrid.addHex(h105);
        hexGrid.addHex(h106);
        hexGrid.addHex(h107);
        hexGrid.addHex(h108);
        hexGrid.addHex(h109);
        hexGrid.addHex(h110);
        hexGrid.addHex(h111);
        hexGrid.addHex(h112);
        hexGrid.addHex(h113);
        hexGrid.addHex(h114);
        hexGrid.addHex(h115);
        hexGrid.addHex(h116);
        hexGrid.addHex(h117);
        hexGrid.addHex(h118);
        hexGrid.addHex(h119);
        hexGrid.addHex(h120);
        hexGrid.addHex(h121);
        hexGrid.addHex(h122);
        hexGrid.addHex(h123);
        hexGrid.addHex(h124);
        hexGrid.addHex(h125);
        hexGrid.addHex(h126);
        hexGrid.addHex(h127);

        polygonToHexMap.put(hex1, h1);
        polygonToHexMap.put(hex2, h2);
        polygonToHexMap.put(hex3, h3);
        polygonToHexMap.put(hex4, h4);
        polygonToHexMap.put(hex5, h5);
        polygonToHexMap.put(hex6, h6);
        polygonToHexMap.put(hex7, h7);
        polygonToHexMap.put(hex8, h8);
        polygonToHexMap.put(hex9, h9);
        polygonToHexMap.put(hex10, h10);
        polygonToHexMap.put(hex11, h11);
        polygonToHexMap.put(hex12, h12);
        polygonToHexMap.put(hex13, h13);
        polygonToHexMap.put(hex14, h14);
        polygonToHexMap.put(hex15, h15);
        polygonToHexMap.put(hex16, h16);
        polygonToHexMap.put(hex17, h17);
        polygonToHexMap.put(hex18, h18);
        polygonToHexMap.put(hex19, h19);
        polygonToHexMap.put(hex20, h20);
        polygonToHexMap.put(hex21, h21);
        polygonToHexMap.put(hex22, h22);
        polygonToHexMap.put(hex23, h23);
        polygonToHexMap.put(hex24, h24);
        polygonToHexMap.put(hex25, h25);
        polygonToHexMap.put(hex26, h26);
        polygonToHexMap.put(hex27, h27);
        polygonToHexMap.put(hex28, h28);
        polygonToHexMap.put(hex29, h29);
        polygonToHexMap.put(hex30, h30);
        polygonToHexMap.put(hex31, h31);
        polygonToHexMap.put(hex32, h32);
        polygonToHexMap.put(hex33, h33);
        polygonToHexMap.put(hex34, h34);
        polygonToHexMap.put(hex35, h35);
        polygonToHexMap.put(hex36, h36);
        polygonToHexMap.put(hex37, h37);
        polygonToHexMap.put(hex38, h38);
        polygonToHexMap.put(hex39, h39);
        polygonToHexMap.put(hex40, h40);
        polygonToHexMap.put(hex41, h41);
        polygonToHexMap.put(hex42, h42);
        polygonToHexMap.put(hex43, h43);
        polygonToHexMap.put(hex44, h44);
        polygonToHexMap.put(hex45, h45);
        polygonToHexMap.put(hex46, h46);
        polygonToHexMap.put(hex47, h47);
        polygonToHexMap.put(hex48, h48);
        polygonToHexMap.put(hex49, h49);
        polygonToHexMap.put(hex50, h50);
        polygonToHexMap.put(hex51, h51);
        polygonToHexMap.put(hex52, h52);
        polygonToHexMap.put(hex53, h53);
        polygonToHexMap.put(hex54, h54);
        polygonToHexMap.put(hex55, h55);
        polygonToHexMap.put(hex56, h56);
        polygonToHexMap.put(hex57, h57);
        polygonToHexMap.put(hex58, h58);
        polygonToHexMap.put(hex59, h59);
        polygonToHexMap.put(hex60, h60);
        polygonToHexMap.put(hex61, h61);
        polygonToHexMap.put(hex62, h62);
        polygonToHexMap.put(hex63, h63);
        polygonToHexMap.put(hex64, h64);
        polygonToHexMap.put(hex65, h65);
        polygonToHexMap.put(hex66, h66);
        polygonToHexMap.put(hex67, h67);
        polygonToHexMap.put(hex68, h68);
        polygonToHexMap.put(hex69, h69);
        polygonToHexMap.put(hex70, h70);
        polygonToHexMap.put(hex71, h71);
        polygonToHexMap.put(hex72, h72);
        polygonToHexMap.put(hex73, h73);
        polygonToHexMap.put(hex74, h74);
        polygonToHexMap.put(hex75, h75);
        polygonToHexMap.put(hex76, h76);
        polygonToHexMap.put(hex77, h77);
        polygonToHexMap.put(hex78, h78);
        polygonToHexMap.put(hex79, h79);
        polygonToHexMap.put(hex80, h80);
        polygonToHexMap.put(hex81, h81);
        polygonToHexMap.put(hex82, h82);
        polygonToHexMap.put(hex83, h83);
        polygonToHexMap.put(hex84, h84);
        polygonToHexMap.put(hex85, h85);
        polygonToHexMap.put(hex86, h86);
        polygonToHexMap.put(hex87, h87);
        polygonToHexMap.put(hex88, h88);
        polygonToHexMap.put(hex89, h89);
        polygonToHexMap.put(hex90, h90);
        polygonToHexMap.put(hex91, h91);
        polygonToHexMap.put(hex92, h92);
        polygonToHexMap.put(hex93, h93);
        polygonToHexMap.put(hex94, h94);
        polygonToHexMap.put(hex95, h95);
        polygonToHexMap.put(hex96, h96);
        polygonToHexMap.put(hex97, h97);
        polygonToHexMap.put(hex98, h98);
        polygonToHexMap.put(hex99, h99);
        polygonToHexMap.put(hex100, h100);
        polygonToHexMap.put(hex101, h101);
        polygonToHexMap.put(hex102, h102);
        polygonToHexMap.put(hex103, h103);
        polygonToHexMap.put(hex104, h104);
        polygonToHexMap.put(hex105, h105);
        polygonToHexMap.put(hex106, h106);
        polygonToHexMap.put(hex107, h107);
        polygonToHexMap.put(hex108, h108);
        polygonToHexMap.put(hex109, h109);
        polygonToHexMap.put(hex110, h110);
        polygonToHexMap.put(hex111, h111);
        polygonToHexMap.put(hex112, h112);
        polygonToHexMap.put(hex113, h113);
        polygonToHexMap.put(hex114, h114);
        polygonToHexMap.put(hex115, h115);
        polygonToHexMap.put(hex116, h116);
        polygonToHexMap.put(hex117, h117);
        polygonToHexMap.put(hex118, h118);
        polygonToHexMap.put(hex119, h119);
        polygonToHexMap.put(hex120, h120);
        polygonToHexMap.put(hex121, h121);
        polygonToHexMap.put(hex122, h122);
        polygonToHexMap.put(hex123, h123);
        polygonToHexMap.put(hex124, h124);
        polygonToHexMap.put(hex125, h125);
        polygonToHexMap.put(hex126, h126);
        polygonToHexMap.put(hex127, h127);

        hexGrid.linkNeighbors();


    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        setupHexGrid();
        testAndPrintConnectedGroup();

        stonePlacement = new AudioClip(getClass().getResource("/sounds/stone_place.mp3").toExternalForm());

        Media bgMusic = new Media(getClass().getResource("/sounds/background_music.mp3").toExternalForm());
        bgMusicPlayer = new MediaPlayer(bgMusic); // Assign to the class-level variable
        bgMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the background music indefinitely
        bgMusicPlayer.setVolume(0.5);  // Set background music volume to 50%
        bgMusicPlayer.play();

        assert hex1 != null : "fx:id=\"hex1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex10 != null : "fx:id=\"hex10\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex100 != null : "fx:id=\"hex100\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex101 != null : "fx:id=\"hex101\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex102 != null : "fx:id=\"hex102\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex103 != null : "fx:id=\"hex103\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex104 != null : "fx:id=\"hex104\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex105 != null : "fx:id=\"hex105\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex106 != null : "fx:id=\"hex106\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex107 != null : "fx:id=\"hex107\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex108 != null : "fx:id=\"hex108\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex109 != null : "fx:id=\"hex109\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex11 != null : "fx:id=\"hex11\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex110 != null : "fx:id=\"hex110\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex111 != null : "fx:id=\"hex111\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex112 != null : "fx:id=\"hex112\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex113 != null : "fx:id=\"hex113\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex114 != null : "fx:id=\"hex114\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex115 != null : "fx:id=\"hex115\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex116 != null : "fx:id=\"hex116\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex117 != null : "fx:id=\"hex117\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex118 != null : "fx:id=\"hex118\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex119 != null : "fx:id=\"hex119\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex12 != null : "fx:id=\"hex12\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex120 != null : "fx:id=\"hex120\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex121 != null : "fx:id=\"hex121\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex122 != null : "fx:id=\"hex122\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex123 != null : "fx:id=\"hex123\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex124 != null : "fx:id=\"hex124\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex125 != null : "fx:id=\"hex125\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex126 != null : "fx:id=\"hex126\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex127 != null : "fx:id=\"hex127\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex13 != null : "fx:id=\"hex13\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex14 != null : "fx:id=\"hex14\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex15 != null : "fx:id=\"hex15\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex16 != null : "fx:id=\"hex16\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex17 != null : "fx:id=\"hex17\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex18 != null : "fx:id=\"hex18\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex19 != null : "fx:id=\"hex19\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex2 != null : "fx:id=\"hex2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex20 != null : "fx:id=\"hex20\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex21 != null : "fx:id=\"hex21\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex22 != null : "fx:id=\"hex22\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex23 != null : "fx:id=\"hex23\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex24 != null : "fx:id=\"hex24\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex25 != null : "fx:id=\"hex25\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex26 != null : "fx:id=\"hex26\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex27 != null : "fx:id=\"hex27\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex28 != null : "fx:id=\"hex28\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex29 != null : "fx:id=\"hex29\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex3 != null : "fx:id=\"hex3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex30 != null : "fx:id=\"hex30\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex31 != null : "fx:id=\"hex31\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex32 != null : "fx:id=\"hex32\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex33 != null : "fx:id=\"hex33\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex34 != null : "fx:id=\"hex34\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex35 != null : "fx:id=\"hex35\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex36 != null : "fx:id=\"hex36\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex37 != null : "fx:id=\"hex37\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex38 != null : "fx:id=\"hex38\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex39 != null : "fx:id=\"hex39\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex4 != null : "fx:id=\"hex4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex40 != null : "fx:id=\"hex40\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex41 != null : "fx:id=\"hex41\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex42 != null : "fx:id=\"hex42\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex43 != null : "fx:id=\"hex43\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex44 != null : "fx:id=\"hex44\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex45 != null : "fx:id=\"hex45\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex46 != null : "fx:id=\"hex46\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex47 != null : "fx:id=\"hex47\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex48 != null : "fx:id=\"hex48\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex49 != null : "fx:id=\"hex49\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex5 != null : "fx:id=\"hex5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex50 != null : "fx:id=\"hex50\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex51 != null : "fx:id=\"hex51\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex52 != null : "fx:id=\"hex52\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex53 != null : "fx:id=\"hex53\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex54 != null : "fx:id=\"hex54\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex55 != null : "fx:id=\"hex55\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex56 != null : "fx:id=\"hex56\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex57 != null : "fx:id=\"hex57\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex58 != null : "fx:id=\"hex58\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex59 != null : "fx:id=\"hex59\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex6 != null : "fx:id=\"hex6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex60 != null : "fx:id=\"hex60\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex61 != null : "fx:id=\"hex61\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex62 != null : "fx:id=\"hex62\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex63 != null : "fx:id=\"hex63\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex64 != null : "fx:id=\"hex64\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex65 != null : "fx:id=\"hex65\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex66 != null : "fx:id=\"hex66\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex67 != null : "fx:id=\"hex67\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex68 != null : "fx:id=\"hex68\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex69 != null : "fx:id=\"hex69\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex7 != null : "fx:id=\"hex7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex70 != null : "fx:id=\"hex70\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex71 != null : "fx:id=\"hex71\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex72 != null : "fx:id=\"hex72\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex73 != null : "fx:id=\"hex73\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex74 != null : "fx:id=\"hex74\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex75 != null : "fx:id=\"hex75\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex76 != null : "fx:id=\"hex76\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex77 != null : "fx:id=\"hex77\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex78 != null : "fx:id=\"hex78\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex79 != null : "fx:id=\"hex79\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex8 != null : "fx:id=\"hex8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex80 != null : "fx:id=\"hex80\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex81 != null : "fx:id=\"hex81\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex82 != null : "fx:id=\"hex82\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex83 != null : "fx:id=\"hex83\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex84 != null : "fx:id=\"hex84\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex85 != null : "fx:id=\"hex85\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex86 != null : "fx:id=\"hex86\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex87 != null : "fx:id=\"hex87\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex88 != null : "fx:id=\"hex88\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex89 != null : "fx:id=\"hex89\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex9 != null : "fx:id=\"hex9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex90 != null : "fx:id=\"hex90\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex91 != null : "fx:id=\"hex91\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex92 != null : "fx:id=\"hex92\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex93 != null : "fx:id=\"hex93\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex94 != null : "fx:id=\"hex94\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex95 != null : "fx:id=\"hex95\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex96 != null : "fx:id=\"hex96\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex97 != null : "fx:id=\"hex97\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex98 != null : "fx:id=\"hex98\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex99 != null : "fx:id=\"hex99\" was not injected: check your FXML file 'hello-view.fxml'.";

    }

}
