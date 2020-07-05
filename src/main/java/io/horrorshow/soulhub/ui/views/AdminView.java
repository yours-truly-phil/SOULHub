package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulhub.ui.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = "admin", layout = MainLayout.class)
@Secured("ROLE_ADMIN")
@PageTitle("SOULHub | Admin")
public class AdminView extends VerticalLayout {
    private static final long serialVersionUID = -4744767698452492047L;

    @Autowired
    public AdminView() {
        H1 h1 = new H1("Admin View");
        Label label = new Label("this is a secured location");
        add(h1, label);
    }
}
