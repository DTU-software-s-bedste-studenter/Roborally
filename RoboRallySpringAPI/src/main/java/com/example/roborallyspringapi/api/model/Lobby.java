package com.example.roborallyspringapi.api.model;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private int id;

    private List<String> players;

    private String JSON;

    public Lobby(){

    }
    public Lobby(int id){
        this.id = id;
        this.players = new ArrayList<>();
        this.JSON = JSON;
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

    public String getJSON(){
        return JSON;
    }

    public void setJSON(String JSON){
        this.JSON = JSON;
    }

}
