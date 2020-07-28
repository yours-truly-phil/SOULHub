package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.service.SOULHubUserDetailsService;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.ui.views.EditSOULPatchView;
import io.horrorshow.soulhub.ui.views.SOULPatchesView;
import org.springframework.beans.factory.annotation.Autowired;

public class SOULPatchForm extends Div {

    private static final long serialVersionUID = -7531039924193682445L;
    private final SOULPatchesView soulPatchesView;

    private final SOULPatchService soulPatchService;
    private final SOULHubUserDetailsService userDetailsService;

    private final TextField id = new TextField("id");
    private final TextField name = new TextField("name");
    private final TextArea description = new TextArea("description");
    private final TextField author = new TextField("author");
    private final TextField noServings = new TextField("no servings");
    private final Grid<SPFile> spFilesGrid = new Grid<>();
    private final Button newSpFile = new Button("create soulpatch file");
    private final Binder<SOULPatch> binder = new Binder<>(SOULPatch.class);
    private final Button editSOULPatch = new Button("edit soulpatch", VaadinIcon.EDIT.create());

    public SOULPatchForm(SOULPatchesView soulPatchesView,
                         @Autowired SOULPatchService soulPatchService,
                         @Autowired SOULHubUserDetailsService userDetailsService) {
        this.soulPatchesView = soulPatchesView;
        this.soulPatchService = soulPatchService;
        this.userDetailsService = userDetailsService;

        setClassName("soulpatch-form");

        initFields();

        arrangeComponents();

        initSOULPatchBinder();
    }

    private void initFields() {
        id.setWidth("100%");
        id.setReadOnly(true);

        name.setWidth("100%");
        name.setReadOnly(true);

        description.setWidth("100%");
        description.setReadOnly(true);

        author.setWidth("100%");
        author.setReadOnly(true);

        noServings.setWidth("100%");
        noServings.setReadOnly(true);

        spFilesGrid.setHeightByRows(true);
        spFilesGrid.setWidth("200");

        spFilesGrid.addColumn(new ComponentRenderer<>(it ->
                new Button(it.getName(), event -> soulPatchesView.previewSpFile(it))))
                .setHeader("filename").setAutoWidth(true);
        spFilesGrid.addColumn(spFile -> (spFile.getFileType() != null) ? spFile.getFileType().toString() : "")
                .setHeader("filetype").setAutoWidth(true);

        editSOULPatch.setVisible(false);
        editSOULPatch.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editSOULPatch.addClickListener(event -> gotoEditSOULPatch());
    }

    private void arrangeComponents() {
        VerticalLayout content = new VerticalLayout();
        content.addClassName("soulpatch-form-content");
        content.add(editSOULPatch);
        content.add(id);
        content.add(name);
        content.add(description);
        content.add(author);
        content.add(noServings);
        content.add(newSpFile);
        content.add(spFilesGrid);
        add(content);
    }

    private void initSOULPatchBinder() {
        binder.forField(id).bind(it -> String.valueOf(it.getId()), null);
        binder.forField(name).bind(SOULPatch::getName, null);
        binder.forField(description).bind(SOULPatch::getDescription, null);
        binder.forField(author).bind(soulPatch -> soulPatch.getAuthor().getUserName(), null);
        binder.forField(noServings).bind(it -> String.valueOf(it.getNoViews()), null);
    }

    public void showSOULPatch(SOULPatch soulPatch) {

        setupEditSOULPatchButton(soulPatch);

        binder.setBean(soulPatch);
        spFilesGrid.setItems(soulPatch.getSpFiles());
        setVisible(true);
        name.focus();
    }

    private void setupEditSOULPatchButton(SOULPatch soulPatch) {
        editSOULPatch.setVisible(
                userDetailsService.isCurrentUserOwnerOf(soulPatch));
    }

    public void gotoEditSOULPatch() {
        UI.getCurrent().navigate(EditSOULPatchView.class,
                String.valueOf(binder.getBean().getId()));
    }

    public void hideSOULPatchForm() {
        setVisible(false);
    }
}
