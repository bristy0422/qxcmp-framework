package com.qxcmp.framework.news.web;

import com.google.common.collect.ImmutableSet;
import com.qxcmp.framework.audit.ActionException;
import com.qxcmp.framework.news.*;
import com.qxcmp.framework.user.User;
import com.qxcmp.framework.web.QXCMPBackendController;
import com.qxcmp.framework.web.model.RestfulResponse;
import com.qxcmp.framework.web.view.Component;
import com.qxcmp.framework.web.view.elements.grid.AbstractGrid;
import com.qxcmp.framework.web.view.elements.grid.Col;
import com.qxcmp.framework.web.view.elements.grid.Row;
import com.qxcmp.framework.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.framework.web.view.elements.header.IconHeader;
import com.qxcmp.framework.web.view.elements.html.HtmlText;
import com.qxcmp.framework.web.view.elements.icon.Icon;
import com.qxcmp.framework.web.view.elements.image.Image;
import com.qxcmp.framework.web.view.elements.segment.Segment;
import com.qxcmp.framework.web.view.support.Alignment;
import com.qxcmp.framework.web.view.support.Wide;
import com.qxcmp.framework.web.view.support.utils.TableHelper;
import com.qxcmp.framework.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static com.qxcmp.framework.core.QXCMPConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.framework.core.QXCMPNavigationConfiguration.NAVIGATION_ADMIN_NEWS;
import static com.qxcmp.framework.core.QXCMPNavigationConfiguration.NAVIGATION_ADMIN_NEWS_USER_CHANNEL;

@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/news/user/channel")
@RequiredArgsConstructor
public class AdminNewsUserChannelPageController extends QXCMPBackendController {

    private final ChannelService channelService;

    private final ArticleService articleService;

    private final TableHelper tableHelper;

    private final AdminNewsPageHelper adminNewsPageHelper;

    @GetMapping("")
    public ModelAndView userChannelPage(Pageable pageable) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        Page<Channel> channels = channelService.findByUser(user, pageable);

