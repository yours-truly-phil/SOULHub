package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import io.horrorshow.soulhub.ui.components.AppUserInfo;
import io.horrorshow.soulhub.ui.presenter.AppUserPresenter;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = UIConst.ROUTE_USERINFO, layout = MainLayout.class)
@PageTitle(UIConst.TITLE_USERINFO)
public class UserInfoView extends VerticalLayout
        implements HasUrlParameter<String> {

    private static final long serialVersionUID = 6423464010060256222L;

    private final AppUserInfo appUserInfo = new AppUserInfo();

    private final AppUserPresenter appUserPresenter;

    public UserInfoView(@Autowired AppUserPresenter appUserPresenter) {
        this.appUserPresenter = appUserPresenter;
        appUserPresenter.init(this);

        add(appUserInfo);
    }

    public void setError(String msg) {
        removeAll();
        add(new H3(String.format("%s", msg)));
    }

    public AppUserInfo getAppUserInfo() {
        return appUserInfo;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        var parameterMap = event.getLocation().getQueryParameters().getParameters();
        appUserPresenter.onNavigation(parameter, parameterMap);
    }
}
