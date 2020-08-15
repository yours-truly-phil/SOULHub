package io.horrorshow.soulhub.ui.views;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.shared.Registration;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.service.UserService;
import io.horrorshow.soulhub.ui.MainLayout;
import io.horrorshow.soulhub.ui.UIConst;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Route(value = UIConst.ROUTE_SOULPATCH, layout = MainLayout.class)
@PageTitle(UIConst.TITLE_SOULPATCH)
public class SOULPatchView extends VerticalLayout implements HasUrlParameter<String>,
        HasValueAndElement<
                AbstractField.ComponentValueChangeEvent<SOULPatchView, SOULPatch>, SOULPatch> {

    private static final long serialVersionUID = -6869511952510668506L;

    private final SOULPatchService soulPatchService;
    private final UserService userService;

    private final AbstractFieldSupport<SOULPatchView, SOULPatch> fieldSupport;

    public SOULPatchView(@Autowired SOULPatchService soulPatchService,
                         @Autowired UserService userService) {
        this.soulPatchService = soulPatchService;
        this.userService = userService;

        this.fieldSupport = new AbstractFieldSupport<>(this, null, Objects::equals, sp -> {
        });

        setClassName("soulpatch-view");
    }

    @Override
    public SOULPatch getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public void setValue(SOULPatch value) {
        fieldSupport.setValue(value);
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<SOULPatchView, SOULPatch>> listener) {
        return fieldSupport.addValueChangeListener(listener);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {

    }
}
