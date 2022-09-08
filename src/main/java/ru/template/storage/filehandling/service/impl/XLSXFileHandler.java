package ru.template.storage.filehandling.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.template.storage.filehandling.anotation.Spreadsheet;
import ru.template.storage.filehandling.dto.request.AccessEnum;
import ru.template.storage.filehandling.dto.request.RequestDto;
import ru.template.storage.filehandling.dto.request.TemplateInfo;
import ru.template.storage.filehandling.dto.templates.Template;
import ru.template.storage.filehandling.service.FileHandler;
import ru.template.storage.filehandling.service.style.XLSStyle;

import javax.validation.constraints.NotNull;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для работы с файлами расширения .xls и .xlsx
 */
@Slf4j
@Service
public class XLSXFileHandler extends XLSStyle implements FileHandler {

    private XSSFWorkbook workbook;
    private Sheet sheet;
    private CellStyle cellTableStyle;
    private CellStyle headerTableStyle;

    @Override
    public <T extends Template> ByteArrayOutputStream create(@NotNull RequestDto<T> content) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            this.workbook = workbook;
            this.sheet = workbook.createSheet(SHEET_NAME);

            cellTableStyle = createStandardTableCellsStyle(workbook);
            headerTableStyle = createHeaderTableCellsStyle(workbook);

            Iterator<T> iterator = content.getListNeedObjects().iterator();
            if (!iterator.hasNext()) {
                return null;
            }

            T currentObject = iterator.next();

            List<PropertyDescriptor> accessibleProperties = getAccessibleProperties(currentObject);

            if (accessibleProperties != null) {
                createHeaderPart(currentObject, content.getTemplateInfo(), accessibleProperties.size());
                createFilterPart(content.getFilter());
                createHeaderTablePart(accessibleProperties, currentObject);

                Long totalElements = 0L;
                while (currentObject != null) {

                    totalElements++;
                    addObjectInTable(accessibleProperties, currentObject, totalElements);
                    if (iterator.hasNext()) {
                        currentObject = iterator.next();
                    } else {
                        currentObject = null;
                    }
                }

                createCell(sheet.createRow(sheet.getLastRowNum() + 1).createCell(0),
                        String.class, "Всего: " + totalElements, cellTableStyle);
            }

            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            workbook.write(byteArray);

            byteArray.close();

            return byteArray;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private <T> void createCell(Cell cell, Class<?> aClass, T value, CellStyle style) {
        cell.setCellStyle(style);

        if (value == null) {
            cell.setCellValue(IF_CELL_IS_NULL);
        } else {
            if (LocalDate.class.equals(aClass) || LocalDateTime.class.equals(aClass) || Date.class.equals(aClass)) {
                cell.setCellValue(((LocalDate) value).toString());
            } else if (Long.class.equals(aClass) || Integer.class.equals(aClass)) {
                cell.setCellValue((Long) value);
            } else if (Double.class.equals(aClass) || Float.class.equals(aClass)) {
                cell.setCellValue((Double) value);
            } else {
                cell.setCellValue(value.toString());
            }
        }
    }

