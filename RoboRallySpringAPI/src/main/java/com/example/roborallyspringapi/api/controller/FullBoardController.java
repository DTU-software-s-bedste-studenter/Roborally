package com.example.roborallyspringapi.api.controller;

import com.example.roborallyspringapi.api.model.FullBoard;
import com.example.roborallyspringapi.service.FullBoardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class FullBoardController {

    private FullBoardService fullBoardService;

    public FullBoardController(FullBoardService fullBoardService){
        this.fullBoardService = fullBoardService;
    }
    @GetMapping("/fullboard")
    public FullBoard getFullBoard(@RequestParam Integer id){
        Optional fullboard = fullBoardService.getFullboard(id);
        if(fullboard.isPresent()){
            return (FullBoard) fullboard.get();
        }
        return null;
    }
}
