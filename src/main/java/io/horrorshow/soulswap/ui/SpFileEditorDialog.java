package io.horrorshow.soulswap.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;

@Getter
public class SpFileEditorDialog extends Dialog {
    private final SOULFileEditor editor;

    private final Button close = new Button("close file editor dialog");

    public SpFileEditorDialog(SOULPatchesView SOULPatchesView) {
        super();
        editor = new SOULFileEditor(SOULPatchesView);

        close.addClickListener(event -> close());

        arrangeComponents();
    }

    private void arrangeComponents() {
        VerticalLayout content = new VerticalLayout();
        content.add(editor);
        content.add(close);
        add(content);
    }
}
