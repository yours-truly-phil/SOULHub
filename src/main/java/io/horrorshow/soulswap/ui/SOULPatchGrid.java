package io.horrorshow.soulswap.ui;

import com.vaadin.flow.component.grid.Grid;
import io.horrorshow.soulswap.data.SOULPatch;

public class SOULPatchGrid extends Grid<SOULPatch> {
    public SOULPatchGrid() {
        super(SOULPatch.class);
    }

    public SOULPatchGrid addSOULPatchColumns() {
        addColumn(SOULPatch::getId).setHeader("Id");
        addColumn(SOULPatch::getName).setHeader("name");
        addColumn(SOULPatch::getDescription).setHeader("description");
        addColumn(SOULPatch::getSoulFileName).setHeader("soulFileName");
        addColumn(SOULPatch::getSoulFileContent).setHeader("soulFileContent");
        addColumn(SOULPatch::getSoulpatchFileName).setHeader("soulpatchFileName");
        addColumn(SOULPatch::getSoulpatchFileContent).setHeader("soulpatchFileContent");
        addColumn(SOULPatch::getAuthor).setHeader("author");
        addColumn(SOULPatch::getNoServings).setHeader("noServings");
        return this;
    }
}
