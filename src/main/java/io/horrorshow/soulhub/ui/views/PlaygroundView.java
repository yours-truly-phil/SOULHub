package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import io.horrorshow.soulhub.HasLogger;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SOULPatchRating;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.security.SecurityUtils;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.service.UserService;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import io.horrorshow.soulhub.ui.components.StarsRating;
import io.horrorshow.soulhub.ui.dataproviders.SOULPatchesGridDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Comparator;

import static java.lang.String.format;
import static java.lang.String.valueOf;

@Route(value = UIConst.ROUTE_PLAYGROUND, layout = MainLayout.class)
@PageTitle(UIConst.TITLE_PLAYGROUND)
public class PlaygroundView extends Div
        implements HasUrlParameter<String>, HasLogger {

    private static final long serialVersionUID = 6587633236690463135L;

    private static final String COL_NAME = "name";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_FILES = "files";
    private static final String COL_VIEWS = "views";
    private static final String COL_AUTHOR = "author";
    private static final String COL_RATINGS = "rating";

    private final SOULPatchService soulPatchService;
    private final UserService userService;
    private final SOULPatchesGridDataProvider dataProvider;

    private final Grid<SOULPatch> grid = new Grid<>();

    public PlaygroundView(@Autowired SOULPatchService soulPatchService,
                          @Autowired UserService userService,
                          @Autowired SOULPatchesGridDataProvider dataProvider) {
        this.soulPatchService = soulPatchService;
        this.userService = userService;
        this.dataProvider = dataProvider;

        dataProvider.setPageObserver(this::pageObserved);

        grid.setDataProvider(dataProvider);

        gridColumns();

        arrangeComponents();
    }

    private void gridColumns() {
        grid.addColumn(SOULPatch::getName)
                .setHeader(COL_NAME)
                .setResizable(true)
                .setAutoWidth(true)
                .setFrozen(true)
                .setKey(COL_NAME)
                .setSortable(true);

        grid.addColumn(getColDescriptionRenderer())
                .setHeader(COL_DESCRIPTION)
                .setKey(COL_DESCRIPTION)
                .setFlexGrow(10)
                .setResizable(true)
                .setSortable(true)
                .setComparator(Comparator.comparingInt(sp -> sp.getDescription().length()));

        grid.addColumn(getColSpFilesRenderer())
                .setHeader(COL_FILES)
                .setAutoWidth(true)
                .setResizable(true)
                .setSortable(true)
                .setKey(COL_FILES)
                .setComparator(Comparator.comparingInt(sp -> sp.getSpFiles().size()));

        grid.addColumn(getColRatingRenderer())
                .setHeader(COL_RATINGS)
                .setKey(COL_RATINGS)
                .setWidth("14em")
                .setSortable(true)
                .setResizable(true)
                .setComparator(Comparator.comparingDouble(soulPatch ->
                        soulPatch.getRatings().stream()
                                .mapToDouble(SOULPatchRating::getStars)
                                .average().orElse(0d)));

        grid.addColumn(soulPatch -> valueOf(soulPatch.getNoViews()))
                .setHeader(COL_VIEWS)
                .setResizable(true)
                .setAutoWidth(true)
                .setKey(COL_VIEWS)
                .setSortable(true)
                .setComparator(Comparator.comparingLong(SOULPatch::getNoViews));

        grid.addColumn(soulPatch -> soulPatch.getAuthor().getUserName())
                .setHeader(COL_AUTHOR)
                .setResizable(true)
                .setAutoWidth(true)
                .setKey(COL_AUTHOR)
                .setSortable(true)
                .setComparator(Comparator.comparing(sp -> sp.getAuthor().getUserName()));
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
                                    event -> openSpFile(spFile)));

                    spFilesLayout.add(layout);
                });
            } else {
                spFilesLayout.add(new Span("no files attached"));
            }
            return spFilesLayout;
        });
    }

    private void openSpFile(SPFile spFile) {
        LOGGER().debug("open sp file {}", spFile);
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
                    ratingEvent -> currentUserSOULPatchRating(sp, ratingEvent));
            return new HorizontalLayout(rating, starsRating);
        });
    }

    private void currentUserSOULPatchRating(SOULPatch soulPatch,
                                            AbstractField.ComponentValueChangeEvent<StarsRating, Integer> event) {
        LOGGER().debug("soulpatch rating event {} for soulpatch {}", event, soulPatch);

        if (SecurityUtils.isUserLoggedIn() && userService.getCurrentAppUser().isPresent()) {
            AppUser currentUser = userService.getCurrentAppUser().get();
            soulPatch.getRatings().stream()
                    .filter(soulPatchRating ->
                            soulPatchRating.getAppUser()
                                    .equals(currentUser)).distinct().findAny()
                    .ifPresentOrElse(soulPatchRating -> {
                                // user rating present
                                soulPatchRating.setStars(event.getValue());
                                soulPatchService.save(soulPatch);

                                LOGGER().debug("rating by {} exists {}",
                                        currentUser.getUserName(),
                                        soulPatchRating.toString());
                            },
                            () -> {
                                // no rating present
                                SOULPatchRating rating = new SOULPatchRating();
                                rating.setAppUser(currentUser);
                                rating.setSoulPatch(soulPatch);
                                rating.setStars(event.getValue());
                                soulPatch.getRatings().add(rating);
                                soulPatchService.save(soulPatch);

                                LOGGER().debug("no rating by {} exists",
                                        currentUser.getUserName());
                            });
        } else {
            event.getSource().setNumstars(event.getOldValue());
            new Notification("log in to rate soulpatches", 3000, Notification.Position.MIDDLE).open();
        }
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

    private void arrangeComponents() {
        add(grid);
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
