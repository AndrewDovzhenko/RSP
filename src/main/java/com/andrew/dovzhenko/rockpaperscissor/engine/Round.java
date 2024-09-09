package com.andrew.dovzhenko.rockpaperscissor.engine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public final class Round {
    private final GameItem userChoice;
    private final GameItem programChoice;

    public RoundResult getResult(){
        if (userChoice == programChoice)
            return RoundResult.DRAW;

        if ((userChoice == GameItem.SCISSORS && programChoice == GameItem.PAPER)
                || (userChoice == GameItem.PAPER && programChoice == GameItem.ROCK)
                || (userChoice == GameItem.ROCK && programChoice == GameItem.SCISSORS))
            return RoundResult.USER_WIN;

        return RoundResult.USER_LOSE;
    }

}
