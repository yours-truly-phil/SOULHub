package io.horrorshow.soulswap.ui.views;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulswap.ui.MainLayout;

@Route(value = "registration", layout = MainLayout.class)
@PageTitle("SOULSwap | Registration")
public class RegistrationView extends VerticalLayout {

    public RegistrationView() {
        add(new Label("Registration"));
    }

}
