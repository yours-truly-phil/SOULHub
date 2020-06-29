package io.horrorshow.soulswap.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulswap.service.SOULPatchService;
import io.horrorshow.soulswap.ui.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "about", layout = MainLayout.class)
@PageTitle("SOULSwap | About")
public class AboutView extends VerticalLayout {

    private SOULPatchService soulPatchService;

    public AboutView(@Autowired SOULPatchService soulPatchService) {
        this.soulPatchService = soulPatchService;
        addClassName("about-view");

        createContent();
    }

    private void createContent() {
        H1 title = new H1("About SOULSwap");

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
