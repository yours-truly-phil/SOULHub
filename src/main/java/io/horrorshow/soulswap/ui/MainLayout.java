package io.horrorshow.soulswap.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import io.horrorshow.soulswap.ui.views.AboutView;
import io.horrorshow.soulswap.ui.views.SOULPatchesView;

@PWA(name = "SOULSwap - SOUL-Patch Web UI",
        shortName = "SOULSwap-Web",
        description = "Serves SOULPatches (SOUL files)",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css",
        themeFor = "vaadin-text-field")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("SOULSwap - serving your SOUL Patches");
        logo.addClassName("logo");

        DrawerToggle drawerToggle = new DrawerToggle();

        Anchor login = new Anchor("login", "Login");
        Anchor logout = new Anchor("logout", "Log out");

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setWidth("100%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);


        header.add(drawerToggle, logo, logout);
        header.expand(logo);
        header.add(login, logout);
        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink soulPatchesLink = new RouterLink("SOULPatches", SOULPatchesView.class);
        soulPatchesLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink aboutLink = new RouterLink("About", AboutView.class);
        aboutLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                soulPatchesLink,
                aboutLink
        ));
    }
}
