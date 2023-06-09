package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class PushPanel extends FieldAction {
    private Heading heading;
    private int reg1;
    private int reg2;
    private int reg3;

    public Heading getHeading() {
        return heading;
    }
    public int getReg1(){return this.reg1;}
    public int getReg2(){return this.reg2;}
    public int getReg3(){return this.reg3;}
    @Override
    public boolean doAction(GameController gameController, Space space) {
        if ((gameController.board.getStep() == getReg1() || gameController.board.getStep() == getReg2() || gameController.board.getStep() == getReg3()) && space.getPlayer() != null){
                gameController.pushPlayer(space.getPlayer(), getHeading());
                return true;
            }
        return false;
    }
}
