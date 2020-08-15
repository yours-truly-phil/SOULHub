package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.service.UserService;
import io.horrorshow.soulhub.ui.views.EditSOULPatchView;
import io.horrorshow.soulhub.ui.views.SOULPatchView;
import org.springframework.beans.factory.annotation.Autowired;

public class SOULPatchReadOnlyDialog extends Dialog
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent
        <SOULPatchReadOnly, SOULPatch>, SOULPatch> {

    private static final long serialVersionUID = 469080434031031088L;

    private final UserService userService;

    private final SOULPatchReadOnly soulPatchReadOnly;

    private final Button editSOULPatch = new Button("edit SOULPatch", VaadinIcon.EDIT.create());

    public SOULPatchReadOnlyDialog(@Autowired SOULPatchService soulPatchService,
                                   @Autowired UserService userService) {

        this.userService = userService;

        soulPatchReadOnly = new SOULPatchReadOnly(soulPatchService, userService);

        addValueChangeListener(this::valueChanged);

        VerticalLayout content = new VerticalLayout();

        content.add(soulPatchReadOnly);

        Button toSoulPatchView = new Button("open SOULPatch", VaadinIcon.VIEWPORT.create());
        toSoulPatchView.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        toSoulPatchView.addClickListener(this::toSoulPatchViewClicked);
        content.add(toSoulPatchView);

        editSOULPatch.setVisible(false);
        editSOULPatch.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editSOULPatch.addClickListener(this::editSOULPatchClicked);
        content.add(editSOULPatch);

        Button close = new Button("close dialog");
        close.addClickListener(event -> close());
        content.add(close);

        add(content);

        setResizable(true);
        setDraggable(true);
    }

    private void valueChanged(
            AbstractField.ComponentValueChangeEvent<SOULPatchReadOnly, SOULPatch> event) {

        editSOULPatch.setVisible(userService.isCurrentUserOwnerOf(event.getValue()));
    }

    private void editSOULPatchClicked(ClickEvent<Button> event) {
        UI.getCurrent().navigate(EditSOULPatchView.class,
                String.valueOf(soulPatchReadOnly.getValue().getId()));
    }

    private void toSoulPatchViewClicked(ClickEvent<Button> event) {
        UI.getCurrent().navigate(SOULPatchView.class,
                String.valueOf(soulPatchReadOnly.getValue().getId()));
    }

    public void open(SOULPatch soulPatch) {
        setValue(soulPatch);
        open();
    }

    @Override
    public SOULPatch getValue() {
        return soulPatchReadOnly.getValue();
    }

    @Override
    public void setValue(SOULPatch value) {
        soulPatchReadOnly.setValue(value);
    }

    @Override
    public Registration addValueChangeListener(
            ValueChangeListener<? super AbstractField
                    .ComponentValueChangeEvent<SOULPatchReadOnly, SOULPatch>> listener) {
        return soulPatchReadOnly.addValueChangeListener(listener);
    }
}
