package com.andrew.dovzhenko.rockpaperscissor;

import com.andrew.dovzhenko.rockpaperscissor.controller.GameController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RockPaperScissorApplicationTests {

    @Autowired
    private GameController gameController;

    @Test
    void contextLoads() {
        Assertions.assertThat(gameController).isNotNull();
    }

}
