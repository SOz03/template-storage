package ru.template.storage.common.dto.filter;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleFilterDto.class, name = "EQ"),
        @JsonSubTypes.Type(value = SimpleFilterDto.class, name = "GT"),
        @JsonSubTypes.Type(value = SimpleFilterDto.class, name = "LT"),
        @JsonSubTypes.Type(value = ComplexFilter.class, name = "OR"),
        @JsonSubTypes.Type(value = ComplexFilter.class, name = "AND"),
        @JsonSubTypes.Type(value = SimpleFilterDto.class, name = "CONTAINS")
})
@NoArgsConstructor
@AllArgsConstructor
public class CommonFilter implements Serializable {

    @Serial
    private static final long serialVersionUID = 503120348100248558L;

    private FilterType type;

}