package dk.dtu.compute.se.pisd.roborally.controller;

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

    @Override
    public void executeStep() {

    }

    private void getOnlineBoardAndOverwrite() {
        this.appController.lobbyClient.getJSONbyID(this.lobbyID);
    }

}
