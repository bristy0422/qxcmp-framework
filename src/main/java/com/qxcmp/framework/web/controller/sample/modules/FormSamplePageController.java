package com.qxcmp.framework.web.controller.sample.modules;

import com.qxcmp.framework.web.controller.sample.AbstractSamplePageController;
import com.qxcmp.framework.web.view.Component;
import com.qxcmp.framework.web.view.elements.grid.Col;
import com.qxcmp.framework.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.framework.web.view.elements.message.InfoMessage;
import com.qxcmp.framework.web.view.elements.message.SuccessMessage;
import com.qxcmp.framework.web.view.elements.segment.Segment;
import com.qxcmp.framework.web.view.modules.form.Form;
import com.qxcmp.framework.web.view.modules.form.FormEnctype;
import com.qxcmp.framework.web.view.modules.form.FormMethod;
import com.qxcmp.framework.web.view.modules.form.FormSection;
import com.qxcmp.framework.web.view.modules.form.field.PasswordField;
import com.qxcmp.framework.web.view.modules.form.field.TextInputField;
import com.qxcmp.framework.web.view.support.ColumnCount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/test/sample/form")
public class FormSamplePageController extends AbstractSamplePageController {

    @GetMapping("")
    public ModelAndView sample() {
        return page(() -> new VerticallyDividedGrid().setTextContainer().setVerticallyPadded().setColumnCount(ColumnCount.ONE)
                .addItem(new Col().addComponent(createFormSegment()))
        );
    }

    private Component createFormSegment() {
        return new Segment().addComponent(new Form().setAction("/test/sample/form").setMethod(FormMethod.POST).setEnctype(FormEnctype.APPLICATION)
                .setInfoMessage((InfoMessage) new InfoMessage("关于表单说明", "本表单展示了所有的表单组件").setCloseable())
                .setSuccessMessage(new SuccessMessage("表单信息提交成功"))
                .addSection(new FormSection("基本资料"))
                .addItem(new TextInputField("username", "", "用户名").setMaxLength(20).setPlaceholder("请输入用户名").setTooltip("用户名由字母、数字和下划线组成").setRequired())
                .addItem(new TextInputField("nickname", "", "昵称").setMaxLength(10).setPlaceholder("请输入昵称").setRequired().setErrorMessage("昵称不能为空"))
                .addItem(new PasswordField("password", "", "登录密码").setMaxLength(20).setPlaceholder("请输入密码").setTooltip("密码由字母、数字和特殊符号组成").setRequired())
                .addSection(new FormSection("补充资料"))
        );
    }

}
