package dk.dtu.compute.se.pisd.roborally.HTTPClient;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class LobbyClient implements ILobbyService {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    public String getLobbys() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://34.88.181.0:8080/lobbys"))
                .setHeader("User-Agent", "FullBoard Client")
                .header("Content-Type", "application/json")
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
        return result;
    }

    @Override
    public Lobby getLobbyById(int id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://34.88.181.0:8080/lobbys/" + id))
                    .setHeader("User-Agent", "FullBoard Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            Gson gson = new Gson();
            Lobby l = gson.fromJson(result, Lobby.class);
            return l;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean addLobby(Lobby l) {
        try {
            String lobbyJSON = new Gson().toJson(l);
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(lobbyJSON))
                    .uri(URI.create("http://34.88.181.0:8080/lobbys"))
                    .setHeader("User-Agent", "FullBoard Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            return result.equals("added") ? true : false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateLobby(int id, Lobby f) {
        try {
            String fullBoardJSON = new Gson().toJson(f);
            HttpRequest request = HttpRequest.newBuilder()
                    .PUT(HttpRequest.BodyPublishers.ofString(fullBoardJSON))
                    .uri(URI.create("http://34.88.181.0:8080/lobbys/" + id))
                    .setHeader("User-Agent", "FullBoard Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            return result.equals("updated") ? true : false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteLobbyById(int id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(URI.create("http://34.88.181.0:8080/fullBoards/" + id))
                    .setHeader("User-Agent", "FullBoard Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            return result.equals("deleted") ? true : false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getJSONbyID(int id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://34.88.181.0:8080/lobbys/" + id + "/JSON"))
                    .setHeader("User-Agent", "FullBoard Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            return result;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public boolean addJSONbyID(String json, int id){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .uri(URI.create("http://34.88.181.0:8080/lobbys/" + id + "/JSON"))
                    .setHeader("User-Agent", "FullBoard Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            return result.equals("added") ? true : false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateJSON(String json, int id){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .uri(URI.create("http://34.88.181.0:8080/lobbys/" + id + "/JSON"))
                    .setHeader("User-Agent", "FullBoard Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            return result.equals("updated") ? true : false;
        } catch (Exception e) {
            return false;
        }
    }

}
