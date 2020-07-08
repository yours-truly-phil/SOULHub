package io.horrorshow.soulhub.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import io.horrorshow.soulhub.security.SecurityUtils;
import io.horrorshow.soulhub.ui.components.AppUserNavBarComponent;
import io.horrorshow.soulhub.ui.components.LoginNavBarComponent;
import io.horrorshow.soulhub.ui.views.AboutView;
import io.horrorshow.soulhub.ui.views.AdminView;
import io.horrorshow.soulhub.ui.views.SOULPatchesView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PWA(name = "SOULHub - SOUL-Patch Web UI",
        shortName = "SOULHub-Web",
        description = "Serves SOULPatches (SOUL files)",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css",
        themeFor = "vaadin-text-field")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainLayout extends AppLayout {
    private static final long serialVersionUID = -4791247729805265577L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainLayout.class);

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        Image logo = new Image("img/Logo.svg", "SOULHub logo");
        logo.addClickListener(event -> UI.getCurrent().navigate(SOULPatchesView.class));

        logo.addClassName("logo");

        DrawerToggle drawerToggle = new DrawerToggle();

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setWidth("100%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);


        header.add(drawerToggle, logo);
        Div navbarCenterSpace = new Div();
        header.add(navbarCenterSpace);
        header.expand(navbarCenterSpace);

        HorizontalLayout appUserNavBar;
        if (SecurityUtils.isUserLoggedIn()) {
            appUserNavBar = new AppUserNavBarComponent(SecurityUtils.getUsername());
        } else {
            appUserNavBar = new LoginNavBarComponent();
        }
        header.add(appUserNavBar);

        addToNavbar(header);
    }

    private void createDrawer() {
        VerticalLayout drawerLayout = new VerticalLayout();

        RouterLink soulPatchesLink = new RouterLink("SOULPatches", SOULPatchesView.class);
        soulPatchesLink.addComponentAsFirst(VaadinIcon.CLUSTER.create());
        soulPatchesLink.setHighlightCondition(HighlightConditions.sameLocation());
        drawerLayout.add(soulPatchesLink);
        RouterLink aboutLink = new RouterLink("About", AboutView.class);
        aboutLink.setHighlightCondition(HighlightConditions.sameLocation());
        drawerLayout.add(aboutLink);

        if (SecurityUtils.isAccessGranted(AdminView.class)) {
            RouterLink adminLink = new RouterLink("Administration", AdminView.class);
            adminLink.setHighlightCondition(HighlightConditions.sameLocation());
            drawerLayout.add(adminLink);
        }

        addToDrawer(drawerLayout);
    }
}
