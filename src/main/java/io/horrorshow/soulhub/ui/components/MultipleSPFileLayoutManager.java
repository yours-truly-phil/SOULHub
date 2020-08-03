package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.service.SOULHubUserDetailsService;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.ui.events.SPFileDeleteEvent;
import io.horrorshow.soulhub.ui.events.SPFileSaveEvent;
import io.horrorshow.soulhub.ui.views.EditSPFileView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MultipleSPFileLayoutManager extends VerticalLayout {

    private static final long serialVersionUID = 2456720596209534505L;

    private final SOULPatchService soulPatchService;
    private final SOULHubUserDetailsService userDetailsService;

    private final Checkbox isOpenMultipleFiles = new Checkbox();

    private final Map<Long, Runnable> openSpFiles = new HashMap<>();

    public MultipleSPFileLayoutManager(@Autowired SOULPatchService soulPatchService,
                                       @Autowired SOULHubUserDetailsService userDetailsService) {
        this.soulPatchService = soulPatchService;
        this.userDetailsService = userDetailsService;

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

        add(isOpenMultipleFiles);
    }

    public void showSpFile(final SPFile spFile) {
        VerticalLayout soulFileEditorLayout = new VerticalLayout();
        soulFileEditorLayout.addClassName("soulfile-editor");

        SOULFileEditor soulFileEditor =
                new SOULFileEditor(soulPatchService, userDetailsService);
        soulFileEditor.setValue(spFile);
        soulFileEditor.addSpFileSavedListener(this::spFileSaved);
        soulFileEditor.addSpFileDeleteListener(this::spFileDeleted);

        Button removeFileEditorButton = new Button("close file editor");
        removeFileEditorButton.addClickListener(event ->
                closeSpFileEditor(spFile.getId()));

        if (spFile.getId() > -1) {
            RouterLink showFileInSPFileView =
                    new RouterLink("maximize editor",
                            EditSPFileView.class, spFile.getId().toString());
            soulFileEditorLayout.add(showFileInSPFileView);
        }
        soulFileEditorLayout.add(soulFileEditor);
        soulFileEditorLayout.add(removeFileEditorButton);

        add(soulFileEditorLayout);

        if (!isOpenMultipleFiles.getValue() && openSpFiles.size() > 0) {
            openSpFiles.values().forEach(Runnable::run);
            openSpFiles.clear();
        }
        if (openSpFiles.containsKey(spFile.getId())) {
            closeSpFileEditor(spFile.getId());
        }
        openSpFiles.put(
                spFile.getId(),
                () -> remove(soulFileEditorLayout));
    }

    private void spFileDeleted(SPFileDeleteEvent event) {
        closeSpFileEditor(event.getSpFile().getId());
        fireEvent(event);
    }

    private void closeSpFileEditor(Long spFileId) {
        openSpFiles.remove(spFileId).run();
    }

    private void spFileSaved(SPFileSaveEvent event) {
        if (event.isNew()) {
            openSpFiles.put(event.getSpFile().getId(),
                    openSpFiles.remove(-1L));
        }
        if (!Objects.equals(event.getOldSpFile().getId(), event.getSpFile().getId())) {
            openSpFiles.put(
                    event.getSpFile().getId(),
                    openSpFiles.remove(event.getOldSpFile().getId()));
        }
        fireEvent(event);
    }

    public Registration addSpFileSavedListener(ComponentEventListener<SPFileSaveEvent> listener) {
        return addListener(SPFileSaveEvent.class, listener);
    }

    public Registration addSpFileDeleteListener(ComponentEventListener<SPFileDeleteEvent> listener) {
        return addListener(SPFileDeleteEvent.class, listener);
    }
}
