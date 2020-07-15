package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.security.SecurityUtils;
import io.horrorshow.soulhub.service.SOULHubUserDetailsService;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;

@Route(value = UIConst.ROUTE_USERINFO, layout = MainLayout.class)
@Secured(UIConst.ROLE_USER)
@PageTitle(UIConst.TITLE_USERINFO)
public class UserInfoView extends VerticalLayout
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<UserInfoView, AppUser>, AppUser>,
        HasUrlParameter<String> {

    private static final long serialVersionUID = 6423464010060256222L;

    private final SOULHubUserDetailsService userDetailsService;

    private final TextField username = new TextField();
    private final TextField email = new TextField();

    private final Binder<AppUser> binder = new Binder<>(AppUser.class);

    private final AbstractFieldSupport<UserInfoView, AppUser> fieldSupport;

    public UserInfoView(@Autowired SOULHubUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;

        fieldSupport = new AbstractFieldSupport<>(this, null,
                Objects::equals, appUser -> {
        });


        addClassName("userinfo-view");

        init();
    }

    private void init() {
        H1 title = new H1("Userinfo");

        Span helloUser = new Span(format("Hello %s", SecurityUtils.getUsername()));

        Span isLoggedIn = new Span(format("you are %s",
                this.userDetailsService.isAuthenticated()
                        ? "authenticated"
                        : "here?! hmm"));

        username.setReadOnly(true);
        email.setReadOnly(true);

        binder.forField(username)
                .withValidator(new StringLengthValidator(
                        "Username must be between 3 and 129 characters long",
                        3, 129))
                .bind(AppUser::getUserName, null);
        binder.forField(email)
                .withValidator(new EmailValidator(
                        "Invalid Email address"))
                .bind(AppUser::getEmail, null);

        FormLayout formLayout = new FormLayout();

        formLayout.addFormItem(username, "Username");
        formLayout.addFormItem(email, "Email");

        add(title, helloUser, isLoggedIn);
        add(formLayout);
    }

    @Override
    public AppUser getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public void setValue(AppUser appUser) {
        fieldSupport.setValue(appUser);
        binder.readBean(appUser);
    }

    @Override
    public Registration addValueChangeListener(
            ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<UserInfoView, AppUser>> listener) {

        return fieldSupport.addValueChangeListener(listener);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        Optional<AppUser> appUser = Optional.ofNullable(userDetailsService.loadAppUser(parameter));
        appUser.ifPresent(this::setValue);
    }
}
