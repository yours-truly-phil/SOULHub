package io.horrorshow.soulswap.ui;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.server.StreamResource;
import io.horrorshow.soulswap.data.SOULPatch;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;

public class SOULPatchForm extends Div {

    private final MainView mainView;

    private final VerticalLayout content;

    private final Binder<SOULPatch> binder;

    private final TextField id = new TextField("id");
    private final TextField name = new TextField("name");
    private final TextArea description = new TextArea("description");
    private final TextField author = new TextField("author");
    private final TextField noServings = new TextField("no servings");

    private final Button save = new Button("save");
    private final Button delete = new Button("delete");

    private SOULPatch soulPatch;

    public SOULPatchForm(MainView mainView) {
        this.mainView = mainView;

        setClassName("soulpatch-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("soulpatch-form-content");
        add(content);

        Component upload = createFileUpload();
        add(upload);

        id.setWidth("100%");
        id.setReadOnly(true);
        content.add(id);

        name.setWidth("100%");
        name.setRequired(true);
        name.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(name);

        description.setWidth("100%");
        description.setRequired(true);
        content.add(description);

        author.setWidth("100%");
        author.setRequired(true);
        content.add(author);

        noServings.setWidth("100%");
        noServings.setReadOnly(true);
        content.add(noServings);

        binder = new Binder<>(SOULPatch.class);

        binder.forField(id).bind(it -> String.valueOf(it.getId()), null);
        binder.forField(name).bind(SOULPatch::getName, SOULPatch::setName);
        binder.forField(description).bind(SOULPatch::getDescription, SOULPatch::setDescription);
        binder.forField(author).bind(SOULPatch::getAuthor, SOULPatch::setAuthor);
        binder.forField(noServings).bind(it -> String.valueOf(it.getNoServings()), null);

        save.setWidth("100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> save());

        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(e -> delete());

        content.add(save, delete);
    }

    private Component createFileUpload() {

        VerticalLayout fileUploadLayout = new VerticalLayout();
        VerticalLayout uploadResult = new VerticalLayout();

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes(".soul", ".soulpatch");

        NativeButton uploadButton = new NativeButton("Upload");
        upload.setUploadButton(uploadButton);

        Span dropLabel = new Span("Drag soul and soulpatch files here");
        upload.setDropLabel(dropLabel);

        Span dropIcon = new Span("¸¸.•*♫♪*\uD83C\uD83C\uDFB6\uDFB5¨*•♫♪");
        upload.setDropLabelIcon(dropIcon);

        upload.addSucceededListener(event -> {
            Component component = createComponent(
                    event.getMIMEType(),
                    event.getFileName(),
                    buffer.getInputStream(event.getFileName()));
            showOutput(event.getFileName(), component, uploadResult);
        });
        upload.addFileRejectedListener(event -> {
            Paragraph component = new Paragraph();
            showOutput(event.getErrorMessage(), component, uploadResult);
        });

        fileUploadLayout.add(upload);
        fileUploadLayout.add(uploadResult);

        return fileUploadLayout;
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

    private Component createComponent(String mimeType, String fileName, InputStream stream) {
        if (mimeType.startsWith("text")) {
            return createTextComponent(stream);
        } else if (mimeType.startsWith("image")) {
            Image image = new Image();
            try {
                byte[] bytes = IOUtils.toByteArray(stream);
                image.getElement().setAttribute("src", new StreamResource(
                        fileName, () -> new ByteArrayInputStream(bytes)));
                try (ImageInputStream in = ImageIO.createImageInputStream(
                        new ByteArrayInputStream(bytes))) {
                    final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
                    if (readers.hasNext()) {
                        ImageReader reader = readers.next();
                        try {
                            reader.setInput(in);
                            image.setWidth(reader.getWidth(0) + "px");
                            image.setHeight(reader.getHeight(0) + "px");
                        } finally {
                            reader.dispose();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return image;
        }
        Div content = new Div();
        String text = String.format("Mime type: '%s'\nSHA-256 hash: '%s'",
                mimeType, Arrays.toString(MessageDigestUtil.sha256(stream.toString())));
        content.setText(text);
        return content;
    }

    private Component createTextComponent(InputStream stream) {
        String text;
        try {
            text = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            text = "exception reading stream";
        }
        return new Text(text);
    }

    private void showOutput(String text, Component content, HasComponents outputContainer) {
        HtmlComponent p = new HtmlComponent(Tag.P);
        p.getElement().setText(text);
        outputContainer.add(p);
        outputContainer.add(content);
    }
}
