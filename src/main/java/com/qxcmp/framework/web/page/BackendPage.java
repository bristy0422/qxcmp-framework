package com.qxcmp.framework.web.page;

import com.qxcmp.framework.config.SiteService;
import com.qxcmp.framework.user.User;
import com.qxcmp.framework.user.UserService;
import com.qxcmp.framework.web.model.navigation.NavigationService;
import com.qxcmp.framework.web.view.Component;
import com.qxcmp.framework.web.view.elements.breadcrumb.AbstractBreadcrumb;
import com.qxcmp.framework.web.view.elements.breadcrumb.Breadcrumb;
import com.qxcmp.framework.web.view.elements.breadcrumb.BreadcrumbItem;
import com.qxcmp.framework.web.view.elements.container.Container;
import com.qxcmp.framework.web.view.elements.grid.AbstractGrid;
import com.qxcmp.framework.web.view.elements.grid.Col;
import com.qxcmp.framework.web.view.elements.grid.Row;
import com.qxcmp.framework.web.view.elements.grid.VerticallyDividedGrid;
import com.qxcmp.framework.web.view.elements.label.AbstractLabel;
import com.qxcmp.framework.web.view.elements.label.Label;
import com.qxcmp.framework.web.view.elements.menu.Menu;
import com.qxcmp.framework.web.view.elements.menu.RightMenu;
import com.qxcmp.framework.web.view.elements.menu.VerticalMenu;
import com.qxcmp.framework.web.view.elements.menu.VerticalSubMenu;
import com.qxcmp.framework.web.view.elements.menu.item.*;
import com.qxcmp.framework.web.view.modules.accordion.AccordionItem;
import com.qxcmp.framework.web.view.modules.sidebar.AbstractSidebar;
import com.qxcmp.framework.web.view.modules.sidebar.AccordionMenuSidebar;
import com.qxcmp.framework.web.view.support.Color;
import com.qxcmp.framework.web.view.support.Fixed;
import com.qxcmp.framework.web.view.support.Wide;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.qxcmp.framework.core.QXCMPConfiguration.QXCMP_BACKEND_URL;
import static com.qxcmp.framework.core.QXCMPNavigationConfiguration.NAVIGATION_ADMIN_PROFILE;
import static com.qxcmp.framework.core.QXCMPNavigationConfiguration.NAVIGATION_ADMIN_SIDEBAR;