    /**
     * Получаем доступные поля объекта
     *
     * @param object объект, поля которого заносятся в таблицу
     * @param <T>    класс объекта
     * @return список из наименований полей объекта
     */
    private <T> List<PropertyDescriptor> getAccessibleProperties(T object) {
        try {
            return Arrays.stream(Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors())
                    .filter(pd -> pd.getReadMethod() != null && !"class".equals(pd.getName())
                            && !IGNORE_FIELDS.contains(pd.getDisplayName()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            return null;
        }
    }

    /**
     * Создание заголовка из N строчек, где происходит объединение колонок
     * в зависимости от размера главной таблицы
     *
     * @param maxColumns количество объединяемых столбцов
     */
    private <T extends Template> void createHeaderPart(T template, TemplateInfo info, int maxColumns) {
        CellStyle headerSheetStyle = createHeaderSheetStyle(workbook);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, maxColumns - 2));
        createCell(sheet.createRow(0).createCell(1),
                String.class,
                template.getTitle(),
                headerSheetStyle);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, maxColumns - 2));
        createCell(sheet.createRow(2).createCell(1),
                String.class,
                info.getDescription(),
                headerSheetStyle);
    }


    /**
     * Создание информации об условиях фильтрации данных
     *
     * @param <T> класс объекта, который содержит все условия поиска
     */
    private <T> void createFilterPart(T filter) throws InvocationTargetException, IllegalAccessException {
        CellStyle titleStyle = commonStyle(workbook, 12, true);
        CellStyle fieldStyle = commonStyle(workbook, 12, false);

        List<PropertyDescriptor> properties = getAccessibleProperties(filter);

        int startRow = sheet.getLastRowNum() + ROW_OFFSET + 1;
        int index = startRow;
        if (properties != null) {
            Map<String, String> mapInfo = storingFieldNameToReplace(filter);
            for (PropertyDescriptor prop : properties) {
                Object value = prop.getReadMethod().invoke(filter);
                if (value != null) {
                    Row row = sheet.createRow(index);
                    createCell(row.createCell(0), String.class, "Условие поиска: ", fieldStyle);
                    createCell(row.createCell(1), String.class, mapInfo.get(prop.getName()), titleStyle);

                    String access = AccessEnum.fromType(value.toString());
                    createCell(row.createCell(2), String.class,
                            (access == null) ? value.toString() : access, fieldStyle);

                    index++;
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(startRow, sheet.getLastRowNum(), 0, 0));
        }
    }

    /**
     * Создание заголовков таблицы. Имя поля преобразуется в текст с помощью Аннотации {@link Spreadsheet}
     * и заносится в ячейку
     *
     * @param accessibleProperties список имён методов
     */
    private <T> void createHeaderTablePart(List<PropertyDescriptor> accessibleProperties, T object) {
        Row row = sheet.createRow(sheet.getLastRowNum() + ROW_OFFSET + 1);

        createCell(row.createCell(0), String.class, NAME_ID_COLUMN, headerTableStyle);

        Map<String, String> mapInfo = storingFieldNameToReplace(object);

        for (PropertyDescriptor field : accessibleProperties) {
            sheet.setColumnWidth(row.getLastCellNum(), ALL_COLUMN_WIDTH);
            createCell(row.createCell(row.getLastCellNum()), String.class,
                    mapInfo.get(field.getName()),
                    headerTableStyle);
        }
    }

    /**
     * Добавление объекта в таблицу. Каждому значению из списка присваивается своё значение,
     * после чего создаётся новая строка и добавляется N кол-во ячеек для каждого поля
     *
     * @param accessibleProperties список имён методов
     * @param id                   порядковый номер в таблице
     * @param <T>                  класс объекта, значения полей которого заносятся в таблицу
     */
    private <T> void addObjectInTable(List<PropertyDescriptor> accessibleProperties, T currentObject, Long id) {
        createCell(sheet.createRow(sheet.getLastRowNum() + 1).createCell(0),
                Integer.class, id, cellTableStyle);

        for (PropertyDescriptor accessibleProperty : accessibleProperties) {
            try {
                Object value = accessibleProperty.getReadMethod().invoke(currentObject);
                createCell(
                        sheet.getRow(sheet.getLastRowNum()).createCell(sheet.getRow(sheet.getLastRowNum()).getLastCellNum()),
                        accessibleProperty.getPropertyType(),
                        value, cellTableStyle);
            } catch (InvocationTargetException | IllegalAccessException exception) {
                log.error("{}", exception.getMessage());
            }
        }
    }

    private <T> Map<String, String> storingFieldNameToReplace(T object) {
        Map<String, String> mapInfo = new HashMap<>();

        Class<T> classTemplate = (Class<T>) object.getClass();
        for (Field field : classTemplate.getDeclaredFields()) {
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof Spreadsheet info && info.isColumn()) {
                    mapInfo.put(field.getName(), info.name());
                }
            }
        }

        return mapInfo;
    }

}
