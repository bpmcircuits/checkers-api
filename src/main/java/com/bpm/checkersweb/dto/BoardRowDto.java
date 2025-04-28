package com.bpm.checkersweb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoardRowDto {
    private List<FigureDto> cols;
}
