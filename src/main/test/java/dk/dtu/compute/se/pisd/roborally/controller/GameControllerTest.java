package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.junit.jupiter.api.Test;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameControllerTest {
    RoboRally robo = new RoboRally();
    AppController appController = new AppController(robo);
    Board board = new Board(8, 8, 2, "testBoard");
    GameController gameC = new GameController(board, appController);

    @Test
    void moveCurrentPlayerToSpace() {
        Player player1 = new Player(board, null,"Player1");
        gameC.board.addPlayer(player1);
        gameC.board.setCurrentPlayer(player1);
        gameC.moveCurrentPlayerToSpace(gameC.board.getSpace(5,5));

        assertEquals(player1.getSpace(), gameC.board.getSpace(5,5));
    }

    @Test
    void startProgrammingPhase() {
        assertEquals(board.getPhase(), INITIALISATION);
        gameC.startProgrammingPhase();
        assertEquals(board.getPhase(), PROGRAMMING);
    }

    @Test
    void movePlayer_NotReversed() {
        Player player1 = new Player(board, null,"Player1");
        player1.setSpace(gameC.board.getSpace(0,0));
        assertEquals(player1.getSpace(), gameC.board.getSpace(0, 0));
        gameC.movePlayer(player1, 4, false);
        assertEquals(player1.getSpace(), gameC.board.getSpace(0,4));
        player1.setHeading(Heading.EAST);
        gameC.movePlayer(player1, 4, false);
        assertEquals(player1.getSpace(), gameC.board.getSpace(4,4));
    }

    @Test
    void movePlayer_Reversed(){
        Player player1 = new Player(board, null,"Player1");
        player1.setSpace(gameC.board.getSpace(6,6));
        gameC.movePlayer(player1, 2, true);
        assertEquals(player1.getSpace(), gameC.board.getSpace(6,4));
    }

    @Test
    void pushPlayer() {
        Player player1 = new Player(board, null,"Player1");
        player1.setSpace(gameC.board.getSpace(0,0));
        assertEquals(player1.getSpace(), gameC.board.getSpace(0,0));
        gameC.pushPlayer(player1, player1.getHeading());
        assertEquals(player1.getSpace(), gameC.board.getSpace(0,1));


    }

    @Test
    void turnRight() {
        Player player1 = new Player(board, null,"Player1");
        assertEquals(player1.getHeading(), Heading.SOUTH);
        gameC.turnRight(player1);
        assertEquals(player1.getHeading(), Heading.WEST);
    }

    @Test
    void turnLeft() {
        Player player1 = new Player(board, null,"Player1");
        assertEquals(player1.getHeading(), Heading.SOUTH);
        gameC.turnLeft(player1);
        assertEquals(player1.getHeading(), Heading.EAST);
    }

    @Test
    void makeUTurn() {
        Player player1 = new Player(board, null,"Player1");
        player1.setHeading(Heading.NORTH);
        gameC.makeUTurn(player1);
        assertEquals(player1.getHeading(), Heading.SOUTH);
    }
}