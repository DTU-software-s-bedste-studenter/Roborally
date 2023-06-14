package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.fileaccess.SaveLoad;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.FullBoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
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
                    Board updatedBoard = SaveLoad.load(appController.lobbyClient.getJSONbyID(lobbyID), true);
                    updateBoardTotal(board, updatedBoard);
                    board.setPhase(Phase.ACTIVATION);
                    this.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(waitForActivationPhase, 0, 3000);
    }

    public Board downloadAndUpdateJsonAfterProgramming() {
        String download = this.appController.lobbyClient.getJSONbyID(this.lobbyID);
        Board board = SaveLoad.load(download, true);
        int i;
        for (i = 0; i < this.board.getNumberOfPlayers(); i++) {
            if (this.board.getPlayer(i).getName().equals(localPlayer.getName())) {
                break;
            }
        }

            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                board.getPlayer(i).getProgramField(j).setCard(localPlayer.getProgramField(j).getCard());
                board.getPlayer(i).getProgramField(j).setVisible(localPlayer.getProgramField(j).isVisible());
            }

            for (int k = 0; k < Player.NO_CARDS; k++) {
                board.getPlayer(i).getCardField(k).setCard(localPlayer.getCardField(k).getCard());
                board.getPlayer(i).getCardField(k).setVisible(localPlayer.getCardField(k).isVisible());
            }

            String updatedBoard = SaveLoad.buildGameStateToJSON(board);
            this.appController.lobbyClient.updateJSON(updatedBoard, this.lobbyID);
            return board;

    }

    private void updateBoardTotal(Board board, Board updatedBoard){
        FullBoardTemplate template = SaveLoad.buildBoardTemplate(updatedBoard, SaveLoad.buildSpaceTemplates(updatedBoard), SaveLoad.buildPlayerTemplates(updatedBoard));
        board.setPhase(template.phase);

        for (int i = 0; i < template.players.size(); i++)
        {
            PlayerTemplate playerTemplate = template.players.get(i);
            Player player = board.getPlayer(i);
            player.setStartSpace(board.getSpace(playerTemplate.startSpace.x, playerTemplate.startSpace.y));
            player.setSpace(board.getSpace(playerTemplate.space.x, playerTemplate.space.y));
            player.setPrevSpace(board.getSpace(playerTemplate.prevSpace.x, playerTemplate.prevSpace.y));
            player.setActivated(playerTemplate.activated);
            player.setHeading(playerTemplate.heading);

            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                player.getProgramField(j).setCard(playerTemplate.program[j].card);
                player.getProgramField(j).setVisible(playerTemplate.program[j].visible);
            }

            for (int k = 0; k < Player.NO_CARDS; k++) {
                player.getCardField(k).setCard(playerTemplate.cards[k].card);
                player.getCardField(k).setVisible(playerTemplate.cards[k].visible);
            }

            player.setPowerCubes(playerTemplate.powerCubes);
            player.setCheckpointTokens(playerTemplate.checkpointTokens);

        }
    }

    @Override
    public void executeStep() {
        board.setCurrentPlayer(board.getPlayer(0));
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                executeNextStep();
                if (appController.lobbyClient.canProceedToNextPhase(lobbyID)){
                    startProgrammingPhase();
                    this.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }
}
