package ru.template.storage.filehandling.dto.templates;

public interface Template {

    default String getTitle() {
        return "ГОСУДАРСТВЕННАЯ АВТОМАТИЗИРОВАННАЯ СИСТЕМА ПРАВОВОЙ СТАТИСТИКИ";
    }

}
