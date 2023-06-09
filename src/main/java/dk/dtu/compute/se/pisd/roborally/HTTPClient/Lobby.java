package dk.dtu.compute.se.pisd.roborally.HTTPClient;

import java.util.List;

public class Lobby {

    private int id;
    private List<String> players;
    private boolean gameStarted;
    private int selectedNrOfPlayers;
    private String JSON;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public int getSelectedNrOfPlayers() {
        return selectedNrOfPlayers;
    }

    public void setSelectedNrOfPlayers(int selectedNrOfPlayers) {
        this.selectedNrOfPlayers = selectedNrOfPlayers;
    }
    public String getJSON() {
        return JSON;
    }

    public void setJSON(String JSON) {
        this.JSON = JSON;
    }
}
