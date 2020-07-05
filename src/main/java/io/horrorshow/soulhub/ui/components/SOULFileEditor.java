package io.horrorshow.soulhub.ui.components;

import com.hilerio.ace.AceEditor;
import com.hilerio.ace.AceMode;
import com.hilerio.ace.AceTheme;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.ui.views.SOULPatchesView;

import java.time.format.DateTimeFormatter;

public class SOULFileEditor extends VerticalLayout {

    private static final long serialVersionUID = -6950603059471911545L;
    private final SOULPatchesView SOULPatchesView;

    private final TextField name = new TextField("filename");
    private final AceEditor aceEditor = new AceEditor();
    private final TextField createdAt = new TextField("created at");
    private final TextField updatedAt = new TextField("updated at");
    private final TextField fileType = new TextField("filetype");

    private final Button save = new Button("save");
    private final Button delete = new Button("delete");

    private final Binder<SPFile> binder = new Binder<>(SPFile.class);

    public SOULFileEditor(SOULPatchesView SOULPatchesView) {
        super();
        this.SOULPatchesView = SOULPatchesView;
        setSizeFull();

        initFields();

        arrangeComponents();

        initSpFileBinder();
    }

    /**
     * sets the properties of all ui components of this
     * editor, like their visual style, sizes, titles, listeners
     */
    private void initFields() {
        name.setWidth("100%");
        name.setRequired(true);

        createdAt.setTitle("Created at");
        createdAt.setReadOnly(true);

        updatedAt.setTitle("Updated at");
        updatedAt.setReadOnly(true);

        fileType.setTitle("File-Type");
        fileType.setReadOnly(true);

        aceEditor.setTheme(AceTheme.cobalt);
        aceEditor.setMode(AceMode.c_cpp);

        aceEditor.setSofttabs(true);
        aceEditor.setTabSize(4);
        aceEditor.setWrap(false);

        aceEditor.setSizeFull();
        aceEditor.setMinlines(10);

        aceEditor.setPlaceholder("SOULFile content");

        aceEditor.addFocusListener(e ->
                System.out.println("aceEditor focus listener bam"));


        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> save());

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> delete());
    }

    /**
     * places the components at the right locations relative to each other
     * into the gui
     */
    private void arrangeComponents() {
        add(name);
        add(createdAt);
        add(updatedAt);
        add(fileType);
        add(aceEditor);
        add(save);
        add(delete);
    }

    /**
     * binds SPFile attribute values to the UI components.
     * <p>
     * it's not necessary to have to set values of ui components manually,
     * after binding (in this method), to display another SPFile's contents,
     * it's enough to call binder.setBean(newSpFile)
     */
    private void initSpFileBinder() {
        binder.forField(name).bind(SPFile::getName, SPFile::setName);
        binder.forField(aceEditor).bind(SPFile::getFileContent, SPFile::setFileContent);
        binder.forField(createdAt).bind(it ->
                it.getCreatedAt() != null
                        ? it.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        : "not persisted", null);
        binder.forField(updatedAt).bind(it ->
                it.getUpdatedAt() != null
                        ? it.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        : "not persisted", null);
        binder.forField(fileType).bind(it ->
                it.getFileType() != null
                        ? it.getFileType().toString()
                        : "unknown", null);
    }

    public void showSpFile(SPFile spFile) {
        binder.setBean(spFile);
        setVisible(true);
        aceEditor.focus();
    }

    private void save() {
        SPFile spFile = binder.getBean();
        SOULPatchesView.service.saveSpFile(spFile);
        SOULPatchesView.updateList();
        new Notification(String.format("file %s saved", spFile.getName()),
                3000).open();
    }

    private void delete() {
        SPFile spFile = binder.getBean();
        SOULPatchesView.service.deleteSpFile(spFile);
        SOULPatchesView.updateList();
        new Notification(String.format("file %s removed", spFile.getName()),
                3000).open();
    }
}
