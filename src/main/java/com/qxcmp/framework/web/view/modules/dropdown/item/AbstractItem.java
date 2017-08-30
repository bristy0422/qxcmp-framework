package com.qxcmp.framework.web.view.modules.dropdown.item;

import com.qxcmp.framework.web.view.QXCMPComponent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AbstractItem extends QXCMPComponent {

    /**
     * 选项文本
     */
    private String text;

    /**
     * 选项描述，靠右侧显示
     */
    private String description;

    public AbstractItem() {
        super("qxcmp/modules/dropdown");
    }
}
