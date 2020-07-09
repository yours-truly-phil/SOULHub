package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import io.horrorshow.soulhub.security.SecurityUtils;
import io.horrorshow.soulhub.service.SOULHubUserDetailsService;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import static java.lang.String.format;

@Route(value = UIConst.ROUTE_USERINFO, layout = MainLayout.class)
@Secured(UIConst.ROLE_USER)
@PageTitle(UIConst.TITLE_USERINFO)
public class UserInfoView extends VerticalLayout {

    private static final long serialVersionUID = 6423464010060256222L;

    private final SOULHubUserDetailsService userDetailsService;

    public UserInfoView(@Autowired SOULHubUserDetailsService userDetailsService) {
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
