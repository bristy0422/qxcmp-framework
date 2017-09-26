package com.qxcmp.framework.core.web;

import com.qxcmp.framework.web.view.annotation.form.Form;
import com.qxcmp.framework.web.view.annotation.form.TextInputField;
import com.qxcmp.framework.web.view.annotation.form.TextSelectionField;
import lombok.Data;

@Form(value = "密保问题", submitText = "设置密保")
@Data
public class AdminProfileSecurityQuestionForm {

    @TextSelectionField("密保问题一")
    private String question1;

    @TextInputField("答案")
    private String answer1;

    @TextSelectionField("密保问题二")
    private String question2;

    @TextInputField("答案")
    private String answer2;

    @TextSelectionField("密保问题三")
    private String question3;

    @TextInputField("答案")
    private String answer3;

}
