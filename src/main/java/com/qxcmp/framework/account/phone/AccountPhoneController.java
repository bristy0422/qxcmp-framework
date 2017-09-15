package com.qxcmp.framework.account.phone;

import com.qxcmp.framework.account.AccountService;
import com.qxcmp.framework.core.QXCMPSystemConfigConfiguration;
import com.qxcmp.framework.domain.Code;
import com.qxcmp.framework.domain.CodeService;
import com.qxcmp.framework.user.User;
import com.qxcmp.framework.account.AccountPageController;
import com.qxcmp.framework.web.view.elements.header.HeaderType;
import com.qxcmp.framework.web.view.elements.header.IconHeader;
import com.qxcmp.framework.web.view.elements.header.PageHeader;
import com.qxcmp.framework.web.view.elements.html.P;
import com.qxcmp.framework.web.view.elements.icon.Icon;
import com.qxcmp.framework.web.view.elements.image.Image;
import com.qxcmp.framework.web.view.support.Alignment;
import com.qxcmp.framework.web.view.views.Overview;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/account/phone/")
public class AccountPhoneController extends AccountPageController {

    public AccountPhoneController(AccountService accountService, CodeService codeService) {
        super(accountService, codeService);
    }

    /**
     * 邮箱账户注册页面
     *
     * @return 邮箱账户注册页面
     */
    @GetMapping("logon")
    public ModelAndView logon(final AccountPhoneLogonForm form) {

        if (!systemConfigService.getBoolean(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_ACCOUNT_ENABLE_PHONE).orElse(false)) {
            return logonClosedPage().build();
        }

        return buildPage(segment -> segment
                .addComponent(new PageHeader(HeaderType.H2, qxcmpConfiguration.getTitle()).setImage(new Image(qxcmpConfiguration.getLogo())).setSubTitle("手机号注册").setDividing().setAlignment(Alignment.LEFT))
                .addComponent(convertToForm(form))
        ).addObject(form)
                .build();
    }

    @PostMapping("logon")
    public ModelAndView logon(@Valid final AccountPhoneLogonForm form, BindingResult bindingResult) {

        if (!systemConfigService.getBoolean(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_ACCOUNT_ENABLE_PHONE).orElse(false)) {
            return logonClosedPage().build();
        }

        if (userService.findById(form.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "Username.exist");
        }

        if (userService.findById(form.getPhone()).isPresent()) {
            bindingResult.rejectValue("phone", "Phone.exist");
        }

        if (!Objects.equals(form.getPassword(), form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "PasswordConfirm");
        }

        verifyCaptcha(form.getCaptcha(), bindingResult);

        if (bindingResult.hasErrors()) {
            return buildPage(segment -> segment
                    .addComponent(new PageHeader(HeaderType.H2, qxcmpConfiguration.getTitle()).setImage(new Image(qxcmpConfiguration.getLogo())).setSubTitle("手机号注册").setDividing().setAlignment(Alignment.LEFT))
                    .addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
            ).addObject(form)
                    .build();
        }

        try {
            userService.create(() -> {
                User user = userService.next();
                user.setUsername(form.getUsername());
                user.setPhone(form.getPhone());
                user.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
                user.setAccountNonExpired(true);
                user.setAccountNonLocked(true);
                user.setCredentialsNonExpired(true);
                user.setEnabled(true);
                userService.setDefaultPortrait(user);
                return user;
            });

            return overviewPage(new Overview("注册成功", "你已经成功注册账号，现在可以登录了").addLink("立即登录", "/login")).build();
        } catch (Exception e) {
            return overviewPage(new Overview(new IconHeader("注册失败", new Icon("warning circle"))).addComponent(new P(e.getMessage())).addLink("返回登录", "/login")).build();
        }
    }

    @GetMapping("reset")
    public ModelAndView reset(final AccountPhoneResetForm form) {

        if (!systemConfigService.getBoolean(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_ACCOUNT_ENABLE_PHONE).orElse(false)) {
            return resetClosedPage().build();
        }

        return buildPage(segment -> segment
                .addComponent(new PageHeader(HeaderType.H2, qxcmpConfiguration.getTitle()).setImage(new Image(qxcmpConfiguration.getLogo())).setSubTitle("短信找回密码").setDividing().setAlignment(Alignment.LEFT))
                .addComponent(convertToForm(form))
        ).addObject(form)
                .build();
    }

    @PostMapping("reset")
    public ModelAndView reset(@Valid final AccountPhoneResetForm form, BindingResult bindingResult) {

        if (!systemConfigService.getBoolean(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_ACCOUNT_ENABLE_PHONE).orElse(false)) {
            return resetClosedPage().build();
        }

        Optional<User> userOptional = userService.findByPhone(form.getPhone());

        if (!userOptional.isPresent()) {
            bindingResult.rejectValue("phone", "Account.reset.noPhone");
        }

        verifyCaptcha(form.getCaptcha(), bindingResult);

        if (bindingResult.hasErrors()) {
            return buildPage(segment -> segment
                    .addComponent(new PageHeader(HeaderType.H2, qxcmpConfiguration.getTitle()).setImage(new Image(qxcmpConfiguration.getLogo())).setSubTitle("短信找回密码").setDividing().setAlignment(Alignment.LEFT))
                    .addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form)))
            ).addObject(form)
                    .build();
        }

        Code code = codeService.nextPasswordCode(userOptional.get().getId());

        return redirect("/account/reset/" + code.getId());
    }
}
