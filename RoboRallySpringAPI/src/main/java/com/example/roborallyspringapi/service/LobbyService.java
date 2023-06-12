package com.example.roborallyspringapi.service;

import com.example.roborallyspringapi.api.model.Lobby;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LobbyService implements ILobbyService{
    ArrayList<Lobby> lobbyList = new ArrayList<>();

    public LobbyService(){
    }
    @Override
    public  List<Lobby> findAll() {
        return lobbyList;
    }

    @Override
    public boolean addLobby(Lobby l) {
        lobbyList.add(l);
        return true;
    }

    @Override
    public Lobby getLobbyById(int id) {
        for(Lobby f : lobbyList) {
            if(f.getId() == id) {
                return f;
            }
        }
        return null;
    }


    @Override
    public boolean updateLobby(int id, Lobby f) {
        for(Lobby lb : lobbyList) {
            if(lb.getId() == id) {
                lb.setId(f.getId());
                lb.setSelectedNrOfPlayers(f.getSelectedNrOfPlayers());
                lb.setAreAllPlayersFinished(f.getAreAllPlayersFinished());
                lb.setActive(f.isActive());
                lb.setPlayerOptions(f.getPlayerOptions());
                lb.setGameStarted(f.isGameStarted());
                lb.setPlayers(f.getPlayers());
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean deleteLobbyById(int id) {
        int oldSize = lobbyList.size();
        lobbyList.forEach((lobby -> {
            if(lobby.getId() == id)
                lobbyList.remove(
                        lobby
                );
        }));
        return oldSize < lobbyList.size() ? true : false;
    }

    @Override
    public String getJSONById(int id) {
        for(Lobby f : lobbyList) {
            if(f.getId() == id) {
                return f.getJSON();
            }
        }
        return null;
    }

    @Override
    public boolean updateJSON(int id, String l) {
        for(Lobby lb : lobbyList) {
            if(lb.getId() == id) {
                lb.setJSON(l);
                return true;
            }
        }
        return false;
    }

}
