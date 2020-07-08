package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.BrowserWindowResizeEvent;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.security.SecurityUtils;
import io.horrorshow.soulhub.service.SOULHubUserDetailsService;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.components.SOULPatchForm;
import io.horrorshow.soulhub.ui.components.SpFileEditorDialog;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.String.format;

@Route(value = "", layout = MainLayout.class)
@Getter
@PageTitle("SOULHub | SOUL Patches")
public class SOULPatchesView extends VerticalLayout {

    private static final long serialVersionUID = 3981631233877217865L;
    private static final String COL_NAME = "name";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_FILES = "files";
    private static final String COL_VIEWS = "views";
    private static final String COL_AUTHOR = "author";
    private static final Logger LOGGER = LoggerFactory.getLogger(SOULPatchesView.class);
    public final SOULPatchService service;
    public final SOULHubUserDetailsService userService;
    private final Grid<SOULPatch> grid = new Grid<>();
    private final TextField filterText = new TextField("filter by (regex)");
    private final Button addSOULPatch = new Button("add SOULPatch");
    private final SOULPatchForm form = new SOULPatchForm(this);
    private final SpFileEditorDialog spFileEditorDialog = new SpFileEditorDialog(this);
    private final Span userGreeting = new Span("Hello!");

    public SOULPatchesView(@Autowired SOULPatchService service, @Autowired SOULHubUserDetailsService userService) {

        this.service = service;
        this.userService = userService;

        addClassName("soulpatches-view");

        UI.getCurrent().getPage().addBrowserWindowResizeListener(this::browserWindowResized);

        initFields();

        arrangeComponents();

        form.setVisible(false);

        updateList();
    }

    private void browserWindowResized(BrowserWindowResizeEvent event) {
        LOGGER.debug("browser window resized: width={} height={}", event.getWidth(), event.getHeight());
        // TODO: check out https://vaadin.com/directory/component/sizereporter/samples
        // TODO: find way to get scaling (4k width on ipad feels different than 4k width on normal screen)
        // TODO: this might already be "normalized" since I only got 2560 with window size pretty much fullscreen
        grid.getColumnByKey(COL_VIEWS).setVisible(event.getWidth() >= 1300);
        grid.getColumnByKey(COL_AUTHOR).setVisible(event.getWidth() >= 1100);
        grid.getColumnByKey(COL_DESCRIPTION).setVisible(event.getWidth() >= 900);
    }

    private void initFields() {
        initFilterText();

        initSOULPatchesGrid();

        initAddSOULPatchLink();

        initSOULPatchForm();

        initGreeting();
    }

    private void initGreeting() {
        userGreeting.setText("Hello " + SecurityUtils.getUsername());
    }

    private void initSOULPatchForm() {
        form.setMinWidth("20em");
    }

    private void initAddSOULPatchLink() {
        addSOULPatch.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addSOULPatch.addClickListener(e -> {
            grid.asSingleSelect().clear();
            UI.getCurrent().navigate(EditSOULPatchView.class);
        });
    }

    private void initSOULPatchesGrid() {
        grid.addThemeName("bordered");
        grid.setHeightFull();
        grid.setColumnReorderingAllowed(true);

        addSOULPatchesGridColumns();

        grid.asSingleSelect().addValueChangeListener(e ->
                grid.asSingleSelect().getOptionalValue()
                        .ifPresentOrElse(form::showSOULPatch, form::hideSOULPatchForm));
    }

    private void addSOULPatchesGridColumns() {

        grid.addColumn(SOULPatch::getName)
                .setHeader(COL_NAME)
                .setResizable(true).setAutoWidth(true).setFrozen(true)
                .setKey(COL_NAME);

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
                                    format("%s (%s-type)", spFile.getName(),
                                            spFile.getFileType().toString()),
                                    event -> showFileEditor(spFile)));

                    spFilesLayout.add(layout);
                });
            } else {
                spFilesLayout.add(new Span("no files attached"));
            }
            return spFilesLayout;
        })).setHeader(COL_FILES).setAutoWidth(true).setResizable(true)
                .setKey(COL_FILES);

        grid.addColumn(soulPatch -> String.valueOf(soulPatch.getNoViews()))
                .setHeader(COL_VIEWS).setResizable(true)
                .setKey(COL_VIEWS);

        grid.addColumn(soulPatch -> soulPatch.getAuthor().getUserName())
                .setHeader(COL_AUTHOR).setResizable(true)
                .setKey(COL_AUTHOR);
    }

    private void initFilterText() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);

        filterText.addValueChangeListener(e -> updateList());
    }

    private void arrangeComponents() {
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addSOULPatch);

        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();

        add(userGreeting, toolbar, mainContent);
        setSizeFull();
    }

    public void updateList() {
        grid.setItems(service.findAll(filterText.getValue()));
    }

    public void showFileEditor(SPFile spFile) {
        spFileEditorDialog.getEditor().showSpFile(spFile);
        spFileEditorDialog.setMaxWidth("100%");
        spFileEditorDialog.setMaxHeight("100%");
        spFileEditorDialog.open();
    }
}
