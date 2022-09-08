package ru.template.storage.filehandling.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;


@Getter
@AllArgsConstructor
public enum TemplateInfo implements Serializable {

    MEASURE_RESPONSE("Мера реагирования", "Мера реагирования", "Учет деятельности прокуроров"),

    BOOK_RECORD("Книги учета", "Книги учета", "Учет книг");

    private final String filename;
    private final String title;
    private final String description;

}
