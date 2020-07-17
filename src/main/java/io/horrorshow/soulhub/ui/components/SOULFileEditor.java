package io.horrorshow.soulhub.ui.components;

import com.hilerio.ace.AceEditor;
import com.hilerio.ace.AceMode;
import com.hilerio.ace.AceTheme;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.service.SOULHubUserDetailsService;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.ui.events.SPFileDeleteEvent;
import io.horrorshow.soulhub.ui.events.SPFileSaveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SOULFileEditor extends VerticalLayout
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<SOULFileEditor, SPFile>, SPFile> {

    private static final long serialVersionUID = -6950603059471911545L;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TextField name = new TextField();
    private final AceEditor aceEditor = new AceEditor();
    private final TextField createdAt = new TextField();
    private final TextField updatedAt = new TextField();
    private final TextField fileType = new TextField();

    private final Button save = new Button("save");
    private final Button delete = new Button("delete");

    private final Select<AceTheme> aceTheme = new Select<>();

    private final Binder<SPFile> binder = new Binder<>(SPFile.class);
    private final SOULPatchService soulPatchService;
    private final SOULHubUserDetailsService userDetailsService;

    private final AbstractFieldSupport<SOULFileEditor, SPFile> fieldSupport;

    public SOULFileEditor(@Autowired SOULPatchService soulPatchService,
                          @Autowired SOULHubUserDetailsService userDetailsService) {
        super();
        this.fieldSupport = new AbstractFieldSupport<>(this, null,
                Objects::equals, spFile -> {
        });

        this.soulPatchService = soulPatchService;
        this.userDetailsService = userDetailsService;

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

        aceTheme.setItems(AceTheme.values());
        aceTheme.addValueChangeListener(event ->
                aceEditor.setTheme(event.getValue()));
        aceTheme.setValue(AceTheme.dracula);
        aceTheme.setLabel("Editor theme");

        aceEditor.setTheme(AceTheme.dracula);
        aceEditor.setMode(AceMode.c_cpp);

        aceEditor.setSofttabs(true);
        aceEditor.setTabSize(4);
        aceEditor.setWrap(false);

        aceEditor.setSizeFull();
        aceEditor.setMinlines(10);

        aceEditor.setPlaceholder("SOULFile content");

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
        FormLayout spFileAttributesLayout = new FormLayout();
        spFileAttributesLayout.addFormItem(name, "Name");
        spFileAttributesLayout.addFormItem(createdAt, "Created At");
        spFileAttributesLayout.addFormItem(updatedAt, "Updated At");
        spFileAttributesLayout.addFormItem(fileType, "File-Type");
        spFileAttributesLayout.addFormItem(aceTheme, "Editor Theme");
        add(spFileAttributesLayout);
        add(aceEditor);
        add(new HorizontalLayout(save, delete));
    }

    /**
     * binds SPFile attribute values to the UI components.
     * <p>
     * it's not necessary to have to set values of ui components manually,
     * after binding (in this method), to display another SPFile's contents,
     * it's enough to call binder.setBean(newSpFile)
     */
    private void initSpFileBinder() {
        binder.forField(name)
                .asRequired("File needs a filename")
                .bind(SPFile::getName, SPFile::setName);
        binder.forField(aceEditor)
                .bind(SPFile::getFileContent, SPFile::setFileContent);
        binder.forField(createdAt)
                .bind(it -> it.getCreatedAt() != null
                        ? it.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        : "not persisted", null);
        binder.forField(updatedAt)
                .bind(it -> it.getUpdatedAt() != null
                        ? it.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        : "not persisted", null);
        binder.forField(fileType)
                .bind(it -> it.getFileType() != null
                        ? it.getFileType().toString()
                        : "unknown", null);

        binder.addValueChangeListener(event -> {
            logger.debug("ValueChangeEvent oldValue: {} newValue: {}",
                    event.getOldValue(), event.getValue());
        });
        binder.addStatusChangeListener(event -> {
            boolean isValid = event.getBinder().isValid();
            boolean hasChanges = event.getBinder().hasChanges();
            logger.debug("StatusChangedEvent from {} isValid: {} hasChanges: {}",
                    event.getSource(), isValid, hasChanges);
            save.setEnabled(hasChanges && isValid);
        });
    }

    private void save() {
        try {
            SPFile spFile = fieldSupport.getValue();
            binder.writeBean(spFile);
            soulPatchService.saveSpFile(spFile);

            new Notification(String.format("file %s saved", spFile.getName()),
                    3000).open();

            fireEvent(new SPFileSaveEvent(this, spFile));
        } catch (ValidationException e) {
            logger.debug(e.getMessage());
        }
    }

    private void delete() {
        SPFile spFile = fieldSupport.getValue();

        soulPatchService.deleteSpFile(spFile);

        new Notification(String.format("file %s removed", spFile.getName()),
                3000).open();

        fireEvent(new SPFileDeleteEvent(this, spFile));
    }

    public Registration addSpFileChangeListener(ComponentEventListener<SPFileSaveEvent> listener) {
        return addListener(SPFileSaveEvent.class, listener);
    }

    public Registration addSpFileDeleteListener(ComponentEventListener<SPFileDeleteEvent> listener) {
        return addListener(SPFileDeleteEvent.class, listener);
    }

    @Override
    public SPFile getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public void setValue(SPFile spFile) {
        fieldSupport.setValue(spFile);
        binder.readBean(spFile);
        setVisible(true);
        aceEditor.focus();
        logger.debug("end of setValue({}) isValid: {} hasChanges: {}",
                spFile.getName(), binder.isValid(), binder.hasChanges());
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<SOULFileEditor, SPFile>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }
}
