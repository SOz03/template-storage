package ru.template.storage.filehandling.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccessEnum {

    ALL("Все"),

    NO_ALL("Не все");

    final String value;

    public static String fromType(String type) {
        for (AccessEnum a : AccessEnum.values()) {
            if (a.name().equals(type)) {
                return a.getValue();
            }
        }
        return null;
    }

}
