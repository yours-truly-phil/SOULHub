package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.security.SecurityUtils;
import io.horrorshow.soulhub.service.SOULHubUserDetailsService;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.ui.MainLayout;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import static java.lang.String.format;

@Secured(value = "ROLE_USER")
@Route(value = "editsoulpatch", layout = MainLayout.class)
@PageTitle("SOULHub | Edit SOULPatch")
public class EditSOULPatchView extends VerticalLayout implements HasUrlParameter<String> {

    private static final long serialVersionUID = -4704235426941430447L;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SOULPatchService soulPatchService;
    private final SOULHubUserDetailsService userDetailsService;

    private final TextField name = new TextField("Name of the your SOULPatch");
    private final TextArea description = new TextArea("Describe your SOULPatch");
    private final Grid<SPFile> files = new Grid<>();
    private final Button save = new Button("save");
    private final Button delete = new Button("delete SOULPatch");
    private final Button addFile = new Button("add SOUL file");
    private final Binder<SOULPatch> binder = new Binder<>(SOULPatch.class);

    public EditSOULPatchView(@Autowired SOULPatchService soulPatchService, @Autowired SOULHubUserDetailsService userDetailsService) {
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
        files.addColumn(new ComponentRenderer<>(spFile ->
                new Button(spFile.getName(), event ->
                        new Notification(format("%s clicked!", spFile.getName())))))
                .setHeader("filename").setAutoWidth(true);
        files.addColumn(spFile -> spFile.getFileType().toString())
                .setHeader("filetype").setAutoWidth(true);

        save.setWidthFull();
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> saveSOULPatch());

        delete.setWidthFull();
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> deleteSOULPatch());

        addFile.setWidthFull();
        addFile.addClickListener(event -> new Notification("add file clicked").open());
    }

    private void initBinder() {
        binder.forField(name).bind(SOULPatch::getName, SOULPatch::setName);
        binder.forField(description).bind(SOULPatch::getDescription, SOULPatch::setDescription);
    }

    private void arrangeComponents() {
        setSizeFull();
        add(name);
        add(description);
        add(addFile);
        add(files);
        add(save);
        add(delete);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (isPossibleSOULPatchId(parameter)) {
            SOULPatch soulPatch = soulPatchService.findById(Long.valueOf(parameter));
            if (!userDetailsService.isCurrentUserOwnerOf(soulPatch)) {
                logger.debug("user not authorized to edit username={} soulpatch={} urlparameter={} event={}",
                        SecurityUtils.getUsername(), soulPatch, parameter, event);
                createErrorView(format("Insufficient rights to edit SOULPatch %s", parameter));
            } else {
                updateView(soulPatch);
            }
        } else if (parameter.equals("new")) {
            updateView(newSOULPatch());
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
        binder.setBean(soulPatch);
        files.setItems(soulPatch.getSpFiles());
        name.focus();
    }

    private boolean isPossibleSOULPatchId(String parameter) {
        return NumberUtils.isCreatable(parameter)
                && soulPatchService.existsById(Long.valueOf(parameter));
    }

    private SOULPatch newSOULPatch() {
        AppUser currentUser = userDetailsService.loadAppUser(SecurityUtils.getUsername());
        return soulPatchService.createSOULPatch(currentUser);
    }

    private void saveSOULPatch() {
        SOULPatch soulPatch = binder.getBean();
        soulPatchService.save(soulPatch);
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
