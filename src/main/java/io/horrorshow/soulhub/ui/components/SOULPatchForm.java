package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.service.SOULHubUserDetailsService;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.ui.events.SPFileSelectEvent;
import io.horrorshow.soulhub.ui.views.EditSOULPatchView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;

public class SOULPatchForm extends Div
        implements HasValueAndElement<AbstractField.
        ComponentValueChangeEvent<SOULPatchForm, SOULPatch>, SOULPatch> {

    private static final long serialVersionUID = -7531039924193682445L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SOULPatchForm.class);

    private final SOULPatchService soulPatchService;
    private final SOULHubUserDetailsService userDetailsService;

    private final TextField id = new TextField("id");
    private final TextField name = new TextField("name");
    private final TextArea description = new TextArea("description");
    private final TextField author = new TextField("author");
    private final TextField noServings = new TextField("no servings");
    private final Grid<SPFile> spFilesGrid = new Grid<>();
    private final Button newSpFile = new Button("create soulpatch file");
    private final Button editSOULPatch = new Button("edit soulpatch", VaadinIcon.EDIT.create());
    private final Anchor downloadLink = new Anchor();

    private final Binder<SOULPatch> binder = new Binder<>(SOULPatch.class);
    private final AbstractFieldSupport<SOULPatchForm, SOULPatch> fieldSupport;

    public SOULPatchForm(@Autowired SOULPatchService soulPatchService,
                         @Autowired SOULHubUserDetailsService userDetailsService) {
        this.soulPatchService = soulPatchService;
        this.userDetailsService = userDetailsService;

        this.fieldSupport = new AbstractFieldSupport<>(this, null,
                Objects::equals, soulPatch -> {
        });

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
                new Button(it.getName(), event -> fireEvent(new SPFileSelectEvent(this, it)))))
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
        content.add(downloadLink);
        add(content);
    }

    private void initSOULPatchBinder() {
        binder.forField(id).bind(it -> String.valueOf(it.getId()), null);
        binder.forField(name).bind(SOULPatch::getName, null);
        binder.forField(description).bind(SOULPatch::getDescription, null);
        binder.forField(author).bind(soulPatch -> soulPatch.getAuthor().getUserName(), null);
        binder.forField(noServings).bind(it -> String.valueOf(it.getNoViews()), null);
    }

    private void setupEditSOULPatchButton(SOULPatch soulPatch) {
        editSOULPatch.setVisible(
                userDetailsService.isCurrentUserOwnerOf(soulPatch));
    }

    private void setupDownloadLink(SOULPatch soulPatch) {
        try {
            final var byteArrayInputStream =
                    new ByteArrayInputStream(soulPatchService.zipSOULPatchFiles(soulPatch));

            StreamResource streamResource =
                    new StreamResource(
                            String.format("%s.zip", soulPatch.getName()),
                            () -> byteArrayInputStream);
            downloadLink.setHref(streamResource);
            downloadLink.setText(String.format("Download full %s", soulPatch.getName()));
        } catch (IOException e) {
            LOGGER.debug("Problem zipping soulpatch: {}", soulPatch, e);
        }
    }

    public void gotoEditSOULPatch() {
        UI.getCurrent().navigate(EditSOULPatchView.class,
                String.valueOf(fieldSupport.getValue().getId()));
    }

    public void hideSOULPatchForm() {
        setVisible(false);
    }

    public Registration addSpFileSelectListener(ComponentEventListener<SPFileSelectEvent> listener) {
        return addListener(SPFileSelectEvent.class, listener);
    }

    @Override
    public SOULPatch getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public void setValue(SOULPatch soulPatch) {
        fieldSupport.setValue(soulPatch);

        binder.readBean(soulPatch);
        spFilesGrid.setItems(soulPatch.getSpFiles());

        setupEditSOULPatchButton(soulPatch);
        setupDownloadLink(soulPatch);
        setVisible(true);
        name.focus();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<SOULPatchForm, SOULPatch>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }
}
