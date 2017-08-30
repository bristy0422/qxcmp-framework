package com.qxcmp.framework.web.view.elements.divider;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VerticalDivider extends AbstractDivider {

    public VerticalDivider() {
    }

    public VerticalDivider(String text) {
        super(text);
    }

    @Override
    public String getFragmentName() {
        return "vertical";
    }

    @Override
    public String getClassName() {
        return super.getClassName() + " vertical divider";
    }
}
