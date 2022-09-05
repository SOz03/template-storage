package ru.template.storage.filehandling.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Builder
public class RequestSearchConditionDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -8268427940442806288L;

    private AccessEnum office;
    private AccessEnum region;
    private AccessEnum division;
    private String regionalCity;
}
