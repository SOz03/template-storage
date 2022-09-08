package ru.template.storage.filehandling.dto.templates;

import lombok.*;
import ru.template.storage.filehandling.anotation.Spreadsheet;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeasureResponseTemplate implements Template, Serializable {

    @Serial
    private static final long serialVersionUID = 3364282719457537861L;

    @Spreadsheet(name = "Мера реагирования")
    private String responseMeasure;

    @Spreadsheet(name = "Дата внесения")
    private String dateResponseActionTaken;

    @Spreadsheet(name = "Адресат(ведомство)")
    private String agency;

    @Spreadsheet(name = "Адресат(регион)")
    private String region;

    @Spreadsheet(name = "Исполнитель по документу")
    private String executorDocument;

    @Spreadsheet(name = "Количество нарушений")
    private String numberViolations;

    @Spreadsheet(name = "Из них фактически устранено")
    private String actuallyEliminated;

    @Spreadsheet(name = "Дата рассмотрения")
    private String dateConsideration;

    @Spreadsheet(name = "Результат рассмотрения")
    private String considerationResult;

    @Spreadsheet(name = "Количество лиц, привлеченных к ответственности")
    private String numberPersonsBroughtJustice;

    @Spreadsheet(name = "Из них руководителей")
    private String managers;

    @Spreadsheet(name = "Карточка")
    private String card;

}
