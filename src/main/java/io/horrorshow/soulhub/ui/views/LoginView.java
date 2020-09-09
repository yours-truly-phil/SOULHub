package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulhub.service.UserService;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = UIConst.ROUTE_LOGIN, layout = MainLayout.class)
@PageTitle(UIConst.TITLE_LOGIN)
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private static final long serialVersionUID = 7631869443546476927L;

    private final LoginForm loginForm = new LoginForm();

    private final Dialog resendConfirm = new Dialog();

    private final UserService userService;

    public LoginView(@Autowired UserService userService) {
        this.userService = userService;

        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        loginForm.addForgotPasswordListener(event -> {
            Notification notification = new Notification("Password reset not available");
            notification.setDuration(5003);
            notification.open();
        });

        loginForm.setAction(UIConst.ROUTE_LOGIN);

        add(new H1("SOULHub Login"), loginForm);

        FormLayout formLayout = new FormLayout();
        Button send = new Button("Request Confirmation Mail");
        send.setEnabled(false);
        send.setDisableOnClick(true);
        TextField email = new TextField();
        email.addValueChangeListener(event -> {
            send.setEnabled(!event.getValue().isBlank());
        });
        formLayout.addFormItem(email, "Email");

        VerticalLayout layout = new VerticalLayout();
        layout.add(new H3("Resend Confirmation Mail"));
        layout.add(formLayout);
        layout.add(send);

        resendConfirm.add(layout);

        Button openResendConfirm = new Button("request new confirmation email");
        openResendConfirm.addClickListener(event -> resendConfirm.open());
        add(openResendConfirm);
        add(resendConfirm);

        send.addClickListener(event -> {
            userService.resendConfirmationEmail(email.getValue());
            new Notification(
                    String.format("If %s exists, a new verification email has been sent",
                            email.getValue()),
                    5000, Notification.Position.MIDDLE).open();
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }

}
