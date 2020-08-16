package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.*;
import io.horrorshow.soulhub.HasLogger;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.service.UserService;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import io.horrorshow.soulhub.ui.components.SOULPatchesGrid;
import io.horrorshow.soulhub.ui.dataproviders.SOULPatchesGridDataProvider;
import io.horrorshow.soulhub.ui.events.SOULPatchRatingEvent;
import io.horrorshow.soulhub.ui.events.SPFileSelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

@Route(value = UIConst.ROUTE_PLAYGROUND, layout = MainLayout.class)
@PageTitle(UIConst.TITLE_PLAYGROUND)
public class PlaygroundView extends Div
        implements HasUrlParameter<String>, HasLogger {

    private static final long serialVersionUID = 6587633236690463135L;

    private final SOULPatchService soulPatchService;
    private final UserService userService;
    private final SOULPatchesGridDataProvider dataProvider;

    private final SOULPatchesGrid soulPatchesGrid;

    public PlaygroundView(@Autowired SOULPatchService soulPatchService,
                          @Autowired UserService userService,
                          @Autowired SOULPatchesGridDataProvider dataProvider) {
        this.soulPatchService = soulPatchService;
        this.userService = userService;
        this.dataProvider = dataProvider;

        soulPatchesGrid = new SOULPatchesGrid();
        soulPatchesGrid.setDataProvider(dataProvider);
        soulPatchesGrid.addSPFileSelectListener(this::spFileSelected);
        soulPatchesGrid.addSOULPatchRatingsListener(this::soulpatchRating);
        soulPatchesGrid.asSingleSelect().addValueChangeListener(this::soulPatchesGridSingleSelection);

        dataProvider.setPageObserver(this::pageObserved);

        arrangeComponents();
    }

    private void soulPatchesGridSingleSelection(AbstractField.ComponentValueChangeEvent<Grid<SOULPatch>, SOULPatch> event) {
        soulPatchesGrid.asSingleSelect().getOptionalValue()
                .ifPresentOrElse(
                        soulPatch -> LOGGER().debug("soulpatch selected {}", soulPatch),
                        () -> LOGGER().debug("nothing selected"));
    }

    private void soulpatchRating(SOULPatchRatingEvent event) {
        LOGGER().debug("soulpatch rating {}", event);
    }

    private void spFileSelected(SPFileSelectEvent event) {
        LOGGER().debug("sp file selected {}", event);
    }

    private void arrangeComponents() {
        add(soulPatchesGrid);
    }

    private void pageObserved(Page<SOULPatch> soulPatches) {
        LOGGER().debug("page observer soulpatches: {}, pages: {}",
                soulPatches.getTotalElements(),
                soulPatches.getTotalPages());
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {

    }
}
