package com.andrew.dovzhenko.rockpaperscissor.service;

import com.andrew.dovzhenko.rockpaperscissor.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService {

    @Autowired
    private ApplicationContext applicationContext;

    public Game startNewGame(){
        return applicationContext.getBean(Game.class);
    }

    public Round makeMove(Game currentGame, GameItem choice) throws IllegalStateException {
        return Optional.ofNullable(currentGame)
                .filter(game -> !GameStatus.TERMINATE.equals(game.gameStatus()))
                .map(game -> game.move(choice))
                .orElseThrow(() -> new IllegalStateException("Game not initialized or terminated"));
    }

    public Game terminate(Game currentGame){
        Optional.ofNullable(currentGame)
                .ifPresentOrElse(
                        game -> game.setGameStatus(GameStatus.TERMINATE),
                        () -> { throw new IllegalStateException("Game not initialized"); }
                );
        return currentGame;
    }
}
