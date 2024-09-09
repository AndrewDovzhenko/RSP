package com.andrew.dovzhenko.rockpaperscissor.engine.impl;

import com.andrew.dovzhenko.rockpaperscissor.engine.GameItem;
import com.andrew.dovzhenko.rockpaperscissor.engine.Predicator;
import com.andrew.dovzhenko.rockpaperscissor.engine.Round;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class RandomPredicator implements Predicator {

    private Map<Integer, GameItem> itemMap;

    @PostConstruct
    private void init(){
        itemMap = Map.of(0, GameItem.ROCK, 1, GameItem.SCISSORS, 2, GameItem.PAPER);
    }

    @Override
    public GameItem predicateNext(List<Round> results) {
        return itemMap.get(new Random().nextInt(3));
    }
}
