package com.qxcmp.article.page;

import com.google.common.collect.ImmutableList;
import com.qxcmp.article.Channel;
import com.qxcmp.web.view.elements.html.HtmlText;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;
import static com.qxcmp.article.NewsModuleNavigation.ADMIN_MENU_NEWS;
import static com.qxcmp.article.NewsModuleNavigation.ADMIN_MENU_NEWS_USER_CHANNEL;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author Aaric
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AdminUserChannelDetailsPage extends AbstractNewsPage {

    private final Channel channel;

    @Override
    public void render() {
        setMenu(ADMIN_MENU_NEWS, ADMIN_MENU_NEWS_USER_CHANNEL);
        addComponent(viewHelper.nextOverview(channel.getName())
                .addComponent(new HtmlText(channel.getContent()))
                .addLink("返回", ADMIN_NEWS_URL + "/user/channel")
        );
    }

    @Override
    protected List<String> getBreadcrumb() {
        return ImmutableList.of("新闻管理", ADMIN_NEWS_URL, "我的栏目", ADMIN_NEWS_URL + "/user/channel", "栏目预览");
    }
}
