package com.example.roborallyspringapi.api.controller;

import com.example.roborallyspringapi.api.model.Lobby;
import com.example.roborallyspringapi.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ClientInfoStatus;
import java.util.List;
@RestController
public class LobbyController{

    private Integer newestID = 1;

    @Autowired
    private LobbyService lobbyService;


    @GetMapping(value = "/lobbys")
    public ResponseEntity<List<Lobby>> getLobby()
    {
        List<Lobby> lobbys = lobbyService.findAll();
        return ResponseEntity.ok().body(lobbys);
    }

    @GetMapping(value = "/lobbyID")
    public ResponseEntity<Integer> getNextID()
    {
        return ResponseEntity.ok().body(newestID++);
    }

    @PostMapping("/lobbys")
    public ResponseEntity<String> addLobby(@RequestBody Lobby l) {
        boolean added = lobbyService.addLobby(l);
        if(added)
            return ResponseEntity.ok().body("added");
        else
            return ResponseEntity.internalServerError().body("not added");

    }

    @GetMapping("/lobbys/{id}")
    public ResponseEntity<Lobby> getLobbyById(@PathVariable int id) {
        Lobby l = lobbyService.getLobbyById(id);
        return ResponseEntity.ok().body(l);
    }

    @PutMapping("/lobbys/{id}")
    public ResponseEntity<String> updateLobby(@PathVariable int id, @RequestBody Lobby l) {
        boolean added = lobbyService.updateLobby(id, l);
        return ResponseEntity.ok().body("updated");
    }

    @DeleteMapping("/lobbys/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        boolean deleted = lobbyService.deleteLobbyById(id);
        return ResponseEntity.ok().body("deleted");
    }

    @GetMapping("/lobbys/{id}/JSON")
    public ResponseEntity<String> getJSONById(@PathVariable int id) {
        String s = lobbyService.getJSONById(id);
        return ResponseEntity.ok().body(s);
    }

    @PutMapping("/lobbys/{id}/JSON")
    public ResponseEntity<String> updateJSON(@PathVariable int id, @RequestBody String s) {
        boolean added = lobbyService.updateJSON(id, s);
        return ResponseEntity.ok().body("updated");
    }

}
