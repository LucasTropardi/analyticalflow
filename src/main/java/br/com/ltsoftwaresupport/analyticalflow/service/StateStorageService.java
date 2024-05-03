package br.com.ltsoftwaresupport.analyticalflow.service;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Service
@Scope(WebApplicationContext.SCOPE_SESSION)
public class StateStorageService {
    private Long gameId;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}