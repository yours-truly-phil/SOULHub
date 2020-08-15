package io.horrorshow.soulhub.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import io.horrorshow.soulhub.data.SPFile;

public class SPFileDownloadEvent extends ComponentEvent<Component> {

    private static final long serialVersionUID = 5560769012791261213L;

    private final SPFile spFile;

    public SPFileDownloadEvent(Component source, SPFile spFile) {
        super(source, false);
        this.spFile = spFile;
    }

    public SPFile getSpFile() {
        return spFile;
    }
}
