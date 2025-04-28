package com.bpm.checkersweb.checkers.logic;

import com.bpm.checkersweb.checkers.figures.*;
import com.bpm.checkersweb.checkers.player.Player;
import lombok.Getter;
import lombok.Setter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Board {

    @Setter
    private String gameId;

    private static final int BOARD_SIZE = 8;
    private final List<BoardRow> rows = new ArrayList<>();
    private Player currentPlayer;
    private final Player playerOne;
    private final Player playerTwo;
    private int whiteFigures = 0;
    private int blackFigures = 0;
    private final List<Figure> capturedWhiteFigures = new ArrayList<>();
    private final List<Figure> capturedBlackFigures = new ArrayList<>();
    private Point forcedMovePosition = null;

    private final MoveValidator moveValidator;
    private final CaptureHandler captureHandler;

    public Board(Player playerOne, Player playerTwo) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            rows.add(new BoardRow());
        }
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        currentPlayer = playerOne;

        this.moveValidator = new MoveValidator(this);
        this.captureHandler = new CaptureHandler(this);
    }

    public Board init() {
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                initializeBoardPosition(x, y);
            }
        }
        return this;
    }

    private void initializeBoardPosition(int x, int y) {
        Point position = new Point(x, y);
        if (shouldPlacePawn(x, y, FigureColor.BLACK)) {
            setFigure(position, new Pawn(FigureColor.BLACK));
            blackFigures++;
        } else if (shouldPlacePawn(x, y, FigureColor.WHITE)) {
            setFigure(position, new Pawn(FigureColor.WHITE));
            whiteFigures++;
        } else {
            setFigure(position, new None());
        }
    }

    private boolean shouldPlacePawn(int x, int y, FigureColor color) {
        boolean isDarkSquare = (x + y) % 2 == 1;

        if (color == FigureColor.BLACK) {
            return y < 3 && isDarkSquare;
        } else if (color == FigureColor.WHITE) {
            return y > 4 && isDarkSquare;
        }
        return false;
    }

    public Figure getFigure(Point point) {
        if (point == null) return null;
        return rows.get(point.y).getCols().get(point.x);
    }

    public void setFigure(Point point, Figure figure) {
        if (figure == null) return;
        rows.get(point.y).getCols().set(point.x, figure);
    }

    public void addCapturedWhiteFigure(Figure figure) {
        capturedWhiteFigures.add(figure);
        whiteFigures--;
    }

    public void addCapturedBlackFigure(Figure figure) {
        capturedBlackFigures.add(figure);
        blackFigures--;
    }

    public boolean moveFigure(Move move) {
        if (move == null) return false;

        List<Move> captureMoves = captureHandler.getAllCapturesForPlayer(currentPlayer.getFigureColor());
        if (!captureMoves.isEmpty() && !captureMoves.contains(move)) {
            return false;
        }

        if (forcedMovePosition != null && !forcedMovePosition.equals(move.getFromPoint())) {
            return false;
        }

        if (!moveValidator.isLegal(move, currentPlayer)) {
            return false;
        }

        Figure movingFigure = getFigure(move.getFromPoint());
        performBasicMove(move, movingFigure);

        boolean capturePerformed = captureHandler.isCaptureHandled(move);

        if (capturePerformed && captureHandler.canContinueCaptures(move.getToPoint(), movingFigure.getColor())) {
            forcedMovePosition = move.getToPoint();
        } else {
            switchToNextTurn();
            forcedMovePosition = null;
        }

        setPawnToQueen(move.getToPoint(), movingFigure.getColor());

        return true;
    }


    private void performBasicMove(Move move, Figure figure) {
        if (move == null || figure == null) return;
        setFigure(move.getToPoint(), figure);
        setFigure(move.getFromPoint(), new None());
    }

    public void switchToNextTurn() {
        currentPlayer = (currentPlayer == playerOne) ? playerTwo : playerOne;
    }

    public FigureColor checkWinner() {
        if (whiteFigures == 0) return FigureColor.BLACK;
        if (blackFigures == 0) return FigureColor.WHITE;

        List<Move> currentPlayerMoves = moveValidator.getAllLegalMoves(currentPlayer.getFigureColor());
        if (currentPlayerMoves.isEmpty()) {
            return currentPlayer == playerOne ? playerTwo.getFigureColor() : playerOne.getFigureColor();
        }

        return null;
    }

    public boolean setPawnToQueen(Point point, FigureColor color) {
        Figure figure = getFigure(point);
        if (point == null || !(figure instanceof Pawn)) return false;
        if (figure.getColor() != color) return false;

        boolean reachedEndRow = (color == FigureColor.BLACK && point.y == BOARD_SIZE - 1) ||
                (color == FigureColor.WHITE && point.y == 0);

        if (reachedEndRow) {
            setFigure(point, new Queen(color));
            return true;
        }
        return false;
    }

    public int evaluateScore(FigureColor color) {
        int score = 0;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Figure figure = getFigure(new Point(col, row));
                if (figure instanceof None) continue;

                int value = (figure instanceof Queen) ? 3 : 1;
                score += (figure.getColor() == color) ? value : -value;
            }
        }

        return score;
    }

    public Board deepCopy() {
        Board copy = new Board(playerOne, playerTwo);

        for (int y = 0; y < BOARD_SIZE; y++) {
            BoardRow originalRow = this.rows.get(y);
            BoardRow copiedRow = new BoardRow();

            for (int x = 0; x < BOARD_SIZE; x++) {
                Figure originalFigure = originalRow.getCols().get(x);
                copiedRow.getCols().set(x, originalFigure.clone());
            }

            copy.rows.set(y, copiedRow);
        }

        copy.currentPlayer = this.currentPlayer;
        copy.whiteFigures = this.whiteFigures;
        copy.blackFigures = this.blackFigures;
        copy.capturedWhiteFigures.addAll(cloneFigures(this.capturedWhiteFigures));
        copy.capturedBlackFigures.addAll(cloneFigures(this.capturedBlackFigures));
        if (this.forcedMovePosition != null) {
            copy.forcedMovePosition = new Point(this.forcedMovePosition.x, this.forcedMovePosition.y);
        }

        return copy;
    }

    private List<Figure> cloneFigures(List<Figure> original) {
        if (original == null) return null;
        List<Figure> cloned = new ArrayList<>();
        for (Figure f : original) {
            cloned.add(f.clone());
        }
        return cloned;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(capturedWhiteFigures).append("\n");
        sb.append("   A  B  C  D  E  F  G  H  \n");
        sb.append("  |==|==|==|==|==|==|==|==|\n");
        for (int row = 0; row < BOARD_SIZE; row++) {
            sb.append(rows.get(row).toString(row + 1));
        }
        sb.append("  |==|==|==|==|==|==|==|==|\n");
        sb.append("   A  B  C  D  E  F  G  H  \n");
        sb.append(capturedBlackFigures);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return whiteFigures == board.whiteFigures
                && blackFigures == board.blackFigures
                && Objects.equals(rows, board.rows)
                && Objects.equals(currentPlayer, board.currentPlayer)
                && Objects.equals(playerOne, board.playerOne)
                && Objects.equals(playerTwo, board.playerTwo)
                && Objects.equals(capturedWhiteFigures, board.capturedWhiteFigures)
                && Objects.equals(capturedBlackFigures, board.capturedBlackFigures)
                && Objects.equals(forcedMovePosition, board.forcedMovePosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, currentPlayer, playerOne, playerTwo,
                whiteFigures, blackFigures, capturedWhiteFigures, capturedBlackFigures, forcedMovePosition);
    }


}