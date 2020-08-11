package io.horrorshow.soulhub.ui.components;

import com.hilerio.ace.AceEditor;
import com.hilerio.ace.AceMode;
import com.hilerio.ace.AceTheme;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.ui.events.SPFileDownloadEvent;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;

public class SOULFilePreview extends Dialog {

    private static final long serialVersionUID = 3233168273572823081L;

    private final H3 name = new H3();
    private final AceEditor aceEditor = new AceEditor();
    private final Select<AceTheme> aceThemeSelect = new Select<>();
    private final Span createdAt = new Span();
    private final Span updatedAt = new Span();
    private final Span fileType = new Span();
    private final Button close = new Button("close dialog");
    private final Anchor downloadLink = new Anchor();

    public SOULFilePreview() {
        initComponents();

        arrangeComponents();
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
        aceEditor.setMinlines(1);
        aceEditor.setMaxlines(30);
        aceEditor.setPlaceholder("SOULFile content");

        close.addClickListener(event -> close());
    }

    private void arrangeComponents() {
        VerticalLayout content = new VerticalLayout();

        FormLayout attributesLayout = new FormLayout();
        attributesLayout.addFormItem(name, "filename");
        attributesLayout.addFormItem(createdAt, "created at");
        attributesLayout.addFormItem(fileType, "filetype");
        attributesLayout.addFormItem(aceThemeSelect, "color theme");

        content.add(attributesLayout);

        content.add(aceEditor);
        content.add(downloadLink);
        content.add(close);
        add(content);

        setResizable(true);
        setDraggable(true);
    }

    public void showSpFile(SPFile spFile) {
        name.setText(spFile.getName());
        createdAt.setText(spFile.getCreatedAt()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        updatedAt.setText(spFile.getUpdatedAt()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        fileType.setText((spFile.getFileType() != null)
                ? spFile.getFileType().toString()
                : "");
        aceEditor.setValue(spFile.getFileContent());

        StreamResource streamResource = new StreamResource(spFile.getName(),
                () -> downloadSPFileBytes(spFile));
        downloadLink.setHref(streamResource);
        downloadLink.setText(String.format("Download %s", spFile.getName()));

        open();
    }

    private ByteArrayInputStream downloadSPFileBytes(SPFile spFile) {
        fireEvent(new SPFileDownloadEvent(this, spFile));
        return new ByteArrayInputStream(spFile.getFileContent().getBytes());
    }

    public Registration addSPFileDownloadListener(
            ComponentEventListener<SPFileDownloadEvent> listener) {
        return addListener(SPFileDownloadEvent.class, listener);
    }
}
