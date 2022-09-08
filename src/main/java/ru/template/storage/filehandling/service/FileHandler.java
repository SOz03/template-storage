package ru.template.storage.filehandling.service;

import ru.template.storage.filehandling.dto.ContentDto;
import ru.template.storage.filehandling.dto.request.RequestDto;
import ru.template.storage.filehandling.dto.templates.Template;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;

public interface FileHandler {

    <T extends Template> ByteArrayOutputStream create(@NotNull RequestDto<T> content);

    default ContentDto download(@NotNull ByteArrayOutputStream outputStream) {

        return ContentDto.builder()
                .content(new ByteArrayInputStream(outputStream.toByteArray()))
                .size((long) outputStream.toByteArray().length)
                .build();
    }

}
