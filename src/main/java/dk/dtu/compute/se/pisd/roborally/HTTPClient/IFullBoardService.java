package dk.dtu.compute.se.pisd.roborally.HTTPClient;

public interface IFullBoardService {
    FullBoard getFullBoardById(int id);

    boolean addFullBoard(FullBoard f);

    boolean updateFullBoard(int id, FullBoard f);

    boolean deleteProductById(int id);
}
