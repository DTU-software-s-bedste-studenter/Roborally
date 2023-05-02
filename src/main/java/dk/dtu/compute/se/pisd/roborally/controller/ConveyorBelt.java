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

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class ConveyorBelt extends FieldAction {

    private Heading heading;

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        Player currentPlayer = space.getPlayer();
        ConveyorBelt conveyorBelt = (ConveyorBelt) space.getActions().get(0);
        Heading currentHeading = conveyorBelt.getHeading();
        if (currentPlayer != null) {
            Space nextSpace = gameController.board.getNeighbour(space, currentHeading);
            Player nextPlayer = nextSpace.getPlayer();
            if (nextPlayer == null) {
                gameController.pushPlayer(currentPlayer, currentHeading);
                return true;
            } else {
                if(gameController.board.getPlayerNumber(currentPlayer) < gameController.board.getPlayerNumber(nextPlayer)) {
                    if(!nextSpace.getActions().isEmpty()) {
                        if (nextSpace.getActions().get(0).getClass() == ConveyorBelt.class) {
                            nextPlayer.setActivated(true);
                            doAction(gameController, nextSpace);
                            gameController.pushPlayer(currentPlayer, currentHeading);
                        } else {
                            gameController.pushPlayer(currentPlayer, currentHeading);
                        }
                    } else{
                        gameController.pushPlayer(currentPlayer, currentHeading);
                    }
                    return true;
                }
                else{
                    if(nextPlayer.getSpace() == nextPlayer.getPrevSpace()){
                            gameController.pushPlayer(currentPlayer, currentHeading);
                            return true;
                        }
                    else{
                        nextPlayer.setSpace(nextPlayer.getPrevSpace());
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
