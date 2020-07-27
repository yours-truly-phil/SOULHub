package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConfirmationDialog extends Dialog {

    private static final long serialVersionUID = 3463241091711306172L;

    private final Label title = new Label();
    private final Label question = new Label();
    private final Button confirm = new Button("Confirm");

    public ConfirmationDialog() {
        createHeader();
        createContent();
        createFooter();
    }

    public ConfirmationDialog(String title, String content, ComponentEventListener<ClickEvent<Button>> listener) {
        this();
        setTitle(title);
        setQuestion(content);
        addConfirmationListener(listener);
    }

    private void createHeader() {
        Button close = new Button();
        close.setIcon(VaadinIcon.CLOSE.create());
        close.addClickListener(event -> close());

        HorizontalLayout header = new HorizontalLayout();
        header.add(this.title, close);
        header.setFlexGrow(1, this.title);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        add(header);
    }

    private void createContent() {
        VerticalLayout content = new VerticalLayout();
        content.add(question);
        content.setPadding(false);
        add(content);
    }

    private void createFooter() {
        Button abort = new Button("Abort");
        abort.addClickListener(event -> close());
        confirm.addClickListener(event -> close());

        HorizontalLayout footer = new HorizontalLayout();
        footer.add(abort, confirm);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        add(footer);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setQuestion(String question) {
        this.question.setText(question);
    }

    public void addConfirmationListener(ComponentEventListener<ClickEvent<Button>> listener) {
        confirm.addClickListener(listener);
    }
}
