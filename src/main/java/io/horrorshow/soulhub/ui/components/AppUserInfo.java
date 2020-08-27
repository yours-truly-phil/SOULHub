package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ReadOnlyHasValue;
import com.vaadin.flow.data.binder.StatusChangeEvent;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.ui.UIConst;
import io.horrorshow.soulhub.ui.events.AppUserSaveEvent;

import java.util.Map;
import java.util.Objects;

import static java.lang.String.format;
import static java.lang.String.valueOf;

public class AppUserInfo extends Div
        implements HasValueAndElement<
        AbstractField.ComponentValueChangeEvent<
                AppUserInfo, AppUser>, AppUser> {

    private static final long serialVersionUID = 3174711278391584444L;

    private final AbstractFieldSupport<AppUserInfo, AppUser> fieldSupport;

    private final Binder<AppUser> binder = new Binder<>();

    private final Label id = new Label();
    private final Label userName = new Label();
    private final Label email = new Label();

    private final Button save = new Button("save");

    private final RouterLink showSOULPatches = new RouterLink();

    public AppUserInfo() {
        fieldSupport = new AbstractFieldSupport<>(
                this, null, Objects::equals, this::update);

        setClassName("app-user-display");

        init();

        arrangeComponents();
    }

    private void update(AppUser appUser) {
        binder.readBean(appUser);
    }

    private void init() {
        id.setTitle("id");

        userName.setTitle("username");
        userName.setWidthFull();

        email.setTitle("email");
        email.setWidthFull();

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(this::saveAppUser);

        binder.forField(new ReadOnlyHasValue<>(userName::setText, null))
                .bind(AppUser::getUserName, null);

        binder.forField(new ReadOnlyHasValue<>(email::setText, null))
                .bind(AppUser::getEmail, null);

        binder.forField(new ReadOnlyHasValue<>(id::setText, null))
                .bind(appUser -> valueOf(appUser.getId()), null);

        binder.forField(new ReadOnlyHasValue<>(showSOULPatches::setText, null))
                .bind(appUser -> format("show SOULPatches by %s", appUser.getUserName()), null);

        binder.forField(new ReadOnlyHasValue<>(showSOULPatches::setQueryParameters, null))
                .bind(appUser -> QueryParameters.simple(
                        Map.of(UIConst.PARAM_SHOW_BY_USER, appUser.getId().toString())), null);

        binder.addStatusChangeListener(this::binderStatusChanged);

        addValueChangeListener(this::valueChanged);
    }

    private void binderStatusChanged(StatusChangeEvent event) {
        save.setEnabled(binder.hasChanges() && binder.isValid());
    }

    private void valueChanged(
            AbstractField
                    .ComponentValueChangeEvent<AppUserInfo, AppUser> event) {
        binder.readBean(event.getValue());
    }

    private void saveAppUser(ClickEvent<Button> event) {
        if (binder.writeBeanIfValid(getValue())) {
            fireEvent(new AppUserSaveEvent(this, getValue()));
        } else {
            new Notification("Validation errors",
                    3000, Notification.Position.MIDDLE)
                    .open();
        }
    }

    private void arrangeComponents() {
        VerticalLayout layout = new VerticalLayout();

        layout.add(new H3("Userinfo:"));

        FormLayout formLayout = new FormLayout();
        formLayout.addFormItem(id, "id");
        formLayout.addFormItem(userName, "username");
        formLayout.addFormItem(email, "email");
        formLayout.add(save);
        layout.add(formLayout);
        layout.add(showSOULPatches);
        add(layout);
    }

    public Registration addAppUserSaveListener(
            ComponentEventListener<AppUserSaveEvent> listener) {
        return addListener(AppUserSaveEvent.class, listener);
    }

    @Override
    public AppUser getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public void setValue(AppUser value) {
        fieldSupport.setValue(value);
    }

    @Override
    public Registration addValueChangeListener(
            ValueChangeListener<? super AbstractField
                    .ComponentValueChangeEvent<AppUserInfo, AppUser>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }
}
