package io.horrorshow.soulhub.ui.events;

import com.vaadin.flow.component.ComponentEvent;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.ui.components.SOULPatchesGrid;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SOULPatchRatingEvent extends ComponentEvent<SOULPatchesGrid> {

    private static final long serialVersionUID = -6435075410376628669L;

    private final SOULPatch soulPatch;
    private final Integer value;
    private final Integer oldValue;

    public SOULPatchRatingEvent(SOULPatchesGrid source,
                                SOULPatch soulPatch,
                                Integer value,
                                Integer oldValue) {
        super(source, false);
        this.soulPatch = soulPatch;
        this.value = value;
        this.oldValue = oldValue;
    }
}
