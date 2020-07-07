package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.ui.MainLayout;
import org.springframework.security.access.annotation.Secured;

import static java.lang.String.format;

@Secured(value = "ROLE_USER")
@Route(value = "editsoulpatch", layout = MainLayout.class)
@PageTitle("SOULHub | Edit SOULPatch")
public class EditSOULPatchView extends VerticalLayout {

    private static final long serialVersionUID = -4704235426941430447L;

    private final TextField name = new TextField("Name of the your SOULPatch");
    private final TextArea description = new TextArea("Describe your SOULPatch");
    private final Grid<SPFile> files = new Grid<>();
    private final Button save = new Button("save");
    private final Button delete = new Button("delete SOULPatch");
    private final Button addFile = new Button("add SOUL file");
    private final Binder<SOULPatch> binder = new Binder<>(SOULPatch.class);

    public EditSOULPatchView() {
        setClassName("edit-soulpatch-view");

        initFields();

        initBinder();

        arrangeComponents();
    }

    private void initFields() {
        name.setWidth("100%");
        name.setRequired(true);

        description.setWidth("100%");
        description.setRequired(true);

        files.setHeightByRows(true);
        files.setWidthFull();
        files.addColumn(new ComponentRenderer<>(spFile ->
                new Button(spFile.getName(), event ->
                        new Notification(format("%s clicked!", spFile.getName())))))
                .setHeader("filename").setAutoWidth(true);
        files.addColumn(spFile -> spFile.getFileType().toString())
                .setHeader("filetype").setAutoWidth(true);

        save.setWidthFull();
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> new Notification("save clicked"));

        delete.setWidthFull();
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> new Notification("delete clicked"));

        addFile.setWidthFull();
        addFile.addClickListener(event -> new Notification("add file clicked"));
    }

    private void initBinder() {
        binder.forField(name).bind(SOULPatch::getName, SOULPatch::setName);
        binder.forField(description).bind(SOULPatch::getDescription, SOULPatch::setDescription);
    }

    private void arrangeComponents() {
        setSizeFull();
        add(name);
        add(description);
        add(files);
        add(save);
        add(delete);
        add(addFile);
    }


}
