package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import io.horrorshow.soulhub.ui.UIConst;
import io.horrorshow.soulhub.ui.views.EditSOULPatchView;
import io.horrorshow.soulhub.ui.views.RegistrationView;

import javax.annotation.PostConstruct;

public class LoginNavBarComponent extends HorizontalLayout {
    private static final long serialVersionUID = 7056111594761246275L;

    private final Anchor login = new Anchor(UIConst.ROUTE_LOGIN, UIConst.LINK_TEXT_LOGIN);
    private final Anchor confirm = new Anchor(UIConst.ROUTE_CONFIRM_REGISTER, UIConst.LINK_TEXT_CONFIRM_REGISTER);
    private final RouterLink registration =
            new RouterLink(UIConst.LINK_TEXT_REGISTER, RegistrationView.class);
    private final Button addSOULPatch = new Button("add SOULPatch", VaadinIcon.FILE_ADD.create());

    public LoginNavBarComponent() {
        init();
    }

    @PostConstruct
    private void init() {
        initComponents();
        arrangeComponents();
    }

    private void initComponents() {
        addSOULPatch.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addSOULPatch.addClickListener(e -> UI.getCurrent().navigate(EditSOULPatchView.class, "new"));

        registration.setHighlightCondition(HighlightConditions.sameLocation());
    }

    private void arrangeComponents() {
        add(addSOULPatch);
        add(confirm);
        add(login);
        add(registration);
    }

}
