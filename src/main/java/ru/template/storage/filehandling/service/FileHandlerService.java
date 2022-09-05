package ru.template.storage.filehandling.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.template.storage.filehandling.dto.ContentDto;
import ru.template.storage.filehandling.dto.request.RequestDto;
import ru.template.storage.filehandling.service.impl.ODSFileHandler;
import ru.template.storage.filehandling.service.impl.XLSXFileHandler;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileHandlerService {

    private final XLSXFileHandler xlsx;
    private final ODSFileHandler ods;

    public ContentDto writeODS(String filename) {
        return null;
    }

    public <T> ContentDto writeXLS(RequestDto requestDto, Collection<T> collection) {
        log.info("Start creating a spreadsheet with filename");
        ByteArrayOutputStream content = xlsx.create(requestDto, collection);
        log.info("Get the byte stream");

        ContentDto dto = xlsx.download(content);
        log.info("The file is ready to be downloaded, return file {} with size {}", requestDto.getFilename(), dto.getSize());

        return dto;
    }

}
