package ru.template.storage.filehandling.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.template.storage.filehandling.anotation.Spreadsheet;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Builder
public class FilterDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -8268427940442806288L;

    @Spreadsheet(name = "Ведомство")
    private AccessEnum office;

    @Spreadsheet(name = "Регион")
    private AccessEnum region;

    @Spreadsheet(name = "Подразделение")
    private AccessEnum division;

    @Spreadsheet(name = "Регион (город)")
    private String regionalCity;

}
