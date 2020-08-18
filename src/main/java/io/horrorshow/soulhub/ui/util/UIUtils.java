package io.horrorshow.soulhub.ui.util;

import com.vaadin.flow.component.UI;

public class UIUtils {
    public static void updateURI(String uri) {
        UI.getCurrent()
                .getPage()
                .executeJs("history.pushState(history.state,'','" + uri + "');");
    }
}
