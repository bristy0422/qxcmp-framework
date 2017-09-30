package com.qxcmp.framework.news.web;

import com.qxcmp.framework.audit.ActionException;
import com.qxcmp.framework.news.Channel;
import com.qxcmp.framework.news.ChannelService;
import com.qxcmp.framework.user.User;
import com.qxcmp.framework.web.QXCMPBackendController;
import com.qxcmp.framework.web.model.RestfulResponse;
import com.qxcmp.framework.web.view.elements.header.IconHeader;
import com.qxcmp.framework.web.view.elements.html.HtmlText;
import com.qxcmp.framework.web.view.elements.icon.Icon;
import com.qxcmp.framework.web.view.elements.segment.Segment;
import com.qxcmp.framework.web.view.support.Alignment;
import com.qxcmp.framework.web.view.views.Overview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static com.qxcmp.framework.core.QXCMPConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.framework.news.web.AdminNewsPageController.getVerticalMenu;

@Controller
@RequestMapping(QXCMP_BACKEND_URL + "/news/channel")
@RequiredArgsConstructor
public class AdminNewsChannelPageController extends QXCMPBackendController {

    private final ChannelService channelService;

    @GetMapping("")
    public ModelAndView newsChannelPage(Pageable pageable) {
        return page().addComponent(convertToTable(pageable, channelService))
                .setBreadcrumb("控制台", QXCMP_BACKEND_URL, "新闻管理", QXCMP_BACKEND_URL + "/news", "栏目管理")
                .setVerticalMenu(getVerticalMenu("栏目管理"))
                .build();
    }

    @GetMapping("/{id}/preview")
    public ModelAndView newsChannelPreviewPage(@PathVariable String id) {
        return channelService.findOne(id).map(channel -> page().addComponent(new Overview(channel.getName()).setAlignment(Alignment.CENTER)
                .addComponent(new HtmlText(channel.getContent()))
                .addLink("返回", QXCMP_BACKEND_URL + "/news/channel"))
                .setBreadcrumb("控制台", QXCMP_BACKEND_URL, "新闻管理", QXCMP_BACKEND_URL + "/news", "栏目管理", QXCMP_BACKEND_URL + "/news/channel", "栏目预览")
                .setVerticalMenu(getVerticalMenu("栏目管理"))
                .build()).orElse(overviewPage(new Overview(new IconHeader("栏目不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/channel")).build());
    }

    @GetMapping("/new")
    public ModelAndView newsChannelNewPage(final AdminNewsChannelNewForm form) {

        User user = currentUser().orElseThrow(RuntimeException::new);

        form.setOwner(user);

        return page().addComponent(new Segment().addComponent(convertToForm(form)))
                .setBreadcrumb("控制台", QXCMP_BACKEND_URL, "新闻管理", QXCMP_BACKEND_URL + "/news", "栏目管理", QXCMP_BACKEND_URL + "/news/channel", "新建栏目")
                .setVerticalMenu(getVerticalMenu("栏目管理"))
                .addObject("selection_items_owner", userService.findAll())
                .addObject("selection_items_admins", userService.findAll())
                .build();
    }

    @PostMapping("/new")
    public ModelAndView newsChannelNewPage(@Valid final AdminNewsChannelNewForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", QXCMP_BACKEND_URL, "新闻管理", QXCMP_BACKEND_URL + "/news", "栏目管理", QXCMP_BACKEND_URL + "/news/channel", "新建栏目")
                    .setVerticalMenu(getVerticalMenu("栏目管理"))
                    .addObject("selection_items_owner", userService.findAll())
                    .addObject("selection_items_admins", userService.findAll())
                    .build();
        }

        return submitForm(form, context -> {
            try {
                Channel channel = channelService.next();
                channel.setCover(form.getCover());
                channel.setName(form.getName());
                channel.setDescription(form.getDescription());
                channel.setOwner(form.getOwner());
                channel.setAdmins(form.getAdmins());
                channel.setContent(form.getContent());
                channel.setContentQuill(form.getContentQuill());
                channelService.create(() -> channel);
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        }, (stringObjectMap, overview) -> overview.addLink("返回栏目管理", QXCMP_BACKEND_URL + "/news/channel").addLink("继续新建栏目", QXCMP_BACKEND_URL + "/news/channel/new"));
    }

    @GetMapping("/{id}/edit")
    public ModelAndView newsChannelEditPage(@PathVariable String id, @Valid final AdminNewsChannelEditForm form) {
        return channelService.findOne(id).map(channel -> {
            form.setCover(channel.getCover());
            form.setName(channel.getName());
            form.setDescription(channel.getDescription());
            form.setOwner(channel.getOwner());
            form.setAdmins(channel.getAdmins());
            form.setContent(channel.getContent());
            form.setContentQuill(channel.getContentQuill());
            return page().addComponent(new Segment().addComponent(convertToForm(form)))
                    .setBreadcrumb("控制台", QXCMP_BACKEND_URL, "新闻管理", QXCMP_BACKEND_URL + "/news", "栏目管理", QXCMP_BACKEND_URL + "/news/channel", "编辑栏目")
                    .setVerticalMenu(getVerticalMenu("栏目管理"))
                    .addObject("selection_items_owner", userService.findAll())
                    .addObject("selection_items_admins", userService.findAll())
                    .build();
        }).orElse(overviewPage(new Overview(new IconHeader("栏目不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/channel")).build());
    }

    @PostMapping("/{id}/edit")
    public ModelAndView newsChannelEditPage(@PathVariable String id, final AdminNewsChannelEditForm form, BindingResult bindingResult) {
        return channelService.findOne(id).map(channel -> {

            if (bindingResult.hasErrors()) {
                return page().addComponent(new Segment().addComponent(convertToForm(form).setErrorMessage(convertToErrorMessage(bindingResult, form))))
                        .setBreadcrumb("控制台", QXCMP_BACKEND_URL, "新闻管理", QXCMP_BACKEND_URL + "/news", "栏目管理", QXCMP_BACKEND_URL + "/news/channel", "编辑栏目")
                        .setVerticalMenu(getVerticalMenu("栏目管理"))
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
            }, (stringObjectMap, overview) -> overview.addLink("返回", QXCMP_BACKEND_URL + "/news/channel"));
        }).orElse(overviewPage(new Overview(new IconHeader("栏目不存在", new Icon("warning circle"))).addLink("返回", QXCMP_BACKEND_URL + "/news/channel")).build());
    }

    @PostMapping("/{id}/remove")
    public ResponseEntity<RestfulResponse> newsChannelRemove(@PathVariable String id) {
        RestfulResponse restfulResponse = audit("删除栏目", context -> {
            try {
                channelService.remove(Long.parseLong(id));
            } catch (Exception e) {
                throw new ActionException(e.getMessage(), e);
            }
        });
        return ResponseEntity.status(restfulResponse.getStatus()).body(restfulResponse);
    }
}
