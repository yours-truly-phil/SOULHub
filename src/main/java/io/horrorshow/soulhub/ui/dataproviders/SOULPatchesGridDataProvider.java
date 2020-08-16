package io.horrorshow.soulhub.ui.dataproviders;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import io.horrorshow.soulhub.HasLogger;
import io.horrorshow.soulhub.data.AppUser;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.ui.UIConst;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@SpringComponent
@UIScope
public class SOULPatchesGridDataProvider
        extends FilterablePageableDataProvider<SOULPatch, SOULPatchesGridDataProvider.SOULPatchFilter>
        implements HasLogger {

    private static final long serialVersionUID = 8027534129208314189L;

    private final SOULPatchService soulPatchService;
    private List<QuerySortOrder> defaultSortOrders;
    private Consumer<Page<SOULPatch>> pageObserver;

    public SOULPatchesGridDataProvider(@Autowired SOULPatchService soulPatchService) {
        this.soulPatchService = soulPatchService;
        setSortOrders(UIConst.DEFAULT_SORT_DIRECTION, UIConst.ORDER_SORT_FIELDS_SOULPATCHES);
    }

    public void setSortOrders(Sort.Direction direction, String[] properties) {
        QuerySortOrderBuilder builder = new QuerySortOrderBuilder();
        for (String property : properties) {
            if (direction.isAscending()) {
                builder.thenAsc(property);
            } else {
                builder.thenDesc(property);
            }
        }
        defaultSortOrders = builder.build();
    }

    @Override
    protected Page<SOULPatch> fetchFromBackEnd(
            Query<SOULPatch, SOULPatchFilter> query, Pageable pageable) {
        SOULPatchFilter filter = query.getFilter().orElse(SOULPatchFilter.getEmptyFilter());
        Page<SOULPatch> page = soulPatchService.findAnyMatching(filter, pageable);
        if (pageObserver != null) {
            pageObserver.accept(page);
        }
        LOGGER().debug("query: {} pageable: {} result: {}", query, pageable, page);
        return page;
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return defaultSortOrders;
    }

    @Override
    protected int sizeInBackEnd(Query<SOULPatch, SOULPatchFilter> query) {
        SOULPatchFilter filter = query.getFilter().orElse(SOULPatchFilter.getEmptyFilter());
        int count = soulPatchService.countAnyMatching(filter);
        LOGGER().debug("size in backend for query {}: {}", query, count);
        return count;
    }

    public void setPageObserver(Consumer<Page<SOULPatch>> pageObserver) {
        this.pageObserver = pageObserver;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static class SOULPatchFilter implements Serializable {
        private static final long serialVersionUID = -2667841574926699231L;
        private Set<AppUser> userFilter = new HashSet<>();
        private Optional<String> namesFilter = Optional.empty();

        public static SOULPatchFilter getEmptyFilter() {
            return new SOULPatchFilter();
        }
    }
}
