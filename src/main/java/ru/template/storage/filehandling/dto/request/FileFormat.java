package ru.template.storage.filehandling.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileFormat {

    /**
     * расширение файла Excel (1997–2003).
     */
    XLS(".xls"),

    XLSX(".xlsx"),

    ODS(".ods");

    private final String type;

}
