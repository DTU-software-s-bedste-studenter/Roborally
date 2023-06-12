package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Rotation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GearsTest {

    RoboRally robo = new RoboRally();
    AppController appController = new AppController(robo);
    Board board = new Board(8, 8, 2, "testBoard");
    GameController gameC = new GameController(board, appController);

    @Test
    void doAction() {
        Player player1 = new Player(board, null,"Player1");
        player1.setSpace(gameC.board.getSpace(5,5));

        Gears gears = new Gears();
        gears.setRotation(Rotation.CLOCKWISE);
        gears.doAction(gameC, gameC.board.getSpace(5,5));

        assertEquals(player1.getHeading(), Heading.SOUTH);
    }
}