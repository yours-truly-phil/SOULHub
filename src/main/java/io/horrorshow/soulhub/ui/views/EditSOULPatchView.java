package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.security.SecurityUtils;
import io.horrorshow.soulhub.service.SOULHubUserDetailsService;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import io.horrorshow.soulhub.ui.components.SOULFileEditor;
import io.horrorshow.soulhub.ui.components.SOULFileUpload;
import io.horrorshow.soulhub.ui.events.SPFileSaveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.lang.String.format;

@Secured(value = UIConst.ROLE_USER)
@Route(value = UIConst.ROUTE_EDIT_SOULPATCH, layout = MainLayout.class)
@PageTitle(UIConst.TITLE_EDIT_SOULPATCH)
public class EditSOULPatchView extends VerticalLayout implements HasUrlParameter<String> {

    private static final long serialVersionUID = -4704235426941430447L;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SOULPatchService soulPatchService;
    private final SOULHubUserDetailsService userDetailsService;

    private final RouterLink toSOULPatchesViewLink =
            new RouterLink(format("navigate back to %s", UIConst.LINK_TEXT_SOULPATCHES),
                    SOULPatchesView.class);

    private final TextField name = new TextField();
    private final TextArea description = new TextArea();
    private final Grid<SPFile> files = new Grid<>();
    private final Button save = new Button("save");
    private final Button delete = new Button("delete SOULPatch");
    private final Button addFile = new Button("open SOULFile-Editor with new empty file");

    private final VerticalLayout soulFileEditorsLayout = new VerticalLayout();

    private final Binder<SOULPatch> binder = new Binder<>(SOULPatch.class);

    private final SOULFileUpload soulFileUpload = new SOULFileUpload();

    private final Map<Long, Runnable> openSpFiles = new HashMap<>();

    private final Checkbox isOpenMultipleFiles = new Checkbox();

    private SOULPatch soulPatch;

    public EditSOULPatchView(@Autowired SOULPatchService soulPatchService,
                             @Autowired SOULHubUserDetailsService userDetailsService) {
        this.soulPatchService = soulPatchService;
        this.userDetailsService = userDetailsService;

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
        files.addColumn(SPFile::getName)
                .setHeader("filename").setAutoWidth(true);
        files.addColumn(spFile -> (spFile.getFileType() != null) ? spFile.getFileType().toString() : "")
                .setHeader("filetype").setAutoWidth(true);

        files.asSingleSelect().addValueChangeListener(event ->
                files.asSingleSelect().getOptionalValue()
                        .ifPresent(this::showSpFile));

        save.setWidthFull();
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> saveSOULPatch());

