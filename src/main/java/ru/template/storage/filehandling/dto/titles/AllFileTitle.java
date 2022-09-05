package ru.template.storage.filehandling.dto.titles;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum AllFileTitle {

    COURT_ORDER_AT("Время и дата поступления постановления прокурору в порядке ч. 5 ст. 226.4 УПК РФ", "courtOrdersAt"),
    CONDUCT_INQUIRY_AT("Время и дата принятия решения о производстве дознания в сокращенной форме в порядке п. 1 ч. 3 ст. 226.4 УПК РФ", "conductInquiryAt"),
    NAME_BODY_INQUIRY("Наименование органа дознания", "nameBodyInquiry"),
    CRIMINAL_NUMBER("№ уголовного дела", "criminalNumber"),
    INITIATION_DATE("дата возбуждения", "initiationDate"),
    FULLNAME(" Ф.И.О. подозреваемого", "fullname"),
    CRIMINAL_CODE("статья УК РФ", "criminalCode"),

    OFFICE("Ведомство", "office"),
    REGION("Регион", "region"),
    DIVISION("Подразделение", "division"),
    REGIONAL_CITY("Регион (город)", "regionalCity");

    private final String ru;
    private final String eng;

    public static AllFileTitle fromEng(String eng) {
        for (AllFileTitle title : AllFileTitle.values()) {
            if (title.getEng().equalsIgnoreCase(eng)) {
                return title;
            }
        }
        return null;
    }

}
