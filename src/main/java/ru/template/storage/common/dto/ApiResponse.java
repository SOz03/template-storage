package ru.template.storage.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.template.storage.common.enums.ErrorCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class ApiResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -2253186063741586165L;

    private ErrorCode errorCode;
    private List<T> content = new ArrayList<>();
    private int page;
    private long totalElements;

}
