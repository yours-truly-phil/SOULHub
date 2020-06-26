package io.horrorshow.soulswap.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import io.horrorshow.soulswap.data.SOULPatch;
import io.horrorshow.soulswap.data.SPFile;
import io.horrorshow.soulswap.service.SOULPatchService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Route
@PWA(name = "SOULSwap - SOUL-Patch Web UI",
        shortName = "SOULSwap-Web",
        description = "Serves SOULPatches (SOUL files)",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css",
        themeFor = "vaadin-text-field")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@Getter
public class MainView extends VerticalLayout {

    public final SOULPatchService service;

    private final Grid<SOULPatch> grid = new Grid<>();

    private final TextField filterText = new TextField("filter by (regex)");

    private final SOULPatchForm form = new SOULPatchForm(this);

    private final SOULFileEditor.SpFileEditorDialog spFileEditorDialog = new SOULFileEditor.SpFileEditorDialog(this);

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service
     *         The message service. Automatically injected Spring managed bean.
     */
    public MainView(@Autowired SOULPatchService service) {

        this.service = service;

        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);

        filterText.addValueChangeListener(e -> updateList());

        grid.addThemeName("bordered");
        grid.setHeightByRows(true);

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
                layout.add(new Button(spFile.getName(), event -> {
                    showFileEditor(spFile);
                }));
                layout.add(new Label(String.format("Type: %s", spFile.getFileType().toString())));
                files.add(layout);
            });

            return files;
        })).setHeader("Files").setFlexGrow(10).setResizable(true);

        grid.addColumn(SOULPatch::getAuthor)
                .setHeader("author").setFlexGrow(0).setResizable(true);
        grid.addColumn(soulPatch -> String.valueOf(soulPatch.getNoServings()))
                .setHeader("noServings").setFlexGrow(0).setResizable(true);

        Button addPatchBtn = new Button("add SOULPatch",
                e -> Notification.show("not yet implemented"));
        // Theme variants give you predefined extra styles for components.
        // Example: Primary button has a more prominent look.
        addPatchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addPatchBtn.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.setSOULPatch(new SOULPatch());
        });

        Button createIndexBtn = new Button("create Database Index");
        createIndexBtn.addClickListener(
                e -> service.createDatabaseIndex()
        );


        HorizontalLayout toolbar = new HorizontalLayout(filterText, addPatchBtn, createIndexBtn);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();
        form.setWidth("40%");

        add(toolbar, mainContent);
        setSizeFull();

        form.setSOULPatch(null);
        updateList();

        grid.asSingleSelect().addValueChangeListener(
                e -> form.setSOULPatch(grid.asSingleSelect().getValue())
        );
    }

    public void updateList() {
        grid.setItems(service.findAll(filterText.getValue()));
    }

    public void showFileEditor(SPFile spFile) {
        spFileEditorDialog.getEditor().setSpFile(spFile);
        spFileEditorDialog.setSizeFull();
        spFileEditorDialog.open();
    }
}
