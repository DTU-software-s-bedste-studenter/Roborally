package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Rotation;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Gears extends FieldAction{

    private Rotation rot;

    public Gears(Rotation rotation){
        this.rot = rotation;
    }


    @Override
    public boolean doAction(GameController gameController, Space space) {
        if(this.rot == Rotation.CLOCKWISE){
            gameController.turnRight(space.getPlayer());
        }


        if(this.rot == Rotation.COUNTERCLOCKWISE){
            gameController.turnLeft(space.getPlayer());
        }

        return false;
    }
}
