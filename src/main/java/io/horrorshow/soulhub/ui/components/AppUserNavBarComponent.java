package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.ui.UIConst;

import java.util.Optional;

public class AppUserNavBarComponent extends HorizontalLayout {

    private static final long serialVersionUID = 833982187716488558L;

    public AppUserNavBarComponent(AppUser user) {

        Anchor toUserInfoView = new Anchor(UIConst.ROUTE_USERINFO + "/" + user.getUserName(), user.getUserName());
        add(toUserInfoView);

        Anchor logout = new Anchor(UIConst.ROUTE_LOGOUT, UIConst.LINK_TEXT_LOGOUT);
        add(logout);
    }

}
