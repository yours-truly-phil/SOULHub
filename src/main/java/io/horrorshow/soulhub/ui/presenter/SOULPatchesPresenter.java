package io.horrorshow.soulhub.ui.presenter;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import io.horrorshow.soulhub.HasLogger;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.ui.dataproviders.SOULPatchesGridDataProvider;
import io.horrorshow.soulhub.ui.events.SOULPatchRatingEvent;
import io.horrorshow.soulhub.ui.events.SPFileSelectEvent;
import io.horrorshow.soulhub.ui.views.PlaygroundView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SOULPatchesPresenter implements HasLogger {

    private final SOULPatchesGridDataProvider dataProvider;
    private PlaygroundView view;

    public SOULPatchesPresenter(@Autowired SOULPatchesGridDataProvider dataProvider) {
        this.dataProvider = dataProvider;

        dataProvider.setPageObserver(this::observePage);
    }

    private void observePage(Page<SOULPatch> soulPatchPage) {
        LOGGER().debug("page observer soulpatches: {}, pages: {}",
                soulPatchPage.getTotalElements(),
                soulPatchPage.getTotalPages());
    }

    public void init(PlaygroundView view) {
        this.view = view;
        view.getGrid().setDataProvider(dataProvider);
        view.getGrid().addSPFileSelectListener(this::spFileSelected);
        view.getGrid().addSOULPatchRatingsListener(this::soulpatchRating);
        view.getGrid().asSingleSelect().addValueChangeListener(this::soulPatchesGridSelection);
        view.getFilterText().addValueChangeListener(this::filterTextChanged);
    }

    private void filterTextChanged(
            AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        var filter = new SOULPatchesGridDataProvider.SOULPatchFilter();
        filter.setNamesFilter(
                (event.getValue().isBlank())
                        ? Optional.empty()
                        : Optional.of(event.getValue()));
        dataProvider.setFilter(filter);
    }

    private void soulPatchesGridSelection(
            AbstractField.ComponentValueChangeEvent<Grid<SOULPatch>, SOULPatch> event) {
        view.getGrid().asSingleSelect().getOptionalValue()
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

    public void onNavigation(String parameter, Map<String, List<String>> parameterMap) {
        LOGGER().debug("on navigation with parameter: {} and parameterMap {}",
                parameter, parameterMap.toString());
    }
}
