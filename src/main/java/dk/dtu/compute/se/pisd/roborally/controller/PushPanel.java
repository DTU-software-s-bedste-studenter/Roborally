package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class PushPanel extends FieldAction{
    private Heading heading;

    public Heading getHeading() {
        return heading;
    }
    @Override
    public boolean doAction(GameController gameController, Space space) {
        while(space.getPlayer() != null){
            if (gameController.board.getStep() == 2 || gameController.board.getStep() == 4){
                gameController.pushPlayer(space.getPlayer(), getHeading());
            }
        }
        return false;
    }
}
