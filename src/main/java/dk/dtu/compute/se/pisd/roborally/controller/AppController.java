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

import dk.dtu.compute.se.pisd.roborally.HTTPClient.Lobby;
import dk.dtu.compute.se.pisd.roborally.HTTPClient.LobbyClient;
import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.fileaccess.SaveLoad;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(1, 2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    final private List<String> BOARD_NAMES = Arrays.asList("RiskyCrossing", "SprintCramp", "Fractionation", "DeathTrap", "ChopShopChallenge");

    final private RoboRally roboRally;
    private GameController gameController;

    public final LobbyClient lobbyClient = new LobbyClient();

    private Lobby lobby = new Lobby();


    private boolean isOnline;

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    public void startNewLocalGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isEmpty()) {
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
            gameController = new OfflineGameController(board, this);
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

    public void startNewOnlineGame() {
        this.isOnline = true;

        if (this.lobby == null) {
            this.lobby = new Lobby();
        }
        int id = lobbyClient.getNewLobbyID();
        this.lobby.setId(id);

        ChoiceDialog<Integer> selectNrOfPlayersDialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        selectNrOfPlayersDialog.setTitle("Player number");
        selectNrOfPlayersDialog.setHeaderText("Select number of players");
        Optional<Integer> selectedNrOfPlayers = selectNrOfPlayersDialog.showAndWait();
        lobby.setSelectedNrOfPlayers(selectedNrOfPlayers.get());

        TextInputDialog selectNameDialog = new TextInputDialog("Name");
        selectNameDialog.setHeaderText("Enter your playername");
        Optional<String> selectedName = selectNameDialog.showAndWait();
        List<String> players = new ArrayList<>();
        List<String> playerOptions = new ArrayList<>();
        players.add(selectedName.get());
        playerOptions.add(selectedName.get());
        lobby.setPlayers(players); //made a little adjustment
        lobby.setPlayerOptions(players);
        lobbyClient.addLobby(lobby);
        playerOptions.remove(selectedName.get());
        lobby.setPlayerOptions(playerOptions);
        lobbyClient.updateLobby(lobby.getId(), lobby);

        ChoiceDialog<String> selectMapDialog = new ChoiceDialog<>(BOARD_NAMES.get(0), BOARD_NAMES);
        selectMapDialog.setTitle("Map");
        selectMapDialog.setHeaderText("Select the map you want to play:");
        Optional<String> selectedMap = selectMapDialog.showAndWait();

        Alert currentNrOfPlayersInLobby = new Alert(AlertType.INFORMATION, "Close", ButtonType.CANCEL);
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        currentNrOfPlayersInLobby.setContentText(getLobbyPlayerListText(lobby.isGameStarted()));

        lobby = lobbyClient.getLobbyById(lobby.getId());
        currentNrOfPlayersInLobby.setTitle("Waiting for players to join lobby");
        currentNrOfPlayersInLobby.setHeaderText("Lobby ID: " + lobby.getId());
        currentNrOfPlayersInLobby.setOnShown(e -> delay.playFromStart());
        currentNrOfPlayersInLobby.setOnCloseRequest(e -> currentNrOfPlayersInLobby.close());
        currentNrOfPlayersInLobby.getDialogPane().setMinHeight(400);
        delay.setOnFinished(e -> {
            lobby = lobbyClient.getLobbyById(lobby.getId());
            currentNrOfPlayersInLobby.setContentText(getLobbyPlayerListText(lobby.isGameStarted()));
            delay.playFromStart();
            if (lobby.getPlayers().size() == lobby.getSelectedNrOfPlayers()) {
                currentNrOfPlayersInLobby.close();
                currentNrOfPlayersInLobby.setResult(ButtonType.OK);
                delay.stop();
            }
        });
        currentNrOfPlayersInLobby.showAndWait();
        if (currentNrOfPlayersInLobby.getResult() == ButtonType.CLOSE || currentNrOfPlayersInLobby.getResult() == ButtonType.CANCEL) {
            lobbyClient.deleteLobbyById(lobby.getId());
            lobby = null;
            this.isOnline = false;
            return;
        }
        Alert wantToStartGame = new Alert(AlertType.CONFIRMATION);
        wantToStartGame.setTitle("Online lobby");
        wantToStartGame.setHeaderText("Selected number of players has joined lobby");
        wantToStartGame.setContentText("Begin game?");
        wantToStartGame.showAndWait();

        ButtonType buttonTypeOne = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeCancel = ButtonType.CANCEL;
        wantToStartGame.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        if (wantToStartGame.getResult() == ButtonType.CANCEL) {
            lobbyClient.deleteLobbyById(lobby.getId());
            lobby = null;
            this.isOnline = false;
            return;
        }

        if (gameController != null) {
            // The UI should not allow this, but in case this happens anyway.
            // give the user the option to save the game or abort this operation!
            if (!stopGame()) {
                this.isOnline = false;
                return;
            }
        }

        String map = selectedMap.get();
        Board board = LoadBoard.loadBoard(map);
        int i = 0;
        for (String playerName : lobby.getPlayers()) {
            Player player = new Player(board, PLAYER_COLORS.get(i++), playerName);
            board.addPlayer(player);
            player.setSpace(board.getRandomStartSpace());
            player.setStartSpace(player.getSpace());
            player.setHeading(Heading.EAST);
        }
        gameController = new OnlineGameController(board, this, selectNameDialog.getResult(), lobby.getId());
        for (int j = 0; j < gameController.board.getNumberOfPlayers(); j++) {
            gameController.giveNewCardsToPlayer(gameController.board.getPlayer(j));
        }
        lobby.setJSON(SaveLoad.buildGameStateToJSON(board));
        lobby.setActive(true);
        lobby.setGameStarted(true);
        lobbyClient.updateJSON(lobby.getJSON(), lobby.getId());
        lobbyClient.updateLobby(lobby.getId(), lobby);
        gameController.startProgrammingPhase();
        roboRally.createBoardView(gameController);
    }


    public void saveGame(boolean stop) {
        if (this.gameController != null) {
            SaveLoad.save(this.gameController.board, stop);
        }
    }

    public void loadGame() {
        String filename;
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
            startLoadedGame(SaveLoad.load(filename, false));
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Save file not found!");
            alert.setContentText("Save file not found!\n\nA new game will be started!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == (ButtonType.OK)) {
                    startNewLocalGame();
            }
        }
    }

    public void joinOnlineGame() {
        isOnline = true;
        int realSelectedGameIDtoJoin;
        TextInputDialog inputGameIDtoJoin = new TextInputDialog("Join Game");
        Optional<String> selectedGameIDtoJoin;
        boolean newDialog = false;
        while (true) {
            if(!newDialog) {
                inputGameIDtoJoin.setHeaderText("Input the ID of the game you wish to join");
            }
            selectedGameIDtoJoin = inputGameIDtoJoin.showAndWait();
            try {
                realSelectedGameIDtoJoin = Integer.parseInt(selectedGameIDtoJoin.get());
            } catch (NumberFormatException e) {
                inputGameIDtoJoin.setHeaderText("Game ID must be a number!");
                newDialog = true;
                continue;
            }
            break;
        }

        lobby = lobbyClient.getLobbyById(realSelectedGameIDtoJoin);


        if (lobby == null || ( !lobby.isGameStarted() && lobby.getPlayers().size() == lobby.getSelectedNrOfPlayers() )
                || ( lobby.isGameStarted() && lobby.getPlayerOptions().size() == 0) ) {

            do {
                inputGameIDtoJoin = new TextInputDialog("Join Game");
                inputGameIDtoJoin.setHeaderText("The gameID you inputted either didn't exist, the game was already started or the game was full\n" +
                        "Input the ID of the game you wish to join");
                selectedGameIDtoJoin = inputGameIDtoJoin.showAndWait();
                realSelectedGameIDtoJoin = Integer.parseInt(selectedGameIDtoJoin.get());
                lobby = lobbyClient.getLobbyById(realSelectedGameIDtoJoin);
            } while (lobby == null || ( !lobby.isGameStarted() && lobby.getPlayers().size() == lobby.getSelectedNrOfPlayers() )
                    || ( lobby.isGameStarted() && lobby.getPlayerOptions().size() == 0 ));
        }
        List<String> players = lobby.getPlayers();
        List<String> playerOptions = lobby.getPlayerOptions();
        Optional<String> selectedName;
        if (!lobby.isGameStarted()) {
            TextInputDialog inputName = new TextInputDialog("Name");
            inputName.setHeaderText("Enter your playername");
            selectedName = inputName.showAndWait();
            players.add(selectedName.get());
            lobby.setPlayers(players);
        } else {
            ChoiceDialog<String> selectYouPlayerName = new ChoiceDialog<>(playerOptions.get(0), playerOptions);
            selectYouPlayerName.setTitle("Player name");
            selectYouPlayerName.setHeaderText("Select the name of your player");
            selectedName = selectYouPlayerName.showAndWait();
            playerOptions.remove(selectedName.get());
            lobby.setPlayerOptions(playerOptions);
        }

        lobbyClient.updateLobby(lobby.getId(), lobby);


        Alert waitForPlayersToJoin = new Alert(AlertType.INFORMATION, "Close", ButtonType.CANCEL);
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        waitForPlayersToJoin.setContentText(getLobbyPlayerListText(lobby.isGameStarted()));
        lobby = lobbyClient.getLobbyById(lobby.getId());
        waitForPlayersToJoin.setTitle("Waiting for players to join lobby");
        waitForPlayersToJoin.setHeaderText("Lobby ID: " + lobby.getId());
        waitForPlayersToJoin.setOnShown(e -> delay.playFromStart());
        waitForPlayersToJoin.setOnCloseRequest(e -> waitForPlayersToJoin.close());
        waitForPlayersToJoin.getDialogPane().setMinHeight(400);
        delay.setOnFinished(e -> {
            lobby = lobbyClient.getLobbyById(lobby.getId());
            waitForPlayersToJoin.setContentText(getLobbyPlayerListText(lobby.isGameStarted()));
            delay.playFromStart();
            if (lobby.isActive() || lobby.getPlayerOptions().size() == 0 && lobby.isGameStarted()) {
                waitForPlayersToJoin.close();
                waitForPlayersToJoin.setResult(ButtonType.OK);
                delay.stop();
            }
        });
        waitForPlayersToJoin.showAndWait();
        lobby.setActive(true);
        lobbyClient.updateLobby(lobby.getId(), lobby);
        if (waitForPlayersToJoin.getResult() == ButtonType.CLOSE || waitForPlayersToJoin.getResult() == ButtonType.CANCEL) {
            players.remove(selectedName.get());
            lobby.setPlayers(players);
            lobbyClient.updateLobby(lobby.getId(), lobby);
            lobby = null;
            this.isOnline = false;
            return;
        }
        String json;
        do {
            json = lobbyClient.getJSONbyID(lobby.getId());
        } while (json == null || json.equals(""));
        while(lobby.getPlayerOptions().size() != 0){
            lobby = lobbyClient.getLobbyById(lobby.getId());
        }
        Board board = SaveLoad.load(json, true);
        // this should only be done for a fresh game so players generate their cards in the first turn
        board.setIsFirstTurnOfLoadedGame(true);
        gameController = new OnlineGameController(board, this, selectedName.get(), lobby.getId());
        gameController.startProgrammingPhase();
        roboRally.createBoardView(gameController);
    }

    private void startLoadedGame(Board board) {
        gameController = new OfflineGameController(board, this);
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
            if(lobby != null && this.isOnline){
                lobby = lobbyClient.getLobbyById(lobby.getId());
                lobby.setPlayerOptions(lobby.getPlayers());
                if(lobby.isActive()) {
                    lobby.setActive(false);
                }
                lobbyClient.updateLobby(lobby.getId(), lobby);
            }
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
     *
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

    public boolean getIsOnline() {
        return isOnline;
    }

    private String getLobbyPlayerListText(boolean active) {
        StringBuilder playersInLobby;
        if(active){
            playersInLobby =
                    new StringBuilder("Waiting for players(" + lobby.getPlayerOptions().size() + "/" + lobby.getSelectedNrOfPlayers() + "):\n\n");
            for (String player : lobby.getPlayers()){
                if(lobby.getPlayerOptions().contains(player)) {
                    playersInLobby.append("waiting for " + player).append("\n");
                } else{
                    playersInLobby.append(player).append(" has joined\n");
                }
            }
        } else {
            playersInLobby = new StringBuilder("Players in lobby (" + lobby.getPlayers().size() + "/" + lobby.getSelectedNrOfPlayers() + "):\n\n");
            for (String player : lobby.getPlayers()) {
                playersInLobby.append(player).append("\n");
            }
        }
        return playersInLobby.toString();
    }

}

