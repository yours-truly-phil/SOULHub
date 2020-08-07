package io.horrorshow.soulhub.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import io.horrorshow.soulhub.ui.views.LoginView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    private static final long serialVersionUID = 7619995414809223142L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigureUIServiceInitListener.class);

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }

    /**
     * Reroutes the user if not authorized to access the view
     *
     * @param event
     *         - before navigation event with event details
     */
    private void beforeEnter(BeforeEnterEvent event) {
        LOGGER.debug("BeforeEnter event: {}", event.getLocation().getPathWithQueryParameters());
        if (!SecurityUtils.isAccessGranted(event.getNavigationTarget())) {
            if (SecurityUtils.isUserLoggedIn()) {
                LOGGER.debug("reroute to error");
                event.rerouteToError(NotFoundException.class);
            } else {
                LOGGER.debug("reroute to {}", LoginView.class.getSimpleName());
                event.rerouteTo(LoginView.class);
            }
        }
    }

}
