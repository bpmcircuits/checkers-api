package com.bpm.checkersweb.checkers.logic;

import com.bpm.checkersweb.checkers.figures.Figure;
import com.bpm.checkersweb.checkers.figures.FigureColor;
import com.bpm.checkersweb.checkers.figures.None;
import com.bpm.checkersweb.checkers.figures.Queen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CaptureHandler {

    private final Board board;

    private static final int BOARD_SIZE = 8;
    private static final int[][] DIAGONAL_DIRECTIONS = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    public CaptureHandler(Board board) {
        this.board = board;
    }

    public List<Move> getAllCapturesForPlayer(FigureColor color) {
        List<Move> allCaptures = new ArrayList<>();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Point position = new Point(col, row);
                Figure figure = board.getFigure(position);

                if (figure != null && !(figure instanceof None) && figure.getColor() == color) {
                    allCaptures.addAll(getAllLegalCaptures(position, color));
                }
            }
        }

        return allCaptures;
    }

    private List<Move> getAllLegalCaptures(Point position, FigureColor color) {
        List<Move> possibleCaptures = new ArrayList<>();
        Figure figure = board.getFigure(position);

        if (figure == null || figure.getColor() != color) {
            return possibleCaptures;
        }

        if (figure instanceof Queen) {
            addQueenCaptures(possibleCaptures, position);
        } else {
            addPawnCaptures(possibleCaptures, position, color);
        }

        return possibleCaptures;
    }

    private void addPawnCaptures(List<Move> captures, Point position, FigureColor color) {
        MoveValidator validator = new MoveValidator(board);
        for (int[] dir : DIAGONAL_DIRECTIONS) {
            Point adjacentPoint = new Point(position.x + dir[0], position.y + dir[1]);
            Point landingPoint = new Point(position.x + 2*dir[0], position.y + 2*dir[1]);

            if (validator.isInBounds(adjacentPoint) && validator.isInBounds(landingPoint)) {
                Figure adjacentFigure = board.getFigure(adjacentPoint);

                if (!(adjacentFigure instanceof None) && adjacentFigure.getColor() != color &&
                        isPlaceEmpty(landingPoint)) {
                    captures.add(new Move(position, landingPoint));
                }
            }
        }
    }

    private void addQueenCaptures(List<Move> captures, Point position) {
        MoveValidator validator = new MoveValidator(board);
        for (int[] dir : DIAGONAL_DIRECTIONS) {
            int dirX = dir[0];
            int dirY = dir[1];

            boolean foundOpponent = false;
            Point current = new Point(position.x + dirX, position.y + dirY);

            while (validator.isInBounds(current)) {
                Figure currentFigure = board.getFigure(current);

                if (currentFigure instanceof None) {
                    if (foundOpponent) {
                        captures.add(new Move(position, new Point(current.x, current.y)));
                    }
                    current.x += dirX;
                    current.y += dirY;
                } else if (currentFigure.getColor() != board.getFigure(position).getColor()) {
                    if (foundOpponent) {
                        break;
                    }
                    foundOpponent = true;
                    current.x += dirX;
                    current.y += dirY;
                } else {
                    break;
                }
            }
        }
    }

    public boolean performCapture(Move move) {
        Figure fig = board.getFigure(move.getFromPoint());
        if (fig instanceof Queen) {
            return handleQueenCapture(move);
        } else {
            return handlePawnCapture(move);
        }
    }

    public boolean handlePawnCapture(Move move) {
        Point capturedPoint = getCapturedPoint(move);
        if (capturedPoint == null) {
            return false;
        }

        if (!isOpponentFigure(capturedPoint)) {
            return false;
        }

        captureFigure(capturedPoint);
        return true;
    }

    public boolean handleQueenCapture(Move move) {
        Point from = move.getFromPoint();
        Point to = move.getToPoint();

        int dirX = Integer.compare(to.x - from.x, 0);
        int dirY = Integer.compare(to.y - from.y, 0);

        Point opponentPosition = null;
        int foundOpponents = 0;

        Point current = new Point(from.x + dirX, from.y + dirY);
        while (!current.equals(to)) {
            if (!isPlaceEmpty(current)) {
                Figure figure = board.getFigure(current);
                if (figure.getColor() == board.getCurrentPlayer().getFigureColor()) {
                    return false;
                } else {
                    foundOpponents++;
                    opponentPosition = new Point(current.x, current.y);
                }
            }
            current.x += dirX;
            current.y += dirY;
        }

        if (foundOpponents == 1) {
            captureFigure(opponentPosition);
            return true;
        }

        return false;
    }

    public void captureFigure(Point capturedPoint) {
        Figure capturedFigure = board.getFigure(capturedPoint);

        if (capturedFigure.getColor() == FigureColor.WHITE) {
            board.addCapturedWhiteFigure(capturedFigure);
        } else {
            board.addCapturedBlackFigure(capturedFigure);
        }

        board.setFigure(capturedPoint, new None());
    }

    public Point getCapturedPoint(Move move) {
        if (move == null) return null;
        int x = (move.getFromPoint().x + move.getToPoint().x) / 2;
        int y = (move.getFromPoint().y + move.getToPoint().y) / 2;
        Point capturedPoint = new Point(x, y);
        MoveValidator validator = new MoveValidator(board);
        if (validator.isInBounds(capturedPoint)) {
            return capturedPoint;
        }
        return null;
    }

    public boolean canQueenCapture(Move move) {
        Point from = move.getFromPoint();
        Point to = move.getToPoint();

        int dirX = Integer.compare(to.x - from.x, 0);
        int dirY = Integer.compare(to.y - from.y, 0);

        int opponentCount = 0;

        Point current = new Point(from.x + dirX, from.y + dirY);
        while (!current.equals(to)) {
            if (!isPlaceEmpty(current)) {
                Figure figure = board.getFigure(current);
                if (figure.getColor() == board.getCurrentPlayer().getFigureColor()) {
                    return false;
                } else {
                    opponentCount++;
                    Point next = new Point(current.x + dirX, current.y + dirY);
                    MoveValidator validator = new MoveValidator(board);
                    if (!validator.isInBounds(next) || (!next.equals(to) && !isPlaceEmpty(next))) {
                        return false;
                    }
                }
            }
            current.x += dirX;
            current.y += dirY;
        }

        return opponentCount == 1;
    }

    private boolean isPlaceEmpty(Point point) {
        return board.getFigure(point) instanceof None;
    }

    private boolean isOpponentFigure(Point point) {
        Figure figure = board.getFigure(point);
        return !(figure instanceof None) && figure.getColor() != board.getCurrentPlayer().getFigureColor();
    }

    public boolean isCaptureHandled(Move move) {
        Figure movingFigure = board.getFigure(move.getFromPoint());
        if (movingFigure instanceof Queen) {
            return handleQueenCapture(move);
        } else {
            return handlePawnCapture(move);
        }
    }

    public boolean canContinueCaptures(Point currentPosition, FigureColor color) {
        return !getAllLegalCaptures(currentPosition, color).isEmpty();
    }
}
