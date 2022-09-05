package ru.template.storage.filehandling.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRecordDto {

    private String courtOrdersAt;
    private String conductInquiryAt;
    private String nameBodyInquiry;
    private Long criminalNumber;
    private LocalDate initiationDate;
    private String fullname;
    private String criminalCode;

}
