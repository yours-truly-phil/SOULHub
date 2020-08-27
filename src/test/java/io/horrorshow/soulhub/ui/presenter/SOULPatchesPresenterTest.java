package io.horrorshow.soulhub.ui.presenter;

import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.service.UserService;
import io.horrorshow.soulhub.ui.dataproviders.SOULPatchesGridDataProvider;
import io.horrorshow.soulhub.ui.views.SOULPatchesView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

class SOULPatchesPresenterTest {
    @Mock
    SOULPatchesGridDataProvider dataProvider;
    @Mock
    UserService userService;
    @Mock
    SOULPatchService soulPatchService;

    SOULPatchesPresenter presenter;
    SOULPatchesView view;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        presenter = new SOULPatchesPresenter(dataProvider, userService, soulPatchService);
        view = new SOULPatchesView(presenter);
    }

    @Test
    void onNavigation_no_filter_without_parameter() {
        presenter.init(view);
        view.getHeader().addValueChangeListener(event -> fail("no event expected"));

        presenter.onNavigation(null, new HashMap<>());
    }

    @Test
    void onNavigation_filter_by_user() {
        presenter.init(view);
        view.getHeader().addValueChangeListener(event -> {
            var appUser = event.getValue().getAppUserFilter();
            assertThat(appUser.size() == 1);
            assertThat(
                    appUser.stream()
                            .map(AppUser::getUserName)
                            .allMatch(
                                    s -> s.equals("username returned by service")));
        });
        var map = Map.of("user", List.of("4711"));
        var userToReturn = new AppUser();
        userToReturn.setUserName("username from service");
        when(userService.findById(4711L)).thenReturn(Optional.of(userToReturn));

        presenter.onNavigation(null, map);
    }

    @Test
    void onNavigation_filter_by_multiple_users() {
        presenter.init(view);
        view.getHeader().addValueChangeListener(event -> {
            var appUser = event.getValue().getAppUserFilter();
            assertThat(appUser.size() == 3);
            var usernames =
                    appUser
                            .stream()
                            .map(AppUser::getUserName)
                            .collect(Collectors.toSet());
            assertThat(usernames.contains("aaa"));
            assertThat(usernames.contains("bbb"));
            assertThat(usernames.contains("ccc"));
        });
        var map = Map.of("user", List.of("1", "2", "3"));
        var aaa = new AppUser();
        aaa.setUserName("aaa");
        var bbb = new AppUser();
        bbb.setUserName("bbb");
        var ccc = new AppUser();
        ccc.setUserName("ccc");
        when(userService.findById(1L)).thenReturn(Optional.of(aaa));
        when(userService.findById(2L)).thenReturn(Optional.of(bbb));
        when(userService.findById(3L)).thenReturn(Optional.of(ccc));

        presenter.onNavigation(null, map);
    }

    @Test
    void onNavigation_no_filter_on_illegal_user_param() {
        presenter.init(view);
        view.getHeader().addValueChangeListener(
                event -> fail("no event expected"));
        var map = Map.of("user", List.of("invalid_id"));
        verify(userService, never()).findById(any());
        presenter.onNavigation(null, map);
    }
}