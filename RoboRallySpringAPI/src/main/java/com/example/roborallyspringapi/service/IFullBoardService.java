package com.example.roborallyspringapi.service;

import com.example.roborallyspringapi.api.model.FullBoard;

import java.util.List;

public interface IFullBoardService {
    public boolean updateFullBoard(int id, FullBoard f);

    public boolean deleteFullBoardById(int id);

    public boolean addFullBoard(FullBoard f);

    public List<FullBoard> findAll();
    public FullBoard getFullBoardById(int id);
}
