package gui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import vehicles.Autobus;
import vehicles.Tram;
import vehicles.Trolley;
import vehicles.Vehicle;

/**
 * Graphic representation of the Vehicle the scene
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class ViewVehicle extends Circle {
    private Vehicle vehicle;
    private Glow glow = new Glow();
    VBox infoBox;
    Group f_group = new Group();

    /**
    * Constructor of the graphic Vehicle representation 
    * @param in_vehicle  input vechicle object 
    * @param Front_Group group of the front objects in the scene
    * @param in_infoBox  input infoBox object 
    */
    public ViewVehicle(Vehicle in_vehicle,Group Front_Group, VBox in_infoBox) {
        this.infoBox = in_infoBox;
        this.vehicle = in_vehicle;
        this.f_group = Front_Group;
        setId(in_vehicle.getId());
        glow.setLevel(0.9);
        setOnMouseEntered(mouseEntered);
        setOnMouseExited(mouseExited);
        setOnMouseClicked(mouseClicked);
        setupToolTip();
        drawVehicle();
        this.f_group.getChildren().add(this);
    }

    /**
    * Set base settings of toolTip box 
    */
    private void setupToolTip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setText("stop: " + vehicle.getId());
        Tooltip.install(this, tooltip);
        tooltip.getStyleClass().add("stop-tip");
    }

    /**
    * Draw the vehicle object  
    */
    private void drawVehicle() {
        setRadius(0);
        if (vehicle instanceof  Tram) {
            setFill(new ImagePattern(new Image("tra.png")));
        } else if (vehicle instanceof  Trolley) {
            setFill(new ImagePattern(new Image("tro.png")));
        } else if (vehicle instanceof  Autobus) {
            setFill(new ImagePattern(new Image("bus.png")));
        }
        setStyle("-fx-opacity: 0.8; -fx-stroke: " + vehicle.getLine().getColor());
    }        

    /**
    * Update position of the vehicle graphic representation 
    */
    public void UpdatePosition() {
        setCenterX(vehicle.getPosition().getX());
        setCenterY(vehicle.getPosition().getY());
        setRadius(14);
        setStrokeWidth(1);
    }

    /**
    * Get base Vehicle object of the graphic representation
    * @return internal vehicle object
    */
    public Vehicle GetVehicle() {
        return vehicle;
    }  

    /**
    * EventHandler - mouse entered the vehicle graphic representation
    */
    EventHandler<MouseEvent> mouseEntered = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            setEffect(glow);  
            System.out.println("Mouse entered on auto: " + vehicle.getId());
        }
    };

    /**
    * EventHandler - mouse exited the vehicle graphic representation
    */
    EventHandler<MouseEvent> mouseExited = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            setEffect(null);
            System.out.println("Mouse exited from auto: " + vehicle.getId());
        }
    };

    /**
    * EventHandler - mouse clicked on the vehicle graphic representation
    */
    EventHandler<MouseEvent> mouseClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if (e.getButton() == MouseButton.PRIMARY)
            {   
                System.out.println("Mouse LEFT clicked on vehicle: " + vehicle.getId());
                InfoBoxController.InfoBoxController(infoBox, vehicle);
                
            } else if (e.getButton() == MouseButton.SECONDARY)
            {                
                System.out.println("Mouse RIGHT clicked on vehicle: " + vehicle.getId());
            }
        }
    };

}