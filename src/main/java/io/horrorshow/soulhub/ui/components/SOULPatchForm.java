package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.server.StreamResource;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.ui.views.SOULPatchesView;
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

    private static final long serialVersionUID = -7531039924193682445L;
    private final SOULPatchesView SOULPatchesView;

    private final TextField id = new TextField("id");
    private final TextField name = new TextField("name");
    private final TextArea description = new TextArea("description");
    private final TextField author = new TextField("author");
    private final TextField noServings = new TextField("no servings");
    private final Grid<SPFile> spFilesGrid = new Grid<>();
    private final Button save = new Button("save");
    private final Button delete = new Button("delete");
    private final Dialog fileEditorDialog = new Dialog(); // TODO check if it's used
    private final Button newSpFile = new Button("create soulpatch file");
    private final Component upload = createFileUpload();
    private final Binder<SOULPatch> binder = new Binder<>(SOULPatch.class);

    public SOULPatchForm(SOULPatchesView SOULPatchesView) {
        this.SOULPatchesView = SOULPatchesView;

        setClassName("soulpatch-form");

        initFields();

        arrangeComponents();

        initSOULPatchBinder();
    }

    /**
     * sets the properties of all ui components of this
     * editor, like their visual style, sizes, titles, listeners
     */
    private void initFields() {
        id.setWidth("100%");
        id.setReadOnly(true);

        name.setWidth("100%");
        name.setRequired(true);
        name.setValueChangeMode(ValueChangeMode.EAGER);

        description.setWidth("100%");
        description.setRequired(true);

        author.setWidth("100%");
        author.setRequired(true);
        author.setReadOnly(true);

        noServings.setWidth("100%");
        noServings.setReadOnly(true);

        newSpFile.addClickListener(event -> SOULPatchesView.showFileEditor(new SPFile()));

        spFilesGrid.addThemeName("bordered");
        spFilesGrid.setHeightByRows(true);
        spFilesGrid.setWidthFull();

        spFilesGrid.addColumn(new ComponentRenderer<>(it ->
                new Button(it.getName(), event -> SOULPatchesView.showFileEditor(it))))
                .setHeader("filename").setAutoWidth(true);
        spFilesGrid.addColumn(spFile -> spFile.getFileType().toString())
                .setHeader("filetype").setAutoWidth(true);


        save.setWidth("100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> save());

        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(e -> delete());
    }

    /**
     * position all components in the right order relative to each other
     * into the gui
     */
    private void arrangeComponents() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("soulpatch-form-content");
        content.add(upload);
        content.add(id);
        content.add(name);
        content.add(description);
        content.add(author);
        content.add(noServings);
        content.add(newSpFile);
        content.add(spFilesGrid);
        content.add(fileEditorDialog);
        content.add(save, delete);
        add(content);
    }

    /**
     * binds SOULPatch values to the UI components
     * <p>
     * To change the values displayed in the UI, call binder.setBean(newSOULPatch)
     */
    private void initSOULPatchBinder() {
        binder.forField(id).bind(it -> String.valueOf(it.getId()), null);
        binder.forField(name).bind(SOULPatch::getName, SOULPatch::setName);
        binder.forField(description).bind(SOULPatch::getDescription, SOULPatch::setDescription);
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
        binder.setBean(soulPatch);
        spFilesGrid.setItems(soulPatch.getSpFiles());
        setVisible(true);
        name.focus();
    }

    public void hideSOULPatchForm() {
        setVisible(false);
    }

    private void save() {
        SOULPatch patch = binder.getBean();
        SOULPatchesView.service.save(patch);
        SOULPatchesView.updateList();
        hideSOULPatchForm();
        new Notification(String.format(
                "soulpatch %s saved", patch.getName()),
                3000).open();
    }

    private void delete() {
        SOULPatch patch = binder.getBean();
        SOULPatchesView.service.delete(patch);
        SOULPatchesView.updateList();
        hideSOULPatchForm();
        new Notification(String.format(
                "soulpatch %s removed", patch.getName()),
                3000).open();
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
