package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SOULFileUpload extends VerticalLayout {

    private static final long serialVersionUID = 3216291855595166003L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SOULFileUpload.class);

    private static final String[] ACCEPTED_FILETYPE = {".soul", ".soulpatch"};

    private final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();

    private final Upload upload;


    public SOULFileUpload() {
        upload = createFileUpload(buffer);

        add(upload);
    }

    private static Upload createFileUpload(MultiFileMemoryBuffer buffer) {
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes(ACCEPTED_FILETYPE);
        upload.setUploadButton(new NativeButton("Upload"));
        upload.setDropLabel(new Span("Drag soul and soulpatch files here"));
        upload.setDropLabelIcon(new Span("¸¸.•*♫♪*\uD83C\uDFB6¨*•♫♪"));

        upload.addSucceededListener(event -> {
            LOGGER.debug("file upload succeeded - MimeType: {} FileName: {}",
                    event.getMIMEType(), event.getFileName());
        });
        upload.addFileRejectedListener(event -> {
            LOGGER.debug("file rejected - {}", event.getErrorMessage());
        });

        return upload;
    }
}
