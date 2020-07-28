package io.horrorshow.soulhub.ui.events;

import com.vaadin.flow.component.ComponentEvent;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.ui.components.SOULFileEditor;

public class SPFileSaveEvent extends ComponentEvent<SOULFileEditor> {

    private static final long serialVersionUID = -8771024784630177208L;

    private final SPFile spFile;
    private final SPFile oldSpFile;
    private final boolean isNew;

    public SPFileSaveEvent(SOULFileEditor component, SPFile spFile, SPFile oldSpFile, boolean isNew) {
        super(component, false);
        this.spFile = spFile;
        this.oldSpFile = oldSpFile;
        this.isNew = isNew;
    }

    public SPFile getSpFile() {
        return spFile;
    }

    public SPFile getOldSpFile() {
        return oldSpFile;
    }

    public boolean isNew() {
        return isNew;
    }
}
