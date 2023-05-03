package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Pit extends FieldAction{

    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player currentPlayer = space.getPlayer();
        if (currentPlayer != null) {
            gameController.clearPlayersCards(currentPlayer);
            gameController.spaceOccupied(currentPlayer.getStartSpace());
            currentPlayer.setSpace(currentPlayer.getStartSpace());
            return true;
        }
        return false;
    }
}
