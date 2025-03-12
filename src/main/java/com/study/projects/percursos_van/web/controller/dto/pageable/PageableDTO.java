package com.study.projects.percursos_van.web.controller.dto.pageable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PageableDTO(
        List content,
        boolean first,
        boolean last,
        int totalPages,
        long totalElements,
        int size,
        @JsonProperty("page")
        int number,
        @JsonProperty("pageElements")
        int numberOfElements
) {
}
