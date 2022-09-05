package ru.template.storage.common.dto.filter;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
public class ComplexFilter extends CommonFilter {

    @Serial
    private static final long serialVersionUID = -6936377397031539072L;

    private List<CommonFilter> operands;

}
