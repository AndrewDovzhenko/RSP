package com.andrew.dovzhenko.rockpaperscissor.engine;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.andrew.dovzhenko.rockpaperscissor.engine.RoundResult.*;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Game {
    private GameStatus gameStatus;
    private final Predicator predicator;
    private List<Round> roundsHistory;

    public Game(Predicator predicator) {
        this.predicator = predicator;
    }

    @PostConstruct
    private void init(){
        gameStatus = GameStatus.ACTIVE;
        roundsHistory = new ArrayList<>();
    }

    public GameStatus gameStatus(){
        return gameStatus;
    }

    public Round move(GameItem userChoice){
        var round = new Round(userChoice, predicator.predicateNext(roundsHistory));
        roundsHistory.add(round);
        return round;
    }


    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus){
        this.gameStatus = gameStatus;
    }

    public Integer getCountOfRounds(){
        if (roundsHistory==null)
            return 0;
        return roundsHistory.size();
    }

    public Long getWins(){
        if (roundsHistory==null)
            return 0L;
        return roundsHistory.stream().filter(round -> USER_WIN.equals(round.getResult())).count();
    }

    public Long getLose(){
        if (roundsHistory==null)
            return 0L;
        return roundsHistory.stream().filter(round -> USER_LOSE.equals(round.getResult())).count();
    }

    public Long getDraw(){
        if (roundsHistory==null)
            return 0L;
        return roundsHistory.stream().filter(round -> DRAW.equals(round.getResult())).count();
    }
    public List<Round> getRoundsHistory() {
        return roundsHistory;
    }
}
