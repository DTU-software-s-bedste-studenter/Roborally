package com.example.roborallyspringapi.api.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FullBoard {

    private int id;
    private int width;
    private int height;
    private int checkpoints;
    private String boardName;
    private List<Object> spaces;
    private List<Object> players;
    public FullBoard(){

    }

    public FullBoard(int id, int width, int height, int checkpoints, String boardName){
        this.id = id;
        this.width = width;
        this.height = height;
        this.checkpoints = checkpoints;
        this.boardName = boardName;
        this.spaces = new ArrayList<>();
        this.players = new ArrayList<>();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(int checkpoints) {
        this.checkpoints = checkpoints;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public List<Object> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<Object> spaces) {
        this.spaces = spaces;
    }

    public List<Object> getPlayers() {
        return players;
    }

    public void setPlayers(List<Object> players) {
        this.players = players;
    }


}
