package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.horrorshow.soulhub.service.SOULHubUserDetailsService;
import io.horrorshow.soulhub.service.SOULPatchService;
import lombok.Getter;

@Getter
public class SpFileEditorDialog extends Dialog {

    private static final long serialVersionUID = 3233168273572823081L;

    private final SOULFileEditor editor;

    private final Button close = new Button("close file editor dialog");

    public SpFileEditorDialog(SOULPatchService soulPatchService,
                              SOULHubUserDetailsService soulHubUserDetailsService) {
        super();
        editor = new SOULFileEditor(soulPatchService, soulHubUserDetailsService);

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
