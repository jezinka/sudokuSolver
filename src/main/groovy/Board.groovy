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
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                board[i][j] = new Cell(value: input[i][j])
            }
        }
    }

    Cell[] getRecord(int rowNum) { return board[rowNum] }

    Cell[] getColumn(int colNum) { return (0..<board.length).collect { board[it][colNum] } }

    Cell[] getSquare(int rowNum, int colNum) {
        int beginRow = rowNum - rowNum % 3
        int beginColumn = colNum - colNum % 3

        return (beginRow..<beginRow + 3).collect { row ->
            (beginColumn..<beginColumn + 3).collect { column ->
                board[row][column]
            }
        }.flatten()
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

    List<Cell> getEmptyCells() {
        return this.board
                .collect { row -> row.findAll { cell -> cell.value == 0 } }
                .flatten()
                .collect { it as Cell }
    }

    @Override
    String toString() {
        return board.collect { it.collect { it.toString() }.join(' ') }.join('\n') + '\n'
    }

}
