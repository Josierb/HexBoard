/**
 * Sample Skeleton for 'hello-view.fxml' Controller Class
 */

package comp20050.hexboard;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.control.Label;
// import javafx.scene.media.AudioClip;



public class Controller {

    // Tracking which players
    private boolean isRedTurn = true;
 //   private AudioClip stonePlacement;

    @FXML
    private Label turnlabel;

    @FXML
    void getHexID(MouseEvent event) {
        Polygon hexagon = (Polygon) event.getSource();
        if(hexagon.getFill().equals(Color.LIGHTGRAY)){
            if(isRedTurn){
                hexagon.setFill(Color.RED);
                turnlabel.setText("Blues Turn");
            }
            else{
                hexagon.setFill(Color.BLUE);
                turnlabel.setText("Reds Turn");
            }
            isRedTurn = !isRedTurn;
           // stonePlacement.play();

        }
    }

    @FXML
    private Button quitbutton;

    @FXML
    void quit(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void onHexHover(MouseEvent event) {
        Polygon hexagon = (Polygon) event.getSource();

        if (hexagon.getFill().equals(Color.WHITE)) {
            hexagon.setFill(Color.LIGHTGRAY);
        }
    }

    @FXML
    void exitHexHover(MouseEvent event) {
        Polygon hexagon = (Polygon) event.getSource();
        if (hexagon.getFill().equals(Color.LIGHTGRAY)) {
            hexagon.setFill(Color.WHITE);
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


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

     //   stonePlacement = new AudioClip(getClass().getResource("/sounds/stone_place.mp3").toExternalForm());

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
