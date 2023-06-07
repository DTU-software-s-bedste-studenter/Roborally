package dk.dtu.compute.se.pisd.roborally.HTTPClient;

import dk.dtu.compute.se.pisd.roborally.fileaccess.model.FullBoardTemplate;

public interface IFullBoardService {
    FullBoardTemplate getFullBoardById(int id);

    boolean addFullBoard(FullBoardTemplate f);

    boolean updateFullBoard(int id, FullBoardTemplate f);

    boolean deleteProductById(int id);
}
