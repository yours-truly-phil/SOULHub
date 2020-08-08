package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.FileRejectedEvent;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class SOULFileUpload extends VerticalLayout {

    private static final long serialVersionUID = 3216291855595166003L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SOULFileUpload.class);

    private static final String[] ACCEPTED_FILETYPE = {".soul", ".soulpatch"};

    private final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();

    public SOULFileUpload() {
        Upload upload = createFileUpload(buffer);

        add(upload);
    }

    private Upload createFileUpload(MultiFileMemoryBuffer buffer) {
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes(ACCEPTED_FILETYPE);
        upload.setUploadButton(new NativeButton("Upload"));
        upload.setDropLabel(new Span("Drag soul and soulpatch files here"));
        upload.setDropLabelIcon(new Span("¸¸.•*♫♪*\uD83C\uDFB6¨*•♫♪"));

        upload.addSucceededListener(this::fileUploaded);
        upload.addFileRejectedListener(this::fileRejected);

        return upload;
    }

    private void fileRejected(FileRejectedEvent event) {
        LOGGER.debug(event.getErrorMessage());
    }

    private void fileUploaded(SucceededEvent event) {
        InputStream is = buffer.getInputStream(event.getFileName());

        String content = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));

        LOGGER.debug("Filename [{}] MIMEType [{}] Length [{}] toString [{}]",
                event.getFileName(),
                event.getMIMEType(),
                event.getContentLength(),
                content);

    }
}
