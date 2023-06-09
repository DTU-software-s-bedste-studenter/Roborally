package dk.dtu.compute.se.pisd.roborally.HTTPClient;

import java.util.List;

public class Lobby {

    public int id;
    public List<String> players;
    public boolean gameStarted;
    public int selectedNrOfPlayers;
    public String JSON;

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
