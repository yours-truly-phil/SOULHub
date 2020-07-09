package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import org.springframework.security.access.annotation.Secured;

@Route(value = UIConst.ROUTE_ADMIN, layout = MainLayout.class)
@Secured(UIConst.ROLE_ADMIN)
@PageTitle(UIConst.TITLE_ADMIN)
public class AdminView extends VerticalLayout {
    private static final long serialVersionUID = -4744767698452492047L;

    public AdminView() {
        H1 h1 = new H1("Admin View");
        Label label = new Label("this is a secured location");
        add(h1, label);
    }
}
