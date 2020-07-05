package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class AppUserNavBarComponent extends HorizontalLayout {

    private static final long serialVersionUID = 833982187716488558L;

    public AppUserNavBarComponent(String username) {

        Anchor toUserInfoView = new Anchor("userinfo", username);
        add(toUserInfoView);

        Anchor logout = new Anchor("logout", "Logout");
        add(logout);
    }

}
