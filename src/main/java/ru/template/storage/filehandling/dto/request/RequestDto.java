package ru.template.storage.filehandling.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
public class RequestDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -2263811251763727994L;

    @NotNull(message = "Имя файла не должно быть пустым")
    private String filename;

    @NotNull(message = "Формат файла не должно быть пустым")
    private FileFormat format;

    private RequestSearchConditionDto filter;

    @Min(1)
    @NotNull(message = "Должен быть хоть 1 заголовок")
    private List<String> headers;

}
