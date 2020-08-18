package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import io.horrorshow.soulhub.HasLogger;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import io.horrorshow.soulhub.ui.components.SOULPatchesGrid;
import io.horrorshow.soulhub.ui.presenter.SOULPatchesPresenter;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = UIConst.ROUTE_PLAYGROUND, layout = MainLayout.class)
@PageTitle(UIConst.TITLE_PLAYGROUND)
public class PlaygroundView extends Div
        implements HasUrlParameter<String>, HasLogger {

    private static final long serialVersionUID = 6587633236690463135L;

    private final TextField filterText = new TextField("Filter names by");
    private final SOULPatchesGrid soulPatchesGrid = new SOULPatchesGrid();

    private final SOULPatchesPresenter soulPatchesPresenter;

    public PlaygroundView(@Autowired SOULPatchesPresenter soulPatchesPresenter) {
        this.soulPatchesPresenter = soulPatchesPresenter;
        soulPatchesPresenter.init(this);
        soulPatchesGrid.setHeightByRows(true);

        arrangeComponents();
    }

    public SOULPatchesGrid getGrid() {
        return this.soulPatchesGrid;
    }

    public TextField getFilterText() {
        return this.filterText;
    }

    private void arrangeComponents() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(filterText);
        layout.add(soulPatchesGrid);
        add(layout);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        var parameterMap = event.getLocation().getQueryParameters().getParameters();
        soulPatchesPresenter.onNavigation(parameter, parameterMap);
    }
}
