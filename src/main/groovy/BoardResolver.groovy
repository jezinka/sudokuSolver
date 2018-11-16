class BoardResolver {

    Board board

    BoardResolver(Board board) {
        this.board = board
    }

    void tryToResolve() {
        println board.emptyCells.size()
        boolean changed = true

        while (this.board.getEmptyCells() && changed) {
            changed = findOnlyOneCandidate() | findOnlyOnePosition() | findPairs() | findBlockedCandidatesRecord()
            board.validate()
        }
        println board.emptyCells.size()
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

    void updateCandidates(Cell cell) {
        Closure removeValue = { Cell emptyCell -> emptyCell.candidates -= cell.value }
        board.getEmptyCellsFromRecord(cell.rowNum).each(removeValue)
        board.getEmptyCellsFromColumn(cell.colNum).each(removeValue)
        board.getEmptyCellsFromSquare(cell.rowNum, cell.colNum).each(removeValue)
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
                    updateCandidates(cell)
                    changed = true
                }
            }
        }
        return changed
    }

    boolean findOnlyOnePosition() {
        boolean changed = false
        (0..<board.board.length).each { rowNum ->
            changed = changed | findAndSetOnePositionCandidate(board.getEmptyCellsFromRecord(rowNum))
        }

        (0..<board.board[0].length).each { colNum ->
            changed = changed | findAndSetOnePositionCandidate(board.getEmptyCellsFromColumn(colNum))
        }

        (0..6).step(3).each { rowNum ->
            (0..6).step(3).each { colNum ->
                changed = changed | findAndSetOnePositionCandidate(board.getEmptyCellsFromSquare(rowNum, colNum))
            }
        }
        return changed
    }

    private boolean findAndSetOnePositionCandidate(Cell[] cells) {
        boolean changed = false
        cells*.candidates
                .flatten()
                .countBy { it }
                .findAll { it.value == 1 }
                *.key
                .each { int value ->
            Cell cell = cells.find { it.candidates.contains(value) }
            cell.setValue(value)
            updateCandidates(cell)
            changed = true

        }
        return changed
    }

    boolean findPairs() {
        boolean changed = false
        (0..<board.board.length).each { rowNum ->
            changed = changed | findAndRemovePairsCandidates(board.getEmptyCellsFromRecord(rowNum))
        }
        (0..<board.board[0].length).each { colNum ->
            changed = changed | findAndRemovePairsCandidates(board.getEmptyCellsFromColumn(colNum))
        }

        (0..6).step(3).each { rowNum ->
            (0..6).step(3).each { colNum ->
                changed = changed | findAndRemovePairsCandidates(board.getEmptyCellsFromSquare(rowNum, colNum))
            }
        }
        return changed
    }

    boolean findAndRemovePairsCandidates(Cell[] cells) {
        boolean changed = false
        cells.findAll { it.candidates.size() == 2 }
                .groupBy { it.candidates }
                .findAll { it.value.size() == 2 }
                .each { List<Integer> k, v ->
            cells.each { cell ->
                if (!(cell in v) && cell.candidates.any { k.contains(it) }) {
                    cell.candidates.removeAll(k)
                    changed = true
                }
            }
        }
        return changed
    }

    boolean findBlockedCandidatesRecord() {
        boolean changed = false
        (0..<board.board.length).each { rowNum ->
            changed = changed | findAndRemoveBlockedCandidates(board.getEmptyCellsFromRecord(rowNum))
        }
        return changed
    }

    boolean findAndRemoveBlockedCandidates(Cell[] cells) {
        boolean changed = false
        cells*.candidates.flatten().unique().each { int candidate ->
            List<Cell> cellsWithCandidate = cells.findAll { it.candidates.contains(candidate) }
            if (board.inTheSameSquare(cellsWithCandidate)) {
                Cell firstFit = cellsWithCandidate.first()
                List<Cell> emptyCells = board
                        .getEmptyCellsFromSquare(firstFit.rowNum, firstFit.colNum)
                        .findAll { it.candidates.contains(candidate) && !(it in cells) }
                        .each { Cell cell ->
                    cell.candidates -= candidate
                    changed = true
                }
            }
        }
        return changed
    }
}