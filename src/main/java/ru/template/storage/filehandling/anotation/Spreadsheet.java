package ru.template.storage.filehandling.anotation;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Spreadsheet {

    /**
     * При вызове имени метода будет отображаться данное значение
     *
     * @return имя колонки
     */
    @NotNull String name() default "";

    boolean isColumn() default true;

}
