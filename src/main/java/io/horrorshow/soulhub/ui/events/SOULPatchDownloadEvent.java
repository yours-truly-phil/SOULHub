package io.horrorshow.soulhub.ui.events;

import com.vaadin.flow.component.ComponentEvent;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.ui.components.SOULPatchForm;

public class SOULPatchDownloadEvent extends ComponentEvent<SOULPatchForm> {

    private static final long serialVersionUID = 6709860142221597167L;

    private final SOULPatch soulPatch;

    public SOULPatchDownloadEvent(SOULPatchForm source, SOULPatch soulPatch) {
        super(source, false);
        this.soulPatch = soulPatch;
    }

    public SOULPatch getSoulPatch() {
        return soulPatch;
    }
}
