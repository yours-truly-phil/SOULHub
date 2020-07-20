package io.horrorshow.soulhub.ui.components;

import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.PropertyDescriptor;
import com.vaadin.flow.component.PropertyDescriptors;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

@Tag("stars-rating")
@NpmPackage(value = "@manufosela/stars-rating", version = "3.3.3")
@JsModule("@manufosela/stars-rating")
public class StarsRating extends AbstractSinglePropertyField<StarsRating, Integer> {

    private static final long serialVersionUID = -6410609058681495208L;
    private static final PropertyDescriptor<Integer, Integer> numstarsProperty =
            PropertyDescriptors.propertyWithDefault("numstars", 0);
    private static final PropertyDescriptor<Boolean, Boolean> manualProperty =
            PropertyDescriptors.propertyWithDefault("manual", false);

    public StarsRating() {
        super("rating", 0, false);
    }

    public Integer getNumstars() {
        return numstarsProperty.get(this);
    }

    public void setNumstars(Integer numstars) {
        numstarsProperty.set(this, numstars);
    }

    public Boolean getManual() {
        return manualProperty.get(this);
    }

    public void setManual(Boolean manual) {
        manualProperty.set(this, manual);
    }
}
