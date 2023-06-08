package com.example.roborallyspringapi.service;

import com.example.roborallyspringapi.api.model.Lobby;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LobbyService implements ILobbyService{
    ArrayList<Lobby> lobbyList = new ArrayList<>();

    public LobbyService(){
        Lobby lobby1 = new Lobby(1);
        Lobby lobby2 = new Lobby(2);
        Lobby lobby3 = new Lobby(3);
        Lobby lobby4 = new Lobby(4);
        Lobby lobby5 = new Lobby(5);
        lobby1.setJSON("Hey smukke smatso");
        lobbyList.add(lobby1);
        lobbyList.add(lobby2);
        lobbyList.add(lobby3);
        lobbyList.add(lobby4);
        lobbyList.add(lobby5);
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
                lb.setPlayers(f.getPlayers());
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean deleteLobbyById(int id) {
        ArrayList<Lobby> newLobbys = new ArrayList<>();
        int oldSize = lobbyList.size();
        lobbyList.forEach((lobby -> {
            if(lobby.getId() == id)
                newLobbys.add(
                        lobby
                );
        }));
        lobbyList = newLobbys;
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
