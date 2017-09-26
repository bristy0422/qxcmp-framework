package com.qxcmp.framework.core.web;

import com.qxcmp.framework.web.view.annotation.form.AvatarField;
import com.qxcmp.framework.web.view.annotation.form.Form;
import com.qxcmp.framework.web.view.annotation.form.TextInputField;
import lombok.Data;

@Form("基本资料")
@Data
public class AdminProfileInfoForm {

    @AvatarField("你的头像")
    private String portrait;

    @TextInputField("真实姓名")
    private String name;

    @TextInputField("昵称")
    private String nickname;

    @TextInputField("个性签名")
    private String personalizedSignature;
}
