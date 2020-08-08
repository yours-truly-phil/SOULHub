package io.horrorshow.soulhub.ui.events;

import com.vaadin.flow.component.ComponentEvent;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.ui.components.SOULFileUpload;

public class SPFileUploadedEvent extends ComponentEvent<SOULFileUpload> {

    private static final long serialVersionUID = -503341689039604270L;

    private final SPFile spFile;

    public SPFileUploadedEvent(SOULFileUpload source, SPFile spFile) {
        super(source, false);
        this.spFile = spFile;
    }

    public SPFile getSpFile() {
        return spFile;
    }
}
