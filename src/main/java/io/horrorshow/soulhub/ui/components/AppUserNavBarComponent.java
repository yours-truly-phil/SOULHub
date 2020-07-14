package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.horrorshow.soulhub.ui.UIConst;

public class AppUserNavBarComponent extends HorizontalLayout {

    private static final long serialVersionUID = 833982187716488558L;

    public AppUserNavBarComponent(String username) {

        Anchor toUserInfoView = new Anchor(UIConst.ROUTE_USERINFO + "/" + username, username);
        add(toUserInfoView);

        Anchor logout = new Anchor(UIConst.ROUTE_LOGOUT, UIConst.LINK_TEXT_LOGOUT);
        add(logout);
    }

}
