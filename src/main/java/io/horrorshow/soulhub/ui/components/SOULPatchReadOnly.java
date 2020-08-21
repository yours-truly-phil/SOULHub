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
import io.horrorshow.soulhub.data.util.StringUtils;
import io.horrorshow.soulhub.ui.events.SOULPatchDownloadEvent;

import java.io.InputStream;
import java.util.Objects;
import java.util.function.Function;

public class SOULPatchReadOnly extends Div
        implements HasValueAndElement<
        AbstractField.ComponentValueChangeEvent<
                SOULPatchReadOnly, SOULPatch>, SOULPatch> {

    private static final long serialVersionUID = -3610662759595192083L;

    private final AbstractFieldSupport<SOULPatchReadOnly, SOULPatch> fieldSupport;

    private final Binder<SOULPatch> binder = new Binder<>(SOULPatch.class);

    private final Label id = new Label("id");
    private final Label name = new Label("name");
    private final Paragraph description = new Paragraph("description");
    private final Label author = new Label("author"); // TODO create insertable author component
    private final Label downloads = new Label("downloads");
    private final Anchor downloadLink = new Anchor();
    private Function<SOULPatch, InputStream> soulPatchZipByteStreamSupplier;

    public SOULPatchReadOnly() {

        this.fieldSupport =
                new AbstractFieldSupport<>(this, null, Objects::equals, sp -> {
                });

        setClassName("soulpatch-read-only-element");

        addValueChangeListener(this::soulPatchChanged);

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

    private void soulPatchChanged(
            AbstractField.ComponentValueChangeEvent<SOULPatchReadOnly, SOULPatch> event) {

        SOULPatch sp = event.getValue();
        binder.readBean(sp);
    }

    public Registration addSOULPatchDownloadListener(
            ComponentEventListener<SOULPatchDownloadEvent> listener) {
        return addListener(SOULPatchDownloadEvent.class, listener);
    }

    public void setSOULPatchZipInputStreamProvider(Function<SOULPatch, InputStream> streamProvider) {
        this.soulPatchZipByteStreamSupplier = streamProvider;
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
                    String filename = String.format("%s.zip", StringUtils.toValidFilename(sp.getName()));
                    StreamResource sr = new StreamResource(
                            filename,
                            () -> downloadSOULPatch(sp));
                    downloadLink.setHref(sr);
                    downloadLink.setText(filename);
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

    public InputStream downloadSOULPatch(SOULPatch soulPatch) {
        fireEvent(new SOULPatchDownloadEvent(this, soulPatch));
        return soulPatchZipByteStreamSupplier.apply(soulPatch);
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
