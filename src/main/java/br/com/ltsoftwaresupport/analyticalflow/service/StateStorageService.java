package br.com.ltsoftwaresupport.analyticalflow.service;

import org.springframework.stereotype.Service;

@Service
public class StateStorageService {
    private Long gameId;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}