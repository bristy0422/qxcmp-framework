package com.qxcmp.framework.user.event;

import com.qxcmp.framework.config.SiteService;
import com.qxcmp.framework.message.MessageService;
import com.qxcmp.framework.user.User;
import com.qxcmp.framework.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.qxcmp.framework.core.QxcmpSecurityConfiguration.PRIVILEGE_USER_ROLE;

/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class AdminUserEventListener {

    private final MessageService messageService;
    private final UserService userService;
    private final SiteService siteService;

    @EventListener
    public void onUserRoleEditEvent(AdminUserRoleEditEvent event) {
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_USER_ROLE);
        feedUsers.add(event.getSource());
        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), event.getSource(),
                String.format("%s 修改了用户 <a href='https://%s/admin/user/%s/role'>%s</a> 角色",
                        event.getSource().getDisplayName(),
                        siteService.getDomain(),
                        event.getTarget().getId(),
                        event.getTarget().getDisplayName()));
    }

    @EventListener
    public void onUserStatusEditEvent(AdminUserStatusEditEvent event) {
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_USER_ROLE);
        feedUsers.add(event.getSource());
        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), event.getSource(),
                String.format("%s 修改了用户 <a href='https://%s/admin/user/%s/status'>%s</a> 状态",
                        event.getSource().getDisplayName(),
                        siteService.getDomain(),
                        event.getTarget().getId(),
                        event.getTarget().getDisplayName()));
    }
}
