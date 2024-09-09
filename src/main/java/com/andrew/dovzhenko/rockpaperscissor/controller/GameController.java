package com.andrew.dovzhenko.rockpaperscissor.controller;

import com.andrew.dovzhenko.rockpaperscissor.engine.Game;
import com.andrew.dovzhenko.rockpaperscissor.engine.GameItem;
import com.andrew.dovzhenko.rockpaperscissor.engine.GameStatus;
import com.andrew.dovzhenko.rockpaperscissor.service.GameService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;


    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @GetMapping
    @RequestMapping("/start")
    public ResponseEntity<Game> startGame(HttpSession session){
        if (session.getAttribute(SessionAttributes.GAME_ATTRIBUTE)==null
                || ((Game)session.getAttribute(SessionAttributes.GAME_ATTRIBUTE)).gameStatus() == GameStatus.TERMINATE)
            session.setAttribute(
                    SessionAttributes.GAME_ATTRIBUTE,
                    gameService.startNewGame());

        return new ResponseEntity<>((Game)session.getAttribute(SessionAttributes.GAME_ATTRIBUTE), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("/move")
    public ResponseEntity<?> makeMove(HttpSession session, @RequestParam("choice") GameItem choice){
        try {
            return new ResponseEntity<>(
                    gameService.makeMove((Game) session.getAttribute(SessionAttributes.GAME_ATTRIBUTE), choice),
                    HttpStatus.OK);
        } catch (IllegalStateException e){
            return new ResponseEntity<>("Game not initialized or terminated",HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    @RequestMapping("/terminate")
    public ResponseEntity<?> terminateGame(HttpSession session){
        try {
            return new ResponseEntity<>(
                    gameService.terminate((Game) session.getAttribute(SessionAttributes.GAME_ATTRIBUTE)),
                    HttpStatus.OK);
        } catch (IllegalStateException e){
            return new ResponseEntity<>("Game not initialized",HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    @RequestMapping("/statistic")
    public ResponseEntity<Game> statistic(HttpSession session){
        return new ResponseEntity<>((Game) session.getAttribute(SessionAttributes.GAME_ATTRIBUTE), HttpStatus.OK);
    }
}
