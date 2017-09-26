package com.qxcmp.framework.core.web;

import com.qxcmp.framework.core.validation.Phone;
import com.qxcmp.framework.web.view.annotation.form.Form;
import com.qxcmp.framework.web.view.annotation.form.PhoneCaptchaField;
import com.qxcmp.framework.web.view.annotation.form.PhoneField;
import lombok.Data;

@Form(value = "手机绑定", submitText = "确认绑定")
@Data
public class AdminProfileSecurityPhoneForm {

    @Phone
    @PhoneField(value = "手机号码", autoFocus = true)
    private String phone;

    @PhoneCaptchaField(value = "短信验证码", phoneField = "phone")
    private String captcha;
}
