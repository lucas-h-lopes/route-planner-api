package com.study.projects.percursos_van.web.mapper.page;

import com.study.projects.percursos_van.web.controller.dto.pageable.PageableDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableMapper {

    public static PageableDTO toPageableDTO(Page<?> userProjection){
        return new PageableDTO(
                userProjection.getContent(),
                userProjection.isFirst(),
                userProjection.isLast(),
                userProjection.getTotalPages(),
                userProjection.getTotalElements(),
                userProjection.getSize(),
                userProjection.getNumber(),
                userProjection.getNumberOfElements()
        );
    }
}
