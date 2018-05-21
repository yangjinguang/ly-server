package com.liyu.server.model;

public class Pagination {
    private Integer total;
    private Integer page;
    private Integer size;

    public Pagination(Integer total, Integer page, Integer size) {
        this.total = total;
        this.page = page;
        this.size = size;
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

    @Override
    public String toString() {
        return "Pagination{" +
                "total=" + total +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
