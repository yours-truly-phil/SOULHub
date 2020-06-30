package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.ui.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "about", layout = MainLayout.class)
@PageTitle("SOULHub | About")
public class AboutView extends VerticalLayout {

    private SOULPatchService soulPatchService;

    public AboutView(@Autowired SOULPatchService soulPatchService) {
        this.soulPatchService = soulPatchService;
        addClassName("about-view");

        createContent();
    }

    private void createContent() {
        setAlignItems(Alignment.CENTER);

        H1 title = new H1("About SOULHub");

        Span noSOULPatches = new Span(
                String.format("%s SOUL Patches stored",
                        soulPatchService.countSOULPatches()));

        Span noSPFiles = new Span(
                String.format("%s soul files attached",
                        soulPatchService.countSPFiles()));

        add(title);
        add(noSOULPatches);
        add(noSPFiles);
    }

}
