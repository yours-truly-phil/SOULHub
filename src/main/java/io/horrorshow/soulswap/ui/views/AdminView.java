package io.horrorshow.soulswap.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route("/admin") // TODO still shows in available routes error view
@Secured("ROLE_ADMIN")
public class AdminView extends VerticalLayout {
    @Autowired
    public AdminView() {
        H1 h1 = new H1("Admin View");
        Label label = new Label("this is a secured location");
        add(h1, label);
    }
}
