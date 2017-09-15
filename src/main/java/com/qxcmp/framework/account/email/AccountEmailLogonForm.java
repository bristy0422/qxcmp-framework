package com.qxcmp.framework.account.email;

import com.qxcmp.framework.validation.Username;
import com.qxcmp.framework.web.view.annotation.form.Form;
import com.qxcmp.framework.web.view.annotation.form.ImageCaptchaField;
import com.qxcmp.framework.web.view.annotation.form.PasswordField;
import com.qxcmp.framework.web.view.annotation.form.TextInputField;
import com.qxcmp.framework.web.view.modules.form.FormMethod;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * 邮箱账户注册平台统一表单
 * <p>
 * 用户在使用邮箱注册账户的时候需要填写的信息
 *
 * @author aaric
 */
@Form(submitText = "立即注册", method = FormMethod.POST)
@Data
public class AccountEmailLogonForm {

    /**
     * 用户名
     * <p>
     * 全局唯一，注册的时候需要检查是否已经存在
     */
    @Size(min = 6, max = 20, message = "{Size.username}")
    @Username
    @TextInputField(value = "用户名", tooltip = "用户名只能由字母、数字、下划线组成，且长度在6到20个字符之间", maxLength = 20, placeholder = "用户名只能由字母、数字、下划线组成")
    private String username;

    /**
     * 用户邮箱
     * <p>
     * 全局唯一，注册的时候需要检查邮箱是否已经被占用
     */
    @Email(message = "{Email}")
    @NotEmpty(message = "{NotEmpty.emailRegisterForm.email}")
    @TextInputField("邮箱")
    private String email;

    /**
     * 登录密码
     * <p>
     * 用户登录时候需要输入的密码
     */
    @Size(min = 6, max = 20, message = "{Size.password}")
    @PasswordField(value = "设置密码", placeholder = "请使用至少两种以上的字符组合", maxLength = 20)
    private String password;

    /**
     * 确认密码
     * <p>
     * 保证用户两次输入的密码一致
     */
    @PasswordField(value = "确认密码", placeholder = "请再次输入您的登录密码", maxLength = 20)
    private String passwordConfirm;

    /**
     * 会话验证码
     * <p>
     * 用户输入的会话验证码，需要和验证码图片中的信息一致
     */
    @ImageCaptchaField("验证码")
    private String captcha;
}
