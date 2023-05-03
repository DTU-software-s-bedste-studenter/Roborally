package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import java.io.*;
import java.util.ArrayList;

public class SaveLoad {

    private static final String SAVEFOLDER = System.getProperty("user.dir") + "/src/main/resources/save/";
    public static void save(Board board)
    {
        ClassLoader classLoader = Gson.class.getClassLoader();
        String boardSaveFilename = SAVEFOLDER + "board.json";

        FileWriter fileWriter = null;
        JsonWriter writer = null;

        try {
            new File(boardSaveFilename).createNewFile();
            fileWriter = new FileWriter(boardSaveFilename);
            GsonBuilder builder = new GsonBuilder().registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>()).setPrettyPrinting();

            Gson gson = builder.create();

            writer = gson.newJsonWriter(fileWriter);

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

            BoardTemplate boardTemplate = new BoardTemplate();
            boardTemplate.width = board.width;
            boardTemplate.height = board.height;
            boardTemplate.checkpoints = board.checkpoints;
            boardTemplate.spaces = spaceTemplates;

            gson.toJson(boardTemplate, BoardTemplate.class, writer);

            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load(String pathToSaveFile)
    {

    }
}
