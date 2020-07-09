package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;

@Route(value = UIConst.ROUTE_REGISTER, layout = MainLayout.class)
@PageTitle(UIConst.TITLE_REGISTER)
public class RegistrationView extends VerticalLayout {

    private static final long serialVersionUID = -5002067867566531052L;

    public RegistrationView() {
        add(new Label("Registration"));
    }

}
