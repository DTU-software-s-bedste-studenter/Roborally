package com.example.roborallyspringapi.api.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.roborallyspringapi.api.model.Phase;

public class Lobby {

    private int id;

    private List<String> players;

    private List<String> playerOptions;

    private boolean active;

    private boolean gameStarted;
    private int selectedNrOfPlayers;

    private String JSON;
    private HashMap<String, Phase> playerPhases;

    public Lobby(){

    }
    public Lobby(int id){
        this.id = id;
        this.players = new ArrayList<>();
        this.playerOptions = new ArrayList<>();
        this.playerPhases = new HashMap<>();
        this.active = active;
        this.gameStarted = gameStarted;
        this.JSON = JSON;
        this.selectedNrOfPlayers = selectedNrOfPlayers;
    }

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
    public List<String> getPlayerOptions() {
        return playerOptions;
    }

    public void setPlayerOptions(List<String> playerOptions) {
        this.playerOptions = playerOptions;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getSelectedNrOfPlayers() {
        return selectedNrOfPlayers;
    }

    public void setSelectedNrOfPlayers(int selectedNrOfPlayers) {
        this.selectedNrOfPlayers = selectedNrOfPlayers;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public String getJSON(){
        return JSON;
    }

    public void setJSON(String JSON){
        this.JSON = JSON;
    }

    public HashMap<String, Phase> getPlayerPhases() {
        return playerPhases;
    }
}
