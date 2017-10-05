package com.qxcmp.framework.web.page;

import com.qxcmp.framework.user.User;
import com.qxcmp.framework.web.model.navigation.NavigationService;
import com.qxcmp.framework.web.view.Component;
import com.qxcmp.framework.web.view.elements.breadcrumb.AbstractBreadcrumb;
import com.qxcmp.framework.web.view.elements.breadcrumb.Breadcrumb;
import com.qxcmp.framework.web.view.elements.breadcrumb.BreadcrumbItem;
import com.qxcmp.framework.web.view.elements.grid.AbstractGrid;
import com.qxcmp.framework.web.view.elements.grid.Col;
import com.qxcmp.framework.web.view.elements.grid.Row;
import com.qxcmp.framework.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.framework.web.view.elements.label.AbstractLabel;
import com.qxcmp.framework.web.view.elements.label.Label;
import com.qxcmp.framework.web.view.elements.menu.AbstractMenu;
import com.qxcmp.framework.web.view.elements.menu.VerticalMenu;
import com.qxcmp.framework.web.view.elements.menu.item.TextItem;
import com.qxcmp.framework.web.view.modules.sidebar.AbstractSidebar;
import com.qxcmp.framework.web.view.modules.sidebar.AccordionMenuSidebar;
import com.qxcmp.framework.web.view.modules.sidebar.SidebarConfig;
import com.qxcmp.framework.web.view.support.Color;
import com.qxcmp.framework.web.view.support.Wide;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.qxcmp.framework.core.QXCMPConfiguration.QXCMP_BACKEND_URL;

