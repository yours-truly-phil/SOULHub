package io.horrorshow.soulhub.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import io.horrorshow.soulhub.data.SPFile;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SPFileSelectEvent extends ComponentEvent<Component> {

    private static final long serialVersionUID = -78503947435075563L;

    private final SPFile spFile;

    public SPFileSelectEvent(Component source, SPFile spFile) {
        super(source, false);
        this.spFile = spFile;
    }
}
