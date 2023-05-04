package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.CommandCardFieldTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.FullBoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import java.io.*;
import java.util.ArrayList;

public class SaveLoad {

    private static final String SAVEFILEPATH = System.getProperty("user.dir") + "/src/main/resources/save/roborally_save.json";
    public static void save(Board board)
    {
        FileWriter fileWriter = null;
        JsonWriter writer = null;

        try {
            new File(SAVEFILEPATH).createNewFile();
            fileWriter = new FileWriter(SAVEFILEPATH);
            GsonBuilder builder = new GsonBuilder().registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>()).setPrettyPrinting();

            Gson gson = builder.create();

            writer = gson.newJsonWriter(fileWriter);

            ArrayList<SpaceTemplate> spaceTemplates = buildSpaceTemplates(board);
            ArrayList<PlayerTemplate> playerTemplates = buildPlayerTemplates(board);

            FullBoardTemplate boardTemplate = buildBoardTemplate(board, spaceTemplates, playerTemplates);

            gson.toJson(boardTemplate, FullBoardTemplate.class, writer);

            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load()
    {
        // most of the objects have the board as a member variable, which needs to be set during loading of a save
    }

    private static FullBoardTemplate buildBoardTemplate(Board board, ArrayList<SpaceTemplate> spaceTemplates, ArrayList<PlayerTemplate> playerTemplates)
    {
        FullBoardTemplate boardTemplate = new FullBoardTemplate();
        boardTemplate.width = board.width;
        boardTemplate.height = board.height;
        boardTemplate.checkpoints = board.checkpoints;
        boardTemplate.spaces = spaceTemplates;
        boardTemplate.players = playerTemplates;
        return boardTemplate;
    }
    private static ArrayList<SpaceTemplate> buildSpaceTemplates(Board board)
    {
        ArrayList<SpaceTemplate> spaceTemplates = new ArrayList<>();

        for (int i = 0; i < board.width; i++)
        {
            for (int j = 0; j < board.height; j++)
            {
                Space space = board.getSpace(i, j);
                SpaceTemplate spaceTemplate = new SpaceTemplate();
                spaceTemplate.x = space.x;
                spaceTemplate.y = space.y;
                spaceTemplate.actions = space.getActions();
                spaceTemplate.walls = space.getWalls();
                spaceTemplates.add(spaceTemplate);
            }
        }
        return spaceTemplates;
    }

    private static ArrayList<PlayerTemplate> buildPlayerTemplates(Board board)
    {
        ArrayList<PlayerTemplate> players = new ArrayList<>();
        for (int i = 0; i < board.getNumberOfPlayers(); i++)
        {
            Player player = board.getPlayer(i);
            PlayerTemplate playerTemplate = new PlayerTemplate();

            playerTemplate.name = player.getName();
            playerTemplate.color = player.getColor();

            playerTemplate.startSpace = new SpaceTemplate();
            playerTemplate.startSpace.walls = player.getStartSpace().getWalls();
            playerTemplate.startSpace.actions = player.getStartSpace().getActions();
            playerTemplate.startSpace.x = player.getStartSpace().x;
            playerTemplate.startSpace.y = player.getStartSpace().y;

            playerTemplate.space = new SpaceTemplate();
            playerTemplate.startSpace.walls = player.getSpace().getWalls();
            playerTemplate.startSpace.actions = player.getSpace().getActions();
            playerTemplate.startSpace.x = player.getSpace().x;
            playerTemplate.startSpace.y = player.getSpace().y;

            playerTemplate.prevSpace = new SpaceTemplate();
            if (player.getPrevSpace() != null) {
                playerTemplate.prevSpace.walls = player.getPrevSpace().getWalls();
                playerTemplate.prevSpace.actions = player.getPrevSpace().getActions();
                playerTemplate.prevSpace.x = player.getPrevSpace().x;
                playerTemplate.prevSpace.y = player.getPrevSpace().y;
            }

            playerTemplate.activated = player.getActivated();
            playerTemplate.heading = player.getHeading();

            playerTemplate.program = new CommandCardFieldTemplate[5];
            for (int j = 0; j < playerTemplate.program.length; j++) {
                playerTemplate.program[j] = new CommandCardFieldTemplate();
                playerTemplate.program[j].card = player.getProgramField(j).getCard();
                playerTemplate.program[j].visible = player.getProgramField(j).isVisible();
            }

            playerTemplate.cards = new CommandCardFieldTemplate[8];
            for (int k = 0; k < playerTemplate.cards.length; k++) {
                playerTemplate.cards[k] = new CommandCardFieldTemplate();
                playerTemplate.cards[k].card = player.getCardField(k).getCard();
                playerTemplate.cards[k].visible = player.getCardField(k).isVisible();
            }

            playerTemplate.powerCubes = player.getPowerCubes();
            playerTemplate.checkpointTokens = player.getCheckpointTokens();

            players.add(playerTemplate);
        }
        return players;
    }
}
