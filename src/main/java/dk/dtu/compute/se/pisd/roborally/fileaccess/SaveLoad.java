package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.OutputStream;

public class SaveLoad {

    private static final String SAVEFOLDER = System.getProperty("user.dir") + "src/main/resources/saves/";

    public static void save()
    {
        Gson gson = new Gson();
        OutputStream outputStream = new BufferedOutputStream();
        // gson.newJsonWriter()

    }

    public static void load(String pathToSaveFile)
    {

    }
}
