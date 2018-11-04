class Cell {
    int value
    List<Integer> candidates

    Cell() {}

    Cell(int value) {
        this.value = value
        this.candidates = []
    }

    void setValue(int value) {
        this.value = value
        this.candidates = []
    }

    @Override
    String toString() {
        return value ?: '-'
    }
}
