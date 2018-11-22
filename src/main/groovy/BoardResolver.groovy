class BoardResolver {

    Board board

    BoardResolver(Board board) {
        this.board = board
    }

    void tryToResolve() {
        println board.emptyCells.size()
        boolean changed = true

        while (this.board.getEmptyCells() && changed) {
            changed = findOnlyOneCandidate() || findOnlyOnePosition() || findGroups() || findBlockedCandidatesRecord() || findBlockedCandidatesSquare()
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
            changed = findAndSetOnePositionCandidate(board.getEmptyCellsFromRecord(rowNum)) || changed
        }

        (0..<board.board[0].length).each { colNum ->
            changed = findAndSetOnePositionCandidate(board.getEmptyCellsFromColumn(colNum)) || changed
        }

        (0..6).step(3).each { rowNum ->
            (0..6).step(3).each { colNum ->
                changed = findAndSetOnePositionCandidate(board.getEmptyCellsFromSquare(rowNum, colNum)) || changed
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

    boolean findGroups() {
        boolean changed = false
        (2..4).each { int groupSize ->
            (0..<board.board.length).each { rowNum ->
                changed = findAndRemoveGroupCandidates(board.getEmptyCellsFromRecord(rowNum), groupSize) || changed
            }
            (0..<board.board[0].length).each { colNum ->
                changed = findAndRemoveGroupCandidates(board.getEmptyCellsFromColumn(colNum), groupSize) || changed
            }

            (0..6).step(3).each { rowNum ->
                (0..6).step(3).each { colNum ->
                    changed = findAndRemoveGroupCandidates(board.getEmptyCellsFromSquare(rowNum, colNum), groupSize) || changed
                }
            }
        }

        return changed
    }

    boolean findAndRemoveGroupCandidates(Cell[] cells, int groupSize) {
        boolean changed = false
        cells.toList().subsequences()
                .findAll { it.size() == groupSize }
                .findAll { it.candidates.flatten().unique().size() == groupSize }
                .each { nakedGroup ->
            List<Integer> nakedGroupCandidates = nakedGroup.candidates.flatten().unique().collect { it as Integer }
            cells.each { cell ->
                if (!nakedGroup.contains(cell) && cell.candidates.any { nakedGroupCandidates.contains(it) }) {
                    cell.candidates.removeAll(nakedGroupCandidates)
                    changed = true
                }
            }
        }

        return changed
    }

    boolean findBlockedCandidatesRecord() {
        boolean changed = false
        (0..<board.board.length).each { rowNum ->
            changed = findAndRemoveBlockedCandidatesRecord(board.getEmptyCellsFromRecord(rowNum)) || changed
        }
        return changed
    }

    boolean findAndRemoveBlockedCandidatesRecord(Cell[] cells) {
        boolean changed = false
        cells*.candidates.flatten().unique().each { int candidate ->
            List<Cell> cellsWithCandidate = cells.findAll { it.candidates.contains(candidate) }
            if (board.inTheSameSquare(cellsWithCandidate)) {
                Cell firstFit = cellsWithCandidate.first()
                board.getEmptyCellsFromSquare(firstFit.rowNum, firstFit.colNum)
                        .findAll { it.candidates.contains(candidate) && !(it in cells) }
                        .each { Cell cell ->
                    cell.candidates -= candidate
                    changed = true
                }
            }
        }
        return changed
    }

    boolean findBlockedCandidatesSquare() {
        boolean changed = false
        (0..6).step(3).each { rowNum ->
            (0..6).step(3).each { colNum ->
                changed = findAndRemoveBlockedCandidatesSquare(board.getEmptyCellsFromSquare(rowNum, colNum)) || changed
            }
        }
        return changed
    }

    boolean findAndRemoveBlockedCandidatesSquare(Cell[] cells) {
        boolean changed = false
        cells*.candidates.flatten().unique().each { int candidate ->
            List<Cell> cellsWithCandidate = cells.findAll { it.candidates.contains(candidate) }
            if (board.inTheSameColumn(cellsWithCandidate)) {
                Cell firstFit = cellsWithCandidate.first()
                board.getEmptyCellsFromColumn(firstFit.colNum)
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