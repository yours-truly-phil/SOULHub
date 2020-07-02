package io.horrorshow.soulhub.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
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

    HorizontalLayout appUserNavBar;

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
//        H1 logo = new H1("SOULHub - serving your SOUL Patches");
        Image logo = new Image("img/Logo.svg", "SOULHub logo");

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

        if (SecurityUtils.isUserLoggedIn()) {
            appUserNavBar = new AppUserNavBarComponent(SecurityUtils.getUsername());
        } else {
            appUserNavBar = new LoginNavBarComponent();
        }
        header.add(appUserNavBar);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink soulPatchesLink = new RouterLink("SOULPatches", SOULPatchesView.class);
        soulPatchesLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink aboutLink = new RouterLink("About", AboutView.class);
        aboutLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink adminLink = new RouterLink("Administration", AdminView.class);
        adminLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                soulPatchesLink,
                aboutLink,
                adminLink
        ));
    }
}
