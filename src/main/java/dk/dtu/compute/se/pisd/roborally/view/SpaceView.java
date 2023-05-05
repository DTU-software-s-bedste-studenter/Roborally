/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.*;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Rotation;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import jdk.jshell.spi.ExecutionControl;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 60; // 75;
    final public static int SPACE_WIDTH = 60; // 75;

    public final Space space;

    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    private void updatePlayer() {

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0 );
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90*player.getHeading().ordinal())%360);
            this.getChildren().add(arrow);
        }
    }

    private void drawPlayer(){
        Player player = space.getPlayer();
        if (player != null){
            ImageView imageView = new ImageView();
            try {
                switch (player.getColor()){
                    case "red" -> imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/RobotRed.png")));
                    case "green" -> imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/RobotGreen.png")));
                    case "blue" -> imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/RobotBlue.png")));
                    case "orange" -> imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/RobotOrange.png")));
                    case "grey" -> imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/RobotGrey.png")));
                    case "magenta" -> imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/RobotMagenta.png")));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            switch (player.getHeading()) {
                case EAST -> imageView.setRotate(90);
                case SOUTH -> imageView.setRotate(180);
                case WEST -> imageView.setRotate(270);
            }
            imageView.fitHeightProperty().setValue(SPACE_HEIGHT);
            imageView.fitWidthProperty().setValue(SPACE_WIDTH);
            this.getChildren().add(imageView);
        }


    }

    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().clear();
            drawBackground();
            if (space.getActions().size() > 0) {
                drawFieldActions();
            }
            drawWalls();
            drawPlayer();
        }
    }

    private void drawWalls()
    {
        Image image = null;
        try {
            image = new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/north_wall.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (Heading wall : space.getWalls())
        {
            ImageView imageView = new ImageView(image);
            switch (wall) {
                case EAST -> imageView.setRotate(90);
                case SOUTH -> imageView.setRotate(180);
                case WEST -> imageView.setRotate(270);
            }
            imageView.fitHeightProperty().setValue(SPACE_HEIGHT);
            imageView.fitWidthProperty().setValue(SPACE_WIDTH);
            this.getChildren().add(imageView);
        }
    }

    /**
     * The method responsible for drawing the default tile on the board.
     */
    public void drawBackground()
    {
        ImageView imageView = new ImageView();
        try {
            imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/concrete_floor.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.fitHeightProperty().setValue(SPACE_HEIGHT);
        imageView.fitWidthProperty().setValue(SPACE_WIDTH);
        this.getChildren().add(imageView);
    }

    /**
     * Method responsible for drawing the various action fields depending on the specific field.
     */
    public void drawFieldActions()
    {
        ImageView imageView = new ImageView();
        switch (space.getActions().get(0).getClass().getName()) {
            case "dk.dtu.compute.se.pisd.roborally.controller.ConveyorBelt":
                ConveyorBelt conveyorBelt = (ConveyorBelt) space.getActions().get(0);
                try {
                    if(conveyorBelt.getExpress()){
                        imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/north_express.png")));
                    }else {
                        imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/north_conveyor.png")));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                switch (conveyorBelt.getHeading()) {
                    case EAST -> imageView.setRotate(90);
                    case SOUTH -> imageView.setRotate(180);
                    case WEST -> imageView.setRotate(270);
                }
                break;
            case "dk.dtu.compute.se.pisd.roborally.controller.Gears":
                Gears gears = (Gears) space.getActions().get(0);
                try {
                    if(gears.getRotation() == Rotation.CLOCKWISE) {
                        imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/gears_Clockwise.png")));
                    }else{
                        imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/gears_counterclockwise.png")));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "dk.dtu.compute.se.pisd.roborally.controller.PushPanel":
                PushPanel pushPanel = (PushPanel) space.getActions().get(0);
                try {
                    if(pushPanel.getReg1() == 1){
                        imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/push24.png")));
                    } else {
                        imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/push135.png")));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                switch (pushPanel.getHeading()) {
                    case SOUTH-> imageView.setRotate(90);
                    case WEST -> imageView.setRotate(180);
                    case NORTH -> imageView.setRotate(270);
                }
                break;
            case "dk.dtu.compute.se.pisd.roborally.controller.Checkpoint":
                Checkpoint checkpoint = (Checkpoint) space.getActions().get(0);
                try {
                    switch (checkpoint.getCheckpointNr()){
                        case 1 -> imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/check1.png")));
                        case 2 -> imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/check2.png")));
                        case 3 -> imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/check3.png")));
                        case 4 -> imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/check4.png")));
                        case 5 -> imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/check5.png")));
                        case 6 -> imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/check6.png")));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "dk.dtu.compute.se.pisd.roborally.controller.StartSpace":
                try {
                    imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/StartSpace.png")));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "dk.dtu.compute.se.pisd.roborally.controller.Pit":
                try {
                    imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/pit.png")));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "dk.dtu.compute.se.pisd.roborally.controller.Reboot":
                Reboot reboot = (Reboot) space.getActions().get(0);
                try {
                        imageView.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/north_reboot.png")));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                switch (reboot.getHeading()) {
                    case EAST -> imageView.setRotate(90);
                    case SOUTH -> imageView.setRotate(180);
                    case WEST -> imageView.setRotate(270);
                }
                break;
        }
        imageView.fitHeightProperty().setValue(SPACE_HEIGHT);
        imageView.fitWidthProperty().setValue(SPACE_WIDTH);
        this.getChildren().add(imageView);
    }
}
