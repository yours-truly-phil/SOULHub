package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@Route(value = "", layout = MainLayout.class)
@Getter
@PageTitle("SOULHub | SOUL Patches")
public class SOULPatchesView extends VerticalLayout {

    private static final long serialVersionUID = 3981631233877217865L;

    public final SOULPatchService service;

    public final SOULHubUserDetailsService userService;

    private final Grid<SOULPatch> grid = new Grid<>();
    private final TextField filterText = new TextField("filter by (regex)");
    private final Button addSOULPatch = new Button("add SOULPatch");
    private final SOULPatchForm form = new SOULPatchForm(this);
    private final SpFileEditorDialog spFileEditorDialog = new SpFileEditorDialog(this);
    private final Span userGreeting = new Span("Hello!");

    @Autowired
    public SOULPatchesView(SOULPatchService service, SOULHubUserDetailsService userService) {

        this.service = service;
        this.userService = userService;

        addClassName("soulpatches-view");

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

        initGreeting();
    }

    private void initGreeting() {
        userGreeting.setText("Hello " + SecurityUtils.getUsername());
    }

    private void initSOULPatchForm() {
        form.setMinWidth("20em");
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
                layout.add(new Label(String.format("%s-file", spFile.getFileType().toString())));
                files.add(layout);
            });

            return files;
        })).setHeader("Files").setFlexGrow(10).setResizable(true);

        grid.addColumn(soulPatch -> soulPatch.getAuthor().getUserName())
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

        add(userGreeting, toolbar, mainContent);
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
