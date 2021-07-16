package com.epam.esm.dto;

import org.springframework.hateoas.PagedModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class PagedDTO <T>{

    private Collection<T> page;

    private PagedModel.PageMetadata pageMetadata;

    public PagedDTO() {
        page = new ArrayList<>();
    }

    public PagedDTO(Collection<T> page, PagedModel.PageMetadata pageMetadata) {
        this.page = page;
        this.pageMetadata = pageMetadata;
    }

    public Collection<T> getPage() {
        return page;
    }

    public void setPage(Collection<T> page) {
        this.page = page;
    }

    public PagedModel.PageMetadata getPageMetadata() {
        return pageMetadata;
    }

    public void setPageMetadata(PagedModel.PageMetadata pageMetadata) {
        this.pageMetadata = pageMetadata;
    }

    public boolean isEmpty(){
        return page.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PagedDTO<?> pagedDTO = (PagedDTO<?>) o;
        return Objects.equals(page, pagedDTO.page) && Objects.equals(pageMetadata, pagedDTO.pageMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, pageMetadata);
    }
}
