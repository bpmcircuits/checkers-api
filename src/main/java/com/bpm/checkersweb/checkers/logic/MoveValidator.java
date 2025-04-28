package com.bpm.checkersweb.checkers.logic;

import com.bpm.checkersweb.checkers.figures.Figure;
import com.bpm.checkersweb.checkers.figures.FigureColor;
import com.bpm.checkersweb.checkers.figures.None;
import com.bpm.checkersweb.checkers.figures.Queen;
import com.bpm.checkersweb.checkers.player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MoveValidator {

    private static final int DIR_UP = -1;
    private static final int DIR_DOWN = 1;
    private static final int[][] DIAGONAL_DIRECTIONS = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    private static final int BOARD_SIZE = 8;
    private final Board board;

    public MoveValidator(Board board) {
        this.board = board;
    }

    public List<Move> getAllLegalMoves(FigureColor color) {
        CaptureHandler captureHandler = new CaptureHandler(board);
        List<Move> captureMoves = captureHandler.getAllCapturesForPlayer(color);

        if (!captureMoves.isEmpty()) {
            return captureMoves;
        }

        List<Move> regularMoves = new ArrayList<>();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Point from = new Point(col, row);
                Figure figure = board.getFigure(from);

                if (figure instanceof None || figure.getColor() != color) {
                    continue;
                }

                if (figure instanceof Queen) {
                    addQueenRegularMoves(regularMoves, from);
                } else {
                    addPawnRegularMoves(regularMoves, from);
                }
            }
        }

        return regularMoves;
    }

    private void addPawnRegularMoves(List<Move> moves, Point from) {
        int direction = (board.getFigure(from).getColor() == FigureColor.WHITE) ? DIR_UP : DIR_DOWN;

        for (int dx : new int[]{-1, 1}) {
            Point to = new Point(from.x + dx, from.y + direction);
            if (isInBounds(to) && isPlaceEmpty(to)) {
                moves.add(new Move(from, to));
            }
        }
    }

    private void addQueenRegularMoves(List<Move> moves, Point position) {
        for (int[] dir : DIAGONAL_DIRECTIONS) {
            int dirX = dir[0];
            int dirY = dir[1];

            Point current = new Point(position.x + dirX, position.y + dirY);

            while (isInBounds(current)) {
                if (isPlaceEmpty(current)) {
                    moves.add(new Move(position, new Point(current.x, current.y)));
                    current.x += dirX;
                    current.y += dirY;
                } else {
                    break;
                }
            }
        }
    }

    public boolean isLegal(Move move, Player player) {
        if (move == null) return false;

        CaptureHandler captureHandler = new CaptureHandler(board);

        Point from = move.getFromPoint();
        Point to = move.getToPoint();

        if (!isInBounds(from) || !isInBounds(to)) return false;
        if (isPlaceEmpty(from) || !isPlaceEmpty(to)) return false;

        Figure fig = board.getFigure(from);

        if (fig.getColor() != player.getFigureColor()) return false;
        if (!isMoveDiagonal(move)) return false;

        if (fig instanceof Queen) {
            return isQueenPathClear(move) || captureHandler.canQueenCapture(move);
        } else {
            return isLegalPawnMove(move);
        }
    }

    boolean isInBounds(Point point) {
        return point != null &&
                point.x >= 0 &&
                point.x < BOARD_SIZE &&
                point.y >= 0 &&
                point.y < BOARD_SIZE;
    }

    private boolean isPlaceEmpty(Point point) {
        return board.getFigure(point) instanceof None;
    }

    private boolean isOpponentFigure(Point point) {
        Figure figure = board.getFigure(point);
        return !(figure instanceof None) && figure.getColor() != board.getCurrentPlayer().getFigureColor();
    }

    private boolean isMoveDiagonal(Move move) {
        if (move == null) return false;
        Point from = move.getFromPoint();
        Point to = move.getToPoint();
        return Math.abs(to.x - from.x) == Math.abs(to.y - from.y);
    }

    private boolean isDirectionCorrect(Move move) {
        if (move == null) return false;

        Point from = move.getFromPoint();
        Point to = move.getToPoint();

        int direction = Integer.compare(to.y - from.y, 0);

        if (board.getCurrentPlayer().getFigureColor() == FigureColor.WHITE) {
            return direction == DIR_UP;
        } else if (board.getCurrentPlayer().getFigureColor() == FigureColor.BLACK) {
            return direction == DIR_DOWN;
        }
        return false;
    }

    private boolean isQueenPathClear(Move move) {
        Point from = move.getFromPoint();
        Point to = move.getToPoint();

        int dirX = Integer.compare(to.x - from.x, 0);
        int dirY = Integer.compare(to.y - from.y, 0);

        Point current = new Point(from.x + dirX, from.y + dirY);
        while (!current.equals(to)) {
            if (!isPlaceEmpty(current)) {
                return false;
            }
            current.x += dirX;
            current.y += dirY;
        }

        return true;
    }

    private boolean isLegalPawnMove(Move move) {
        CaptureHandler captureHandler = new CaptureHandler(board);

        if (Math.abs(move.getToPoint().x - move.getFromPoint().x) == 1) {
            if (!isDirectionCorrect(move)) return false;
        }

        boolean isCapture = Math.abs(move.getToPoint().x - move.getFromPoint().x) == 2;

        if (isCapture) {
            Point capturedPoint = captureHandler.getCapturedPoint(move);
            return capturedPoint != null && isOpponentFigure(capturedPoint);
        } else {
            return Math.abs(move.getToPoint().x - move.getFromPoint().x) == 1;
        }
    }

}
