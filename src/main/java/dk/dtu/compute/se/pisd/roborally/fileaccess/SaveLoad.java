package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.roborally.HTTPClient.FullBoardClient;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.CommandCardFieldTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.FullBoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.control.ChoiceDialog;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SaveLoad {

    private static final String SAVEFILEPATH = System.getProperty("user.dir") + "/src/main/resources/save/";
    private static final List<String> SAVEFILENAMES = Arrays.asList("SaveSlot 1", "SaveSlot 2", "SaveSlot 3", "SaveSlot 4", "SaveSlot 5", "SaveSlot 6");
    private static final String LAST_GAME = "LastGame";

    private static FullBoardClient fullBoardClient = new FullBoardClient();
    public static void save(Board board, boolean stop)
    {
        String filename;
        if(stop){
            filename = LAST_GAME;
        }else {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(SAVEFILENAMES.get(0), SAVEFILENAMES);
        dialog.setTitle("Save game");
        dialog.setHeaderText("Select the slot you want to save your game in:");
        Optional<String> result2 = dialog.showAndWait();
        filename = result2.get();
        }

        FileWriter fileWriter = null;
        JsonWriter writer = null;

        try {
            File filefolder = new File(SAVEFILEPATH);
            filefolder.mkdir();
            File jsonFile = new File(SAVEFILEPATH + filename);
            jsonFile.createNewFile();
            fileWriter = new FileWriter(SAVEFILEPATH + filename);
            GsonBuilder builder = new GsonBuilder().registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>()).setPrettyPrinting();

            Gson gson = builder.create();

            writer = gson.newJsonWriter(fileWriter);

            ArrayList<SpaceTemplate> spaceTemplates = buildSpaceTemplates(board);
            ArrayList<PlayerTemplate> playerTemplates = buildPlayerTemplates(board);
            board.setGameId(0);
            FullBoardTemplate boardTemplate = buildBoardTemplate(board, spaceTemplates, playerTemplates);

            gson.toJson(boardTemplate, FullBoardTemplate.class, writer);

            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static Board load(String filename)
    {
        // most of the objects have the board as a member variable, which needs to be set during loading of a save

        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();

        JsonReader reader = null;
        try {
            FileReader fileReader = new FileReader(SAVEFILEPATH + filename);
            reader = gson.newJsonReader(fileReader);
            FullBoardTemplate template = gson.fromJson(reader, FullBoardTemplate.class);

            Board resultBoard = new Board(template.width, template.height, template.checkpoints, template.boardName);

            for (SpaceTemplate spaceTemplate : template.spaces) {
                Space space = resultBoard.getSpace(spaceTemplate.x, spaceTemplate.y);
                if (space != null) {
                    space.getActions().addAll(spaceTemplate.actions);
                    space.getWalls().addAll(spaceTemplate.walls);
                }
            }

            for (int i = 0; i < template.players.size(); i++)
            {
                PlayerTemplate playerTemplate = template.players.get(i);
                Player player = new Player(resultBoard, playerTemplate.color, playerTemplate.name);
                player.setStartSpace(resultBoard.getSpace(playerTemplate.startSpace.x, playerTemplate.startSpace.y));
                player.setSpace(resultBoard.getSpace(playerTemplate.space.x, playerTemplate.space.y));
                player.setPrevSpace(resultBoard.getSpace(playerTemplate.prevSpace.x, playerTemplate.prevSpace.y));
                player.setActivated(playerTemplate.activated);
                player.setHeading(playerTemplate.heading);

                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    player.getProgramField(j).setCard(playerTemplate.program[j].card);
                    player.getProgramField(j).setVisible(playerTemplate.program[j].visible);
                }

                for (int k = 0; k < Player.NO_CARDS; k++) {
                    player.getCardField(k).setCard(playerTemplate.cards[k].card);
                    player.getCardField(k).setVisible(playerTemplate.cards[k].visible);
                }

                player.setPowerCubes(playerTemplate.powerCubes);
                player.setCheckpointTokens(playerTemplate.checkpointTokens);
                resultBoard.addPlayer(player);
            }
            reader.close();
            return resultBoard;
        } catch (IOException e1) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    public static FullBoardTemplate buildBoardTemplate(Board board, ArrayList<SpaceTemplate> spaceTemplates, ArrayList<PlayerTemplate> playerTemplates)
    {
        FullBoardTemplate boardTemplate = new FullBoardTemplate();
        boardTemplate.id = board.getGameId();
        boardTemplate.boardName = board.boardName;
        boardTemplate.width = board.width;
        boardTemplate.height = board.height;
        boardTemplate.checkpoints = board.checkpoints;
        boardTemplate.spaces = spaceTemplates;
        boardTemplate.players = playerTemplates;
        return boardTemplate;
    }
    public static ArrayList<SpaceTemplate> buildSpaceTemplates(Board board)
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

    public static ArrayList<PlayerTemplate> buildPlayerTemplates(Board board)
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
            playerTemplate.space.walls = player.getSpace().getWalls();
            playerTemplate.space.actions = player.getSpace().getActions();
            playerTemplate.space.x = player.getSpace().x;
            playerTemplate.space.y = player.getSpace().y;

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
