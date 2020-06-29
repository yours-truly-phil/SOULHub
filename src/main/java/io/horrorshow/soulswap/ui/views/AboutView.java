package io.horrorshow.soulswap.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulswap.ui.MainLayout;

@Route(value = "about", layout = MainLayout.class)
@PageTitle("SOULSwap | About")
public class AboutView  extends VerticalLayout {

    public AboutView() {
        addClassName("about-view");

        createContent();
    }

    private void createContent() {
        H1 title = new H1("About SOULSwap");
        Paragraph loremIpsum = new Paragraph("Lorem ipsum " +
                "dolor sit amet, consectetur adipiscing elit. Mauris id metus nibh. " +
                "Cras ligula nunc, iaculis quis est vitae, commodo malesuada justo. " +
                "Aliquam semper cursus neque, eu ornare ex tincidunt nec. " +
                "Vivamus ultrices metus enim, nec eleifend diam congue ut. " +
                "Orci varius natoque penatibus et magnis dis parturient montes, " +
                "nascetur ridiculus mus. Suspendisse dignissim nisl tristique orci " +
                "ornare sodales eu ut turpis. Ut vitae mollis urna. Duis sagittis justo " +
                "ac libero accumsan, vitae elementum ex convallis. Aenean quis velit ante. " +
                "Maecenas odio lectus, laoreet quis laoreet suscipit, scelerisque a urna. " +
                "Duis nulla tellus, tincidunt ut pretium eget, consequat ultricies ante.");
        add(title);
        add(loremIpsum);
    }

}
