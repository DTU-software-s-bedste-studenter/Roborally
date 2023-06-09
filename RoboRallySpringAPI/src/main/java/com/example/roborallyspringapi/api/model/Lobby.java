package com.example.roborallyspringapi.api.model;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private int id;

    private List<String> players;

    private boolean gameStarted;
    private int selectedNrOfPlayers;

    private String JSON;

    public Lobby(){

    }
    public Lobby(int id){
        this.id = id;
        this.players = new ArrayList<>();
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
