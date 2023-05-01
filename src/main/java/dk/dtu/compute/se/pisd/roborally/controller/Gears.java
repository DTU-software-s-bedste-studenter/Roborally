package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Gears extends FieldAction{


    @Override
    public boolean doAction(GameController gameController, Space space) {
        if(space.rotation?){
            gameController.turnRight(space.getPlayer());
        }


        if(space.rotation?){
            gameController.turnLeft(space.getPlayer());
        }
        
        return false;
    }
}