/**
 * 后端页面
 *
 * @author Aaric
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BackendPage extends AbstractPage {

    private AbstractSidebar sidebar = new AccordionMenuSidebar().setAttachEventsSelector(".ui.bottom.fixed.menu .sidebar.item");
    private Col mainContent = new Col();
    private AbstractBreadcrumb breadcrumb;
    private VerticalMenu verticalMenu;

    private UserService userService;
    private SiteService siteService;
    private NavigationService navigationService;

    public BackendPage(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public AbstractPage addComponent(Supplier<Component> supplier) {
        Component component = checkNotNull(supplier.get(), "Component is null");
        mainContent.addComponent(component);
        return this;
    }

    @Override
    public AbstractPage addComponent(Component component) {
        mainContent.addComponent(component);
        return this;
    }

    @Override
    public AbstractPage addComponents(Collection<? extends Component> components) {
        mainContent.addComponents(components);
        return this;
    }

    @Override
    public AbstractPage setBreadcrumb(String... breadcrumb) {
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

    @Override
    public AbstractPage setVerticalMenu(List<String> menus) {
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

    @Override
    public AbstractPage setVerticalNavigation(String id, String activeId) {

        VerticalMenu verticalMenu = new VerticalMenu().setFluid();
        verticalMenu.setTabular();

        navigationService.get(id).getItems().forEach(navigation -> {
            if (navigation.isVisible(userService.currentUser())) {
                if (navigation.getItems().isEmpty()) {
                    TextItem textItem = new TextItem(navigation.getTitle(), navigation.getAnchor().getHref());

                    if (StringUtils.equals(activeId, navigation.getId())) {
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

    @Override
    public AbstractPage setVerticalNavigationBadge(String id, String text) {
        return setVerticalNavigationBadge(id, text, Color.NONE);
    }

    @Override
    public AbstractPage setVerticalNavigationBadge(String id, String text, Color color) {
        return setVerticalNavigationBadge(id, new Label(text).setColor(color));
    }

    @Override
    public AbstractPage setVerticalNavigationBadge(String id, AbstractLabel label) {

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

    @Override
    public ModelAndView build() {

        buildSidebarMenu();

        buildPageContent();

        super.addComponent(sidebar);

        return super.build();
    }

    private void buildSidebarMenu() {
        final User user = userService.currentUser();

        buildSidebarTopFixedMenu(user);

        buildSidebarBottomFixedMenu();

        buildSidebarContent(user);
    }

    private void buildSidebarTopFixedMenu(User user) {
        final Menu menu = new Menu();
        menu.setInverted().setFixed(Fixed.TOP);
        menu.addItem(new LogoImageItem(siteService.getLogo(), siteService.getTitle()));
        RightMenu rightMenu = new RightMenu();
        rightMenu.addItem(new BackendAccountMenuItem(user, navigationService.get(NAVIGATION_ADMIN_PROFILE).getItems()));
        menu.setRightMenu(rightMenu);
        sidebar.setTopFixedMenu(menu);
    }

    private void buildSidebarBottomFixedMenu() {
        final Menu menu = new Menu();
        menu.setInverted().setFixed(Fixed.BOTTOM);
        menu.addItem(new SidebarIconItem());
        RightMenu rightMenu = new RightMenu();
        rightMenu.addItem(new TextItem("关于", QXCMP_BACKEND_URL + "/about"));
        menu.setRightMenu(rightMenu);
        sidebar.setBottomFixedMenu(menu);
    }

    private void buildSidebarContent(User user) {
        navigationService.get(NAVIGATION_ADMIN_SIDEBAR).getItems().stream()
                .filter(navigation -> navigation.isVisible(user))
                .forEach(navigation -> {
                    if (navigation.getItems().isEmpty()) {
                        if (Objects.nonNull(navigation.getIcon())) {
                            sidebar.addSideContent(new LabeledIconItem(navigation.getTitle(), navigation.getIcon(), navigation.getAnchor()));
                        } else {
                            sidebar.addSideContent(new TextItem(navigation.getTitle(), navigation.getAnchor().getHref()).setLink());
                        }
                    } else {
                        if (navigation.getItems().stream().anyMatch(n -> n.isVisible(user))) {

                            VerticalSubMenu verticalMenu = new VerticalSubMenu();

                            navigation.getItems().forEach(item -> {
                                if (item.isVisible(user)) {
                                    verticalMenu.addItem(new TextItem(item.getTitle(), item.getAnchor().getHref()));
                                }
                            });

                            AccordionItem accordionItem = new AccordionItem();
                            accordionItem.setTitle(navigation.getTitle());
                            accordionItem.setContent(verticalMenu);

                            sidebar.addSideContent(new AccordionMenuItem(accordionItem).setLink());
                        }
                    }
                });
    }

    private void buildPageContent() {
        final Container container = new Container();
        final AbstractGrid grid = new VerticallyDividedGrid().setVerticallyPadded();
        final Row contentRow = new Row();

        if (Objects.nonNull(breadcrumb)) {
            grid.addItem(new Row().addCol(new Col(Wide.SIXTEEN).addComponent(breadcrumb)));
        }

        if (Objects.nonNull(verticalMenu)) {
            contentRow.addCol(new Col().setComputerWide(Wide.THREE).setMobileWide(Wide.SIXTEEN).addComponent(verticalMenu));
            contentRow.addCol(mainContent.setComputerWide(Wide.THIRTEEN).setMobileWide(Wide.SIXTEEN));
        } else {
            contentRow.addCol(mainContent.setGeneralWide(Wide.SIXTEEN));
        }

        grid.addItem(contentRow);
        container.addComponent(grid);
        sidebar.addContent(container);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    @Autowired
    public void setNavigationService(NavigationService navigationService) {
        this.navigationService = navigationService;
    }
}
