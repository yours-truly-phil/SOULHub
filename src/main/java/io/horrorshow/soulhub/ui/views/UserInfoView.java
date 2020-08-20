package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ReadOnlyHasValue;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.service.UserService;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.Map;
import java.util.Objects;

import static java.lang.String.format;

@Route(value = UIConst.ROUTE_USERINFO, layout = MainLayout.class)
@Secured(UIConst.ROLE_USER)
@PageTitle(UIConst.TITLE_USERINFO)
public class UserInfoView extends VerticalLayout
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<UserInfoView, AppUser>, AppUser>,
        HasUrlParameter<String> {

    private static final long serialVersionUID = 6423464010060256222L;

    private final UserService userDetailsService;

    private final TextField username = new TextField();
    private final TextField email = new TextField();
    private final Label greeting = new Label();

    private final Binder<AppUser> binder = new Binder<>(AppUser.class);

    private final AbstractFieldSupport<UserInfoView, AppUser> fieldSupport;

    public UserInfoView(@Autowired UserService userDetailsService) {
        this.userDetailsService = userDetailsService;

        fieldSupport = new AbstractFieldSupport<>(this, null,
                Objects::equals, appUser -> {
        });


        addClassName("userinfo-view");

        init();
    }

    private void init() {
        H1 title = new H1("Userinfo");

        Span helloUser = new Span("Hello");

        Span isLoggedIn = new Span(format("you are %s",
                this.userDetailsService.isAuthenticated()
                        ? "authenticated"
                        : "here?! hmm"));

        username.setReadOnly(true);
        email.setReadOnly(true);

        ReadOnlyHasValue<String> readOnlyHasValue =
                new ReadOnlyHasValue<>(greeting::setText, greeting.getText());
        binder.forField(readOnlyHasValue)
                .bind(appUser -> format("Hello %s!", appUser.getUserName()), null);

        binder.forField(username)
                .withValidator(new StringLengthValidator(
                        "Username must be between 3 and 255 characters long",
                        3, 255))
                .bind(AppUser::getUserName, null);
        binder.forField(email)
                .withValidator(new EmailValidator(
                        "Invalid Email address"))
                .bind(AppUser::getEmail, null);

        FormLayout formLayout = new FormLayout();

        formLayout.addFormItem(username, "Username");
        formLayout.addFormItem(email, "Email");
        formLayout.add(createShowSoulPatchesByUserLink());

        add(title, helloUser, isLoggedIn, greeting);
        add(formLayout);
    }

    private RouterLink createShowSoulPatchesByUserLink() {
        QueryParameters queryParameters =
                QueryParameters.simple(
                        Map.of(UIConst.PARAM_SHOW_BY_CURRENT_USER, Boolean.TRUE.toString()));

        RouterLink link = new RouterLink(
                "show my soulpatches",
                SOULPatchesView.class);
        link.setQueryParameters(queryParameters);
        return link;
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
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter == null || parameter.isEmpty()) {
            userDetailsService.getCurrentAppUser().ifPresent(this::setValue);
        } else {
            userDetailsService.loadAppUser(parameter).ifPresent(this::setValue);
        }
    }
}
