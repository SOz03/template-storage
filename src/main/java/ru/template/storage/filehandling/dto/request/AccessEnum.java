package ru.template.storage.filehandling.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
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
