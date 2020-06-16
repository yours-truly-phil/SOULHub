package io.horrorshow.soulswap.ui;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import io.horrorshow.soulswap.data.SOULPatch;
import io.horrorshow.soulswap.service.SOULPatchService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route
@PWA(name = "SOULSwap - SOUL-Patch Web UI",
        shortName = "SOULSwap-Web",
        description = "Serves SOULPatches (SOUL files)",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css",
        themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service The message service. Automatically injected Spring managed bean.
     */
    public MainView(@Autowired SOULPatchService service) {
        List<SOULPatch> patches = service.findAll();

        Grid<SOULPatch> grid = new Grid<>();

        grid.addThemeName("bordered");

        grid.setItems(patches);

        grid.addColumn(SOULPatch::getId).setHeader("Id");
        grid.addColumn(SOULPatch::getName).setHeader("name");
        grid.addColumn(SOULPatch::getDescription).setHeader("description");
        grid.addColumn(SOULPatch::getSoulFileName).setHeader("soulFileName");
        grid.addColumn(SOULPatch::getSoulFileContent).setHeader("soulFileContent");
        grid.addColumn(SOULPatch::getSoulpatchFileName).setHeader("soulpatchFileName");
        grid.addColumn(SOULPatch::getSoulpatchFileContent).setHeader("soulpatchFileContent");
        grid.addColumn(SOULPatch::getAuthor).setHeader("author");
        grid.addColumn(SOULPatch::getNoServings).setHeader("noServings");

        Button addPatchBtn = new Button("add SOULPatch",
                e -> Notification.show("not yet implemented"));

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button has a more prominent look.
        addPatchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        addPatchBtn.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        add(addPatchBtn, grid, addPatchBtn);
    }
}