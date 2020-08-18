package io.horrorshow.soulhub.ui.filters;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SOULPatchFilter implements Serializable {
    private static final long serialVersionUID = -3255793403758813065L;
    private boolean onlyCurUser = false;
    private String namesFilter = null;

    public static SOULPatchFilter getEmptyFilter() {
        return new SOULPatchFilter();
    }
}
