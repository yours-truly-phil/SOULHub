package io.horrorshow.soulswap.ui;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
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

        Component upload = createFileUpload();

        id.setReadOnly(true);

        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(id, name, description,
                soulFileName, soulFileContent,
                soulpatchFileName, soulpatchFileContent,
                author, noServings, upload, buttons);

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
