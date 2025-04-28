package com.bpm.checkersweb.checkers.figures;

import java.util.Objects;

public class Queen implements Figure {

    private final FigureColor color;

    public Queen(FigureColor color) {
        this.color = color;
    }

    @Override
    public FigureColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return ((color == FigureColor.BLACK) ? "b" : "w") + "Q";
    }

    @Override
    public Figure clone() {
        return new Queen(this.color);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Queen queen = (Queen) o;
        return color == queen.color;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(color);
    }
}
