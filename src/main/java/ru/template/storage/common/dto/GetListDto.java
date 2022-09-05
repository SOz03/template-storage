package ru.template.storage.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.template.storage.common.dto.filter.CommonFilter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
public class GetListDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -1619336002746091457L;

    private PagingDto paging;
    private SortingDto sorting;
    private CommonFilter filters;

}