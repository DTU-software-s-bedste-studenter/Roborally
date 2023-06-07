package com.example.roborallyspringapi.service;

import com.example.roborallyspringapi.api.model.FullBoard;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FullBoardService implements IFullBoardService {

    ArrayList<FullBoard> fullBoardList = new ArrayList<>();

    public FullBoardService(){

        /*
        FullBoard fullBoard1 = new FullBoard(6,13, 10, 3, "RiskyCrossing");
        FullBoard fullBoard2 = new FullBoard(2,23, 10, 6, "ChopShopChallenge");
        FullBoard fullBoard3 = new FullBoard(3,13, 10, 5, "DeathTrap");
        FullBoard fullBoard4 = new FullBoard(4,13, 10, 3, "Fractionation");
        FullBoard fullBoard5 = new FullBoard(5,13, 10, 3, "SprintCramp");

        fullBoardList.addAll(Arrays.asList(fullBoard1,fullBoard2,fullBoard3,fullBoard4,fullBoard5));
        */
    }
    @Override
    public  List<FullBoard> findAll() {
        return fullBoardList;
    }

    @Override
    public boolean addFullBoard(FullBoard f) {
        fullBoardList.add(f);
        return true;
    }

    @Override
    public FullBoard getFullBoardById(int id) {
        for(FullBoard f : fullBoardList) {
            if(f.getId() == id) {
                return f;
            }
        }
        return null;
    }

    @Override
    public boolean updateFullBoard(int id, FullBoard f) {
        for(FullBoard fB : fullBoardList) {
            if(fB.getId() == id) {
                fB.setId(f.getId());
                fB.setWidth(f.getWidth());
                fB.setHeight(f.getHeight());
                fB.setCheckpoints(f.getCheckpoints());
                fB.setBoardName(f.getBoardName());
                fB.setSpaces(f.getSpaces());
                fB.setPlayers(f.getPlayers());
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean deleteFullBoardById(int id) {
        ArrayList<FullBoard> newFullBoards = new ArrayList<>();
        int oldSize = fullBoardList.size();
        fullBoardList.forEach((fullBoard -> {
            if(fullBoard.getId() == id)
                newFullBoards.add(
                        fullBoard
                );
        }));
        fullBoardList = newFullBoards;
        return oldSize < fullBoardList.size() ? true : false;
    }


}
