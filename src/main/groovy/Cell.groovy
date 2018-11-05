class Cell {
    int value
    List<Integer> candidates
    int rowNum, colNum

    Cell() {}

    Cell(int value, int rowNum, int colNum) {
        this.value = value
        this.candidates = []
        this.rowNum = rowNum
        this.colNum = colNum
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
