package ru.template.storage.filehandling.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.template.storage.filehandling.dto.ContentDto;
import ru.template.storage.filehandling.dto.request.RequestDto;
import ru.template.storage.filehandling.dto.request.TemplateInfo;
import ru.template.storage.filehandling.dto.templates.BookRecordTemplate;
import ru.template.storage.filehandling.dto.templates.MeasureResponseTemplate;
import ru.template.storage.filehandling.dto.templates.Template;
import ru.template.storage.filehandling.service.FileHandlerService;

import javax.validation.constraints.NotNull;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Работа с файлами Microsoft Excel и Open Document")
@RequestMapping("/file-handler")
public class FileHandlerController {

    private final FileHandlerService<MeasureResponseTemplate> measureResponseTemplateFileHandlerService;
    private final FileHandlerService<BookRecordTemplate> bookRecordTemplateFileHandlerService;

    @PostMapping(name = "Мера реагирования", value = "/export-measure-response", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadMeasureResponse(@RequestBody RequestDto<MeasureResponseTemplate> content) {
        return download(measureResponseTemplateFileHandlerService, content, TemplateInfo.MEASURE_RESPONSE);
    }

    @PostMapping(name = "Книги учета", value = "/export-book-record", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadBookRecord(@RequestBody RequestDto<BookRecordTemplate> content) {
        return download(bookRecordTemplateFileHandlerService, content, TemplateInfo.BOOK_RECORD);
    }

    private <T extends Template> ResponseEntity<Resource> download(@NotNull final FileHandlerService<T> service,
                                                                   @NotNull final RequestDto<T> content,
                                                                   @NotNull final TemplateInfo info) {
        content.setTemplateInfo(info);
        String filename = URLEncoder.encode(info.getFilename(), StandardCharsets.UTF_8)
                + content.getFormat().getType();

        log.info("Started downloading the file {}", filename);

        ContentDto dto = switch (content.getFormat()) {
            case XLS, XLSX -> service.writeXLS(content);
            case ODS -> service.writeODS(content);
        };

        log.info("End of file download");
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_OCTET_STREAM_VALUE)
                .contentLength((dto != null) ? dto.getSize() : 0)
                .body((dto != null) ? new InputStreamResource(dto.getContent()) : null);
    }

}
