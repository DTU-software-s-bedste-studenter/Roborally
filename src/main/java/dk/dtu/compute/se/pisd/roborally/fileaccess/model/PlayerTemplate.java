package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.model.Heading;


public class PlayerTemplate {

    public String name;
    public String color;
    public SpaceTemplate startSpace;

    public SpaceTemplate space;
    public SpaceTemplate prevSpace;
    public boolean activated;
    public Heading heading;

    public CommandCardFieldTemplate[] program;
    public CommandCardFieldTemplate[] cards;

    public int powerCubes;
    public int checkpointTokens;
}
