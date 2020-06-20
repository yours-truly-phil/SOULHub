package io.horrorshow.soulswap.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import io.horrorshow.soulswap.data.SOULPatch;
import io.horrorshow.soulswap.service.SOULPatchService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

import static java.lang.String.format;

@Route
@PWA(name = "SOULSwap - SOUL-Patch Web UI",
        shortName = "SOULSwap-Web",
        description = "Serves SOULPatches (SOUL files)",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css",
        themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    public final SOULPatchService service;

    private final Grid<SOULPatch> grid = new Grid<>();

    private final TextField filterText = new TextField("filter by (regex)");

    private final SOULPatchForm form = new SOULPatchForm(this);

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

        grid.addColumn(soulPatch -> String.valueOf(soulPatch.getId())).setHeader("Id");
        grid.addColumn(SOULPatch::getName).setHeader("name");
        grid.addColumn(SOULPatch::getDescription).setHeader("description");
        // TODO: Schema change, one to many soulpatch -> soul/soulpatch files
        grid.addColumn(
                soulPatch -> soulPatch.getSpFiles().stream()
                        .map(spFile ->
                                format("filename: %s (type: %s)",
                                        spFile.getName(), spFile.getFileType().toString()))
                        .collect(Collectors.joining(", "))
        ).setHeader("Files");

        grid.addColumn(SOULPatch::getAuthor).setHeader("author");
        grid.addColumn(soulPatch -> String.valueOf(soulPatch.getNoServings())).setHeader("noServings");

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
        grid.setSizeFull();

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
}
