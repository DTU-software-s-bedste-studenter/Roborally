package dk.dtu.compute.se.pisd.roborally.HTTPClient;

import dk.dtu.compute.se.pisd.roborally.model.Phase;

public interface ILobbyService {
    Integer getNewLobbyID();

    Lobby getLobbyById(int id);

    String getLobbys() throws Exception;

    boolean addLobby(Lobby l);

    boolean updateLobby(int id, Lobby f);

    boolean deleteLobbyById(int id);

    public String getJSONbyID(int id);

    boolean addJSONbyID(String json, int id);

    boolean updateJSON(String json, int id);

    boolean notifyPhaseChange(int lobbyID, String playerName);

    boolean canProceedToNextPhase(int lobbyID);
}
