package dk.dtu.compute.se.pisd.roborally.HTTPClient;

import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.FullBoardTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class FullBoardClient implements IFullBoardService {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static String getFullBoards() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://34.88.181.0:8080/fullBoards"))
                .setHeader("User-Agent", "FullBoard Client")
                .header("Content-Type", "application/json")
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
        return result;
    }
    @Override
    public FullBoardTemplate getFullBoardById(int id) {
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://34.88.181.0:8080/fullBoards/" + id))
                    .setHeader("User-Agent", "FullBoard Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            Gson gson = new Gson();
            FullBoardTemplate f = gson.fromJson(result, FullBoardTemplate.class);
            return f;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean addFullBoard(FullBoardTemplate f) {
        try{
            String fullBoardJSON = new Gson().toJson(f);
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(fullBoardJSON))
                    .uri(URI.create("http://34.88.181.0:8080/fullBoards/"))
                    .setHeader("User-Agent", "FullBoard Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            return result.equals("added")? true : false;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public boolean updateFullBoard(int id, FullBoardTemplate f) {
        try{
            String fullBoardJSON = new Gson().toJson(f);
            HttpRequest request = HttpRequest.newBuilder()
                    .PUT(HttpRequest.BodyPublishers.ofString(fullBoardJSON))
                    .uri(URI.create("http://34.88.181.0:8080/fullBoards/" + id))
                    .setHeader("User-Agent", "FullBoard Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            return result.equals("updated")? true : false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteProductById(int id) {
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(URI.create("http://34.88.181.0:8080/fullBoards/" + id))
                    .setHeader("User-Agent", "FullBoard Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            return result.equals("deleted")? true : false;
        } catch (Exception e) {
            return false;
        }
    }


}
