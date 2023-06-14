package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.fileaccess.SaveLoad;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class OnlineGameController extends GameController{

    private int lobbyID;
    private Player localPlayer;
    public Player getLocalPlayer() {return this.localPlayer;}
    public OnlineGameController(@NotNull Board board, AppController appController, String onlinePlayerName, int lobbyID) {
        super(board, appController);
        for (int i = 0; i < this.board.getNumberOfPlayers(); i++) {
            if (this.board.getPlayer(i).getName().equals(onlinePlayerName)) {
                this.localPlayer = this.board.getPlayer(i);
            }
        }
        this.lobbyID = lobbyID;
    }

    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        //if (!board.getIsFirstTurnOfLoadedGame()) {
        //    this.giveNewCardsToPlayer(this.localPlayer);
        //}
        //else {
        //    board.setIsFirstTurnOfLoadedGame(false);
        //}
    }

    @Override
    public void finishProgrammingPhase() {
        downloadAndUpdateJsonAfterProgramming();
        boolean reply = this.appController.lobbyClient.notifyPhaseChange(this.lobbyID, this.localPlayer.getName());

        Timer timer = new Timer();
        TimerTask waitForActivationPhase = new TimerTask() {
            @Override
            public void run() {
                if (appController.lobbyClient.canProceedToNextPhase(lobbyID)) {

                    board.setPhase(Phase.ACTIVATION);
                    this.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(waitForActivationPhase, 0, 3000);
    }

    public void downloadAndUpdateJsonAfterProgramming() {
        String download = this.appController.lobbyClient.getJSONbyID(this.lobbyID);
        Board board = SaveLoad.load(download, true);

        for (int j = 0; j < Player.NO_REGISTERS; j++) {
            board.getPlayer(board.getPlayerNumber(localPlayer)).getProgramField(j).setCard(localPlayer.getProgramField(j).getCard());
            board.getPlayer(board.getPlayerNumber(localPlayer)).getProgramField(j).setVisible(localPlayer.getProgramField(j).isVisible());
        }

        for (int k = 0; k < Player.NO_CARDS; k++) {
            board.getPlayer(board.getPlayerNumber(localPlayer)).getCardField(k).setCard(localPlayer.getCardField(k).getCard());
            board.getPlayer(board.getPlayerNumber(localPlayer)).getCardField(k).setVisible(localPlayer.getCardField(k).isVisible());
        }

        String updatedBoard = SaveLoad.buildGameStateToJSON(board);
        this.appController.lobbyClient.updateJSON(updatedBoard, this.lobbyID);
    }

    @Override
    public void executeStep() {

    }
}
