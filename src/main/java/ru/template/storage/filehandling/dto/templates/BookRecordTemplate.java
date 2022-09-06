package ru.template.storage.filehandling.dto.templates;

import lombok.*;
import ru.template.storage.filehandling.anotation.Spreadsheet;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRecordTemplate implements Template,Serializable {

    @Serial
    private static final long serialVersionUID = 3364282719457537861L;

    @Spreadsheet(name = "Время и дата поступления постановления прокурору в порядке ч. 5 ст. 226.4 УПК РФ")
    private String courtOrdersAt;

    @Spreadsheet(name = "Время и дата принятия решения о производстве дознания в сокращенной форме в порядке п. 1 ч. 3 ст. 226.4 УПК РФ")
    private String conductInquiryAt;

    @Spreadsheet(name = "Наименование органа дознания")
    private String nameBodyInquiry;

    @Spreadsheet(name = "№ уголовного дела")
    private Long criminalNumber;

    @Spreadsheet(name = "Дата возбуждения")
    private LocalDate initiationDate;

    @Spreadsheet(name = "Ф.И.О. подозреваемого")
    private String fullname;

    @Spreadsheet(name = "Статья УК РФ")
    private String criminalCode;

}
