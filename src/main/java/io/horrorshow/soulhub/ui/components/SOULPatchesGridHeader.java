package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ReadOnlyHasValue;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.security.SecurityUtils;
import io.horrorshow.soulhub.ui.events.SOULPatchFullTextSearchEvent;
import io.horrorshow.soulhub.ui.events.SOULPatchesFilterEvent;
import io.horrorshow.soulhub.ui.filters.SOULPatchFilter;

import java.util.Objects;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class SOULPatchesGridHeader extends Div
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent
        <SOULPatchesGridHeader, SOULPatchFilter>, SOULPatchFilter> {

    private static final long serialVersionUID = 8863450661909139044L;

    private final TextField namesFilter = new TextField("names filter");
    private final Checkbox showOnlyCurUser = new Checkbox("show only my soulpatches");

    private final Button reset = new Button("reset filter");
    private final Label filterByUsers = new Label("filter by users: ");

    private final TextField fullTextSearch = new TextField("search through file contents and descriptions");
    private final Button fullTextBtn = new Button("Find...");

    private final AbstractFieldSupport<SOULPatchesGridHeader, SOULPatchFilter> fieldSupport;

    private final Binder<SOULPatchFilter> binder = new Binder<>(SOULPatchFilter.class);

    public SOULPatchesGridHeader() {
        fieldSupport = new AbstractFieldSupport<>(
                this, SOULPatchFilter.getEmptyFilter(), Objects::equals, filter -> {
            namesFilter.setValue(filter.getNamesFilter());
            showOnlyCurUser.setValue(filter.isOnlyCurUser());
        });
        setupNamesFilter();

        setupShowOnlyCurUserFilter();

        setupFullTextSearch();

        arrangeComponents();

        initBinder();

        addValueChangeListener(this::soulPatchFilterChanged);
    }

    private void resetFilter() {
        setValue(SOULPatchFilter.getEmptyFilter());
    }

    private void initBinder() {
        reset.addClickListener(event -> resetFilter());

        filterByUsers.setTitle("Filter By Users");

        binder.forField(namesFilter)
                .bind(SOULPatchFilter::getNamesFilter, null);
        binder.forField(showOnlyCurUser)
                .bind(SOULPatchFilter::isOnlyCurUser, null);
        binder.forField(new ReadOnlyHasValue<>(filterByUsers::setText, null))
                .bind(spf -> spf.getAppUserFilter().stream()
                        .map(u -> format("[Id: %d] %s", u.getId(), u.getUserName()))
                        .collect(joining(", ")), null);
    }

    private void soulPatchFilterChanged(
            AbstractField.ComponentValueChangeEvent<SOULPatchesGridHeader, SOULPatchFilter> event) {
        binder.readBean(event.getValue());
    }

    private void setupFullTextSearch() {
        fullTextBtn.addClickListener(this::fullTextSearchBtnClicked);
    }

    private void fullTextSearchBtnClicked(ClickEvent<Button> event) {
        fireEvent(new SOULPatchFullTextSearchEvent(this, fullTextSearch.getValue()));
    }

    private void setupNamesFilter() {
        namesFilter.addValueChangeListener(e -> filterComponentsInteracted());
        namesFilter.setPlaceholder("Filter SOULPatch names...");
        namesFilter.setClearButtonVisible(true);
        namesFilter.setValueChangeMode(ValueChangeMode.EAGER);
    }

    private void setupShowOnlyCurUserFilter() {
        showOnlyCurUser.setValue(false);
        var b = SecurityUtils.isUserLoggedIn();
        showOnlyCurUser.addValueChangeListener(e -> filterComponentsInteracted());
        showOnlyCurUser.setVisible(b);
    }

    private void filterComponentsInteracted() {
        SOULPatchFilter filter = SOULPatchFilter.getEmptyFilter();
        filter.setNamesFilter(namesFilter.getValue());
        filter.setOnlyCurUser(showOnlyCurUser.getValue());
        setValue(filter);
    }

    private void arrangeComponents() {
        VerticalLayout filters =
                new VerticalLayout(namesFilter, showOnlyCurUser);
        VerticalLayout fullText =
                new VerticalLayout(fullTextSearch, fullTextBtn);
        VerticalLayout status =
                new VerticalLayout(filterByUsers, reset);
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(false);
        layout.add(filters, fullText, status);
        add(layout);
    }

    public Registration addSOULPatchesFilterListener(
            ComponentEventListener<SOULPatchesFilterEvent> listener) {
        return addListener(SOULPatchesFilterEvent.class, listener);
    }

    public Registration addFullTextSearchListener(
            ComponentEventListener<SOULPatchFullTextSearchEvent> listener) {
        return addListener(SOULPatchFullTextSearchEvent.class, listener);
    }

    @Override
    public SOULPatchFilter getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public void setValue(SOULPatchFilter value) {
        fieldSupport.setValue(value);
    }

    @Override
    public Registration addValueChangeListener(
            ValueChangeListener<? super AbstractField
                    .ComponentValueChangeEvent<SOULPatchesGridHeader, SOULPatchFilter>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }
}
