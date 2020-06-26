package io.horrorshow.soulswap.ui;

import com.hilerio.ace.AceEditor;
import com.hilerio.ace.AceMode;
import com.hilerio.ace.AceTheme;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import io.horrorshow.soulswap.data.SPFile;

import java.time.format.DateTimeFormatter;

public class SOULFileEditor extends VerticalLayout {

    private final TextField name = new TextField("filename");
    private final AceEditor aceEditor = new AceEditor();
    private final Label createdAt = new Label();
    private final Label updatedAt = new Label();
    private final Label fileType = new Label();

    private SPFile spFile;

    private Binder<SPFile> binder = new Binder<>(SPFile.class);

    public SOULFileEditor(SPFile spFile) {
        super();
        this.spFile = spFile;
        setSizeFull();

        name.setWidth("100%");
        name.setRequired(true);
        name.setValue(spFile.getName());
        add(name);

        createdAt.setTitle("Created at");
        createdAt.setText(spFile.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        add(createdAt);

        updatedAt.setTitle("Updated at");
        updatedAt.setText(spFile.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        add(updatedAt);

        fileType.setTitle("File-Type");
        fileType.setText(spFile.getFileType().toString());
        add(fileType);

        aceEditor.setValue(spFile.getFileContent());
        aceEditor.setTheme(AceTheme.monokai);
        aceEditor.setMode(AceMode.xml);

        aceEditor.setSofttabs(true);
        aceEditor.setTabSize(4);
        aceEditor.setWrap(false);

        aceEditor.setSizeFull();
        aceEditor.setMinlines(10);

        aceEditor.setPlaceholder("SOULFile content");

        aceEditor.addFocusListener(e -> {
            System.out.println("aceEditor focus listener bam");
        });

        aceEditor.setValue(spFile.getFileContent());
        add(aceEditor);

        // TODO ✔ use binder to bind bean values
        binder.forField(name).bind(SPFile::getName, SPFile::setName);
        binder.forField(aceEditor).bind(SPFile::getFileContent, SPFile::setFileContent);

        // TODO crud buttons
    }
}
