package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.FileRejectedEvent;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.data.api.SOULPatchParser;
import io.horrorshow.soulhub.ui.events.SPFileUploadedEvent;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Log4j2
public class SOULFileUpload extends VerticalLayout {

    private static final long serialVersionUID = 3216291855595166003L;

    private static final String[] ACCEPTED_FILETYPE = {".soul", ".soulpatch"};

    private final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();

    public SOULFileUpload() {

        Upload upload = createFileUpload(buffer);

        add(upload);
    }

    public Registration addSpFileUploadedListener(
            ComponentEventListener<SPFileUploadedEvent> listener) {
        return addListener(SPFileUploadedEvent.class, listener);
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
        new Notification(event.getErrorMessage(), 3000,
                Notification.Position.MIDDLE).open();
        log.debug(event.getErrorMessage());
    }

    private void fileUploaded(SucceededEvent event) {
        InputStream is = buffer.getInputStream(event.getFileName());

        String filename = event.getFileName();
        String content = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));

        log.debug("Filename [{}] MIMEType [{}] Length [{}] toString [{}]",
                event.getFileName(),
                event.getMIMEType(),
                event.getContentLength(),
                content);

        SPFile spFile = new SPFile();
        spFile.setName(filename);
        spFile.setFileContent(content);
        spFile.setFileType(SOULPatchParser.guessFileType(spFile));
        fireEvent(new SPFileUploadedEvent(this, spFile));
    }
}
