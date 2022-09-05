package ru.template.storage.filehandling.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Builder
public class ContentDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -8226231089121370516L;

    @JsonIgnore
    private String filename;
    @JsonIgnore
    private Long size;
    @JsonIgnore
    private InputStream content;

}