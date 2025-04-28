package com.bpm.checkersweb.checkers.player;

import com.bpm.checkersweb.checkers.figures.FigureColor;
import com.bpm.checkersweb.checkers.logic.Board;
import com.bpm.checkersweb.checkers.logic.Move;
import com.bpm.checkersweb.checkers.ui.UserInterface;

import java.util.Objects;

public final class HumanPlayer implements Player {

    private final String name;
    private final FigureColor color;

    public HumanPlayer(String name, FigureColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public FigureColor getFigureColor() {
        return color;
    }

    public Move getMove(Board board) {
        return UserInterface.getPlayerMove();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (HumanPlayer) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }

    @Override
    public String toString() {
        return "HumanPlayer[" +
                "username=" + name + ", " +
                "playerColor=" + color + ']';
    }

}
