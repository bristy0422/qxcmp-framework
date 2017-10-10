package com.qxcmp.framework.web.view.annotation.form;

import com.qxcmp.framework.web.view.modules.form.support.DateTimeFieldType;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DateTimeField {

    /**
     * 标签名称
     */
    String value();

    /**
     * 字段所属组
     */
    String section() default "";

    /**
     * 字段说明文本
     */
    String tooltip() default "";

    /**
     * 是否为必选项
     * <p>
     * 会在标签上加上红星
     */
    boolean required() default false;

    /**
     * 是否禁用浏览器自动完成
     */
    boolean disableAutoComplete() default false;

    /**
     * 是否在页面加载完成时自动获得焦点
     */
    boolean autoFocus() default false;

    /**
     * 是否为只读
     */
    boolean readOnly() default false;

    /**
     * 提示文本
     */
    String placeholder() default "";

    /**
     * 类型
     */
    DateTimeFieldType type() default DateTimeFieldType.DATETIME;
}
