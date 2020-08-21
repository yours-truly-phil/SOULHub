package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SOULPatchRating;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.security.SecurityUtils;
import io.horrorshow.soulhub.ui.events.SOULPatchRatingEvent;
import io.horrorshow.soulhub.ui.events.SPFileSelectEvent;
import org.vaadin.klaudeta.PaginatedGrid;

import static java.lang.String.format;
import static java.lang.String.valueOf;

public class SOULPatchesGrid extends PaginatedGrid<SOULPatch> {

    public static final String COL_NAME = "name";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_FILES = "files";
    public static final String COL_NO_DOWNLOADS = "downloads";
    public static final String KEY_NO_DOWNLOADS = "noViews";
    public static final String COL_AUTHOR = "author";
    public static final String COL_RATINGS = "rating";
    public static final String KEY_RATINGS = "ratings";

    public static final int DEFAULT_PAGE_SIZE = 50;
    public static final int DEFAULT_PAGINATOR_SIZE = 10;

    private static final long serialVersionUID = 3319346975462092870L;

    public SOULPatchesGrid() {
        setClassName("soulpatches-grid");

        setPageSize(DEFAULT_PAGE_SIZE);
        setPaginatorSize(DEFAULT_PAGINATOR_SIZE);

        initColumns();
    }

    private void initColumns() {
        addColumn(SOULPatch::getName)
                .setHeader(COL_NAME)
                .setKey(COL_NAME)
                .setResizable(true)
                .setAutoWidth(true)
                .setFrozen(true)
                .setSortable(true);

        addColumn(getColDescriptionRenderer())
                .setHeader(COL_DESCRIPTION)
                .setKey(COL_DESCRIPTION)
                .setResizable(true)
                .setFlexGrow(10)
                .setSortable(true);

        addColumn(getColSpFilesRenderer())
                .setHeader(COL_FILES)
                .setKey(COL_FILES)
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(false);

        addColumn(getColRatingRenderer())
                .setHeader(COL_RATINGS)
                .setKey(KEY_RATINGS)
                .setResizable(true)
                .setWidth("14em")
                .setSortable(true);

        addColumn(soulPatch -> valueOf(soulPatch.getNoViews()))
                .setHeader(COL_NO_DOWNLOADS)
                .setKey(KEY_NO_DOWNLOADS)
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(true);

        addColumn(soulPatch -> soulPatch.getAuthor().getUserName())
                .setHeader(COL_AUTHOR)
                .setKey(COL_AUTHOR)
                .setResizable(true)
                .setAutoWidth(true)
                .setSortable(false);
    }

    private ComponentRenderer<VerticalLayout, SOULPatch> getColSpFilesRenderer() {
        return new ComponentRenderer<>(sp -> {
            VerticalLayout spFilesLayout = new VerticalLayout();
            if (!sp.getSpFiles().isEmpty()) {
                sp.getSpFiles().forEach(spFile -> {
                    HorizontalLayout layout = new HorizontalLayout();
                    layout.add(
                            new Button(
                                    format("%s [%s]", spFile.getName(),
                                            (spFile.getFileType() != null) ? spFile.getFileType().toString() : ""),
                                    VaadinIcon.FILE_CODE.create(),
                                    event -> spFileButtonClicked(spFile)));

                    spFilesLayout.add(layout);
                });
            } else {
                spFilesLayout.add(new Span("no files attached"));
            }
            return spFilesLayout;
        });
    }

    private ComponentRenderer<HorizontalLayout, SOULPatch> getColRatingRenderer() {
        return new ComponentRenderer<>(sp -> {
            Span rating = new Span(String.format("%.2f", sp.getAverageRating()));
            StarsRating starsRating = new StarsRating();
            starsRating.setValue((int) Math.round(sp.getRatings().stream()
                    .mapToDouble(SOULPatchRating::getStars)
                    .average().orElse(0d)));
            starsRating.setNumstars(5);
            starsRating.setManual(true);
            starsRating.setReadOnly(!SecurityUtils.isUserLoggedIn());
            starsRating.addValueChangeListener(
                    event -> fireEvent(new SOULPatchRatingEvent(this, sp, event.getValue(), event.getOldValue())));
            return new HorizontalLayout(rating, starsRating);
        });
    }

    private ComponentRenderer<Paragraph, SOULPatch> getColDescriptionRenderer() {
        return new ComponentRenderer<>(sp -> {
            Paragraph p = new Paragraph();
            p.setText(sp.getDescription());
            p.addClassName("sp-grid-col-description");
            p.setWidthFull();
            return p;
        });
    }

    private void spFileButtonClicked(SPFile spFile) {
        fireEvent(new SPFileSelectEvent(this, spFile));
    }

    public Registration addSPFileSelectListener(
            ComponentEventListener<SPFileSelectEvent> listener) {
        return addListener(SPFileSelectEvent.class, listener);
    }

    public Registration addSOULPatchRatingsListener(
            ComponentEventListener<SOULPatchRatingEvent> listener) {
        return addListener(SOULPatchRatingEvent.class, listener);
    }
}
