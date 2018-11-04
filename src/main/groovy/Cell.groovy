class Cell {
    int value
    List<Integer> candidates

    Cell() {}

    Cell(int value) {
        this.value = value
        this.candidates = value ? [] : (1..9)
    }

    @Override
    String toString() {
        return value ?: '-'
    }
}
