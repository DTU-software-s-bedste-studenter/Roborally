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
    private List<Type> spaces;
    private List<Type> players;

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

    public List<Type> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<Type> spaces) {
        this.spaces = spaces;
    }

    public List<Type> getPlayers() {
        return players;
    }

    public void setPlayers(List<Type> players) {
        this.players = players;
    }


}
