package ru.template.storage.common.dto.filter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum FilterType {

    AND("AND", false),

    OR("OR", false),

    EQ("EQ", true),

    CONTAINS("CONTAINS", true),

    GT("GT", true),

    LT("LT", true);

    private final String type;

    private final boolean simpleType;

    FilterType(String type, boolean simpleType) {
        this.type = type;
        this.simpleType = simpleType;
    }

    public String getType() {
        return type;
    }

    public boolean isSimpleType() {
        return simpleType;
    }

    public static List<FilterType> getSimpleTypes() {
        return Arrays.stream(FilterType.values())
                .filter(FilterType::isSimpleType)
                .collect(Collectors.toList());
    }

    public static List<FilterType> getAggTypes() {
        return Arrays.stream(FilterType.values())
                .filter(f -> !f.isSimpleType())
                .collect(Collectors.toList());
    }

    public static FilterType fromType(String type) {
        for (FilterType t : FilterType.values()) {
            if (t.getType().equals(type)) {
                return t;
            }
        }
        return null;
    }
}
