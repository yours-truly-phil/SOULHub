package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.VerificationToken;
import io.horrorshow.soulhub.service.SOULHubUserDetailsService;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Route(value = UIConst.ROUTE_CONFIRM_REGISTER, layout = MainLayout.class)
@PageTitle(UIConst.TITLE_CONFIRM_REGISTER)
public class ConfirmRegistrationView
        extends VerticalLayout
        implements HasUrlParameter<String> {

    private static final long serialVersionUID = 2366499291878068397L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmRegistrationView.class);

    private final SOULHubUserDetailsService userDetailsService;

    public ConfirmRegistrationView(@Autowired SOULHubUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;

        add(new H1("Confirm registration"));
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        LOGGER.debug("confirm registration with param {}", parameter);
        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> pmap
                = queryParameters.getParameters();
        LOGGER.debug("parametersMap: {}", pmap.toString());

        if (pmap.containsKey(UIConst.PARAM_TOKEN)
                && !pmap.get(UIConst.PARAM_TOKEN).isEmpty()) {
            Optional<VerificationToken> token =
                    userDetailsService.getVerificationToken(pmap.get(UIConst.PARAM_TOKEN).get(0));
            if (token.isPresent()
                    && token.get().getExpiryDate().isAfter(LocalDateTime.now())) {
                AppUser user = token.get().getUser();
                user.setStatus(AppUser.UserStatus.ACTIVE);
                AppUser updatedUser = userDetailsService.updateUser(user);
                add(new Span("User " + updatedUser.getUserName() + " now active, please login"));
                add(new RouterLink("login", LoginView.class));
            } else {
                add(new Span("Invalid token"));
            }
        } else {
            add(new Span("Invalid parameter"));
        }
    }
}
