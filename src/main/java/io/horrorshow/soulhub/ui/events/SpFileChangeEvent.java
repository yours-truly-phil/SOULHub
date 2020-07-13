package io.horrorshow.soulhub.ui.events;

import com.vaadin.flow.component.ComponentEvent;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.ui.components.SOULFileEditor;

public class SpFileChangeEvent extends ComponentEvent<SOULFileEditor> {

    private static final long serialVersionUID = -8771024784630177208L;

    private final SPFile spFile;

    public SpFileChangeEvent(SOULFileEditor component, SPFile spFile) {
        super(component, false);
        this.spFile = spFile;
    }

    public SPFile getSpFile() {
        return spFile;
    }
}
