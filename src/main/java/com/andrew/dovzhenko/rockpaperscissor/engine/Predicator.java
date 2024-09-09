package com.andrew.dovzhenko.rockpaperscissor.engine;

import java.util.List;

public interface Predicator {
    GameItem predicateNext(List<Round> results);
}
