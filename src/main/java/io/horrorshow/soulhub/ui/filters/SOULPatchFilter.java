package io.horrorshow.soulhub.ui.filters;

import io.horrorshow.soulhub.data.AppUser;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SOULPatchFilter implements Serializable {
    private static final long serialVersionUID = -3255793403758813065L;
    private boolean onlyCurUser = false;
    @NonNull
    private String namesFilter = "";
    private Set<AppUser> appUserFilter = new HashSet<>();

    public static SOULPatchFilter getEmptyFilter() {
        return new SOULPatchFilter();
    }

    public static SOULPatchFilter getOnlyCurrentUser() {
        var filter = new SOULPatchFilter();
        filter.setOnlyCurUser(true);
        return filter;
    }
}
