package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

public class OfflineGameController extends GameController {
    public OfflineGameController(@NotNull Board board, AppController appController) {
        super(board, appController);
    }

    protected CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        if (!board.getIsFirstTurnOfLoadedGame()) {
            for (int i = 0; i < board.getNumberOfPlayers(); i++) {
                this.giveNewCardsToPlayer(board.getPlayer(i));
            }
        }
        else {
            board.setIsFirstTurnOfLoadedGame(false);
        }
    }
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
    }
    protected void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getNumberOfPlayers(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }
    protected void setNextPlayer(Player currentPlayer, int currentStep) {
        int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
        if (nextPlayerNumber < board.getNumberOfPlayers()) {
            board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
        } else {
            currentStep++;
            activateActions();
            if (currentStep < Player.NO_REGISTERS) {
                makeProgramFieldsVisible(currentStep);
                board.setStep(currentStep);
                board.setCurrentPlayer(board.getPlayer(0));
            } else {
                startProgrammingPhase();
            }
        }
    }
}
