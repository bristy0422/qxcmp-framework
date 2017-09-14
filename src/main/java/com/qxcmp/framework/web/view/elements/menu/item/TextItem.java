package com.qxcmp.framework.web.view.elements.menu.item;

import com.qxcmp.framework.web.view.support.AnchorTarget;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextItem extends AbstractMenuItem {

    private String text;

    private String url;

    private String urlTarget;

    public TextItem(String text) {
        this.text = text;
    }

    public TextItem(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public TextItem(String text, String url, AnchorTarget target) {
        this.text = text;
        this.url = url;
        this.urlTarget = target.toString();
    }

    @Override
    public String getFragmentName() {
        return "item-text";
    }

}
