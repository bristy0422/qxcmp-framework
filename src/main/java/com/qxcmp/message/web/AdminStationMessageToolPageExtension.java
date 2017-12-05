package com.qxcmp.message.web;

import com.qxcmp.web.extension.AdminToolPageExtension;
import org.springframework.stereotype.Component;

import static com.qxcmp.core.QxcmpConfiguration.QXCMP_BACKEND_URL;

/**
 * @author Aaric
 */
@Component
public class AdminStationMessageToolPageExtension implements AdminToolPageExtension {
    @Override
    public String getIcon() {
        return "";
    }

    @Override
    public String getTitle() {
        return "站内信";
    }

    @Override
    public String getUrl() {
        return QXCMP_BACKEND_URL + "/inner/message";
    }
}
