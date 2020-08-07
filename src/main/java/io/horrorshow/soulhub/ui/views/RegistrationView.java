package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.service.UserService;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import org.springframework.beans.factory.annotation.Autowired;

import javax.management.relation.RoleNotFoundException;
import javax.validation.ValidationException;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Route(value = UIConst.ROUTE_REGISTER, layout = MainLayout.class)
@PageTitle(UIConst.TITLE_REGISTER)
public class RegistrationView extends VerticalLayout
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<RegistrationView, AppUser>, AppUser> {

    private static final long serialVersionUID = -5002067867566531052L;

    private final UserService userDetailsService;

    private final TextField username = new TextField();
    private final TextField email = new TextField();
    private final PasswordField password = new PasswordField();
    private final Button register = new Button("Register");

    private final Binder<AppUser> binder = new Binder<>(AppUser.class);

    private final AbstractFieldSupport<RegistrationView, AppUser> fieldSupport;

    public RegistrationView(@Autowired UserService userDetailsService) {
        this.userDetailsService = userDetailsService;

        fieldSupport = new AbstractFieldSupport<>(this, new AppUser(),
                Objects::equals, appUser -> {
        });

        add(new H1("Registration"));

        initBinder();

        initFields();

        arrangeComponents();
    }

    private void arrangeComponents() {
        FormLayout formLayout = new FormLayout();

        formLayout.addFormItem(username, "Username");
        formLayout.addFormItem(email, "Email");
        formLayout.addFormItem(password, "Password");

        add(formLayout);
        add(register);
    }

    private void initFields() {
        username.setRequiredIndicatorVisible(true);
        email.setRequiredIndicatorVisible(true);
        password.setRequiredIndicatorVisible(true);

        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        register.addClickListener(event -> register());
        register.setDisableOnClick(true);
    }

    private void initBinder() {
        SerializablePredicate<String> stringNotBlankPredicate =
                value -> !email.getValue().isBlank()
                        || !username.getValue().isBlank();

        var usernameBinding = binder.forField(username)
                .withValidator(stringNotBlankPredicate,
                        "must not be blank")
                .withValidator(new StringLengthValidator(
                        "Username must be between 3 and 255 characters long",
                        3, 255))
                .bind(AppUser::getUserName, AppUser::setUserName);

        var emailBinding = binder.forField(email)
                .withValidator(stringNotBlankPredicate,
                        "must not be blank")
                .withValidator(new EmailValidator("Invalid Email address"))
                .bind(AppUser::getEmail, AppUser::setEmail);

        initUsernameEmailCrossValidation(usernameBinding, emailBinding);

        binder.forField(password)
                .asRequired()
                .bind(AppUser::getEncryptedPassword, AppUser::setEncryptedPassword);
    }

    private void initUsernameEmailCrossValidation(Binder.Binding<AppUser, String> usernameBinding,
                                                  Binder.Binding<AppUser, String> emailBinding) {
        email.addValueChangeListener(event -> usernameBinding.validate());
        username.addValueChangeListener(event -> emailBinding.validate());
    }

    private void register() {
        try {
            AppUser appUser = fieldSupport.getValue();
            if (binder.writeBeanIfValid(appUser)) {
                AppUser user = userDetailsService.registerAppUser(appUser);
                new Notification(format("registered %s", user));
                UI.getCurrent().navigate(UserInfoView.class, user.getUserName());
            } else {
                BinderValidationStatus<AppUser> validate = binder.validate();
                String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(java.util.Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                new Notification(errorText, 3000, Notification.Position.MIDDLE).open();
            }
        } catch (RoleNotFoundException e) {
            setValue(new AppUser());
            new Notification("something went wrong, " +
                    "something might have been informed about this, " +
                    "something might be done about that",
                    5000,
                    Notification.Position.MIDDLE).open();
        } catch (ValidationException e) {
            setValue(new AppUser());
            new Notification(e.getMessage(), 5000, Notification.Position.MIDDLE).open();
        }
    }

    @Override
    public AppUser getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public void setValue(AppUser value) {
        register.setEnabled(true);
        fieldSupport.setValue(value);
        binder.readBean(value);
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<RegistrationView, AppUser>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }
}
