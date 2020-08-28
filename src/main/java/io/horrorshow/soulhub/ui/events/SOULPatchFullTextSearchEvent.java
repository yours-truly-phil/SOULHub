package io.horrorshow.soulhub.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import io.horrorshow.soulhub.ui.components.SOULPatchesGridHeader;
import lombok.Getter;
import lombok.ToString;

@ToString
public class SOULPatchFullTextSearchEvent extends ComponentEvent<Component> {
    private static final long serialVersionUID = 8655698006118599279L;

    @Getter
    public final String value;

    public SOULPatchFullTextSearchEvent(Component source, String value) {
        super(source, false);
        this.value = value;
    }
}
