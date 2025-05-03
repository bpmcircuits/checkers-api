package com.bpm.checkersweb.service;

import com.bpm.checkersweb.checkers.figures.FigureColor;
import com.bpm.checkersweb.checkers.logic.Board;
import com.bpm.checkersweb.checkers.logic.Move;
import com.bpm.checkersweb.checkers.player.HumanPlayer;
import com.bpm.checkersweb.checkers.player.Player;
import com.bpm.checkersweb.checkers.ui.UserInterface;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CheckersService {

    private final Map<String, Board> boards = new HashMap<>();

    public Board createNewGame(String playerOneName, String playerTwoName) {
        String gameId = generateGameId();
        Player playerOne = new HumanPlayer(playerOneName, FigureColor.WHITE);
        Player playerTwo = new HumanPlayer(playerTwoName, FigureColor.BLACK);
        Board board = new Board(playerOne, playerTwo).init();

        board.setGameId(gameId);

        boards.put(gameId, board);
        return board;
    }

    public Board getGameById(String gameId) {
        Board board = boards.get(gameId);
        checkForBoard(gameId, board);
        return board;
    }

    public boolean makeMove(String gameId, Move move) {
        Board board = boards.get(gameId);
        checkForBoard(gameId, board);
        return board.moveFigure(move);
    }

    private String generateGameId() {
        return UUID.randomUUID().toString();
    }

    private static void checkForBoard(String gameId, Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Game not found with ID: " + gameId);
        }
    }
}
