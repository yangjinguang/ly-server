package com.liyu.server.model;

public class Pagination {
    private Integer total;
    private Integer page;
    private Integer size;
    private Integer totalPages;
    private Boolean last;

    public Pagination(Integer total, Integer page, Integer size) {
        this.total = total;
        this.page = page;
        this.size = size;
        int i = total % size;
        if (i > 0) {
            this.totalPages = total / size + 1;
        } else {
            this.totalPages = total / size;
        }
        this.last = page >= this.totalPages;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "total=" + total +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
