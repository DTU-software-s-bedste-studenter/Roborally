package com.example.roborallyspringapi.service;

import com.example.roborallyspringapi.api.model.Lobby;
import org.springframework.stereotype.Service;
import com.example.roborallyspringapi.api.model.Phase;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Arrays;

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

    @Override
    public boolean notifyPhaseChange(int lobbyID, String playerName) {
        Lobby lobby = null;
        for(Lobby lb : lobbyList) {
            if(lb.getId() == id) {
                lobby = lb;
            }
        }
        ArrayList<Pair<String, Phase>> phases = lobby.getPlayerPhases();
        if (phases.size() < lobby.getSelectedNrOfPlayers()) {
            
        }
        else {
            switch (phases.get(playerName)) {
                case PROGRAMMING -> phases.put(playerName, Phase.ACTIVATION);
                case ACTIVATION -> phases.put(playerName, Phase.PROGRAMMING);
            }
        }
        return true;
    }

    @Override
    public boolean canProceedToNextPhase(int lobbyID) {
        Lobby lobby = null;
        for(Lobby lb : lobbyList) {
            if(lb.getId() == id) {
                lobby = lb;
            }
        }
        ArrayList<Pair<String, Phase>> phases = lobby.getPlayerPhases();
        if (phases.size() == lobby.getSelectedNrOfPlayers()) {
            for (Phase phase : phases) {
                if (phase.getValue() != phases.get(0).getValue()) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

}
