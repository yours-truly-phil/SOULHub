package io.horrorshow.soulhub.data.util;

import io.horrorshow.soulhub.data.AppUser;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SOULPatchesFetchFilter implements Serializable {

    private static final long serialVersionUID = -1706756888052669980L;

    private final Set<AppUser> usersFilter = new HashSet<>();

    private String namesFilter = null;

    private String fullTextSearch = null;

    public static SOULPatchesFetchFilter getEmptyFilter() {
        return new SOULPatchesFetchFilter();
    }

    public Optional<String> getNamesFilter() {
        if (namesFilter != null && !namesFilter.isBlank()) {
            return Optional.of(namesFilter);
        } else {
            return Optional.empty();
        }
    }

    public void setNamesFilter(String namesFilter) {
        this.namesFilter = namesFilter;
    }

    public Optional<String> getFullTextSearch() {
        if (fullTextSearch != null && !fullTextSearch.isBlank()) {
            return Optional.of(fullTextSearch);
        } else {
            return Optional.empty();
        }
    }

    public void setFullTextSearch(String s) {
        fullTextSearch = s;
    }

    public Set<AppUser> getUsersFilter() {
        return usersFilter;
    }
}
