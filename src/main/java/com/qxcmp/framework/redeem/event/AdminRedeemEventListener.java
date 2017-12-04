package com.qxcmp.framework.redeem.event;

import com.qxcmp.framework.config.SiteService;
import com.qxcmp.framework.message.MessageService;
import com.qxcmp.framework.user.User;
import com.qxcmp.framework.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.qxcmp.framework.core.QxcmpSecurityConfiguration.PRIVILEGE_ADMIN_REDEEM;

/**
 * @author Aaric
 */
@Component
@RequiredArgsConstructor
public class AdminRedeemEventListener {

    private final MessageService messageService;
    private final UserService userService;
    private final SiteService siteService;

    @EventListener
    public void onGenerateEvent(AdminRedeemGenerateEvent event) {
        User target = event.getTarget();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_ADMIN_REDEEM);
        feedUsers.add(target);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s 生成了%d个 <a href='https://%s/admin/redeem'>兑换码</a>",
                        target.getDisplayName(),
                        event.getCount(),
                        siteService.getDomain()),
                "业务名称：" + event.getName()
        );
    }

    @EventListener
    public void onSettingsEvent(AdminRedeemSettingsEvent event) {
        User target = event.getTarget();
        List<User> feedUsers = userService.findByAuthority(PRIVILEGE_ADMIN_REDEEM);
        feedUsers.add(target);

        messageService.feed(feedUsers.stream().map(User::getId).collect(Collectors.toList()), target,
                String.format("%s 修改了 <a href='https://%s/admin/redeem/settings'>兑换码配置</a>",
                        target.getDisplayName(),
                        siteService.getDomain()));
    }
}