        delete.setWidthFull();
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> deleteSOULPatch());

        addFile.setWidthFull();
        addFile.addClickListener(event -> addSpFile());

        isOpenMultipleFiles.setLabel("open multiple files");
        isOpenMultipleFiles.setValue(true);
        isOpenMultipleFiles.addValueChangeListener(event -> {
            if (!event.getValue() && openSpFiles.size() > 1) {
                isOpenMultipleFiles.setValue(true);
                new Notification("close open soul file editors first",
                        5000,
                        Notification.Position.MIDDLE)
                        .open();
            }
        });
    }

    private void addSpFile() {
        SPFile spFile = soulPatchService.createSPFile(soulPatch);
        showSpFile(spFile);
    }

    private void showSpFile(final SPFile spFile) {
        VerticalLayout soulFileEditorLayout = new VerticalLayout();
        soulFileEditorLayout.addClassName("soulfile-editor");

        SOULFileEditor soulFileEditor =
                new SOULFileEditor(soulPatchService, userDetailsService);
        soulFileEditor.setValue(spFile);
        soulFileEditor.addSpFileSavedListener(this::spFileChange);
        soulFileEditor.addSpFileDeleteListener(event -> {
            logger.debug("spFile removed: {}", event.getSpFile());
            updateView(event.getSpFile().getSoulPatch());
            closeSpFileEditor(spFile.getId());
        });

        Button removeFileEditorButton = new Button("close file editor");
        removeFileEditorButton.addClickListener(event ->
                closeSpFileEditor(spFile.getId()));

        if (spFile.getId() != null) {
            RouterLink showFileInSPFileView =
                    new RouterLink("maximize editor",
                            EditSPFileView.class, spFile.getId().toString());
            soulFileEditorLayout.add(showFileInSPFileView);
        }
        soulFileEditorLayout.add(soulFileEditor);
        soulFileEditorLayout.add(removeFileEditorButton);

        soulFileEditorsLayout.add(soulFileEditorLayout);

        if (!isOpenMultipleFiles.getValue() && openSpFiles.size() > 0) {
            logger.debug("openSpFiles clearing before: {}", openSpFiles.toString());
            openSpFiles.values().forEach(Runnable::run);
            openSpFiles.clear();
            logger.debug("openSpFiles clearing after: {}", openSpFiles.toString());
        }
        if (openSpFiles.containsKey(spFile.getId())) {
            closeSpFileEditor(spFile.getId());
        }
        openSpFiles.put(
                spFile.getId(),
                () -> soulFileEditorsLayout.remove(soulFileEditorLayout));
        logger.debug("openSpFiles: {}", openSpFiles.toString());
    }

    private void closeSpFileEditor(Long spFileId) {
        openSpFiles.remove(spFileId).run();
        logger.debug("closeSpFileEditor: {}", openSpFiles);
    }

    private void spFileChange(SPFileSaveEvent event) {
        logger.debug("spFileChange:IsNew: {}\nNew: {}\nOld: {}", event.isNew(), event.getSpFile(), event.getOldSpFile());
        logger.debug("spFileChange Before: {}", openSpFiles.toString());
        if (event.isNew()) {
            openSpFiles.put(event.getSpFile().getId(),
                    openSpFiles.remove(null));
        }
        if (!Objects.equals(event.getOldSpFile().getId(), event.getSpFile().getId())) {
            openSpFiles.put(
                    event.getSpFile().getId(),
                    openSpFiles.remove(event.getOldSpFile().getId()));
        }
        logger.debug("spFileChange After: {}", openSpFiles.toString());
        updateView(event.getSpFile().getSoulPatch());
    }

    private void initBinder() {
        binder.forField(name)
                .asRequired("SOULPatch needs a name")
                .bind(SOULPatch::getName, SOULPatch::setName);
        binder.forField(description)
                .asRequired("Description must not be empty")
                .bind(SOULPatch::getDescription, SOULPatch::setDescription);
        binder.addStatusChangeListener(event -> {
            boolean isValid = event.getBinder().isValid();
            boolean hasChanges = event.getBinder().hasChanges();
            logger.debug("isValid: {} hasChanges: {}", isValid, hasChanges);

            save.setEnabled(hasChanges && isValid);
        });
    }

    private void arrangeComponents() {
        setSizeFull();


        FormLayout formLayout = new FormLayout();
        formLayout.addFormItem(name, "SOULPatch Name");
        formLayout.addFormItem(description, "Describe your SOULPatch to other users");

        HorizontalLayout spButtons = new HorizontalLayout();
        spButtons.add(save);
        spButtons.add(delete);

        VerticalLayout createSpFileSection = new VerticalLayout();
        createSpFileSection.add(addFile, soulFileUpload);

        FormLayout spFilesOverviewSection = new FormLayout();
        spFilesOverviewSection.add(createSpFileSection);
        spFilesOverviewSection.addFormItem(files, "click on file to open");
        spFilesOverviewSection.add(isOpenMultipleFiles);

        add(toSOULPatchesViewLink);
        add(formLayout);
        add(spButtons);
        add(spFilesOverviewSection);
        add(soulFileEditorsLayout);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (soulPatchService.isPossibleSOULPatchId(parameter)) {
            SOULPatch soulPatch = soulPatchService.findById(Long.valueOf(parameter));
            if (!userDetailsService.isCurrentUserOwnerOf(soulPatch)) {
                logger.debug("user not authorized to edit username={} soulpatch={} url-parameter={} event={}",
                        SecurityUtils.getUsername(), soulPatch, parameter, event);
                createErrorView(format("Insufficient rights to edit SOULPatch %s", parameter));
            } else {
                updateView(soulPatch);
            }
        } else if (parameter != null && parameter.equals("new")) {
            updateView(createSOULPatchForCurrentUser());
        } else {
            logger.debug("invalid access. parameter={} user={} event={}",
                    parameter, SecurityUtils.getUsername(), event);
            createErrorView(format("Unable to serve request to edit SOULPatch %s", parameter));
        }
    }

    private void createErrorView(String message) {
        removeAll();
        add(new H1(format("Lol, what did you do?! '%s'", message)));
        add(new RouterLink("to SOULPatches view", SOULPatchesView.class));
    }

    private void updateView(SOULPatch soulPatch) {
        this.soulPatch = soulPatchService.findById(soulPatch.getId());
        binder.readBean(this.soulPatch);
        files.setItems(this.soulPatch.getSpFiles());
        name.focus();
    }

    private SOULPatch createSOULPatchForCurrentUser() {
        AppUser currentUser = userDetailsService.loadAppUser(SecurityUtils.getUsername());
        return soulPatchService.createSOULPatch(currentUser);
    }

    private void saveSOULPatch() {
        try {
            binder.writeBean(soulPatch);
            soulPatchService.save(soulPatch);
        } catch (ValidationException e) {
            logger.debug(e.getMessage());
        }
        new Notification(format("soulpatch %s saved", soulPatch.getName()),
                3000).open();
    }

    private void deleteSOULPatch() {
        SOULPatch soulPatch = binder.getBean();
        soulPatchService.delete(soulPatch);
        new Notification(format("soulpatch %s removed", soulPatch.getName()),
                3000).open();
        UI.getCurrent().navigate(SOULPatchesView.class);
    }
}
