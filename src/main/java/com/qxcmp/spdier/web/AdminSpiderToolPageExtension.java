package com.qxcmp.spdier.web;

import com.qxcmp.web.extension.AdminToolPageExtension;
import org.springframework.stereotype.Component;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

/**
 * @author Aaric
 */
@Component
public class AdminSpiderToolPageExtension implements AdminToolPageExtension {
    @Override
    public String getIcon() {
        return "";
    }

    @Override
    public String getTitle() {
        return "蜘蛛管理";
    }

    @Override
    public String getUrl() {
        return QXCMP_BACKEND_URL + "/spider";
    }
}
