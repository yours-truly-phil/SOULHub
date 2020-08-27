package io.horrorshow.soulhub.ui.presenter;

import com.vaadin.flow.spring.annotation.SpringComponent;
import io.horrorshow.soulhub.security.SecurityUtils;
import io.horrorshow.soulhub.service.UserService;
import io.horrorshow.soulhub.ui.events.AppUserSaveEvent;
import io.horrorshow.soulhub.ui.views.UserInfoView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.List;
import java.util.Map;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Log4j2
public class AppUserPresenter {

    private final UserService userService;

    private UserInfoView view;

    public AppUserPresenter(@Autowired UserService userService) {
        this.userService = userService;
    }

    public void init(UserInfoView view) {
        this.view = view;
        view.getAppUserInfo().addAppUserSaveListener(this::appUserSaveEvent);
    }

    private void appUserSaveEvent(AppUserSaveEvent event) {
        try {
            if (userService.getCurrentAppUser().isPresent()
                    && userService.getCurrentAppUser().get().getId().equals(event.getAppUser().getId())) {
                userService.updateUser(event.getAppUser());
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void onNavigation(String parameter, Map<String, List<String>> parameterMap) {
        var user = userService.loadAppUser(parameter);
        var currentUser = userService.getCurrentAppUser();

        view.getAppUserInfo()
                .setEditable(SecurityUtils.isUserLoggedIn()
                        && currentUser.isPresent() && user.isPresent()
                        && currentUser.get().equals(user.get()));

        user.ifPresentOrElse(
                appUser -> view.getAppUserInfo().setValue(appUser)
                , () -> view.setError("No user found for given parameter"));
    }
}
