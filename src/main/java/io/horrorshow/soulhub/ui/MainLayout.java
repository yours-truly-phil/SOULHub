package io.horrorshow.soulhub.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import io.horrorshow.soulhub.ui.components.UserAccessNavbar;
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

    UserAccessNavbar userAccessNavbar = new UserAccessNavbar();

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("SOULHub - serving your SOUL Patches");
        logo.addClassName("logo");

        DrawerToggle drawerToggle = new DrawerToggle();

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setWidth("100%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);


        header.add(drawerToggle, logo);
        header.expand(logo);

        header.add(userAccessNavbar);

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
