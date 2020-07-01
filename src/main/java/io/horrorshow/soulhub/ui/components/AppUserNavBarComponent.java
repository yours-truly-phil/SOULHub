package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class AppUserNavBarComponent extends HorizontalLayout {

    private static final long serialVersionUID = 833982187716488558L;
    private final Anchor logout = new Anchor("logout", "Logout");
    private final Span username;

    public AppUserNavBarComponent(String username) {
        this.username = new Span(username);

        add(this.username);
        add(logout);
    }

}
