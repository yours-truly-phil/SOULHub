package io.horrorshow.soulhub.ui.presenter;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.annotation.SpringComponent;
import io.horrorshow.soulhub.HasLogger;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.service.UserService;
import io.horrorshow.soulhub.ui.UIConst;
import io.horrorshow.soulhub.ui.components.SOULPatchesGridHeader;
import io.horrorshow.soulhub.ui.dataproviders.SOULPatchesGridDataProvider;
import io.horrorshow.soulhub.ui.events.SOULPatchFullTextSearchEvent;
import io.horrorshow.soulhub.ui.events.SOULPatchRatingEvent;
import io.horrorshow.soulhub.ui.events.SPFileSelectEvent;
import io.horrorshow.soulhub.ui.filters.SOULPatchFilter;
import io.horrorshow.soulhub.ui.views.PlaygroundView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SOULPatchesPresenter implements HasLogger {

    private final SOULPatchesGridDataProvider dataProvider;
    private final UserService userService;
    private PlaygroundView view;

    public SOULPatchesPresenter(@Autowired SOULPatchesGridDataProvider dataProvider,
                                @Autowired UserService userService) {
        this.dataProvider = dataProvider;
        this.userService = userService;

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
        view.getHeader().addFullTextSearchListener(this::fullTextSearchEvent);
        view.getHeader().addValueChangeListener(this::soulpatchesHeaderChanged);
    }

    private void soulpatchesHeaderChanged(
            AbstractField.ComponentValueChangeEvent<SOULPatchesGridHeader, SOULPatchFilter> event) {
        LOGGER().debug("soulpatch header value changed: {}", event.getValue());
        var filter = new SOULPatchService.SOULPatchesFetchFilter();
        filter.setNamesFilter(event.getValue().getNamesFilter());
        if (event.getValue().isOnlyCurUser()) {
            userService.getCurrentAppUser().ifPresent(appUser ->
                    filter.getUsersFilter().add(appUser));
        }
        dataProvider.setFilter(filter);
    }

    private void soulPatchesGridSelection(
            AbstractField.ComponentValueChangeEvent<Grid<SOULPatch>, SOULPatch> event) {
        view.getGrid().asSingleSelect().getOptionalValue()
                .ifPresentOrElse(
                        soulPatch -> LOGGER().debug("soulpatch selected {}", soulPatch),
                        () -> LOGGER().debug("nothing selected"));
    }

    private void fullTextSearchEvent(SOULPatchFullTextSearchEvent event) {
        LOGGER().debug("full text search event: {}", event.getValue());
    }

    private void soulpatchRating(SOULPatchRatingEvent event) {
        LOGGER().debug("soulpatch rating {}", event);
        dataProvider.refreshItem(event.getSoulPatch());
    }

    private void spFileSelected(SPFileSelectEvent event) {
        LOGGER().debug("sp file selected {}", event);
    }

    public void onNavigation(String parameter, Map<String, List<String>> parameterMap) {

        if (parameterMap.containsKey(UIConst.PARAM_SHOW_BY_CURRENT_USER)
                && parameterMap.get(UIConst.PARAM_SHOW_BY_CURRENT_USER).stream()
                .anyMatch(Boolean.TRUE.toString()::equalsIgnoreCase)) {

            view.getHeader().setValue(SOULPatchFilter.getOnlyCurrentUser());
        }
        LOGGER().debug("on navigation with parameter: {} and parameterMap {}",
                parameter, parameterMap.toString());
    }
}
