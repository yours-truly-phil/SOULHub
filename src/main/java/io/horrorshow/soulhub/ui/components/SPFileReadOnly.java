package io.horrorshow.soulhub.ui.components;

import com.hilerio.ace.AceEditor;
import com.hilerio.ace.AceMode;
import com.hilerio.ace.AceTheme;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ReadOnlyHasValue;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.ui.events.SPFileDownloadEvent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SPFileReadOnly extends Div
        implements HasValueAndElement
        <AbstractField.ComponentValueChangeEvent
                <SPFileReadOnly, SPFile>, SPFile> {

    private static final long serialVersionUID = 8519553192242944636L;

    private final H3 name = new H3();
    private final AceEditor aceEditor = new AceEditor();
    private final Select<AceTheme> aceThemeSelect = new Select<>();
    private final Span createdAt = new Span();
    private final Span updatedAt = new Span();
    private final Span fileType = new Span();
    private final Anchor downloadLink = new Anchor();

    private final AbstractFieldSupport<SPFileReadOnly, SPFile> fieldSupport;
    private final Binder<SPFile> binder = new Binder<>(SPFile.class);

    public SPFileReadOnly() {

        this.fieldSupport =
                new AbstractFieldSupport<>(this, null, Objects::equals, sp -> {
                });

        setClassName("spfile-read-only");

        setVisible(false); // set to true if spFile in setValue(spFile) != null

        addValueChangeListener(this::valueChanged);

        initComponents();

        arrangeComponents();
    }

    private void arrangeComponents() {
        FormLayout attributes = new FormLayout();
        attributes.addFormItem(name, "filename");
        attributes.addFormItem(createdAt, "created at");
        attributes.addFormItem(updatedAt, "updated at");
        attributes.addFormItem(fileType, "file-type");
        attributes.addFormItem(aceThemeSelect, "color theme");

        VerticalLayout content = new VerticalLayout();

        content.add(attributes);
        content.add(aceEditor);
        content.add(downloadLink);

        add(content);
    }

    private void initComponents() {
        name.setTitle("filename");

        createdAt.setTitle("created at");

        updatedAt.setTitle("updated at");

        fileType.setTitle("file-type");

        aceThemeSelect.setItems(AceTheme.values());
        aceThemeSelect.addValueChangeListener(event ->
                aceEditor.setTheme(event.getValue()));
        aceThemeSelect.setValue(AceTheme.dracula);
        aceThemeSelect.setLabel("color theme");

        aceEditor.setReadOnly(true);
        aceEditor.setTheme(AceTheme.dracula);
        aceEditor.setMode(AceMode.c_cpp);
        aceEditor.setSofttabs(true);
        aceEditor.setTabSize(4);
        aceEditor.setWrap(false);
        aceEditor.setSizeFull();
        aceEditor.setMinlines(10);
        aceEditor.setMaxlines(30);
        aceEditor.setPlaceholder("SOULFile content");

        ReadOnlyHasValue<SPFile> downloadBinding =
                new ReadOnlyHasValue<>(spFile -> {
                    StreamResource sr = new StreamResource(
                            String.format("%s", spFile.getName()),
                            () -> downloadSPFile(spFile));
                    downloadLink.setHref(sr);
                    downloadLink.setText(String.format("%s", spFile.getName()));
                });

        binder.forField(new ReadOnlyHasValue<>(name::setText, null))
                .bind(SPFile::getName, null);
        binder.forField(new ReadOnlyHasValue<>(createdAt::setText, null))
                .bind(spFile -> spFile.getCreatedAt()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), null);
        binder.forField(new ReadOnlyHasValue<>(updatedAt::setText, null))
                .bind(spFile -> spFile.getUpdatedAt()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), null);
        binder.forField(new ReadOnlyHasValue<>(fileType::setText, null))
                .bind(spFile -> (spFile.getFileType() != null)
                        ? spFile.getFileType().toString()
                        : "", null);
        binder.forField(downloadBinding)
                .bind(spFile -> spFile, null);
        binder.forField(new ReadOnlyHasValue<>(aceEditor::setValue, null))
                .bind(SPFile::getFileContent, null);
    }

    private InputStream downloadSPFile(SPFile spFile) {
        fireEvent(new SPFileDownloadEvent(this, spFile));
        return new ByteArrayInputStream(spFile.getFileContent().getBytes());
    }

    private void valueChanged(
            AbstractField.ComponentValueChangeEvent<SPFileReadOnly, SPFile> event) {
        binder.readBean(event.getValue());
    }

    public Registration addSPFileDownloadListener(
            ComponentEventListener<SPFileDownloadEvent> listener) {
        return addListener(SPFileDownloadEvent.class, listener);
    }

    @Override
    public SPFile getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public void setValue(SPFile value) {
        setVisible(value != null);
        fieldSupport.setValue(value);
    }

    @Override
    public Registration addValueChangeListener(
            ValueChangeListener<? super AbstractField
                    .ComponentValueChangeEvent<SPFileReadOnly, SPFile>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }
}