package com.example.roborallyspringapi.service;

import com.example.roborallyspringapi.api.model.FullBoard;
import com.example.roborallyspringapi.api.model.Lobby;

import java.util.List;


public interface ILobbyService {


    public List<Lobby> findAll();

    boolean addLobby(Lobby f);

    Lobby getLobbyById(int id);


    String getJSONById(int id);

    boolean updateLobby(int id, Lobby f);

    boolean deleteLobbyById(int id);

    boolean updateJSON(int id, String l);
}
