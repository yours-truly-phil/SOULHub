package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.BrowserWindowResizeEvent;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SOULPatchRating;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.security.SecurityUtils;
import io.horrorshow.soulhub.service.SOULHubUserDetailsService;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import io.horrorshow.soulhub.ui.components.SOULPatchForm;
import io.horrorshow.soulhub.ui.components.SpFileEditorDialog;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.stream.Collectors;

import static java.lang.String.format;

@Route(value = UIConst.ROUTE_SOULPATCHES, layout = MainLayout.class)
@Getter
@PageTitle(UIConst.TITLE_SOULPATCHES)
public class SOULPatchesView extends VerticalLayout {

    private static final long serialVersionUID = 3981631233877217865L;

    private static final String COL_NAME = "name";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_FILES = "files";
    private static final String COL_VIEWS = "views";
    private static final String COL_AUTHOR = "author";
    private static final String COL_RATINGS = "rating";
    private static final Logger LOGGER = LoggerFactory.getLogger(SOULPatchesView.class);
    private final SOULPatchService service;
    private final SOULHubUserDetailsService userService;
    private final PaginatedGrid<SOULPatch> grid = new PaginatedGrid<>();
    private final TextField filterText = new TextField("filter by (regex)");
    private final Checkbox filterOwnedSoulpatches = new Checkbox("show only my soulpatches");
    private final Button addSOULPatch = new Button("add SOULPatch", VaadinIcon.FILE_ADD.create());
    private final SOULPatchForm form;
    private final SpFileEditorDialog spFileEditorDialog;
    private final Span userGreeting = new Span("Hello!");

    public SOULPatchesView(@Autowired SOULPatchService service, @Autowired SOULHubUserDetailsService userService) {

        this.service = service;
        this.userService = userService;

        spFileEditorDialog = new SpFileEditorDialog(service, userService);
        form = new SOULPatchForm(this, service, userService);

        addClassName("soulpatches-view");

        UI.getCurrent().getPage().addBrowserWindowResizeListener(this::browserWindowResized);

        initFields();

        arrangeComponents();

        form.setVisible(false);

        updateList();
    }

    private void browserWindowResized(BrowserWindowResizeEvent event) {
        grid.getColumnByKey(COL_VIEWS).setVisible(event.getWidth() >= 1050);
        grid.getColumnByKey(COL_AUTHOR).setVisible(event.getWidth() >= 950);
        grid.getColumnByKey(COL_DESCRIPTION).setVisible(event.getWidth() >= 600);
    }

    private void initFields() {
        initFilters();

        initSOULPatchesGrid();

        initAddSOULPatchLink();

        initSOULPatchForm();

        initGreeting();

        initSpFileEditorDialog();
    }

    private void initSpFileEditorDialog() {
        spFileEditorDialog.getEditor().addSpFileChangeListener(event -> updateList());
        spFileEditorDialog.getEditor().addSpFileDeleteListener(event -> {
            spFileEditorDialog.close();
            updateList();
        });
    }

    private void initGreeting() {
        if (SecurityUtils.isUserLoggedIn()) {
            userGreeting.setText(format("Hello %s", SecurityUtils.getUsername()));
        } else {
            userGreeting.setVisible(false);
        }
    }

    private void initSOULPatchForm() {
        form.setMinWidth("30em");
    }

    private void initAddSOULPatchLink() {
        addSOULPatch.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addSOULPatch.addClickListener(e -> UI.getCurrent().navigate(EditSOULPatchView.class, "new"));
    }

    private void initSOULPatchesGrid() {

        addSOULPatchesGridColumns();

        grid.setClassName("soulpatches-grid");

        grid.setColumnReorderingAllowed(true);

        grid.setPageSize(5);

        grid.setPaginatorSize(5);

        grid.asSingleSelect().addValueChangeListener(event ->
                grid.asSingleSelect().getOptionalValue()
                        .ifPresentOrElse(form::showSOULPatch, form::hideSOULPatchForm));
    }

    private void addSOULPatchesGridColumns() {

        grid.addColumn(SOULPatch::getName)
                .setHeader(COL_NAME)
                .setResizable(true).setAutoWidth(true).setFrozen(true)
                .setKey(COL_NAME)
                .setSortable(true);

        grid.addColumn(SOULPatch::getDescription)
                .setHeader(COL_DESCRIPTION)
                .setAutoWidth(true).setResizable(true)
                .setKey(COL_DESCRIPTION);

        grid.addColumn(new ComponentRenderer<>(sp -> {
            VerticalLayout spFilesLayout = new VerticalLayout();
            if (!sp.getSpFiles().isEmpty()) {
                sp.getSpFiles().forEach(spFile -> {
                    HorizontalLayout layout = new HorizontalLayout();
                    layout.add(
                            new Button(
                                    format("%s [%s]", spFile.getName(),
                                            spFile.getFileType().toString()),
                                    VaadinIcon.FILE_CODE.create(),
                                    event -> showFileEditor(spFile)));

                    spFilesLayout.add(layout);
                });
            } else {
                spFilesLayout.add(new Span("no files attached"));
            }
            return spFilesLayout;
        })).setHeader(COL_FILES).setAutoWidth(true).setResizable(true)
                .setKey(COL_FILES);

        grid.addColumn(soulPatch -> soulPatch.getRatings().stream()
                .mapToDouble(SOULPatchRating::getStars)
                .average().orElse(Double.NaN))
                .setHeader(COL_RATINGS)
                .setKey(COL_RATINGS)
                .setSortable(true);

        grid.addColumn(soulPatch -> String.valueOf(soulPatch.getNoViews()))
                .setHeader(COL_VIEWS).setResizable(true)
                .setKey(COL_VIEWS)
                .setSortable(true);

        grid.addColumn(soulPatch -> soulPatch.getAuthor().getUserName())
                .setHeader(COL_AUTHOR).setResizable(true)
                .setKey(COL_AUTHOR)
                .setSortable(true);
    }

    private void initFilters() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);

        filterText.addValueChangeListener(e -> updateList());

        filterOwnedSoulpatches.addValueChangeListener(event -> updateList());
    }

    private void arrangeComponents() {
        HorizontalLayout toolbar = new HorizontalLayout(filterText, filterOwnedSoulpatches, addSOULPatch);

        VerticalLayout gridLayout = new VerticalLayout(grid);

        HorizontalLayout mainContent = new HorizontalLayout(gridLayout, form);
        mainContent.setSizeFull();

        add(userGreeting, toolbar, mainContent);
        setSizeFull();
    }

    public void updateList() {
        grid.setItems(
                service.findAll(filterText.getValue()).stream()
                        .filter(soulPatch -> (filterOwnedSoulpatches.getValue() &&
                                soulPatch.getAuthor().getUserName()
                                        .equals(SecurityUtils.getUsername()))

                                ||
                                !filterOwnedSoulpatches.getValue())
                        .collect(Collectors.toList()));
    }

    public void showFileEditor(SPFile spFile) {
        spFileEditorDialog.getEditor().setValue(spFile);
        spFileEditorDialog.setWidth("90%");
        spFileEditorDialog.open();
    }
}
