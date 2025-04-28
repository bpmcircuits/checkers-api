package com.bpm.checkersweb.checkers.figures;

import java.util.Objects;

public class Pawn implements Figure {

    private final FigureColor color;

    public Pawn(FigureColor color) {
        this.color = color;
    }

    @Override
    public FigureColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return ((color == FigureColor.BLACK) ? "b" : "w") + "P";
    }

    @Override
    public Figure clone() {
        return new Pawn(this.color);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pawn pawn = (Pawn) o;
        return color == pawn.color;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(color);
    }
}
