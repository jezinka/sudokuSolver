import spock.lang.Specification

class BoardResolverTest extends Specification {
    Board bigBoard

    def setup() {
        Integer[][] input = [[0, 3, 9, 0, 0, 0, 1, 0, 0],
                             [0, 0, 0, 0, 9, 2, 4, 0, 3],
                             [2, 4, 0, 3, 1, 5, 0, 8, 0],
                             [0, 0, 3, 0, 2, 0, 6, 0, 0],
                             [0, 0, 4, 1, 3, 8, 2, 0, 0],
                             [1, 9, 2, 5, 7, 6, 8, 3, 4],
                             [0, 7, 0, 0, 6, 0, 3, 0, 0],
                             [0, 0, 0, 2, 0, 0, 5, 0, 0],
                             [0, 2, 8, 0, 5, 0, 0, 4, 0]]

        bigBoard = new Board(input)
    }

    def "findPairs test"() {
        when:
        BoardResolver boardResolver = new BoardResolver(bigBoard)
        boardResolver.fillCandidates()
        boardResolver.findAndRemovePairsCandidates(bigBoard.getEmptyCellsFromRecord(3))

        then:
        bigBoard.getEmptyCellsFromRecord(3)*.candidates.findAll { it.contains(4) || it.contains(9) }.size() == 2
    }

    def "blocked candidates"() {
        given:
        Board board = new Board([[8, 0, 1, 0, 0, 6, 0, 9, 4],
                                 [3, 0, 0, 0, 0, 9, 0, 8, 0],
                                 [9, 7, 0, 0, 8, 0, 5, 0, 0],
                                 [5, 4, 7, 0, 6, 2, 0, 3, 0],
                                 [6, 3, 2, 0, 0, 0, 0, 5, 0],
                                 [1, 9, 8, 3, 7, 5, 2, 4, 6],
                                 [0, 8, 3, 6, 2, 0, 9, 1, 5],
                                 [0, 6, 5, 1, 9, 8, 0, 0, 0],
                                 [2, 1, 9, 5, 0, 0, 0, 0, 8]] as Integer[][])

        when:
        BoardResolver boardResolver = new BoardResolver(board)
        boardResolver.fillCandidates()
        boardResolver.findAndRemoveBlockedCandidates(board.getEmptyCellsFromRecord(3))

        then:
        board.getEmptyCellsFromRecord(3).findAll { it.candidates.contains(1) }.size() == 2
        board.getEmptyCellsFromSquare(3, 6).findAll { it.candidates.contains(1) }.size() == 2
    }

}
