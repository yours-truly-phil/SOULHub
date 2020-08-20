package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ReadOnlyHasValue;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.service.UserService;
import io.horrorshow.soulhub.ui.events.SOULPatchDownloadEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Function;

public class SOULPatchReadOnly extends Div
        implements HasValueAndElement<
        AbstractField.ComponentValueChangeEvent<
                SOULPatchReadOnly, SOULPatch>, SOULPatch> {

    private static final long serialVersionUID = -3610662759595192083L;

//    private final SOULPatchService soulPatchService;
//    private final UserService userService;

    private final AbstractFieldSupport<SOULPatchReadOnly, SOULPatch> fieldSupport;

    private final Binder<SOULPatch> binder = new Binder<>(SOULPatch.class);

    private final Label id = new Label("id");
    private final Label name = new Label("name");
    private final Paragraph description = new Paragraph("description");
    private final Label author = new Label("author"); // TODO create insertable author component
    private final Label downloads = new Label("downloads");
    private final Anchor downloadLink = new Anchor();
    private Function<SOULPatch, InputStream> streamProvider;

//    public SOULPatchReadOnly(@Autowired SOULPatchService soulPatchService,
//                             @Autowired UserService userService) {
//        this.soulPatchService = soulPatchService;
//        this.userService = userService;
    public SOULPatchReadOnly() {

        this.fieldSupport =
                new AbstractFieldSupport<>(this, null, Objects::equals, sp -> {
                });

        setClassName("soulpatch-read-only-element");

        addValueChangeListener(this::soulPatchChanged);
//        addSOULPatchDownloadListener(this::soulPatchDownload);

        init();

        arrangeComponents();
    }

    private void arrangeComponents() {
        FormLayout formLayout = new FormLayout();
        formLayout.addFormItem(id, "id");
        formLayout.addFormItem(name, "name");
        formLayout.addFormItem(description, "description");
        formLayout.addFormItem(author, "author");
        formLayout.addFormItem(downloads, "no. downloads");
        formLayout.addFormItem(downloadLink, "download files of soulpatch");

        add(formLayout);
    }

//    private void soulPatchDownload(SOULPatchDownloadEvent event) {
//        soulPatchService.incrementNoDownloadsAndSave(event.getSoulPatch());
//    }

    private void soulPatchChanged(
            AbstractField.ComponentValueChangeEvent<SOULPatchReadOnly, SOULPatch> event) {

        SOULPatch sp = event.getValue();
        binder.readBean(sp);
    }

//    private InputStream downloadSOULPatchBytes(SOULPatch sp) {
//        fireEvent(new SOULPatchDownloadEvent(this, sp));
//        return new ByteArrayInputStream(soulPatchService.zipSOULPatchFiles(sp));
//    }

    public Registration addSOULPatchDownloadListener(
            ComponentEventListener<SOULPatchDownloadEvent> listener) {
        return addListener(SOULPatchDownloadEvent.class, listener);
    }

    public void setSOULPatchZipInputStreamProvider(Function<SOULPatch, InputStream> streamProvider) {
        this.streamProvider = streamProvider;
    }

    private void init() {
        id.setWidthFull();
        id.setTitle("id");

        name.setWidthFull();
        name.setTitle("name");

        description.setWidthFull();
        description.setTitle("description");
        description.setClassName("sp-description-text");

        author.setWidthFull();
        author.setTitle("author");

        downloads.setWidthFull();
        downloads.setTitle("no. downloads");

        downloadLink.setWidthFull();
        downloadLink.setTitle("download files as zip");

        ReadOnlyHasValue<String> idBinding =
                new ReadOnlyHasValue<>(id::setText, null);
        ReadOnlyHasValue<String> nameBinding =
                new ReadOnlyHasValue<>(name::setText, null);
        ReadOnlyHasValue<String> descriptionBinding =
                new ReadOnlyHasValue<>(description::setText, null);
        ReadOnlyHasValue<AppUser> authorBinding =
                new ReadOnlyHasValue<>(appUser -> author.setText(appUser.getUserName()), null);
        ReadOnlyHasValue<String> noDownloadsBinding =
                new ReadOnlyHasValue<>(downloads::setText, null);
        ReadOnlyHasValue<SOULPatch> downloadBinding =
                new ReadOnlyHasValue<>(sp -> {
                    StreamResource sr = new StreamResource(
                            String.format("%s.zip", sp.getName()),
//                            () -> downloadSOULPatchBytes(sp));
                            () -> streamProvider.apply(sp));
                    downloadLink.setHref(sr);
                    downloadLink.setText(String.format("%s.zip", sp.getName()));
                }, null);

        binder.forField(idBinding)
                .bind(soulPatch -> String.valueOf(soulPatch.getId()), null);
        binder.forField(nameBinding)
                .bind(SOULPatch::getName, null);
        binder.forField(descriptionBinding)
                .bind(SOULPatch::getDescription, null);
        binder.forField(authorBinding)
                .bind(SOULPatch::getAuthor, null);
        binder.forField(noDownloadsBinding)
                .bind(soulPatch -> String.valueOf(soulPatch.getNoViews()), null);
        binder.forField(downloadBinding)
                .bind(soulPatch -> soulPatch, null);
    }

    @Override
    public SOULPatch getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public void setValue(SOULPatch value) {
        fieldSupport.setValue(value);
    }

    @Override
    public Registration addValueChangeListener(
            ValueChangeListener<? super AbstractField
                    .ComponentValueChangeEvent<SOULPatchReadOnly, SOULPatch>> listener) {

        return fieldSupport.addValueChangeListener(listener);
    }
}
