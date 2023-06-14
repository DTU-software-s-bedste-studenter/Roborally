package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Rotation;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Gears extends FieldAction {

    private Rotation rotation;

    public Rotation getRotation(){
        return this.rotation;
    }

    public void setRotation(Rotation rotation){
        this.rotation = rotation;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        if(getRotation() == Rotation.CLOCKWISE){
            gameController.turnRight(space.getPlayer());
            return true;
        }


        if(getRotation() == Rotation.COUNTERCLOCKWISE){
            gameController.turnLeft(space.getPlayer());
            return true;
        }

        return false;
    }
}
