package io.horrorshow.soulhub.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import io.horrorshow.soulhub.data.AppUser;

public class AppUserSaveEvent extends ComponentEvent<Component> {

    private static final long serialVersionUID = 5239271302237688052L;

    private final AppUser appUser;

    public AppUserSaveEvent(Component source, AppUser appUser) {
        super(source, false);
        this.appUser = appUser;
    }

    public AppUser getAppUser() {
        return appUser;
    }
}