        return page().addComponent(tableHelper.convert("user", Channel.class, channels))
                .setBreadcrumb("控制台", "", "新闻管理", "news", "我的栏目")
                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS, NAVIGATION_ADMIN_NEWS_USER_CHANNEL)
                .build();
    }

    @GetMapping("/{id}/details")
    public ModelAndView userChannelDetailsPage(@PathVariable String id) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Channel> channels = channelService.findByUser(user);

        return channelService.findOne(id)
                .filter(channels::contains)
                .map(channel -> page().addComponent(new Overview(channel.getName()).setAlignment(Alignment.CENTER)
                        .addComponent(new HtmlText(channel.getContent()))
                        .addLink("返回", QXCMP_BACKEND_URL + "/news/user/channel"))
                        .setBreadcrumb("控制台", "", "新闻管理", "news", "我的栏目", "news/user/channel", "栏目查看")
                        .setVerticalNavigation(NAVIGATION_ADMIN_NEWS, NAVIGATION_ADMIN_NEWS_USER_CHANNEL)
                        .build()).orElse(overviewPage(new Overview(new IconHeader("栏目不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/user/channel")).build());
    }

    @GetMapping("/{id}/edit")
    public ModelAndView userChannelEditPage(@PathVariable String id) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Channel> channels = channelService.findByUser(user);

        return channelService.findOne(id)
                .filter(channels::contains)
                .map(channel -> {

                    Object form;

                    if (StringUtils.equals(channel.getOwner().getId(), user.getId())) {
                        form = new AdminNewsUserChannelOwnerEditForm();
                        AdminNewsUserChannelOwnerEditForm editForm = (AdminNewsUserChannelOwnerEditForm) form;
                        editForm.setId(channel.getId());
                        editForm.setCover(channel.getCover());
                        editForm.setName(channel.getName());
                        editForm.setDescription(channel.getDescription());
                        editForm.setOwner(channel.getOwner());
                        editForm.setAdmins(channel.getAdmins());
                        editForm.setContent(channel.getContent());
                        editForm.setContentQuill(channel.getContentQuill());
                    } else {
                        form = new AdminNewsUserChannelAdminEditForm();
                        AdminNewsUserChannelAdminEditForm editForm = (AdminNewsUserChannelAdminEditForm) form;
                        editForm.setId(channel.getId());
                        editForm.setCover(channel.getCover());
                        editForm.setName(channel.getName());
                        editForm.setDescription(channel.getDescription());
                        editForm.setAdmins(channel.getAdmins());
                        editForm.setContent(channel.getContent());
                        editForm.setContentQuill(channel.getContentQuill());
                    }

                    return page().addComponent(new Segment().addComponent(convertToForm(form)))
                            .setBreadcrumb("控制台", "", "新闻管理", "news", "我的栏目", "news/user/channel", "栏目编辑")
                            .setVerticalNavigation(NAVIGATION_ADMIN_NEWS, NAVIGATION_ADMIN_NEWS_USER_CHANNEL)
                            .addObject(form)
                            .addObject("selection_items_owner", userService.findAll())
                            .addObject("selection_items_admins", userService.findAll())
                            .build();
                }).orElse(overviewPage(new Overview(new IconHeader("栏目不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/user/channel")).build());
    }

    @PostMapping("/owner/edit")
    public ModelAndView userChannelOwnerPage(@Valid final AdminNewsUserChannelOwnerEditForm form, BindingResult bindingResult) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Channel> channels = channelService.findByOwner(user);

        return channelService.findOne(form.getId())
                .filter(channels::contains)
                .map(channel -> {

                    if (bindingResult.hasErrors()) {
                        return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                                .setBreadcrumb("控制台", "", "新闻管理", "news", "我的栏目", "news/user/channel", "栏目编辑")
                                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS, NAVIGATION_ADMIN_NEWS_USER_CHANNEL)
                                .addObject(form)
                                .addObject("selection_items_owner", userService.findAll())
                                .addObject("selection_items_admins", userService.findAll())
                                .build();
                    }

                    return submitForm(form, context -> {
                        try {
                            channelService.update(channel.getId(), c -> {
                                c.setCover(form.getCover());
                                c.setName(form.getName());
                                c.setDescription(form.getDescription());
                                c.setOwner(form.getOwner());
                                c.setAdmins(form.getAdmins());
                                c.setContent(form.getContent());
                                c.setContentQuill(form.getContentQuill());
                            });
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/news/user/channel"));
                }).orElse(overviewPage(new Overview(new IconHeader("栏目不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/user/channel")).build());
    }

    @PostMapping("/admin/edit")
    public ModelAndView userChannelAdminPage(@Valid final AdminNewsUserChannelAdminEditForm form, BindingResult bindingResult) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Channel> channels = channelService.findByAdmin(user);

        return channelService.findOne(form.getId())
                .filter(channels::contains)
                .map(channel -> {

                    if (bindingResult.hasErrors()) {
                        return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                                .setBreadcrumb("控制台", "", "新闻管理", "news", "我的栏目", "news/user/channel", "栏目编辑")
                                .setVerticalNavigation(NAVIGATION_ADMIN_NEWS, NAVIGATION_ADMIN_NEWS_USER_CHANNEL)
                                .addObject(form)
                                .addObject("selection_items_owner", userService.findAll())
                                .addObject("selection_items_admins", userService.findAll())
                                .build();
                    }

                    return submitForm(form, context -> {
                        try {
                            channelService.update(channel.getId(), c -> {
                                c.setCover(form.getCover());
                                c.setName(form.getName());
                                c.setDescription(form.getDescription());
                                c.setAdmins(form.getAdmins());
                                c.setContent(form.getContent());
                                c.setContentQuill(form.getContentQuill());
                            });
                        } catch (Exception e) {
                            throw new ActionException(e.getMessage(), e);
                        }
                    }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/news/user/channel"));
                }).orElse(overviewPage(new Overview(new IconHeader("栏目不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/user/channel")).build());
    }

    @GetMapping("/{id}/article")
    public ModelAndView userChannelArticlePage(@PathVariable String id, Pageable pageable) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Channel> channels = channelService.findByUser(user);

        return channelService.findOne(id)
                .filter(channels::contains)
                .map(channel -> {

                    Page<Article> articles = articleService.findByChannelsAndStatuses(ImmutableSet.of(channel), ImmutableSet.of(ArticleStatus.PUBLISHED, ArticleStatus.DISABLED), pageable);

                    return page().addComponent(tableHelper.convert("userChannel", String.format(QXCMP_BACKEND_URL + "/news/user/channel/%d/article", channel.getId()), Article.class, articles))
                            .setBreadcrumb("控制台", "", "新闻管理", "news", "我的栏目", "news/user/channel", channel.getName())
                            .setVerticalNavigation(NAVIGATION_ADMIN_NEWS, NAVIGATION_ADMIN_NEWS_USER_CHANNEL)
                            .build();
                }).orElse(overviewPage(new Overview(new IconHeader("栏目不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/user/channel")).build());
    }

    @GetMapping("/{id}/article/{articleId}/preview")
    public ModelAndView userChannelArticlePreviewPage(@PathVariable String id, @PathVariable String articleId) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Channel> channels = channelService.findByUser(user);

        return channelService.findOne(id)
                .filter(channels::contains)
                .map(channel -> articleService.findOne(articleId).filter(article -> article.getChannels().contains(channel)).map(article -> page().addComponent(new Overview(article.getTitle(), article.getAuthor()).setAlignment(Alignment.CENTER)
                        .addComponent(getArticlePreviewContent(article))
                        .addLink("我的栏目", QXCMP_BACKEND_URL + "/news/user/channel")
                        .addLink("栏目文章", String.format(QXCMP_BACKEND_URL + "/news/user/channel/%d/article", channel.getId())))
                        .setBreadcrumb("控制台", "", "新闻管理", "news", "我的栏目", "news/user/channel", channel.getName(), String.format("news/user/channel/%d/article", channel.getId()), "文章预览")
                        .setVerticalNavigation(NAVIGATION_ADMIN_NEWS, NAVIGATION_ADMIN_NEWS_USER_CHANNEL)
                        .build()).orElse(overviewPage(new Overview(new IconHeader("文章不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/user/channel")).build())
                ).orElse(overviewPage(new Overview(new IconHeader("栏目不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/user/channel")).build());
    }

    @PostMapping("/{id}/article/{articleId}/disable")
    public ResponseEntity<RestfulResponse> userChannelArticleDisable(@PathVariable String id, @PathVariable String articleId) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Channel> channels = channelService.findByUser(user);

        return channelService.findOne(id)
                .filter(channels::contains)
                .map(channel -> articleService.findOne(articleId).filter(article -> article.getChannels().contains(channel))
                        .filter(article -> article.getStatus().equals(ArticleStatus.PUBLISHED))
                        .map(article -> {
                            RestfulResponse restfulResponse = audit("禁用栏目文章", context -> {
                                try {
                                    articleService.update(article.getId(), a -> {
                                        a.setDatePublished(new Date());
                                        a.setStatus(ArticleStatus.DISABLED);
                                        a.setDisableUser(user.getId());
                                    });
                                } catch (Exception e) {
                                    throw new ActionException(e.getMessage(), e);
                                }
                            });
                            return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestfulResponse(HttpStatus.NOT_FOUND.value()))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestfulResponse(HttpStatus.NOT_FOUND.value())));
    }

    @PostMapping("/{id}/article/{articleId}/enable")
    public ResponseEntity<RestfulResponse> userChannelArticleEnable(@PathVariable String id, @PathVariable String articleId) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        List<Channel> channels = channelService.findByUser(user);

        return channelService.findOne(id)
                .filter(channels::contains)
                .map(channel -> articleService.findOne(articleId).filter(article -> article.getChannels().contains(channel))
                        .filter(article -> article.getStatus().equals(ArticleStatus.DISABLED))
                        .map(article -> {
                            RestfulResponse restfulResponse = audit("启用栏目文章", context -> {
                                try {
                                    articleService.update(article.getId(), a -> {
                                        a.setDatePublished(new Date());
                                        a.setStatus(ArticleStatus.PUBLISHED);
                                    });
                                } catch (Exception e) {
                                    throw new ActionException(e.getMessage(), e);
                                }
                            });
                            return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
                        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestfulResponse(HttpStatus.NOT_FOUND.value()))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestfulResponse(HttpStatus.NOT_FOUND.value())));
    }

    private Component getArticlePreviewContent(Article article) {
        final AbstractGrid grid = new VerticallyDividedGrid().setVerticallyPadded();
        grid.addItem(new Row()
                .addCol(new Col().setComputerWide(Wide.FOUR).setMobileWide(Wide.SIXTEEN).addComponent(new Image(article.getCover()).setCentered().setBordered().setRounded()))
                .addCol(new Col().setComputerWide(Wide.TWELVE).setMobileWide(Wide.SIXTEEN).addComponent(convertToTable(adminNewsPageHelper.getArticleInfoTable(article))))
        );
        grid.addItem(new Row().addCol(new Col().setGeneralWide(Wide.SIXTEEN).addComponent(new HtmlText(article.getContent()))));
        return grid;
    }
}
