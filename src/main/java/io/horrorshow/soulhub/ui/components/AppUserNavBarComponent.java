package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.ui.UIConst;
import io.horrorshow.soulhub.ui.views.EditSOULPatchView;

import java.util.Optional;

public class AppUserNavBarComponent extends HorizontalLayout {

    private static final long serialVersionUID = 833982187716488558L;

    public AppUserNavBarComponent(AppUser user) {
        Button addSOULPatch = new Button("add SOULPatch", VaadinIcon.FILE_ADD.create());
        addSOULPatch.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addSOULPatch.addClickListener(e -> UI.getCurrent().navigate(EditSOULPatchView.class, "new"));
        add(addSOULPatch);

        Anchor toUserInfoView = new Anchor(UIConst.ROUTE_USERINFO + "/" + user.getUserName(), user.getUserName());
        add(toUserInfoView);

        Anchor logout = new Anchor(UIConst.ROUTE_LOGOUT, UIConst.LINK_TEXT_LOGOUT);
        add(logout);
    }

}
