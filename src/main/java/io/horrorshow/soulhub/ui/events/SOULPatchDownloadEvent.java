package io.horrorshow.soulhub.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import io.horrorshow.soulhub.data.SOULPatch;

public class SOULPatchDownloadEvent extends ComponentEvent<Component> {

    private static final long serialVersionUID = 6709860142221597167L;

    private final SOULPatch soulPatch;

    public SOULPatchDownloadEvent(Component source, SOULPatch soulPatch) {
        super(source, false);
        this.soulPatch = soulPatch;
    }

    public SOULPatch getSoulPatch() {
        return soulPatch;
    }
}
