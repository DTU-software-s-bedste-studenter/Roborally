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

    private Menu controlMenu2;

    private MenuItem saveGame;

    private MenuItem newGame;

    private MenuItem loadGame;

    private MenuItem stopGame;

    private MenuItem exitApp;

    private MenuItem hostGame;

    private MenuItem joinGame;

    private MenuItem stopGame2;

    private MenuItem exitApp2;

    public RoboRallyMenuBar(AppController appController) {
        this.appController = appController;

        controlMenu = new Menu("Local Game");
        this.getMenus().add(controlMenu);

        controlMenu2 = new Menu("Online Game");
        this.getMenus().add(controlMenu2);

        ImageView plusIcon = new ImageView();
        ImageView stopIcon = new ImageView();
        ImageView exitIcon = new ImageView();
        ImageView stopIcon2 = new ImageView();
        ImageView exitIcon2 = new ImageView();
        ImageView loadIcon = new ImageView();
        ImageView saveIcon = new ImageView();
        ImageView hostIcon = new ImageView();
        ImageView joinIcon = new ImageView();
        try {
            plusIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/menuIcons/plus_icon.png")));
            stopIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/menuIcons/redX.png")));
            exitIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/menuIcons/exit_icon.png")));
            stopIcon2.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/menuIcons/redX.png")));
            exitIcon2.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/menuIcons/exit_icon.png")));
            loadIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/menuIcons/load.png")));
            saveIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/menuIcons/save.png")));
            hostIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/menuIcons/host_icon.png")));
            joinIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/menuIcons/join_icon.png")));
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        hostIcon.setFitHeight(20);
        hostIcon.setFitWidth(20);
        hostGame = new MenuItem("Host Game", hostIcon);
        hostGame.setOnAction( e -> this.appController.newGame(true));
        controlMenu2.getItems().add(hostGame);

        joinIcon.setFitHeight(20);
        joinIcon.setFitWidth(20);
        joinGame = new MenuItem("Join Game", joinIcon);
        //joinGame.setOnAction(e -> this.appController...);
        controlMenu2.getItems().add(joinGame);

        plusIcon.setFitHeight(20);
        plusIcon.setFitWidth(20);
        newGame = new MenuItem("New Game", plusIcon);
        newGame.setOnAction( e -> this.appController.newGame(false));
        controlMenu.getItems().add(newGame);

        stopIcon.setFitHeight(20);
        stopIcon.setFitWidth(20);
        stopGame = new MenuItem("Stop Game", stopIcon);
        stopGame.setOnAction( e -> this.appController.stopGame());
        controlMenu.getItems().add(stopGame);

        stopIcon2.setFitHeight(20);
        stopIcon2.setFitWidth(20);
        stopGame2 = new MenuItem("Stop Game", stopIcon2);
        stopGame2.setOnAction( e -> this.appController.stopGame());
        controlMenu2.getItems().add(stopGame2);

        saveIcon.setFitHeight(20);
        saveIcon.setFitWidth(20);
        saveGame = new MenuItem("Save Game", saveIcon);
        saveGame.setOnAction( e -> this.appController.saveGame(false));
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

        exitIcon2.setFitHeight(20);
        exitIcon2.setFitWidth(20);
        exitApp2 = new MenuItem("Exit", exitIcon2);
        exitApp2.setOnAction( e -> this.appController.exit());
        controlMenu2.getItems().add(exitApp2);

        controlMenu.setOnShowing(e -> update());
        controlMenu.setOnShown(e -> this.updateBounds());
        controlMenu2.setOnShowing(e -> update());
        controlMenu2.setOnShown(e -> this.updateBounds());
        update();
    }

    public void update() {
        if (appController.isGameRunning() && !appController.getOnline()) {
            controlMenu.setVisible(true);
            newGame.setVisible(false);
            stopGame.setVisible(true);
            saveGame.setVisible(true);
            exitApp.setVisible(true);
            loadGame.setVisible(false);
            stopGame2.setVisible(false);
            exitApp2.setVisible(false);
            hostGame.setVisible(false);
            joinGame.setVisible(false);
            controlMenu2.setVisible(false);
        } else if(appController.isGameRunning() && appController.getOnline()) {
            controlMenu2.setVisible(true);
            newGame.setVisible(false);
            stopGame.setVisible(false);
            saveGame.setVisible(false);
            loadGame.setVisible(false);
            exitApp.setVisible(false);
            stopGame2.setVisible(true);
            exitApp2.setVisible(true);
            hostGame.setVisible(true);
            joinGame.setVisible(true);
            controlMenu.setVisible(false);
        }else{
            controlMenu2.setVisible(true);
            controlMenu.setVisible(true);
            newGame.setVisible(true);
            stopGame.setVisible(false);
            saveGame.setVisible(false);
            loadGame.setVisible(true);
            exitApp.setVisible(true);
            stopGame2.setVisible(false);
            exitApp2.setVisible(true);
            hostGame.setVisible(true);
            joinGame.setVisible(true);
        }
    }

}
