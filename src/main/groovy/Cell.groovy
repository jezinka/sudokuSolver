class Cell {
    Integer value
    int[] candidates

    Cell() {}

    @Override
    String toString() {
        return value ?: '-'
    }
}
