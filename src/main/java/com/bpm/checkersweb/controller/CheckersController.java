package com.bpm.checkersweb.controller;

import com.bpm.checkersweb.checkers.logic.Board;
import com.bpm.checkersweb.checkers.logic.Move;
import com.bpm.checkersweb.checkers.ui.UserInterface;
import com.bpm.checkersweb.dto.CheckersBoardDto;
import com.bpm.checkersweb.dto.MoveDto;
import com.bpm.checkersweb.mapper.CheckersMapper;
import com.bpm.checkersweb.service.CheckersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CheckersController {

    private final CheckersMapper mapper;
    private final CheckersService checkersService;

    @PostMapping(value = "/game")
    public ResponseEntity<CheckersBoardDto> createGame(@RequestBody CheckersBoardDto boardDto) {
        String playerOne = boardDto.playerOne();
        String playerTwo = boardDto.playerTwo();

        Board board = checkersService.createNewGame(playerOne, playerTwo);
        CheckersBoardDto newGame = mapper.mapToCheckersBoardDto(board);

        return ResponseEntity.ok(newGame);
    }

    @GetMapping(value = "/game/{gameId}")
    public ResponseEntity<CheckersBoardDto> getGame(@PathVariable String gameId) {
        try {
            Board board = checkersService.getGameById(gameId);
            return ResponseEntity.ok(mapper.mapToCheckersBoardDto(board));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/game/{gameId}/move")
    public ResponseEntity<CheckersBoardDto> makeMove(@PathVariable String gameId, @RequestBody MoveDto moveDto) {
        Move move = UserInterface.takeMove(moveDto.move());
        boolean moveCompleted = checkersService.makeMove(gameId, move);
        if (!moveCompleted) {
            return ResponseEntity.badRequest().build();
        }
        Board board = checkersService.getGameById(gameId);
        CheckersBoardDto newGame = mapper.mapToCheckersBoardDto(board);
        return ResponseEntity.ok(newGame);
    }
}
