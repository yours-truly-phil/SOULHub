package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulhub.security.SecurityUtils;
import io.horrorshow.soulhub.service.SOULHubUserDetailsService;
import io.horrorshow.soulhub.ui.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import static java.lang.String.format;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@Route(value = "userinfo", layout = MainLayout.class)
@Secured("ROLE_USER")
@PageTitle("SOULHub | Userinfo")
public class UserInfoView extends VerticalLayout {
    private static final long serialVersionUID = 6423464010060256222L;

    private final SOULHubUserDetailsService userDetailsService;

    @Autowired
    public UserInfoView(SOULHubUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;

        addClassName("userinfo-view");

        init();
    }

    private void init() {
        H1 title = new H1("Userinfo");

        Span helloUser = new Span(format("Hello %s", SecurityUtils.getUsername()));

        Span isLoggedIn = new Span(format("you are %s",
                this.userDetailsService.isAuthenticated()
                        ? "authenticated"
                        : "here?! hmm"));

        add(title, helloUser, isLoggedIn);
    }
}
