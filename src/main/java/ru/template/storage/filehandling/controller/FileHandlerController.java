package ru.template.storage.filehandling.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
import ru.template.storage.filehandling.dto.templates.BookRecordTemplate;
import ru.template.storage.filehandling.entity.BookRecord;
import ru.template.storage.filehandling.service.FileHandlerService;
import ru.template.storage.filehandling.service.dao.BookRecordDaoService;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Работа с файлами Microsoft Excel и Open Document")
@RequestMapping("/file-handler")
public class FileHandlerController {

    private final ModelMapper mapper;
    private final BookRecordDaoService daoService;
    private final FileHandlerService handlerService;

    @PostMapping(value = "/export", produces = APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> download(@RequestBody @Valid RequestDto content) {
        String filename = URLEncoder.encode(content.getFilename(), StandardCharsets.UTF_8)
                + content.getFormat().getType();
        log.info("Started downloading the file {}", content.getFilename());
        List<BookRecord> list = (List<BookRecord>) daoService.findAll();

        ContentDto dto = switch (content.getFormat()) {
            case XLS, XLSX -> handlerService.writeXLS(content, list.stream()
                    .map(value -> mapper.map(value, BookRecordTemplate.class))
                    .collect(Collectors.toList()));
            case ODS -> handlerService.writeODS(content, list);
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
