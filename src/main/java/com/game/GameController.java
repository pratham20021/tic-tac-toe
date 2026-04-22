package com.game;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameState game = new GameState();

    @GetMapping
    public GameState getState() {
        return game;
    }

    @PostMapping("/move")
    public Map<String, Object> makeMove(@RequestBody Map<String, Integer> body) {
        int index = body.get("index");
        boolean valid = game.makeMove(index);
        return Map.of("success", valid, "state", game);
    }

    @PostMapping("/reset")
    public GameState reset() {
        game.reset();
        return game;
    }
}
