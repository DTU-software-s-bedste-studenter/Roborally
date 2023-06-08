/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {

    private boolean winnerFound = false;
    private AppController appController;

    final public Board board;
    public GameController(@NotNull Board board, AppController appController) {
        this.board = board;
        this.appController = appController;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space)  {
        // TODO Assignment V1: method should be implemented by the students:
        //   - the current player should be moved to the given space
        //     (if it is free()
        //   - and the current player should be set to the player
        //     following the current player
        //   - the counter of moves in the game should be increased by one
        //     if the player is moved

        if (space != null && space.board == board) {
            Player currentPlayer = board.getCurrentPlayer();
            if (currentPlayer != null && space.getPlayer() == null) {
                currentPlayer.setSpace(space);
                int playerNumber = (board.getPlayerNumber(currentPlayer) + 1) % board.getNumberOfPlayers();
                board.setCurrentPlayer(board.getPlayer(playerNumber));
            }
        }

    }

    // XXX: V2
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getNumberOfPlayers(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // XXX: V2
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    // XXX: V2
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    // XXX: V2
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getNumberOfPlayers(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX: V2
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getNumberOfPlayers(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    /**
     * starts round with stepmode set to false
     */
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    /**
     * Starts round with stepmode set to true
     */
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    /**
     * Continually executes the next steps only if step mode is activated.
     */
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    /**
     * Only executes the next step, and nothing else.
     */
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null && !card.command.isInteractive()) {
                    Command command = card.command;
                    executeCommand(currentPlayer, command);
                    if (isPrevNonAgainCardInteractive()) {
                        return;
                    }
                }
                else if (card != null) {
                    board.setPhase(Phase.PLAYER_INTERACTION);
                    return;
                }
                setNextPlayer(currentPlayer, step);
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }

    /**
     * executes the current command card for the current player.
     * @param player
     * @param command
     */
    private void executeCommand(@NotNull Player player, Command command) {
        if (player.board == board && command != null) {

            switch (command) {
                case FORWARD_1 -> this.movePlayer(player, 1, false);
                case FORWARD_2 -> this.movePlayer(player, 2, false);
                case FORWARD_3 -> this.movePlayer(player, 3, false);
                case RIGHT -> this.turnRight(player);
                case LEFT -> this.turnLeft(player);
                case U_TURN -> this.makeUTurn(player);
                case BACK_UP -> this.movePlayer(player, 1, true);
                case POWER_UP -> {
                    Player currentPlayer = this.board.getCurrentPlayer();
                    currentPlayer.setPowerCubes(currentPlayer.getPowerCubes() + 1);
                }
                case AGAIN -> this.playPrevCardAgain(player, board.getStep());
            }
        }
    }

    // TODO: V2

    /**
     * Moves a player relative to its heading
     * @param player player to be moved
     * @param numberOfSpaces number of spaces to move
     * @param isReversed if true, player moves backwards
     */
    public void movePlayer(@NotNull Player player, int numberOfSpaces, boolean isReversed) {
        boolean again = false;
        do
        {
            Space space = player.getSpace();
            player.setPrevSpace(player.getSpace());
            if (player != null && player.board == board && space != null) {
                Heading heading = player.getHeading();
                if (isReversed)
                {
                    heading = heading.next();
                    heading = heading.next();
                }
                Space target = board.getNeighbour(space, heading);
                if (willCollideWithWall(space, target, heading))
                {
                    return;
                }
                if (target != null && target.getPlayer() == null) {
                    target.setPlayer(player);
                }
                else {
                    if (pushPlayer(target.getPlayer(), heading)){
                        target.setPlayer(player);
                    }
                }
                again = checkPit(target);
            }
            numberOfSpaces--;
        }while(numberOfSpaces > 0 && !again);
    }

    /**
     * Pushes a player to a neighbouring space recursively while checking if a wall obstructs the movement.
     * @param player player to be pushed
     * @param heading direction of the push
     * @return Returns true if move got completed and false if it was obstructed by a wall.
     */
    public boolean pushPlayer(@NotNull Player player, Heading heading)
    {
        Space space = player.getSpace();
        if (player.board == board && space != null) {
            Space target = board.getNeighbour(space, heading);
            if (willCollideWithWall(space, target, heading)) {
                return false;
            }
            if (target != null && target.getPlayer() == null) {
                player.setPrevSpace(player.getSpace());
                target.setPlayer(player);
                checkPit(target);
                return true;
            }
            else {
                if (target != null && pushPlayer(target.getPlayer(), heading)) {
                    player.setPrevSpace(player.getSpace());
                    target.setPlayer(player);
                    checkPit(target);
                    return true;
                }
            }
        }
        return false;
    }

    // TODO: V2
    public void turnRight(@NotNull Player player) {
        if (player != null && player.board == board) {
            player.setHeading(player.getHeading().next());
        }
    }

    // TODO: V2
    public void turnLeft(@NotNull Player player) {
        if (player != null && player.board == board) {
            player.setHeading(player.getHeading().prev());
        }
    }

    /**
     * Turns the player around
     * @param player subject to be turned
     */
    public void makeUTurn(@NotNull Player player) {
        if (player.board == board) {
            player.setHeading(player.getHeading().prev());
            player.setHeading(player.getHeading().prev());
        }
    }

    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Execution logic for again card. Looks backward in registers until a non-again card is found, then executes it.
     * @param currentPlayer Current currentPlayer
     * @param currentStep Current currentStep
     */
    public void playPrevCardAgain(@NotNull Player currentPlayer, int currentStep){
        int prevStep = currentStep -1;
        if(prevStep >= 0) {
            Command command = board.getCurrentPlayer().getProgramField(prevStep).getCard().command;
            if (command == Command.AGAIN && prevStep >= 1) {
                playPrevCardAgain(currentPlayer, prevStep);
            } else if (command != Command.AGAIN && !command.isInteractive()) {
                executeCommand(currentPlayer, command);
            } else {
                board.setPhase(Phase.PLAYER_INTERACTION);
            }
        }
    }
    /**
     * Runs the execution of the interactive cards chosen option
     * @param player current player
     * @param command command to be executed
     */
    public void runChosenOption(@NotNull Player player, Command command){
        executeCommand(player, command);
        board.setPhase(Phase.ACTIVATION);
        setNextPlayer(player, board.getStep());
        if (!board.isStepMode())
        {
            continuePrograms();
        }
    }
    /**
     * Sets the next currentPlayer in the currentPlayer order to be the current currentPlayer.
     * @param currentPlayer Current player
     * @param currentStep Current step
     */
    private void setNextPlayer(Player currentPlayer, int currentStep) {
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

    /**
     * Runs all activations on spaces where a player is standing,
     * afterwards check if a player's position is on a checkpoint,
     * then checks if a winner has been found
     */
    public void activateActions() {
        for (int i = 0; i < board.getNumberOfPlayers(); i++){
            board.getPlayer(i).setActivated(false);
        }
        for (int i = 0; i < board.getNumberOfPlayers(); i++) {
            Player currentPlayer = board.getPlayer(i);
            currentPlayer.setPrevSpace(currentPlayer.getSpace());
            Space space = currentPlayer.getSpace();
            if (!currentPlayer.getActivated()) {
                for (FieldAction fieldaction : space.getActions()) {
                    fieldaction.doAction(this, space);
                    if(fieldaction.getClass() == ConveyorBelt.class){
                        expressConveyorBelt(fieldaction, space);
                    }
                        }
                    }
                }
        for (int i = 0; i < board.getNumberOfPlayers(); i++){
            Player currentPlayer = board.getPlayer(i);
            currentPlayer.setPrevSpace(currentPlayer.getSpace());
            Space space = currentPlayer.getSpace();
            for (FieldAction fieldaction: space.getActions()) {
                if (fieldaction.getClass() == Checkpoint.class || fieldaction.getClass() == Pit.class) {
                    fieldaction.doAction(this, space);
                }
            }
        }
        for (int i = 0; i < board.getNumberOfPlayers(); i++){
            checkForWinner(board.getPlayer(i));
        }
    }

    /**
     * Checks if moving from a space to one next to it, will result in a wall collision.
     * @return True if a wall collision is detected.
     */
    private boolean willCollideWithWall(Space spaceFrom, Space spaceTo, Heading direction)
    {
        return (spaceFrom.getWalls().contains(direction) || spaceTo.getWalls().contains(direction.next().next()));
    }

    /**
     * Checks the current players registers starting from the current step, checking if the most recent card
     * before any number of again cards is interactive.
     * @return Returns true if an interactive card is found, and false if another type of card is found.
     */
    private boolean isPrevNonAgainCardInteractive()
    {
        CommandCard card = this.board.getCurrentPlayer().getProgramField(this.board.getStep()).getCard();
        if(card != null) {
            if (card.command == Command.AGAIN) {
                int i = board.getStep();
                while (i >= 0) {
                    if (this.board.getCurrentPlayer().getProgramField(i).getCard().command != Command.AGAIN) {
                        if (this.board.getCurrentPlayer().getProgramField(i).getCard().command.isInteractive()) {
                            return true;
                        }
                        return false;
                    }
                    i--;
                }
            }
        }
        return false;
    }
    
    /**
     * Checks if a winner has been found by looking at players chekpointtokens
     * and total checkpoints in game.
     * @param player
     */
    private void checkForWinner(Player player) {
        if(player.getCheckpointTokens() == board.checkpoints){
            winnerFound = true;
        }
        if (winnerFound) {
            appController.resetGame(player);
        }
    }

    /**
     * Checks if space is a pit, and...
     * @param space the space to be checked.
     * @return true if we are out of bounds or if we in fact are in a pit, false otherwise.
     */
    private boolean checkPit(Space space){
        if(space.getPlayer() != null) {
            if (!space.getActions().isEmpty()) {
                if (space.getActions().get(0).getClass() == Pit.class) {
                    space.getActions().get(0).doAction(this, space);
                    return true;
                } else if (OutOfMap(space.getPlayer().getPrevSpace(), space)) {
                    clearPlayersCards(space.getPlayer());
                    spaceOccupied(rebootOrStart(space.getPlayer().getPrevSpace(), space.getPlayer()), Heading.EAST);
                    space.getPlayer().setSpace(rebootOrStart(space.getPlayer().getPrevSpace(), space.getPlayer()));
                    return true;
                }
            } else if (OutOfMap(space.getPlayer().getPrevSpace(), space)) {
                clearPlayersCards(space.getPlayer());
                spaceOccupied(rebootOrStart(space.getPlayer().getPrevSpace(), space.getPlayer()), Heading.EAST);
                space.getPlayer().setSpace(rebootOrStart(space.getPlayer().getPrevSpace(), space.getPlayer()));
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player has fallen out of the map
     * @param prevSpace
     * @param currentSpace
     * @return false if player is still on map, true if not.
     */
    private boolean OutOfMap(Space prevSpace, Space currentSpace){
        if(prevSpace.y == currentSpace.y && (prevSpace.x == currentSpace.x+1 || prevSpace.x == currentSpace.x-1)){
            return false;
        }
        else if(prevSpace.x == currentSpace.x && (prevSpace.y == currentSpace.y+1 || prevSpace.y == currentSpace.y-1)){
            return false;
        } else if(prevSpace.x == currentSpace.x && prevSpace.y == currentSpace.y) {
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Clears the players next progammingcards.
     * @param player
     */
    public void clearPlayersCards(Player player) {
        for (int i = board.getStep()+1; i < 5; i++) {
            player.getProgramField(i).setCard(null);
        }
    }

    /**
     * Checks if the space the player is about to be rebooted at is already occupied
     * if it is then that robot will be pushed away to make room for the next.
     * @param space
     */
    public void spaceOccupied(Space space, Heading heading) {
        Space nextSpace;
        if (space != null) {
            if (space.getPlayer() != null) {
                if (!space.getActions().isEmpty()) {
                    if (space.getActions().get(0).getClass() == Reboot.class) {
                        Reboot reboot = (Reboot) space.getActions().get(0);
                        nextSpace = board.getNeighbour(space, reboot.getHeading());
                        heading = reboot.getHeading();
                    } else {
                        nextSpace = board.getNeighbour(space, heading);
                    }
                } else {
                    nextSpace = board.getNeighbour(space, heading);
                }
                spaceOccupied(nextSpace, heading);
                if(space.getPlayer().getStartSpace() != space) {
                    space.getPlayer().setSpace(nextSpace);
                }
            }
        }
    }

    /**
     * Determines if the player should be rebooted at a rebootstation or startfield.
     * @param space the space which it stood before falling over the edge or in a pit.
     * @param player the player which fell in a pit or out of map.
     * @return the players startspace, or a rebootstation
     */
    public Space rebootOrStart(Space space, Player player){
        if(space.x < 3){
            return player.getStartSpace();
        }
        else{
            return getRebootSpace();
        }
    }

    /**
     * Checks if space is an expressConveyorbelt, and checks if next space is also
     * if it is, it will move one more, if not, it will stay.
     * @param fieldaction
     * @param space
     */
    private void expressConveyorBelt(FieldAction fieldaction, Space space) {
        ConveyorBelt conveyorBelt = (ConveyorBelt) fieldaction;
        if (conveyorBelt.getExpress()) {
            Space newSpace = board.getNeighbour(space, conveyorBelt.getHeading());
            if (!newSpace.getActions().isEmpty()) {
                if (newSpace.getActions().get(0).getClass() == ConveyorBelt.class) {
                    ConveyorBelt secondConveyor = (ConveyorBelt) newSpace.getActions().get(0);
                    if (secondConveyor.getExpress()) {
                        conveyorBelt.doAction(this, newSpace);
                    }
                }
            }
        }
    }

    /**
     * Finds rebootstation on the board.
     * @return rebootstation
     */
    private Space getRebootSpace() {
        for (int i = 0; i < board.width; i++) {
            for (int j = 0; j < board.height; j++) {
                if (!board.getSpace(i, j).getActions().isEmpty()) {
                    if (board.getSpace(i, j).getActions().get(0).getClass() == Reboot.class) {
                        return board.getSpace(i, j);
                    }
                }
            }
        }
        return null;
    }
}