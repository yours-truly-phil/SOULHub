package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import io.horrorshow.soulhub.ui.views.RegistrationView;

public class UserAccessNavbar extends HorizontalLayout {

    Anchor login = new Anchor("login", "Login");
    Anchor logout = new Anchor("logout", "Logout");
    RouterLink registration = new RouterLink("Registration", RegistrationView.class);

    public UserAccessNavbar() {
        initComponents();
        arrangeComponents();
    }

    private void initComponents() {
        registration.setHighlightCondition(HighlightConditions.sameLocation());
    }

    private void arrangeComponents() {
        add(login);
        add(logout);
        add(registration);
    }

}
