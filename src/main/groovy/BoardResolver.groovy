class BoardResolver {

    Board board

    BoardResolver(Board board) {
        this.board = board
    }

    void tryToResolve() {
        boolean changed = true

        while (this.board.getEmptyCells() && changed) {
            changed = findOnlyOneCandidate()
        }

        println(board)
    }

    void fillCandidates() {
        Cell[][] cells = board.board
        (0..<cells.length).each { i ->
            (0..<cells[0].length).each { j ->
                if (!cells[i][j].value) {
                    cells[i][j].candidates = (1..9)
                    removeKnownValuesFromCandidates(i, j)
                }
            }
        }
    }

    void updateCandidates(int i, int j, int value) {
        Closure removeValue = { Cell cell -> cell.candidates -= value }
        board.getRecord(i).each(removeValue)
        board.getColumn(j).each(removeValue)
        board.getSquare(i, j).each(removeValue)
    }

    void removeKnownValuesFromCandidates(int i, int j) {
        Cell cell = board.board[i][j]
        cell.candidates -= this.board.getKnownValuesFromNeighborhood(i, j)
        assert cell.candidates.size() > 0
    }

    boolean findOnlyOneCandidate() {
        boolean changed = false
        Cell[][] cells = board.board
        (0..<cells.length).each { i ->
            (0..<cells[0].length).each { j ->
                Cell cell = cells[i][j]
                if (!cell.value && cell.candidates.size() == 1) {
                    int onlyOne = cell.candidates.first()
                    cell.setValue(onlyOne)
                    updateCandidates(i, j, onlyOne)
                    changed = true
                }
            }
        }
        return changed
    }
}
