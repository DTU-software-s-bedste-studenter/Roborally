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
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.fileaccess.SaveLoad;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    final private List<String> BOARD_NAMES = Arrays.asList("RiskyCrossing", "SprintCramp", "Fractionation", "DeathTrap", "ChopShopChallenge");
    final private RoboRally roboRally;
    private GameController gameController;

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (!result.isPresent()) {
            return;
        }

        ChoiceDialog<String> dialog2 = new ChoiceDialog<>(BOARD_NAMES.get(0), BOARD_NAMES);
        dialog2.setTitle("Map");
        dialog2.setHeaderText("Select the map you want to play:");
        Optional<String> result2 = dialog2.showAndWait();

        if (result2.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            String map = result2.get();
            Board board = LoadBoard.loadBoard(map);
            gameController = new GameController(board, this);
            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getRandomStartSpace());
                player.setStartSpace(player.getSpace());
                player.setHeading(Heading.EAST);
            }

            gameController.startProgrammingPhase();

            roboRally.createBoardView(gameController);
        }
    }

    public void saveGame(boolean stop) {
        if (this.gameController != null) {
            SaveLoad.save(this.gameController.board, stop);
        }
    }

    public void loadGame() {
        String filename = null;
        File folder = new File(System.getProperty("user.dir") + "/src/main/resources/save");
        File[] listOfFiles = folder.listFiles();
        List<String> saveFileNames = new ArrayList<>();
        if(listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                saveFileNames.add(listOfFile.getName());
            }
        }
        if(!saveFileNames.isEmpty()) {
            ChoiceDialog<String> dialog = new ChoiceDialog<>(saveFileNames.get(0), saveFileNames);
            dialog.setTitle("Saved games");
            dialog.setHeaderText("Select the saved game you want to play:");
            Optional<String> selectedSaveGame = dialog.showAndWait();
            if (selectedSaveGame.isPresent()) {
                filename = selectedSaveGame.get();
            }
            else {
                return;
            }
        }
        else {
            filename = "notFound";
        }
        Path pathToSaveFile = Paths.get(System.getProperty("user.dir") + "/src/main/resources/save/" + filename);
        if (Files.exists(pathToSaveFile)) {
            startLoadedGame(SaveLoad.load(filename));
        }
        else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Save file not found!");
            alert.setContentText("Save file not found!\n\nA new game will be started!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == (ButtonType.OK)) {
                newGame();
            }
        }
    }

    private void startLoadedGame(Board board) {
        gameController = new GameController(board, this);
        gameController.startProgrammingPhase();
        roboRally.createBoardView(gameController);
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {
            saveGame(true);
            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    /**
     * Works the same as stop game without saving the game,
     * also prints congratulations to winner.
     * @param player
     * @return true if gamecontroller =! null
     */
    public boolean resetGame(Player player) {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Congratulations");
            alert.setContentText(player.getName() + " has won the game.");
            Optional<ButtonType> result = alert.showAndWait();

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }


    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

}
