package com.andrew.dovzhenko.rockpaperscissor;

import com.andrew.dovzhenko.rockpaperscissor.controller.GameController;
import com.andrew.dovzhenko.rockpaperscissor.controller.SessionAttributes;
import com.andrew.dovzhenko.rockpaperscissor.engine.Game;
import com.andrew.dovzhenko.rockpaperscissor.engine.GameItem;
import com.andrew.dovzhenko.rockpaperscissor.engine.GameStatus;
import com.andrew.dovzhenko.rockpaperscissor.engine.Predicator;
import com.andrew.dovzhenko.rockpaperscissor.service.GameService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfiguration.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Game game;
    @MockBean
    private GameService gameService;
    @MockBean
    private Predicator predicator;
    @Mock
    private HttpSession session;

    @BeforeEach
    private void setUp(){
        when(predicator.predicateNext(anyList())).thenReturn(GameItem.SCISSORS);
    }

    @Test
    void startGame_shouldStartNewGameWhenNotInitialized() throws Exception {
        when(gameService.startNewGame()).thenReturn(game);

        mockMvc.perform(get("/game/start"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameStatus").value(GameStatus.ACTIVE.toString()));

    }

    @Test
    void makeMove_shouldReturnRoundWhenMoveIsMade() throws Exception {
        doCallRealMethod().when(gameService).makeMove(any(),any());


        mockMvc.perform(get("/game/move")
                        .param("choice", "ROCK")
                        .sessionAttr(SessionAttributes.GAME_ATTRIBUTE, game))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result")
                        .value(game.getRoundsHistory().get(game.getRoundsHistory().size()-1).getResult().toString()));
    }

    @Test
    void makeMove_shouldReturnBadRequestWhenGameNotInitialized() throws Exception {
        doCallRealMethod().when(gameService).makeMove(any(),any());

        mockMvc.perform(get("/game/move")
                        .param("choice", "ROCK"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Game not initialized or terminated"));
    }

    @Test
    void makeMove_shouldReturnBadRequestWhenGameIsTerminated() throws Exception {
        doCallRealMethod().when(gameService).makeMove(any(),any());
        game.setGameStatus(GameStatus.TERMINATE);
        mockMvc.perform(get("/game/move")
                        .param("choice", "ROCK")
                        .sessionAttr(SessionAttributes.GAME_ATTRIBUTE, game))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Game not initialized or terminated"));
    }

    @Test
    void terminateGame_shouldReturnOkStatusWhenGameIsTerminated() throws Exception {
        doCallRealMethod().when(gameService).terminate(any());

        mockMvc.perform(get("/game/terminate")
                        .sessionAttr(SessionAttributes.GAME_ATTRIBUTE, game))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameStatus").value(game.getGameStatus().toString()));
    }

    @Test
    void terminateGame_shouldReturnBadRequestWhenGameNotInitialized() throws Exception {
        doCallRealMethod().when(gameService).terminate(any());

        mockMvc.perform(get("/game/terminate"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Game not initialized"));
    }

    @Test
    void statistic_shouldReturnGameStatistic() throws Exception {
        doCallRealMethod().when(gameService).makeMove(any(),any());
        gameService.makeMove(game, GameItem.ROCK);
        gameService.makeMove(game, GameItem.SCISSORS);
        gameService.makeMove(game, GameItem.PAPER);

        mockMvc.perform(get("/game/statistic")
                        .sessionAttr(SessionAttributes.GAME_ATTRIBUTE, game))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countOfRounds").value(3))
                .andExpect(jsonPath("$.wins").value(1))
                .andExpect(jsonPath("$.draw").value(1))
                .andExpect(jsonPath("$.lose").value(1));
    }
}