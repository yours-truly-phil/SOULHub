package io.horrorshow.soulhub.ui.dataproviders;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.util.SOULPatchesFetchFilter;
import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.ui.UIConst;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import java.util.List;
import java.util.function.Consumer;

@SpringComponent
@UIScope
@Log4j2
public class SOULPatchesGridDataProvider
        extends FilterablePageableDataProvider
        <SOULPatch, SOULPatchesFetchFilter> {

    private static final long serialVersionUID = 8027534129208314189L;

    private final SOULPatchService soulPatchService;
    private List<QuerySortOrder> defaultSortOrders;
    private Consumer<Page<SOULPatch>> pageObserver;

    public SOULPatchesGridDataProvider(@Autowired SOULPatchService soulPatchService) {
        this.soulPatchService = soulPatchService;
        setSortOrders(UIConst.DEFAULT_SORT_DIRECTION, UIConst.DEFAULT_SORT_ORDER);
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
            Query<SOULPatch, SOULPatchesFetchFilter> query, Pageable pageable) {
        SOULPatchesFetchFilter filter = query.getFilter().orElse(SOULPatchesFetchFilter.getEmptyFilter());
        Page<SOULPatch> page = soulPatchService.findAnyMatching(filter, pageable);
        if (pageObserver != null) {
            pageObserver.accept(page);
        }
        log.debug("query: {} pageable: {} result: {}", query, pageable, page);
        return page;
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return defaultSortOrders;
    }

    @Override
    protected int sizeInBackEnd(Query<SOULPatch, SOULPatchesFetchFilter> query) {
        SOULPatchesFetchFilter filter = query.getFilter().orElse(SOULPatchesFetchFilter.getEmptyFilter());
        int count = soulPatchService.countAnyMatching(filter);
        log.debug("size in backend for query {}: {}", query, count);
        return count;
    }

    public void setPageObserver(Consumer<Page<SOULPatch>> pageObserver) {
        this.pageObserver = pageObserver;
    }
}
