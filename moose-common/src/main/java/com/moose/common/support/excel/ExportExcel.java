package com.moose.common.support.excel;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportExcel {

    String[] mapper() default {};

    String separator() default ":";

    String name();

    String sheetName() default "sheet";

}
