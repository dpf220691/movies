package com.dpf.movies.dto;

import com.dpf.movies.common.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieOutDTO extends BaseDTO {

    private Long id;

    private String title;

    private int year;

}
