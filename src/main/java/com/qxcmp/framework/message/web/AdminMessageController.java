package com.qxcmp.framework.message.web;

import com.qxcmp.framework.web.QXCMPBackendController;
import com.qxcmp.framework.web.view.elements.grid.Col;
import com.qxcmp.framework.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.framework.web.view.elements.segment.Segment;
import com.qxcmp.framework.web.view.support.Alignment;
import com.qxcmp.framework.web.view.support.ColumnCount;
import com.qxcmp.framework.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static com.qxcmp.framework.core.QXCMPConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.framework.core.QXCMPSystemConfigConfiguration.*;

/**
 * 消息服务后台页面
 *
 * @author Aaric
 */
@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/message")
@RequiredArgsConstructor
public class AdminMessageController extends QXCMPBackendController {

    @GetMapping("")
    public ModelAndView messagePage() {
        return page().addComponent(new VerticallyDividedGrid().setTextContainer().setVerticallyPadded().setAlignment(Alignment.CENTER).setColumnCount(ColumnCount.ONE).addItem(new Col()
                .addComponent(new Overview("消息服务").addLink("邮件服务配置", QXCMP_BACKEND_URL + "/message/email/config").addLink("短信服务配置", QXCMP_BACKEND_URL + "/message/sms/config"))
        )).build();
    }

    @GetMapping("/email/config")
    public ModelAndView emailConfigPage(final AdminMessageEmailConfigForm form) {

        form.setHost(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_HOSTNAME).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_HOSTNAME_DEFAULT_VALUE));
        form.setPort(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_PORT).orElse(String.valueOf(SYSTEM_CONFIG_MESSAGE_EMAIL_PORT_DEFAULT_VALUE)));
        form.setUsername(systemConfigService.getString(SYSTEM_CONFIG_ACCOUNT_ENABLE_USERNAME).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_USERNAME_DEFAULT_VALUE));
        form.setPassword(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_PASSWORD).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_PASSWORD_DEFAULT_VALUE));
        form.setActivateSubject(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_SUBJECT).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_SUBJECT_DEFAULT_VALUE));
        form.setActivateContent(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_CONTENT).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_CONTENT_DEFAULT_VALUE));
        form.setResetSubject(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_SUBJECT).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_SUBJECT_DEFAULT_VALUE));
        form.setResetContent(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_CONTENT).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_CONTENT_DEFAULT_VALUE));
        form.setBindingSubject(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_SUBJECT).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_SUBJECT_DEFAULT_VALUE));
        form.setBindingContent(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_CONTENT).orElse(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_CONTENT_DEFAULT_VALUE));

        return page()
                .addComponent(new VerticallyDividedGrid().setVerticallyPadded().setColumnCount(ColumnCount.ONE).addItem(new Col().addComponent(new Segment()
                        .addComponent(convertToForm(form))
                ))).build();
    }

    @PostMapping("/email/config")
    public ModelAndView emailConfigPage(@Valid final AdminMessageEmailConfigForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page()
                    .addComponent(new VerticallyDividedGrid().setVerticallyPadded().setColumnCount(ColumnCount.ONE).addItem(new Col().addComponent(new Segment()
                            .addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
                    ))).build();
        }

        return submitForm(form, context -> {
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_HOSTNAME, form.getHost());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_PORT, form.getPort());
            systemConfigService.update(SYSTEM_CONFIG_ACCOUNT_ENABLE_USERNAME, form.getUsername());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_PASSWORD, form.getPassword());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_SUBJECT, form.getActivateSubject());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_ACTIVATE_CONTENT, form.getActivateContent());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_SUBJECT, form.getResetSubject());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_RESET_CONTENT, form.getResetContent());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_SUBJECT, form.getBindingSubject());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_EMAIL_ACCOUNT_BINDING_CONTENT, form.getBindingContent());
        });
    }


    @GetMapping("/sms/config")
    public ModelAndView smsConfigPage(final AdminMessageSmsConfigForm form) {

        form.setAccessKey(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_KEY).orElse(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_KEY_DEFAULT_VALUE));
        form.setAccessSecret(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_SECRET).orElse(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_SECRET_DEFAULT_VALUE));
        form.setEndPoint(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_END_POINT).orElse(SYSTEM_CONFIG_MESSAGE_SMS_END_POINT_DEFAULT_VALUE));
        form.setTopicRef(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_TOPIC_REF).orElse(SYSTEM_CONFIG_MESSAGE_SMS_TOPIC_REF_DEFAULT_VALUE));
        form.setSign(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_SIGN).orElse(SYSTEM_CONFIG_MESSAGE_SMS_SIGN_DEFAULT_VALUE));
        form.setCaptchaTemplate(systemConfigService.getString(SYSTEM_CONFIG_MESSAGE_SMS_CAPTCHA_TEMPLATE_CODE).orElse(SYSTEM_CONFIG_MESSAGE_SMS_CAPTCHA_TEMPLATE_CODE_DEFAULT_VALUE));

        return page()
                .addComponent(new VerticallyDividedGrid().setVerticallyPadded().setColumnCount(ColumnCount.ONE).addItem(new Col().addComponent(new Segment()
                        .addComponent(convertToForm(form))
                ))).build();
    }

    @PostMapping("/sms/config")
    public ModelAndView smsConfigPage(@Valid final AdminMessageSmsConfigForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page()
                    .addComponent(new VerticallyDividedGrid().setVerticallyPadded().setColumnCount(ColumnCount.ONE).addItem(new Col().addComponent(new Segment()
                            .addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
                    ))).build();
        }

        return submitForm(form, context -> {
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_KEY, form.getAccessKey());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_SECRET, form.getAccessSecret());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_SMS_END_POINT, form.getEndPoint());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_SMS_TOPIC_REF, form.getTopicRef());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_SMS_SIGN, form.getSign());
            systemConfigService.update(SYSTEM_CONFIG_MESSAGE_SMS_CAPTCHA_TEMPLATE_CODE, form.getCaptchaTemplate());
        });
    }

}
