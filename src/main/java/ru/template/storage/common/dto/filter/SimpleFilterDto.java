package ru.template.storage.common.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SimpleFilterDto extends CommonFilter {

    private static final long serialVersionUID = 7458111147778273068L;

    private String columnKey;
    private String value;

}
