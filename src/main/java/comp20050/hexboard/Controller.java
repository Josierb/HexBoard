package comp20050.hexboard;

import java.net.URL;
import java.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * Controller for the HexOust game board.
 * Manages user interaction, game logic, UI updates, and win condition checks.
 */
public class Controller {

    private HexGrid hexGrid = new HexGrid();
    private Map<Polygon, Hexagon> polygonToHexMap = new HashMap<>();
    private boolean isBlueTurn = true;

    private AudioClip stonePlacement;
    private MediaPlayer bgMusicPlayer;

    private static final Color HEX_NAVY = Color.web("#010437");
    private static final Color COLOR_BLUE = Color.web("#08F7FE");
    private static final Color COLOR_ORANGE = Color.web("#FF8000");

    private int blueCount = 0;
    private int orangeCount = 0;
    private int roundCount = 0;

    @FXML private Label turnlabel;
    @FXML private Label bluecount;
    @FXML private Label orangecount;
    @FXML private Label roundcount;
    @FXML private Button quitbutton;

    /**
     * Handles a user clicking on a hexagon. Finds the associated Hexagon and processes the move.
     */
    @FXML
    private void onHexClick(MouseEvent event) {
        Polygon hexagon = (Polygon) event.getSource();
        Hexagon clickedHex = hexGrid.getHexByShape(hexagon);

        if (clickedHex == null) {
            System.out.println("Hexagon not found!");
            return;
        }

        handlePlayerMove(clickedHex, hexagon);
    }

    /**
     * Handles hover effects for valid moves.
     */
    @FXML
    private void onHexHover(MouseEvent event) {
        Polygon hexagon = (Polygon) event.getSource();
        Hexagon hoveredHex = hexGrid.getHexByShape(hexagon);

        if (hoveredHex == null) return;

        Color currentColor = isBlueTurn ? COLOR_BLUE : COLOR_ORANGE;
        if (isValidMove(hexagon, hoveredHex, currentColor)) {
            if (hexagon.getFill().equals(HEX_NAVY)) {
                hexagon.setFill(Color.DARKMAGENTA);
            }
        }
    }

    /**
     * Removes hover highlight from hexagon.
     */
    @FXML
    private void exitHexHover(MouseEvent event) {
        Polygon hexagon = (Polygon) event.getSource();
        if (hexagon.getFill().equals(Color.DARKMAGENTA)) {
            hexagon.setFill(HEX_NAVY);
        }
    }

