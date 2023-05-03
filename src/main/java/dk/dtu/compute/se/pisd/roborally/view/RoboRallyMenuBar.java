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

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class RoboRallyMenuBar extends MenuBar {

    private AppController appController;

    private Menu controlMenu;

    private MenuItem saveGame;

    private MenuItem newGame;

    private MenuItem loadGame;

    private MenuItem stopGame;

    private MenuItem exitApp;

    public RoboRallyMenuBar(AppController appController) {
        this.appController = appController;

        controlMenu = new Menu("File");
        this.getMenus().add(controlMenu);

        ImageView plusIcon = new ImageView();
        ImageView stopIcon = new ImageView();
        ImageView exitIcon = new ImageView();
        ImageView loadIcon = new ImageView();
        ImageView saveIcon = new ImageView();
        try {
            plusIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/menuIcons/plus_icon.png")));
            stopIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/menuIcons/redX.png")));
            exitIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/menuIcons/exit_icon.png")));
            loadIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/menuIcons/load.png")));
            saveIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/menuIcons/save.png")));
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        plusIcon.setFitHeight(20);
        plusIcon.setFitWidth(20);
        newGame = new MenuItem("New Game", plusIcon);
        newGame.setOnAction( e -> this.appController.newGame());
        controlMenu.getItems().add(newGame);

        stopIcon.setFitHeight(20);
        stopIcon.setFitWidth(20);
        stopGame = new MenuItem("Stop Game", stopIcon);
        stopGame.setOnAction( e -> this.appController.stopGame());
        controlMenu.getItems().add(stopGame);

        saveIcon.setFitHeight(20);
        saveIcon.setFitWidth(20);
        saveGame = new MenuItem("Save Game", saveIcon);
        saveGame.setOnAction( e -> this.appController.saveGame());
        controlMenu.getItems().add(saveGame);

        loadIcon.setFitHeight(20);
        loadIcon.setFitWidth(20);
        loadGame = new MenuItem("Load Game", loadIcon);
        loadGame.setOnAction( e -> this.appController.loadGame());
        controlMenu.getItems().add(loadGame);

        exitIcon.setFitHeight(20);
        exitIcon.setFitWidth(20);
        exitApp = new MenuItem("Exit", exitIcon);
        exitApp.setOnAction( e -> this.appController.exit());
        controlMenu.getItems().add(exitApp);

        controlMenu.setOnShowing(e -> update());
        controlMenu.setOnShown(e -> this.updateBounds());
        update();
    }

    public void update() {
        if (appController.isGameRunning()) {
            newGame.setVisible(false);
            stopGame.setVisible(true);
            saveGame.setVisible(true);
            loadGame.setVisible(false);
        } else {
            newGame.setVisible(true);
            stopGame.setVisible(false);
            saveGame.setVisible(false);
            loadGame.setVisible(true);
        }
    }

}
