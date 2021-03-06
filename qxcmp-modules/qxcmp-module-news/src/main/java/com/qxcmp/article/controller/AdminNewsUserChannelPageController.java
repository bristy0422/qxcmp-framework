package com.qxcmp.article.controller;

import com.google.common.collect.ImmutableSet;
import com.qxcmp.article.Article;
import com.qxcmp.article.ArticleStatus;
import com.qxcmp.article.Channel;
import com.qxcmp.article.form.AdminNewsUserChannelAdminEditForm;
import com.qxcmp.article.form.AdminNewsUserChannelOwnerEditForm;
import com.qxcmp.article.page.*;
import com.qxcmp.user.User;
import com.qxcmp.web.model.RestfulResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

import static com.qxcmp.article.NewsModule.ADMIN_NEWS_URL;
import static com.qxcmp.article.NewsModuleSecurity.PRIVILEGE_NEWS;

/**
 * @author Aaric
 */
@Controller
@RequestMapping(ADMIN_NEWS_URL + "/user/channel")
@RequiredArgsConstructor
public class AdminNewsUserChannelPageController extends AbstractNewsPageController {

    @GetMapping("")
    public ModelAndView userChannelPage(Pageable pageable) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        Page<Channel> channels = channelService.findByUser(user, pageable);
        return page(AdminUserChannelTablePage.class, channels);
    }

    @GetMapping("/{id}/details")
    public ModelAndView userChannelDetailsPage(@PathVariable Long id) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return channelService.findOne(id)
                .filter(channel -> channel.getOwner().equals(user) || channel.getAdmins().contains(user))
                .map(channel -> entityDetailsPage(AdminUserChannelDetailsPage.class, id, channelService))
                .orElse(overviewPage(viewHelper.nextWarningOverview("栏目不存在").addLink("返回", ADMIN_NEWS_URL + "/user/channel")));
    }

    @GetMapping("/{id}/edit")
    public ModelAndView userChannelEditPage(@PathVariable Long id) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return channelService.findOne(id)
                .filter(channel -> channel.getOwner().equals(user) || channel.getAdmins().contains(user))
                .map(channel -> {
                    Object form;
                    if (StringUtils.equals(channel.getOwner().getId(), user.getId())) {
                        form = new AdminNewsUserChannelOwnerEditForm();
                        channelService.mergeToObject(channel, form);
                    } else {
                        form = new AdminNewsUserChannelAdminEditForm();
                        channelService.mergeToObject(channel, form);
                    }
                    return page(AdminUserChannelEditPage.class, form, null)
                            .addObject(form)
                            .addObject("selection_items_owner", userService.findByAuthority(PRIVILEGE_NEWS))
                            .addObject("selection_items_admins", userService.findByAuthority(PRIVILEGE_NEWS));
                })
                .orElse(overviewPage(viewHelper.nextWarningOverview("栏目不存在").addLink("返回", ADMIN_NEWS_URL + "/user/channel")));
    }

    @PostMapping("/owner/edit")
    public ModelAndView userChannelOwnerPage(@Valid final AdminNewsUserChannelOwnerEditForm form) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return channelService.findOne(form.getId())
                .filter(channel -> channel.getOwner().equals(user) || channel.getAdmins().contains(user))
                .map(channel -> updateEntity(form.getId(), channelService, form, overview -> overview.addLink("返回", ADMIN_NEWS_URL + "/user/channel")))
                .orElse(overviewPage(viewHelper.nextWarningOverview("栏目不存在").addLink("返回", ADMIN_NEWS_URL + "/user/channel")));
    }

    @PostMapping("/admin/edit")
    public ModelAndView userChannelAdminPage(@Valid final AdminNewsUserChannelAdminEditForm form) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return channelService.findOne(form.getId())
                .filter(channel -> channel.getOwner().equals(user) || channel.getAdmins().contains(user))
                .map(channel -> updateEntity(form.getId(), channelService, form, overview -> overview.addLink("返回", ADMIN_NEWS_URL + "/user/channel")))
                .orElse(overviewPage(viewHelper.nextWarningOverview("栏目不存在").addLink("返回", ADMIN_NEWS_URL + "/user/channel")));
    }

    @GetMapping("/{id}/article")
    public ModelAndView userChannelArticlePage(@PathVariable Long id, Pageable pageable) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        return channelService.findOne(id)
                .filter(channel -> channel.getOwner().equals(user) || channel.getAdmins().contains(user))
                .map(channel -> {
                    Page<Article> articles = articleService.findByChannelsAndStatuses(ImmutableSet.of(channel), ImmutableSet.of(ArticleStatus.PUBLISHED, ArticleStatus.DISABLED), pageable);
                    return page(AdminUserChannelArticlePage.class, articles);
                }).orElse(overviewPage(viewHelper.nextWarningOverview("栏目不存在").addLink("返回", ADMIN_NEWS_URL + "/user/channel")));
    }

    @GetMapping("/{id}/article/{articleId}/preview")
    public ModelAndView userChannelArticlePreviewPage(@PathVariable Long id, @PathVariable Long articleId) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        List<Channel> channels = channelService.findByUser(user);
        return channelService.findOne(id)
                .filter(channels::contains)
                .map(channel -> articleService.findOne(articleId)
                        .filter(article -> article.getChannels().contains(channel))
                        .map(article -> page(AdminUserChannelArticleDetailsPage.class, article)).orElse(overviewPage(viewHelper.nextWarningOverview("文章不存在").addLink("返回", ADMIN_NEWS_URL + "/user/channel")))
                ).orElse(overviewPage(viewHelper.nextWarningOverview("栏目不存在").addLink("返回", ADMIN_NEWS_URL + "/user/channel")));
    }

    @PostMapping("/{id}/article/{articleId}/disable")
    public ResponseEntity<RestfulResponse> userChannelArticleDisable(@PathVariable Long id, @PathVariable Long articleId) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        List<Channel> channels = channelService.findByUser(user);
        return channelService.findOne(id)
                .filter(channels::contains)
                .map(channel -> disableArticle(articleId, false))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }

    @PostMapping("/{id}/article/{articleId}/enable")
    public ResponseEntity<RestfulResponse> userChannelArticleEnable(@PathVariable Long id, @PathVariable Long articleId) {
        User user = currentUser().orElseThrow(RuntimeException::new);
        List<Channel> channels = channelService.findByUser(user);
        return channelService.findOne(id)
                .filter(channels::contains)
                .map(channel -> enableArticle(articleId, false))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestfulResponse.builder().status(HttpStatus.NOT_FOUND.value()).build()));
    }
}
