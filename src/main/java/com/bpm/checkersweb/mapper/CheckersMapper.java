package com.bpm.checkersweb.mapper;

import com.bpm.checkersweb.checkers.figures.Figure;
import com.bpm.checkersweb.checkers.figures.None;
import com.bpm.checkersweb.checkers.figures.Pawn;
import com.bpm.checkersweb.checkers.figures.Queen;
import com.bpm.checkersweb.checkers.logic.Board;
import com.bpm.checkersweb.checkers.logic.BoardRow;
import com.bpm.checkersweb.dto.BoardRowDto;
import com.bpm.checkersweb.dto.CheckersBoardDto;
import com.bpm.checkersweb.dto.FigureDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckersMapper {

    public CheckersBoardDto mapToCheckersBoardDto(final Board board) {

        String id = board.getGameId();
        String playerOne = board.getPlayerOne().getName();
        String playerTwo = board.getPlayerTwo().getName();

        List<BoardRowDto> rowsDto = board.getRows().stream()
                .map(this::mapToBoardRowDto)
                .toList();


        return new CheckersBoardDto(id, rowsDto, playerOne, playerTwo);
    }

    private BoardRowDto mapToBoardRowDto(BoardRow boardRow) {
        List<FigureDto> colsDto = boardRow.getCols().stream()
                .map(this::mapToFigureDto)
                .collect(Collectors.toList());

        return new BoardRowDto(colsDto);
    }

    private FigureDto mapToFigureDto(Figure figure) {
        String type = switch (figure) {
            case None ignored -> "NONE";
            case Pawn ignored -> "PAWN";
            case Queen ignored -> "QUEEN";
            case null, default -> "UNKNOWN";
        };

        String color;
        if ((figure instanceof None)) {
            color = null;
        } else {
            assert figure != null;
            color = figure.getColor().toString();
        }

        return new FigureDto(color, type);
    }
}
