package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulhub.service.SOULHubUserDetailsService;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = UIConst.ROUTE_REGISTER, layout = MainLayout.class)
@PageTitle(UIConst.TITLE_REGISTER)
public class RegistrationView extends VerticalLayout {

    private static final long serialVersionUID = -5002067867566531052L;

    private final SOULHubUserDetailsService userDetailsService;

    private final TextField username = new TextField();
    private final PasswordField password = new PasswordField();
    private final Button register = new Button("Register");

    public RegistrationView(@Autowired SOULHubUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;

        add(new Label("Registration"));

        username.setRequiredIndicatorVisible(true);
        password.setRequiredIndicatorVisible(true);

        register.addClickListener(event -> register());

        FormLayout formLayout = new FormLayout();

        formLayout.addFormItem(username, "Username");
        formLayout.addFormItem(password, "Password");

        add(formLayout);
        add(register);
    }

    private void register() {
        new Notification("user registration not implemented",
                3000).open();
    }
}
