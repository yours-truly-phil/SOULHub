package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SpFileTabs extends Div
        implements HasValueAndElement
        <AbstractField.ComponentValueChangeEvent
                <SpFileTabs, List<SPFile>>, List<SPFile>> {

    private static final long serialVersionUID = -8496077031496722353L;

    private final Tabs tabs = new Tabs();
    private final SPFileReadOnly spFileReadOnly;

    private final Map<Tab, SPFile> tabsToSpFiles = new HashMap<>();

    private final AbstractFieldSupport<SpFileTabs, List<SPFile>> fieldSupport;

    public SpFileTabs(@Autowired SOULPatchService soulPatchService,
                      @Autowired UserService userService) {
        this.fieldSupport =
                new AbstractFieldSupport<>(this, null, Objects::equals, sp -> {
                });

//        spFileReadOnly = new SPFileReadOnly(soulPatchService, userService);
        spFileReadOnly = new SPFileReadOnly();

        setClassName("spfile-tabs");

        addValueChangeListener(this::spFilesChanged);

        tabs.addSelectedChangeListener(this::selectionChanged);

        arrangeComponents();
    }

    private void selectionChanged(Tabs.SelectedChangeEvent event) {
        SPFile spFile = tabsToSpFiles.get(event.getSelectedTab());
        spFileReadOnly.setValue(spFile);
    }

    private void arrangeComponents() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(tabs);
        verticalLayout.add(spFileReadOnly);
        add(verticalLayout);
    }

    private void spFilesChanged(
            AbstractField.ComponentValueChangeEvent<SpFileTabs, List<SPFile>> event) {
        tabs.removeAll();
        tabsToSpFiles.clear();
        event.getValue().forEach(spFile -> {
            Tab tab = new Tab(spFile.getName());
            tabsToSpFiles.put(tab, spFile);
            tabs.add(tab);
        });
    }

    @Override
    public List<SPFile> getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public void setValue(List<SPFile> value) {
        fieldSupport.setValue(value);
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<SpFileTabs, List<SPFile>>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }
}
