package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.SPFile;

public class SPFileReadOnlyDialog extends Dialog
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent
        <SPFileReadOnly, SPFile>, SPFile> {

    private static final long serialVersionUID = -8137977769181903236L;

    private final SPFileReadOnly spFileReadOnly;

    public SPFileReadOnlyDialog() {
        spFileReadOnly = new SPFileReadOnly();
        Button close = new Button("close dialog");
        close.addClickListener(event -> close());
        Icon x = new Icon(VaadinIcon.CLOSE_SMALL);
        x.getStyle().set("margin-left", "auto");
        x.addClickListener(event -> close());

        VerticalLayout content = new VerticalLayout();
        content.add(x);
        content.add(spFileReadOnly);
        content.add(close);
        add(content);

        setResizable(false);
        setDraggable(false);
    }

    public void open(SPFile spFile) {
        setValue(spFile);
        open();
    }

    public SPFileReadOnly getSpFileReadOnly() {
        return spFileReadOnly;
    }

    @Override
    public SPFile getValue() {
        return spFileReadOnly.getValue();
    }

    @Override
    public void setValue(SPFile value) {
        spFileReadOnly.setValue(value);
    }

    @Override
    public Registration addValueChangeListener(
            ValueChangeListener<? super AbstractField
                    .ComponentValueChangeEvent<SPFileReadOnly, SPFile>> listener) {
        return spFileReadOnly.addValueChangeListener(listener);
    }
}
