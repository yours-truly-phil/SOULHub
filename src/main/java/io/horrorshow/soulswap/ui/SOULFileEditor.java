package io.horrorshow.soulswap.ui;

import com.hilerio.ace.AceEditor;
import com.hilerio.ace.AceMode;
import com.hilerio.ace.AceTheme;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import io.horrorshow.soulswap.data.SPFile;

public class SOULFileEditor extends VerticalLayout {

    private final TextField name = new TextField("filename");
    private AceEditor aceEditor;
    private SPFile spFile;

    public SOULFileEditor(SPFile spFile) {
        this.spFile = spFile;

        name.setWidth("100%");
        name.setRequired(true);
        name.setValue("filename.soul");
        add(name);

        aceEditor = new AceEditor();
        aceEditor.setValue("init");
        aceEditor.setTheme(AceTheme.monokai);
        aceEditor.setMode(AceMode.xml);

        aceEditor.setSofttabs(true);
        aceEditor.setTabSize(4);
        aceEditor.setWrap(false);

        aceEditor.setSizeFull();
        aceEditor.setMinlines(10);

        aceEditor.setPlaceholder("SOULFile content");

        aceEditor.addFocusListener(e -> {
            System.out.println("aceEditor focus listener bam");
        });

        add(aceEditor);
    }

    public SPFile getSpFile() {
        return spFile;
    }
}
