package ru.template.storage.common.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PagingDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 2799800759237725846L;

    private int currentPage;
    private int pageSize;

}
