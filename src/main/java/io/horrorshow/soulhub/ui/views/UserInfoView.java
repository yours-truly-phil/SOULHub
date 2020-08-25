package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import io.horrorshow.soulhub.ui.components.AppUserInfo;
import io.horrorshow.soulhub.ui.presenter.AppUserPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.Map;

@Route(value = UIConst.ROUTE_USERINFO, layout = MainLayout.class)
@Secured(UIConst.ROLE_USER)
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
        add(createShowSoulPatchesByUserLink());
    }

    public AppUserInfo getAppUserInfo() {
        return appUserInfo;
    }

    private RouterLink createShowSoulPatchesByUserLink() {
        QueryParameters queryParameters =
                QueryParameters.simple(
                        Map.of(UIConst.PARAM_SHOW_BY_CURRENT_USER, Boolean.TRUE.toString()));

        RouterLink link = new RouterLink(
                "show my soulpatches",
                SOULPatchesView.class);
        link.setQueryParameters(queryParameters);
        return link;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        // TODO
        var parameterMap = event.getLocation().getQueryParameters().getParameters();
        appUserPresenter.onNavigation(parameter, parameterMap);
    }
}
