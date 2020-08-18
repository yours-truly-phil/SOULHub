package io.horrorshow.soulhub.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import io.horrorshow.soulhub.ui.filters.SOULPatchFilter;

public class SOULPatchesFilterEvent extends ComponentEvent<Component> {

    private static final long serialVersionUID = -8024006512138533311L;

    private final SOULPatchFilter filter;

    public SOULPatchesFilterEvent(Component source, SOULPatchFilter filter) {
        super(source, false);
        this.filter = filter;
    }

    public SOULPatchFilter getFilter() {
        return filter;
    }
}
