package com.andrew.dovzhenko.rockpaperscissor;

import com.andrew.dovzhenko.rockpaperscissor.engine.Game;
import com.andrew.dovzhenko.rockpaperscissor.engine.Predicator;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class TestConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Game game(Predicator predicator) {
        return new Game(predicator);
    }
}