    /**
     * Exits the application.
     */
    @FXML
    private void quit(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Places a stone, updates UI, checks capture logic, and toggles player turn.
     */
    private void handlePlayerMove(Hexagon clickedHex, Polygon hexagon) {
        Color currentColor = isBlueTurn ? COLOR_BLUE : COLOR_ORANGE;

        if (hexagon.getFill().equals(Color.DARKMAGENTA)) {
            hexagon.setFill(currentColor);
            clickedHex.setOwner(currentColor);

            if (isBlueTurn) blueCount++; else orangeCount++;

            Set<Hexagon> capturedHexes = getCapturedHexes(clickedHex);
            boolean isCapture = isCaptureMove(clickedHex, currentColor);

            if (isCapture) {
                captureHexagons(capturedHexes);
            }

            if (!isCapture) {
                if (!isBlueTurn) roundCount++;
                isBlueTurn = !isBlueTurn;
            }

            updateUI();
            stonePlacement.play();

            // Check win condition
            if ((orangeCount == 0 || blueCount == 0) && roundCount > 0) {
                String winner = (blueCount > 0) ? "Blue" : "Orange";
                int stonesLeft = (blueCount > 0) ? blueCount : orangeCount;
                showWinnerPopup(winner, stonesLeft, roundCount);
            }
        }
    }

    /**
     * Updates the turn label, round counter, and stone counts on the UI.
     */
    private void updateUI() {
        turnlabel.setText(isBlueTurn ? "Blue's Turn" : "Orange's Turn");
        turnlabel.setTextFill(isBlueTurn ? COLOR_BLUE : COLOR_ORANGE);
        bluecount.setText("Blue Count: " + blueCount);
        orangecount.setText("Orange Count: " + orangeCount);
        roundcount.setText("Round - " + roundCount);
    }

    /**
     * Validates if a move is allowed (no adjacent same color OR a capture move).
     */
    private boolean isValidMove(Polygon hexagon, Hexagon hoveredHex, Color currentColor) {
        boolean isCapturing = isCaptureMove(hoveredHex, currentColor);
        List<Hexagon> neighbors = getHexNeighbors(hoveredHex);
        boolean adjacentToSameColor = false;

        for (Hexagon neighbor : neighbors) {
            if (neighbor.getOwner() != null && neighbor.getOwner().equals(currentColor)) {
                adjacentToSameColor = true;
                break;
            }
        }

        return isCapturing || !adjacentToSameColor;
    }

    /**
     * Builds the full connected group of a player's stones including the clicked hex.
     */
    private Set<Hexagon> buildConnectedPlayerGroup(Hexagon clickedHex, Color currentColor) {
        Set<Hexagon> group = new HashSet<>();
        for (Hexagon neighbor : clickedHex.getNeighbors().values()) {
            if (neighbor.getOwner() != null && neighbor.getOwner().equals(currentColor)) {
                findConnectedGroupSize(neighbor, currentColor, group);
            }
        }
        group.add(clickedHex);
        return group;
    }


    /**
     * Determines whether a move would result in a capture.
     */
    private boolean isCaptureMove(Hexagon clickedHex, Color currentColor) {
        Color opponentColor = currentColor.equals(COLOR_BLUE) ? COLOR_ORANGE : COLOR_BLUE;
        Set<Hexagon> totalPlayerGroup = buildConnectedPlayerGroup(clickedHex, currentColor);
        int totalPlayerGroupSize = totalPlayerGroup.size();

        int largestOpponentGroup = 0;
        for (Hexagon hex : totalPlayerGroup) {
            for (Hexagon neighbor : hex.getNeighbors().values()) {
                if (neighbor.getOwner() != null && neighbor.getOwner().equals(opponentColor)) {
                    Set<Hexagon> opponentGroup = new HashSet<>();
                    int size = findConnectedGroupSize(neighbor, opponentColor, opponentGroup);
                    largestOpponentGroup = Math.max(largestOpponentGroup, size);
                }
            }
        }

        return (totalPlayerGroupSize > largestOpponentGroup && largestOpponentGroup != 0);
    }

    /**
     * Gets neighboring hexagons of a given hex.
     */
    private List<Hexagon> getHexNeighbors(Hexagon hex) {
        return new ArrayList<>(hex.getNeighbors().values());
    }

    /**
     * Recursively computes the size of a group of hexes with the same owner color.
     */
    public static int findConnectedGroupSize(Hexagon startHex, Color targetColor, Set<Hexagon> visited) {
        if (startHex == null || startHex.isEmpty() ||
                !targetColor.equals(startHex.getOwner()) || visited.contains(startHex)) {
            return 0;
        }

        visited.add(startHex);
        int count = 1;

        for (Hexagon neighbor : startHex.getNeighbors().values()) {
            count += findConnectedGroupSize(neighbor, targetColor, visited);
        }

        return count;
    }

    /**
     * Identifies which opponent hexes would be captured by placing on the selected hex.
     */
    private Set<Hexagon> getCapturedHexes(Hexagon clickedHex) {
        Color currentColor = clickedHex.getOwner();
        Color opponentColor = currentColor.equals(COLOR_BLUE) ? COLOR_ORANGE : COLOR_BLUE;

        Set<Hexagon> totalPlayerGroup = buildConnectedPlayerGroup(clickedHex, currentColor);
        int totalPlayerGroupSize = totalPlayerGroup.size();
        Set<Hexagon> capturedHexes = new HashSet<>();

        for (Hexagon hex : totalPlayerGroup) {
            for (Hexagon neighbor : hex.getNeighbors().values()) {
                if (neighbor.getOwner() != null && neighbor.getOwner().equals(opponentColor)) {
                    Set<Hexagon> opponentGroup = new HashSet<>();
                    int opponentGroupSize = findConnectedGroupSize(neighbor, opponentColor, opponentGroup);
                    if (totalPlayerGroupSize > opponentGroupSize) {
                        capturedHexes.addAll(opponentGroup);
                    }
                }
            }
        }

        return capturedHexes;
    }

    /**
     * Resets captured opponent hexes to empty.
     */
    private void captureHexagons(Set<Hexagon> capturedHexes) {
        for (Hexagon hex : capturedHexes) {
            hex.setOwner(null);
            hex.getHexShape().setFill(HEX_NAVY);
            if (isBlueTurn) orangeCount--;
            else blueCount--;
        }
    }

    /**
     * Displays a styled popup with game results and a restart option.
     */
    private void showWinnerPopup(String winner, int stonesLeft, int roundsPlayed) {
        Platform.runLater(() -> {
            StackPane overlay = new StackPane();
            overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.85);");
            overlay.setPrefSize(HexOustApplication.root.getWidth(), HexOustApplication.root.getHeight());
            overlay.setAlignment(Pos.CENTER);

            String bgColor = winner.equals("Blue") ? "#0084FF" : "#FFA500";

            VBox popup = new VBox(20);
            popup.setStyle("-fx-background-color: " + bgColor + ";" +
                    "-fx-border-color: #08F7FE;" +
                    "-fx-border-width: 3px;" +
                    "-fx-background-radius: 15px;" +
                    "-fx-border-radius: 15px;" +
                    "-fx-padding: 30px;");
            popup.setAlignment(Pos.CENTER);

            Label message = new Label(String.format(
                    "🎉 %s wins the game!\n\n🪨 Stones Left: %d\n🔁 Rounds Played: %d",
                    winner, stonesLeft, roundsPlayed
            ));
            message.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
            message.setWrapText(true);
            message.setAlignment(Pos.CENTER);

            Button closeButton = getCloseButton(overlay);

            popup.getChildren().addAll(message, closeButton, quitbutton);
            overlay.getChildren().add(popup);

            // Set initial opacity and add to root
            overlay.setOpacity(0);
            HexOustApplication.root.getChildren().add(overlay);

            // Fade-in animation
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), overlay);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });
    }

    private Button getCloseButton(StackPane overlay) {
        Button closeButton = new Button("Restart Game");
        closeButton.setStyle("-fx-background-color: #ff00ff;" +
                "-fx-text-fill: #0ff; " +
                "-fx-font-size: 11px; " +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10px 20px;" +
                "-fx-border-radius: 25px; " +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, black, 2, 8, 0, 0) " +
                "dropshadow(gaussian, #0ff, 10, 3, 0, 0);");
        closeButton.setScaleX(1.2);
        closeButton.setScaleY(1.2);

        closeButton.setOnMouseEntered(e -> {
            closeButton.setScaleX(1.3);
            closeButton.setScaleY(1.3);
        });
        closeButton.setOnMouseExited(e -> {
            closeButton.setScaleX(1.2);
            closeButton.setScaleY(1.2);
        });
        closeButton.setOnAction(e -> {
            HexOustApplication.root.getChildren().remove(overlay);
            try {
                restart();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        return closeButton;
    }

    /**
     * Restarts the game: reloads the FXML layout and restarts the background music.
     */
    public void restart() throws Exception {
        if (bgMusicPlayer != null) bgMusicPlayer.stop();

        Parent loadedRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        HexOustApplication.root.getChildren().clear();
        HexOustApplication.root.getChildren().add(loadedRoot);
        HexOustApplication.root.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        Media bgMusic = new Media(Objects.requireNonNull(getClass().getResource("/sounds/background_music.mp3")).toExternalForm());
        bgMusicPlayer = new MediaPlayer(bgMusic);
        bgMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgMusicPlayer.setVolume(0.5);
        bgMusicPlayer.play();
    }

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML private Polygon
            hex1, hex2, hex3, hex4, hex5, hex6, hex7, hex8, hex9, hex10, hex11, hex12,
            hex13, hex14, hex15, hex16, hex17, hex18, hex19, hex20, hex21, hex22, hex23, hex24, hex25,
            hex26, hex27, hex28, hex29, hex30, hex31, hex32, hex33, hex34, hex35, hex36, hex37, hex38,
            hex39, hex40, hex41, hex42, hex43, hex44, hex45, hex46, hex47, hex48, hex49, hex50, hex51,
            hex52, hex53, hex54, hex55, hex56, hex57, hex58, hex59, hex60, hex61, hex62, hex63, hex64,
            hex65, hex66, hex67, hex68, hex69, hex70, hex71, hex72, hex73, hex74, hex75, hex76, hex77,
            hex78, hex79, hex80, hex81, hex82, hex83, hex84, hex85, hex86, hex87, hex88, hex89, hex90,
            hex91, hex92, hex93, hex94, hex95, hex96, hex97, hex98, hex99, hex100, hex101, hex102, hex103,
            hex104, hex105, hex106, hex107, hex108, hex109, hex110, hex111, hex112, hex113, hex114, hex115, hex116,
            hex117, hex118, hex119, hex120, hex121, hex122, hex123, hex124, hex125, hex126, hex127;


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

    /**
     * Initializes the hex grid and background music when the scene loads.
     */
    @FXML
    void initialize() {
        setupHexGrid();

        stonePlacement = new AudioClip(Objects.requireNonNull(getClass().getResource("/sounds/stone_place.mp3")).toExternalForm());
        Media bgMusic = new Media(Objects.requireNonNull(getClass().getResource("/sounds/background_music.mp3")).toExternalForm());
        bgMusicPlayer = new MediaPlayer(bgMusic);
        bgMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgMusicPlayer.setVolume(0.5);
        bgMusicPlayer.play();
    }
}
