package io.horrorshow.soulhub.ui.events;

import com.vaadin.flow.component.ComponentEvent;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.ui.components.SOULPatchForm;

public class SPFileSelectEvent extends ComponentEvent<SOULPatchForm> {

    private static final long serialVersionUID = 7100683187040555239L;

    private final SPFile spFile;

    public SPFileSelectEvent(SOULPatchForm source, SPFile spFile) {
        super(source, false);
        this.spFile = spFile;
    }

    public SPFile getSpFile() {
        return spFile;
    }
}
