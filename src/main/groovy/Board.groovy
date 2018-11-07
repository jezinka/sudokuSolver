class Board {
    static Integer[][] sample = [
            [0, 0, 0, 0, 0, 2, 0, 0, 3],
            [6, 0, 0, 0, 3, 0, 0, 0, 9],
            [9, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 4, 5, 0, 0, 0, 6, 0, 0],
            [8, 0, 0, 6, 0, 0, 3, 0, 7],
            [0, 0, 0, 0, 0, 0, 8, 5, 2],
            [4, 9, 0, 0, 6, 3, 0, 0, 5],
            [0, 3, 0, 5, 0, 8, 0, 0, 0],
            [0, 0, 0, 4, 9, 0, 0, 0, 0]
    ]

    Cell[][] board

    Board(Integer[][] input = sample) {
        board = new Cell[input.length][input[0].length]
        (0..<input.length).each { i ->
            (0..<input[0].length).each { j ->
                board[i][j] = new Cell(input[i][j], i, j)
            }
        }
    }

    Cell[] getRecord(int rowNum) { return board[rowNum] }

    Cell[] getEmptyCellsFromRecord(int rowNum) {
        return getRecord(rowNum).findAll { it.value == 0 }
    }

    Cell[] getColumn(int colNum) { return (0..<board.length).collect { board[it][colNum] } }

    Cell[] getEmptyCellsFromColumn(int colNum) {
        return getColumn(colNum).findAll { it.value == 0 }
    }

    Cell[] getSquare(int rowNum, int colNum) {
        int beginRow = rowNum - rowNum % 3
        int beginColumn = colNum - colNum % 3

        return (beginRow..<beginRow + 3).collect { row ->
            (beginColumn..<beginColumn + 3).collect { column ->
                board[row][column]
            }
        }.flatten()
    }

    Cell[] getEmptyCellsFromSquare(int rowNum, int colNum) {
        return getSquare(rowNum, colNum).findAll { it.value == 0 }
    }

    List<Integer> getValuesForColumn(int colNum) {
        return getColumn(colNum)*.value.findAll()
    }

    List<Integer> getValuesForRecord(int rowNum) {
        return getRecord(rowNum)*.value.findAll()
    }

    List<Integer> getValuesForSquare(int rowNum, int colNum) {
        return getSquare(rowNum, colNum)*.value.flatten().findAll().collect { it as Integer }
    }

    List<Integer> getKnownValuesFromNeighborhood(int rowNum, int colNum) {
        List<Integer> knownValues = []
        knownValues.addAll(getValuesForSquare(rowNum, colNum))
        knownValues.addAll(getValuesForRecord(rowNum))
        knownValues.addAll(getValuesForColumn(colNum))
        return knownValues.unique()
    }

    List<Cell> getEmptyCells() {
        return this.board
                .collect { row -> row.findAll { cell -> cell.value == 0 } }
                .flatten()
                .collect { it as Cell }
    }

    void validate() {
        (0..8).each {
            assert getValuesForRecord(it).countBy { it }.every { it.value == 1 }
            assert getValuesForColumn(it).countBy { it }.every { it.value == 1 }
        }

        (0..6).step(3).each { rowNum ->
            (0..6).step(3).each { colNum ->
                assert getValuesForSquare(rowNum, colNum).countBy { it }.every { it.value == 1 }
            }
        }
    }

    @Override
    String toString() {
        return board.collect { it.collect { it.toString() }.join(' ') }.join('\n') + '\n'
    }

}
