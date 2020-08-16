package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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

import java.util.Optional;

@Route(value = UIConst.ROUTE_PLAYGROUND, layout = MainLayout.class)
@PageTitle(UIConst.TITLE_PLAYGROUND)
public class PlaygroundView extends Div
        implements HasUrlParameter<String>, HasLogger {

    private static final long serialVersionUID = 6587633236690463135L;

    private final SOULPatchService soulPatchService;
    private final UserService userService;
    private final SOULPatchesGridDataProvider dataProvider;

    private final TextField filterText;
    private final SOULPatchesGrid soulPatchesGrid;

    public PlaygroundView(@Autowired SOULPatchService soulPatchService,
                          @Autowired UserService userService,
                          @Autowired SOULPatchesGridDataProvider dataProvider) {
        this.soulPatchService = soulPatchService;
        this.userService = userService;
        this.dataProvider = dataProvider;

        filterText = new TextField("Filter names by");
        filterText.addValueChangeListener(this::filterTextChanged);

        soulPatchesGrid = new SOULPatchesGrid();
        soulPatchesGrid.setDataProvider(dataProvider);
        soulPatchesGrid.addSPFileSelectListener(this::spFileSelected);
        soulPatchesGrid.addSOULPatchRatingsListener(this::soulpatchRating);
        soulPatchesGrid.asSingleSelect().addValueChangeListener(this::soulPatchesGridSingleSelection);

        dataProvider.setPageObserver(this::pageObserved);


        arrangeComponents();
    }

    private void filterTextChanged(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        var filter = new SOULPatchesGridDataProvider.SOULPatchFilter();
        filter.setNamesFilter(
                (event.getValue().isBlank())
                        ? Optional.empty()
                        : Optional.of(event.getValue()));
        dataProvider.setFilter(filter);
    }

    private void soulPatchesGridSingleSelection(AbstractField.ComponentValueChangeEvent<Grid<SOULPatch>, SOULPatch> event) {
        soulPatchesGrid.asSingleSelect().getOptionalValue()
                .ifPresentOrElse(
                        soulPatch -> LOGGER().debug("soulpatch selected {}", soulPatch),
                        () -> LOGGER().debug("nothing selected"));
    }

    private void soulpatchRating(SOULPatchRatingEvent event) {
        LOGGER().debug("soulpatch rating {}", event);
        dataProvider.refreshItem(event.getSoulPatch());
    }

    private void spFileSelected(SPFileSelectEvent event) {
        LOGGER().debug("sp file selected {}", event);
    }

    private void arrangeComponents() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(filterText);
        layout.add(soulPatchesGrid);
        add(layout);
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
