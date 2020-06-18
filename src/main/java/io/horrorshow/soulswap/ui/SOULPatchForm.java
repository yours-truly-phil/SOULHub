package io.horrorshow.soulswap.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import io.horrorshow.soulswap.data.SOULPatch;

public class SOULPatchForm extends FormLayout {

    private final MainView mainView;

    private final Binder<SOULPatch> binder = new Binder<>(SOULPatch.class);

    private final TextField id = new TextField("Id");
    private final TextField name = new TextField("Name");
    private final TextArea description = new TextArea("Description");
    private final TextField soulFileName = new TextField("Soulfile name");
    private final TextArea soulFileContent = new TextArea("Soulfile content");
    private final TextField soulpatchFileName = new TextField("soulpatch file name");
    private final TextArea soulpatchFileContent = new TextArea("soulpatch file content");
    private final TextField author = new TextField("Author");
    private final TextField noServings = new TextField("no Servings");

    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");

    public SOULPatchForm(MainView mainView) {
        this.mainView = mainView;

        id.setReadOnly(true);

        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(id, name, description,
                soulFileName, soulFileContent,
                soulpatchFileName, soulpatchFileContent,
                author, noServings, buttons);

        binder.bind(id, soulPatch -> String.valueOf(soulPatch.getId()),
                (soulPatch, s) -> soulPatch.setId(Long.valueOf(s)));
        binder.bind(name, SOULPatch::getName, SOULPatch::setName);
        binder.bind(description, SOULPatch::getDescription, SOULPatch::setDescription);
        binder.bind(soulFileName, SOULPatch::getSoulFileName, SOULPatch::setSoulFileName);
        binder.bind(soulFileContent, SOULPatch::getSoulFileContent, SOULPatch::setSoulFileContent);
        binder.bind(soulpatchFileName, SOULPatch::getSoulpatchFileName, SOULPatch::setSoulpatchFileName);
        binder.bind(soulpatchFileContent, SOULPatch::getSoulpatchFileContent, SOULPatch::setSoulpatchFileContent);
        binder.bind(author, SOULPatch::getAuthor, SOULPatch::setAuthor);
        binder.bind(noServings, soulPatch -> String.valueOf(soulPatch.getNoServings()),
                (soulPatch, s) -> soulPatch.setNoServings(Long.valueOf(s)));

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
    }

    public void setSOULPatch(SOULPatch soulPatch) {
        binder.setBean(soulPatch);

        if (soulPatch == null) {
            setVisible(false);
        } else {
            setVisible(true);
            name.focus();
        }
    }

    private void save() {
        SOULPatch patch = binder.getBean();
        mainView.service.save(patch);
        mainView.updateList();
        setSOULPatch(null);
    }

    private void delete() {
        SOULPatch patch = binder.getBean();
        mainView.service.delete(patch);
        mainView.updateList();
        setSOULPatch(null);
    }
}
