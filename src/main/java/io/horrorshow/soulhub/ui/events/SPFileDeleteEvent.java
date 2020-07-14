package io.horrorshow.soulhub.ui.events;

import com.vaadin.flow.component.ComponentEvent;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.ui.components.SOULFileEditor;

public class SPFileDeleteEvent extends ComponentEvent<SOULFileEditor> {

    private static final long serialVersionUID = 1704037714964427687L;

    private final SPFile spFile;

    public SPFileDeleteEvent(SOULFileEditor source, SPFile spFile) {
        super(source, false);
        this.spFile = spFile;
    }

    public SPFile getSpFile() {
        return spFile;
    }
}
