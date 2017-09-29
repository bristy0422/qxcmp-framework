package com.qxcmp.framework.web.view.views;

import com.google.common.collect.Lists;
import com.qxcmp.framework.web.view.AbstractComponent;
import com.qxcmp.framework.web.view.Component;
import com.qxcmp.framework.web.view.elements.button.AbstractButton;
import com.qxcmp.framework.web.view.elements.button.Button;
import com.qxcmp.framework.web.view.elements.header.AbstractHeader;
import com.qxcmp.framework.web.view.elements.header.HeaderType;
import com.qxcmp.framework.web.view.elements.header.PageHeader;
import com.qxcmp.framework.web.view.support.Alignment;
import com.qxcmp.framework.web.view.support.AnchorTarget;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

/**
 * 概览视图组件
 *
 * @author Aaric
 */
@Getter
@Setter
public class Overview extends AbstractComponent {

    /**
     * 标题
     */
    private AbstractHeader header;

    /**
     * 底部超链接
     */
    private List<AbstractButton> links = Lists.newArrayList();

    /**
     * 额外内容
     */
    private List<Component> components = Lists.newArrayList();

    /**
     * 内容对齐方式
     */
    private Alignment alignment = Alignment.NONE;

    public Overview addLink(AbstractButton button) {
        links.add(button);
        return this;
    }

    public Overview addLink(String text, String url) {
        links.add(new Button(text, url).setBasic());
        return this;
    }

    public Overview addLink(String text, String url, AnchorTarget target) {
        links.add(new Button(text, url, target).setBasic());
        return this;
    }

    public Overview addComponent(Component component) {
        components.add(component);
        return this;
    }

    public Overview addComponents(Collection<? extends Component> components) {
        this.components.addAll(components);
        return this;
    }

    public Overview(AbstractHeader header) {
        this.header = header;
    }

    public Overview(String title) {
        this.header = new PageHeader(HeaderType.H1, title);
    }

    public Overview(String title, String subTitle) {
        this.header = new PageHeader(HeaderType.H1, title).setSubTitle(subTitle);
    }

    @Override
    public String getFragmentFile() {
        return "qxcmp/views/overview";
    }

    @Override
    public String getClassPrefix() {
        return "ui";
    }

    @Override
    public String getClassContent() {
        return String.valueOf(alignment);
    }

    @Override
    public String getClassSuffix() {
        return "segment";
    }

    public Overview setAlignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }
}
