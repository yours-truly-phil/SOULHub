package io.horrorshow.soulswap.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulswap.data.SOULPatch;
import io.horrorshow.soulswap.data.SPFile;
import io.horrorshow.soulswap.service.SOULPatchService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "", layout = MainLayout.class)
@Getter
@PageTitle("SOULSwap | SOUL Patches")
public class SOULPatchesView extends VerticalLayout {

    public final SOULPatchService service;

    private final Grid<SOULPatch> grid = new Grid<>();

    private final TextField filterText = new TextField("filter by (regex)");

    private final Button addSOULPatch = new Button("add SOULPatch");

    private final SOULPatchForm form = new SOULPatchForm(this);

    private final SpFileEditorDialog spFileEditorDialog = new SpFileEditorDialog(this);

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service
     *         The message service. Automatically injected Spring managed bean.
     */
    public SOULPatchesView(@Autowired SOULPatchService service) {

        this.service = service;

        addClassName("centered-content");

        initFields();

        arrangeComponents();

        form.setVisible(false);

        updateList();
    }

    private void initFields() {
        initFilterText();

        initSOULPatchesGrid();

        initAddSOULPatchBtn();

        initSOULPatchForm();
    }

    private void initSOULPatchForm() {
        form.setWidth("40%");
    }

    private void initAddSOULPatchBtn() {
        addSOULPatch.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addSOULPatch.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.showSOULPatch(new SOULPatch());
        });
    }

    private void initSOULPatchesGrid() {
        grid.addThemeName("bordered");
        grid.setHeightByRows(true);

        addSOULPatchesGridColumns();

        grid.asSingleSelect().addValueChangeListener(e ->
                grid.asSingleSelect().getOptionalValue()
                        .ifPresentOrElse(form::showSOULPatch, form::hideSOULPatchForm));
    }

    private void addSOULPatchesGridColumns() {
        grid.addColumn(soulPatch -> String.valueOf(soulPatch.getId()))
                .setHeader("Id").setFlexGrow(0).setResizable(true);
        grid.addColumn(SOULPatch::getName)
                .setHeader("name").setFlexGrow(0).setResizable(true);
        grid.addColumn(SOULPatch::getDescription)
                .setHeader("description").setAutoWidth(true).setFlexGrow(1).setResizable(true);

        grid.addColumn(new ComponentRenderer<>(sp -> {

            VerticalLayout files = new VerticalLayout();

            sp.getSpFiles().forEach(spFile -> {
                HorizontalLayout layout = new HorizontalLayout();
                layout.add(new Button(spFile.getName(), event ->
                        showFileEditor(spFile)));
                layout.add(new Label(String.format("Type: %s", spFile.getFileType().toString())));
                files.add(layout);
            });

            return files;
        })).setHeader("Files").setFlexGrow(10).setResizable(true);

        grid.addColumn(SOULPatch::getAuthor)
                .setHeader("author").setFlexGrow(0).setResizable(true);
        grid.addColumn(soulPatch -> String.valueOf(soulPatch.getNoServings()))
                .setHeader("noServings").setFlexGrow(0).setResizable(true);
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

        add(toolbar, mainContent);
        setSizeFull();
    }

    public void updateList() {
        grid.setItems(service.findAll(filterText.getValue()));
    }

    public void showFileEditor(SPFile spFile) {
        spFileEditorDialog.getEditor().showSpFile(spFile);
        spFileEditorDialog.setSizeFull();
        spFileEditorDialog.open();
    }
}
