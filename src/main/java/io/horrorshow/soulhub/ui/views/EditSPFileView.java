package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import io.horrorshow.soulhub.data.SPFile;
import io.horrorshow.soulhub.service.UserService;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import io.horrorshow.soulhub.ui.components.SOULFileEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import static java.lang.String.format;

@Secured(value = UIConst.ROLE_USER)
@Route(value = UIConst.ROUTE_EDIT_SPFILE, layout = MainLayout.class)
@PageTitle(UIConst.TITLE_EDIT_SPFILE)
public class EditSPFileView
        extends VerticalLayout
        implements HasUrlParameter<String> {

    private static final long serialVersionUID = 3028744874949379358L;

    private final SOULFileEditor fileEditor;
    private final SOULPatchService soulPatchService;
    private final UserService userDetailsService;

    public EditSPFileView(@Autowired SOULPatchService soulPatchService,
                          @Autowired UserService userDetailsService) {
        this.soulPatchService = soulPatchService;
        this.userDetailsService = userDetailsService;

        fileEditor = new SOULFileEditor(soulPatchService, userDetailsService);

        initFields();
        arrangeComponents();
    }

    private void initFields() {
        fileEditor.getEditor().setMaxlines(Integer.MAX_VALUE);
    }

    private void arrangeComponents() {
        add(fileEditor);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (soulPatchService.isPossibleSPFileId(parameter)) {
            soulPatchService.findSpFile(Long.valueOf(parameter))
                    .ifPresentOrElse(
                            this::checkUserRightsAndUpdate,
                            () -> createErrorView("resource not found"));
        } else {
            createErrorView(format("Unrecognized parameter: %s", parameter));
        }
    }

    private void checkUserRightsAndUpdate(SPFile spFile) {
        if (userDetailsService.isCurrentUserOwnerOf(spFile))
            updateView(spFile);
        else
            createErrorView("not authorized");
    }

    private void createErrorView(String error) {
        removeAll();
        add(new H1("Unable to serve request"));
        add(new Span(error));
        add(new RouterLink("to SOULPatches view", SOULPatchesViewOld.class));
    }

    private void updateView(SPFile spFile) {
        fileEditor.setValue(spFile);
    }
}
