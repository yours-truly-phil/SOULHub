package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.String.format;

@Route(value = UIConst.ROUTE_ABOUT, layout = MainLayout.class)
@PageTitle(UIConst.TITLE_ABOUT)
public class AboutView extends VerticalLayout {

    private static final long serialVersionUID = -2584215264372639999L;

    private final SOULPatchService soulPatchService;

    public AboutView(@Autowired SOULPatchService soulPatchService) {
        this.soulPatchService = soulPatchService;
        addClassName("about-view");

        createContent();
    }

    private void createContent() {
        setAlignItems(Alignment.CENTER);

        H1 title = new H1("About SOULHub");

        Span noSOULPatches = new Span(
                format("%s SOUL Patches stored",
                        soulPatchService.countSOULPatches()));

        Span noSPFiles = new Span(
                format("%s soul or soulpatch manifest files attached",
                        soulPatchService.countSPFiles()));

        Span totalDownloads = new Span(
                format("%s soulpatch downloads",
                        soulPatchService.countTotalDownloads()));

        add(title);
        add(noSOULPatches);
        add(noSPFiles);
        add(totalDownloads);
    }

}
