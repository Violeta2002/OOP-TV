package fileio;

public final class FiltersInput {
    private SortInput sort;
    private ContainsInput contains;

    public FiltersInput() {
    }

    public SortInput getSort() {
        return sort;
    }

    public void setSort(final SortInput sort) {
        this.sort = sort;
    }

    public ContainsInput getContains() {
        return contains;
    }
}

