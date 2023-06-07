package com.example.roborallyspringapi.api.controller;

import com.example.roborallyspringapi.service.FullBoardService;
import com.example.roborallyspringapi.api.model.FullBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class FullBoardController {

    @Autowired
    private FullBoardService fullBoardService;


    @GetMapping(value = "/fullBoards")
    public ResponseEntity<List<FullBoard>> getProduct()
    {
        List<FullBoard> fullBoards = fullBoardService.findAll();
        return ResponseEntity.ok().body(fullBoards);
    }

    @PostMapping("/fullBoards")
    public ResponseEntity<String> addProduct(@RequestBody FullBoard f) {
        boolean added = fullBoardService.addFullBoard(f);
        if(added)
            return ResponseEntity.ok().body("added");
        else
            return ResponseEntity.internalServerError().body("not added");

    }

    @GetMapping("/fullBoards/{id}")
    public ResponseEntity<FullBoard> getProductById(@PathVariable int id) {
        FullBoard f = fullBoardService.getFullBoardById(id);
        return ResponseEntity.ok().body(f);
    }

    @PutMapping("/fullBoards/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestBody FullBoard f) {
        boolean added = fullBoardService.updateFullBoard(id, f);
        return ResponseEntity.ok().body("updated");
    }

    @DeleteMapping("/fullBoards/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        boolean deleted = fullBoardService.deleteFullBoardById(id);
        return ResponseEntity.ok().body("deleted");
    }
}
