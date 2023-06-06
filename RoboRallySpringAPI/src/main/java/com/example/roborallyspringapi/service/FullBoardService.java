package com.example.roborallyspringapi.service;

import com.example.roborallyspringapi.api.model.FullBoard;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class FullBoardService {

    private List<FullBoard> fullBoardList;

    public FullBoardService(){
        fullBoardList = new ArrayList<>();

        FullBoard fullBoard1 = new FullBoard(1,13, 10, 3, "RiskyCrossing");
        FullBoard fullBoard2 = new FullBoard(2,23, 10, 6, "ChopShopChallenge");
        FullBoard fullBoard3 = new FullBoard(3,13, 10, 5, "DeathTrap");
        FullBoard fullBoard4 = new FullBoard(4,13, 10, 3, "Fractionation");
        FullBoard fullBoard5 = new FullBoard(5,13, 10, 3, "SprintCramp");

        fullBoardList.addAll(Arrays.asList(fullBoard1,fullBoard2,fullBoard3,fullBoard4,fullBoard5));
    }

    public Optional<FullBoard> getFullboard(Integer id) {
        Optional optional = Optional.empty();
        for(FullBoard fullBoard : fullBoardList) {
            if (fullBoard.getId() == id) {
                optional = optional.of(fullBoard);
            }
        }
            return optional;
    }
}
