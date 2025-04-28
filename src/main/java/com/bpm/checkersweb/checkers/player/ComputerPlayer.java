package com.bpm.checkersweb.checkers.player;

import com.bpm.checkersweb.checkers.figures.*;
import com.bpm.checkersweb.checkers.logic.Board;
import com.bpm.checkersweb.checkers.logic.Move;
import com.bpm.checkersweb.checkers.logic.MoveValidator;
import com.bpm.checkersweb.checkers.ui.MenuEnum;

import java.util.List;

public class ComputerPlayer implements Player {
    private static final String AI_NAME = "ENIAC";
    private final FigureColor color;
    private final MenuEnum.ComputerLevelEnum difficulty;

    public ComputerPlayer(FigureColor color, MenuEnum.ComputerLevelEnum difficulty) {
        this.color = color;
        this.difficulty = difficulty;
    }

    @Override
    public Move getMove(Board board) {
        if (board == null) return null;
        return findBestMove(board);
    }

    private Move findBestMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = null;
        MoveValidator moveValidator = new MoveValidator(board);

        for (Move move : moveValidator.getAllLegalMoves(color)) {
            Board boardCopy = board.deepCopy();
            boolean captureOccurred = boardCopy.moveFigure(move);

            int score = evaluateMoveScore(boardCopy, captureOccurred, difficulty.level - 1);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int evaluateMoveScore(Board board, boolean captureOccurred, int depth) {
        boolean isMaximizing = !(captureOccurred && board.getForcedMovePosition() != null);
        return minimax(board, depth, isMaximizing);
    }

    private int minimax(Board board, int depth, boolean isMaximizing) {
        if (depth == 0 || board.checkWinner() != null) {
            return board.evaluateScore(color);
        }

        MoveValidator moveValidator = new MoveValidator(board);
        FigureColor currentColor = isMaximizing ? color : getOpponentColor(color);
        List<Move> moves = moveValidator.getAllLegalMoves(currentColor);


        if (moves.isEmpty()) {
            return board.evaluateScore(color);
        }

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Move move : moves) {
            Board boardCopy = board.deepCopy();
            boolean captureOccurred = boardCopy.moveFigure(move);

            boolean continueWithSamePlayer = captureOccurred && boardCopy.getForcedMovePosition() != null;
            boolean nextIsMaximizing = continueWithSamePlayer == isMaximizing;

            int score = minimax(boardCopy, depth - 1, nextIsMaximizing);

            bestScore = isMaximizing
                    ? Math.max(bestScore, score)
                    : Math.min(bestScore, score);
        }

        return bestScore;
    }

    private FigureColor getOpponentColor(FigureColor color) {
        return color == FigureColor.WHITE ? FigureColor.BLACK : FigureColor.WHITE;
    }

    @Override
    public FigureColor getFigureColor() {
        return color;
    }

    @Override
    public String getName() {
        return AI_NAME;
    }

    @Override
    public String toString() {
        return "ComputerPlayer{" +
                "name='" + AI_NAME + '\'' +
                ", color=" + color +
                ", difficulty=" + difficulty +
                '}';
    }
}