/**
 * 后端页面
 *
 * @author Aaric
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BackendPage extends AbstractPage {

    private final NavigationService navigationService;

    private final User user;

    private AbstractSidebar sidebar = new AccordionMenuSidebar().setAttachEventsSelector(".ui.bottom.fixed.menu .sidebar.item").setConfig(SidebarConfig.builder().dimPage(false).build());

    /**
     * 页面容器
     */
    private Col container = new Col();

    /**
     * 面包屑 - 可选
     */
    private AbstractBreadcrumb breadcrumb;

    /**
     * 垂直菜单 - 可选
     */
    private VerticalMenu verticalMenu;

    public BackendPage(HttpServletRequest request, HttpServletResponse response, NavigationService navigationService, User user) {
        super(request, response);
        this.navigationService = navigationService;
        this.user = user;
    }

    @Override
    public BackendPage addComponent(Component component) {
        container.addComponent(component);
        return this;
    }

    @Override
    public BackendPage addComponents(Collection<? extends Component> components) {
        container.addComponents(components);
        return this;
    }

    public BackendPage setTopMenu(AbstractMenu menu) {
        sidebar.setTopFixedMenu(menu);
        return this;
    }

    public BackendPage setBottomMenu(AbstractMenu menu) {
        sidebar.setBottomFixedMenu(menu);
        return this;
    }

    public BackendPage addSideContent(Component content) {
        sidebar.addSideContent(content);
        return this;
    }

    public BackendPage setBreadcrumb(AbstractBreadcrumb breadcrumb) {
        this.breadcrumb = breadcrumb;
        return this;
    }

    /**
     * 增加面包屑
     *
     * @param breadcrumb 必须为单数，依次为 名字 - Url 组合， 最后一个参数为没有导航的项目
     *
     * @return
     */
    public BackendPage setBreadcrumb(String... breadcrumb) {
        checkArgument(breadcrumb.length % 2 == 1);

        Breadcrumb bc = new Breadcrumb();

        for (int i = 0; i < breadcrumb.length; i += 2) {

            String text = breadcrumb[i];

            if (i + 1 == breadcrumb.length) {
                bc.addItem(new BreadcrumbItem(text));
            } else {
                String url = breadcrumb[i + 1];
                if (Objects.nonNull(url)) {
                    bc.addItem(new BreadcrumbItem(text, QXCMP_BACKEND_URL + "/" + url));
                } else {
                    bc.addItem(new BreadcrumbItem(text));
                }
            }
        }

        this.breadcrumb = bc;
        return this;
    }

    public BackendPage setVerticalMenu(VerticalMenu verticalMenu) {
        this.verticalMenu = verticalMenu;
        return this;
    }

    public BackendPage setVerticalMenu(List<String> menus) {
        checkArgument(!menus.isEmpty());
        String activeItem = menus.get(0);

        VerticalMenu verticalMenu = new VerticalMenu().setFluid();
        verticalMenu.setTabular();

        for (int i = 1; i < menus.size(); i += 2) {
            TextItem textItem = new TextItem(menus.get(i), menus.get(i + 1));

            if (textItem.getText().equals(activeItem)) {
                textItem.setActive();
            }

            verticalMenu.addItem(textItem);
        }

        this.verticalMenu = verticalMenu;

        return this;
    }

    /**
     * 从已经定义的导航中获取垂直菜单
     * <p>
     * 垂直菜单不支持子菜单
     *
     * @param id         导航ID
     * @param activeItem 当前激活的菜单项ID
     *
     * @return
     */
    public BackendPage setVerticalNavigation(String id, String activeItem) {

        VerticalMenu verticalMenu = new VerticalMenu().setFluid();
        verticalMenu.setTabular();

        navigationService.get(id).getItems().forEach(navigation -> {
            if (navigation.isVisible(user)) {
                if (navigation.getItems().isEmpty()) {
                    TextItem textItem = new TextItem(navigation.getTitle(), navigation.getAnchor().getHref());

                    if (StringUtils.equals(activeItem, navigation.getId())) {
                        textItem.setActive();
                    }

                    textItem.addContext("navigation-id", navigation.getId());

                    verticalMenu.addItem(textItem);
                }
            }
        });

        if (!verticalMenu.getItems().isEmpty()) {
            this.verticalMenu = verticalMenu;
        }

        return this;
    }

    /**
     * 为垂直菜单项增加徽章
     *
     * @param id    导航栏ID
     * @param label 徽章
     *
     * @return
     */
    public BackendPage setVerticalNavigationBadge(String id, AbstractLabel label) {

        if (Objects.nonNull(verticalMenu)) {
            verticalMenu.getItems().forEach(menuItem -> {
                if (Objects.nonNull(menuItem.getContext("navigation-id")) && StringUtils.equals(menuItem.getContext("navigation-id").toString(), id)) {
                    if (menuItem instanceof TextItem) {
                        TextItem textItem = (TextItem) menuItem;
                        textItem.setBadge(label);
                    }
                }
            });
        }

        return this;
    }

    public BackendPage setVerticalNavigationBadge(String id, String text) {
        return setVerticalNavigationBadge(id, text, Color.NONE);
    }


    public BackendPage setVerticalNavigationBadge(String id, String text, Color color) {
        return setVerticalNavigationBadge(id, new Label(text).setColor(color));
    }

    @Override
    public ModelAndView build() {
        final AbstractGrid grid = new VerticallyDividedGrid().setContainer().setVerticallyPadded();
        final Row row = new Row();

        if (Objects.nonNull(breadcrumb)) {
            grid.addItem(new Row().addCol(new Col().setGeneralWide(Wide.SIXTEEN).addComponent(breadcrumb)));
        }

        if (Objects.nonNull(verticalMenu)) {
            row.addCol(new Col().setComputerWide(Wide.THREE).setMobileWide(Wide.SIXTEEN).addComponent(verticalMenu));
            row.addCol(container.setComputerWide(Wide.THIRTEEN).setMobileWide(Wide.SIXTEEN));
        } else {
            row.addCol(container.setGeneralWide(Wide.SIXTEEN));
        }

        grid.addItem(row);

        super.addComponent(sidebar.addContent(grid));
        return super.build();
    }
}