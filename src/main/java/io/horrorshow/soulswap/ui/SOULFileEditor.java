package io.horrorshow.soulswap.ui;

import com.hilerio.ace.AceEditor;
import com.hilerio.ace.AceMode;
import com.hilerio.ace.AceTheme;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import io.horrorshow.soulswap.data.SPFile;

import java.time.format.DateTimeFormatter;

public class SOULFileEditor extends VerticalLayout {

    private final MainView mainView;

    private final TextField name = new TextField("filename");
    private final AceEditor aceEditor = new AceEditor();
    private final TextField createdAt = new TextField("created at");
    private final TextField updatedAt = new TextField("updated at");
    private final TextField fileType = new TextField("filetype");

    private final Button save = new Button("save");
    private final Button delete = new Button("delete");

    private final Binder<SPFile> binder = new Binder<>(SPFile.class);

    public SOULFileEditor(MainView mainView) {
        super();
        this.mainView = mainView;
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
                it.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), null);
        binder.forField(updatedAt).bind(it ->
                it.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), null);
        binder.forField(fileType).bind(it -> it.getFileType().toString(), null);
    }

    /**
     * TODO this does two thinks with side effects
     *
     * @param spFile
     *         spFile, if not null, this editor gets shown to the user
     *         if null, hides the editor
     */
    public void setSpFile(SPFile spFile) {
        if (spFile == null) {
            setVisible(false);
        } else {
            binder.setBean(spFile);
            setVisible(true);
            aceEditor.focus();
        }
    }

    private void save() {
        // TODO indicate success to the user
        SPFile spFile = binder.getBean();
        mainView.service.saveSpFile(spFile);
        mainView.updateList();
    }

    private void delete() {
        // TODO confirmation dialog
        SPFile spFile = binder.getBean();
        mainView.service.deleteSpFile(spFile);
        mainView.updateList();
    }
}
