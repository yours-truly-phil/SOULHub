package io.horrorshow.soulhub.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import io.horrorshow.soulhub.ui.views.LoginView;
import org.springframework.stereotype.Component;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    private static final long serialVersionUID = 7619995414809223142L;

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
        if(!SecurityUtils.isAccessGranted(event.getNavigationTarget())) {
            if(SecurityUtils.isUserLoggedIn()) {
                event.rerouteToError(NotFoundException.class);
            } else {
                event.rerouteTo(LoginView.class);
            }
        }
    }

}
