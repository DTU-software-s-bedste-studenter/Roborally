package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Checkpoint extends FieldAction {
    private int checkpointNr;
    public int getCheckpointNr(){return checkpointNr;}
    @Override
    public boolean doAction(GameController gameController, Space space) {
        if(space.getPlayer() != null){
            int playersCPTokens = space.getPlayer().getCheckpointTokens();
            if(playersCPTokens == getCheckpointNr()-1){
                space.getPlayer().setCheckpointTokens(getCheckpointNr());
                return true;
            }
        }
        return false;
    }
}
