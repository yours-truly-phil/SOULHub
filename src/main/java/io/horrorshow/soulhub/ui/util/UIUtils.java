package io.horrorshow.soulhub.ui.util;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;

import javax.servlet.http.HttpServletRequest;

public class UIUtils {
    public static void updateURI(String uri) {
        UI.getCurrent()
                .getPage()
                .executeJs("history.pushState(history.state,'','" + uri + "');");
    }

    public static HttpServletRequest getHttpServletRequest() {
        return ((VaadinServletRequest) VaadinService.getCurrentRequest()).getHttpServletRequest();
    }

    public static StringBuffer getRequestUrl() {
        return getHttpServletRequest().getRequestURL();
    }
}
