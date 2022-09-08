package ru.template.storage.filehandling.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.template.storage.filehandling.dto.templates.Template;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto<T extends Template> implements Serializable {

    @Serial
    private static final long serialVersionUID = -2263811251763727994L;

    private FilterDto filter;
    private FileFormat format;
    Collection<T> listNeedObjects = new ArrayList<>();

    @JsonIgnore
    private TemplateInfo templateInfo;

}
