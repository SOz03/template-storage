package ru.template.storage.filehandling.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.template.storage.filehandling.dto.request.RequestDto;
import ru.template.storage.filehandling.dto.titles.AllFileTitle;
import ru.template.storage.filehandling.service.FileHandler;
import ru.template.storage.filehandling.service.style.XLSStyle;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
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
    public <T> ByteArrayOutputStream create(RequestDto content,
                                            Collection<T> collection) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            this.workbook = workbook;
            this.sheet = workbook.createSheet(SHEET_NAME);

            cellTableStyle = createStandardTableCellsStyle(workbook);
            headerTableStyle = createHeaderTableCellsStyle(workbook);

            Iterator<T> iterator = collection.iterator();
            if (!iterator.hasNext()) {
                return null;
            }

            T currentObject = iterator.next();
            List<PropertyDescriptor> accessibleProperties = getAccessibleProperties(currentObject);

            if (accessibleProperties != null) {
                createHeaderPart(content, accessibleProperties.size());
                createFilterPart(content.getFilter());
                createHeaderTablePart(accessibleProperties);

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
     * @param request    объект, который содержит N полей с названием заголовков
     * @param maxColumns количество объединяемых столбцов
     */
    private void createHeaderPart(RequestDto request,
                                  int maxColumns) {
        CellStyle headerSheetStyle = createHeaderSheetStyle(workbook);

        for (int rowIndex = 0; rowIndex < request.getHeaders().size(); rowIndex++) {
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, maxColumns));
            createCell(sheet.createRow(rowIndex).createCell(0), String.class, request.getHeaders().get(rowIndex),
                    headerSheetStyle);
        }
    }

    private <T> void createFilterPart(T filter) throws InvocationTargetException, IllegalAccessException {
        CellStyle titleStyle = createFilterTitleStyle(workbook);
        CellStyle fieldStyle = createFilterFieldStyle(workbook);

        List<PropertyDescriptor> properties = getAccessibleProperties(filter);

        int startRow = sheet.getLastRowNum() + ROW_OFFSET + 1;
        int index = startRow;
        if (properties != null) {
            for (PropertyDescriptor prop : properties) {
                Object value = prop.getReadMethod().invoke(filter);
                if (value != null) {
                    Row row = sheet.createRow(index);
                    createCell(row.createCell(0), String.class, "Условие поиска: ", fieldStyle);
                    createCell(row.createCell(1), String.class, AllFileTitle.fromEng(prop.getName()).getRu(), titleStyle);
                    createCell(row.createCell(2), String.class, value.toString(), fieldStyle);
                    index++;
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(startRow, sheet.getLastRowNum(), 0, 0));
        }
    }

    private void createHeaderTablePart(List<PropertyDescriptor> accessibleProperties) {
        Row row = sheet.createRow(sheet.getLastRowNum() + ROW_OFFSET + 1);

        createCell(row.createCell(0), String.class, NAME_ID_COLUMN, headerTableStyle);

        for (PropertyDescriptor field : accessibleProperties) {
            sheet.setColumnWidth(row.getLastCellNum(), ALL_COLUMN_WIDTH);
            createCell(row.createCell(row.getLastCellNum()), String.class,
                    Objects.requireNonNull(AllFileTitle.fromEng(field.getName())).getRu(),
                    headerTableStyle);
        }
    }

    private <T> void addObjectInTable(List<PropertyDescriptor> accessibleProperties, T currentObject, Long id) {
        createCell(sheet.createRow(sheet.getLastRowNum() + 1).createCell(0),
                Integer.class, id, cellTableStyle);

        for (PropertyDescriptor accessibleProperty : accessibleProperties) {
            try {
                Object value = accessibleProperty.getReadMethod().invoke(currentObject);
                createCell(sheet.getRow(sheet.getLastRowNum())
                                .createCell(sheet.getRow(sheet.getLastRowNum())
                                        .getLastCellNum()),
                        accessibleProperty.getPropertyType(), value, cellTableStyle);
            } catch (InvocationTargetException | IllegalAccessException exception) {
                log.error("{}", exception.getMessage());
            }
        }
    }

}
