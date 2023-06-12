package com.example.roborallyspringapi.api.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lobby {

    private int id;

    private List<String> players;

    private List<String> playerOptions;

    private HashMap<String, Boolean> areAllPlayersFinished;
    private boolean active;

    private boolean gameStarted;
    private int selectedNrOfPlayers;

    private String JSON;

    public Lobby(){

    }
    public Lobby(int id){
        this.id = id;
        this.players = new ArrayList<>();
        this.playerOptions = new ArrayList<>();
        this.areAllPlayersFinished = new HashMap<>();
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
    public HashMap<String, Boolean> getAreAllPlayersFinished() {
        return areAllPlayersFinished;
    }

    public void setAreAllPlayersFinished(HashMap<String, Boolean> areAllPlayersFinished) {
        this.areAllPlayersFinished = areAllPlayersFinished;
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

}
