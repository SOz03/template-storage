package ru.template.storage.common.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SortingDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -4948160794654599317L;

    private String columnKey;
    private SortingDirection direction;

}
