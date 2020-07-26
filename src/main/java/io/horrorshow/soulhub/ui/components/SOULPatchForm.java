package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.server.StreamResource;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.service.SOULHubUserDetailsService;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.ui.views.EditSOULPatchView;
import io.horrorshow.soulhub.ui.views.SOULPatchesView;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

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

    private static final long serialVersionUID = -7531039924193682445L;
    private final SOULPatchesView soulPatchesView;

    private final SOULPatchService soulPatchService;
    private final SOULHubUserDetailsService userDetailsService;

    private final TextField id = new TextField("id");
    private final TextField name = new TextField("name");
    private final TextArea description = new TextArea("description");
    private final TextField author = new TextField("author");
    private final TextField noServings = new TextField("no servings");
    private final Grid<SPFile> spFilesGrid = new Grid<>();
    private final Button newSpFile = new Button("create soulpatch file");
    private final Binder<SOULPatch> binder = new Binder<>(SOULPatch.class);
    private final Button editSOULPatch = new Button("edit soulpatch", VaadinIcon.EDIT.create());

    public SOULPatchForm(SOULPatchesView soulPatchesView,
                         @Autowired SOULPatchService soulPatchService,
                         @Autowired SOULHubUserDetailsService userDetailsService) {
        this.soulPatchesView = soulPatchesView;
        this.soulPatchService = soulPatchService;
        this.userDetailsService = userDetailsService;

        setClassName("soulpatch-form");

        initFields();

        arrangeComponents();

        initSOULPatchBinder();
    }

    private void initFields() {
        id.setWidth("100%");
        id.setReadOnly(true);

        name.setWidth("100%");
        name.setReadOnly(true);

        description.setWidth("100%");
        description.setReadOnly(true);

        author.setWidth("100%");
        author.setReadOnly(true);

        noServings.setWidth("100%");
        noServings.setReadOnly(true);

        spFilesGrid.setHeightByRows(true);
        spFilesGrid.setWidth("200");

        spFilesGrid.addColumn(new ComponentRenderer<>(it ->
                new Button(it.getName(), event -> soulPatchesView.previewSpFile(it))))
                .setHeader("filename").setAutoWidth(true);
        spFilesGrid.addColumn(spFile -> (spFile.getFileType() != null) ? spFile.getFileType().toString() : "")
                .setHeader("filetype").setAutoWidth(true);

        editSOULPatch.setVisible(false);
        editSOULPatch.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editSOULPatch.addClickListener(event -> gotoEditSOULPatch());
    }

    private void arrangeComponents() {
        VerticalLayout content = new VerticalLayout();
        content.addClassName("soulpatch-form-content");
        content.add(editSOULPatch);
        content.add(id);
        content.add(name);
        content.add(description);
        content.add(author);
        content.add(noServings);
        content.add(newSpFile);
        content.add(spFilesGrid);
        add(content);
    }

    private void initSOULPatchBinder() {
        binder.forField(id).bind(it -> String.valueOf(it.getId()), null);
        binder.forField(name).bind(SOULPatch::getName, null);
        binder.forField(description).bind(SOULPatch::getDescription, null);
        binder.forField(author).bind(soulPatch -> soulPatch.getAuthor().getUserName(), null);
        binder.forField(noServings).bind(it -> String.valueOf(it.getNoViews()), null);
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

        Span dropIcon = new Span("¸¸.•*♫♪*\uD83C\uDFB6¨*•♫♪");
        upload.setDropLabelIcon(dropIcon);

        upload.addSucceededListener(event -> {
            // TODO parse the file and open a new soulfileeditor dialog to edit the file
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

    public void showSOULPatch(SOULPatch soulPatch) {

        setupEditSOULPatchButton(soulPatch);

        binder.setBean(soulPatch);
        spFilesGrid.setItems(soulPatch.getSpFiles());
        setVisible(true);
        name.focus();
    }

    private void setupEditSOULPatchButton(SOULPatch soulPatch) {
        editSOULPatch.setVisible(
                userDetailsService.isCurrentUserOwnerOf(soulPatch));
    }

    public void gotoEditSOULPatch() {
        UI.getCurrent().navigate(EditSOULPatchView.class,
                String.valueOf(binder.getBean().getId()));
    }

    public void hideSOULPatchForm() {
        setVisible(false);
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
