package com.qxcmp.framework.web.model.navigation;

import com.qxcmp.framework.view.component.AnchorTarget;

public class Navigation extends AbstractNavigation {
    public Navigation(String id, String title) {
        super(id, title);
    }

    public Navigation(String id, String title, String url) {
        super(id, title, url);
    }

    public Navigation(String id, String title, String url, AnchorTarget target) {
        super(id, title, url, target);
    }
}
