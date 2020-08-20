package io.horrorshow.soulhub.ui.presenter;

import com.vaadin.flow.spring.annotation.SpringComponent;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.service.UserService;
import io.horrorshow.soulhub.ui.events.SOULPatchDownloadEvent;
import io.horrorshow.soulhub.ui.views.SOULPatchView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Log4j2
public class SOULPatchPresenter {

    private final SOULPatchService soulPatchService;
    private final UserService userService;
    private SOULPatchView view;

    public SOULPatchPresenter(@Autowired SOULPatchService soulPatchService,
                              @Autowired UserService userService) {
        this.soulPatchService = soulPatchService;
        this.userService = userService;
    }

    public void init(SOULPatchView view) {
        this.view = view;
        view.getSoulPatchReadOnly()
                .setSOULPatchZipInputStreamProvider(soulPatchService::getZipSOULPatchStreamProvider);
        view.getSoulPatchReadOnly()
                .addSOULPatchDownloadListener(this::soulPatchDownloaded);
    }

    private void soulPatchDownloaded(SOULPatchDownloadEvent event) {
        var sp = soulPatchService.incrementNoDownloadsAndSave(event.getSoulPatch());
        view.setValue(sp);
    }

    public void onNavigation(String parameter, Map<String, List<String>> parameterMap) {
        if(soulPatchService.isPossibleSOULPatchId(parameter)) {
            var soulPatch = soulPatchService.findById(Long.valueOf(parameter));
            view.setValue(soulPatch);
        } else {
            view.createErrorView("No SOULPatch with the given parameter");
        }
    }
}
