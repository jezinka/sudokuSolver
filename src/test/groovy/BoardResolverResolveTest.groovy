import spock.lang.Specification

class BoardResolverResolveTest extends Specification {

    def "TryToResolve simple"() {
        given:
        Integer[][] input = [[0, 3, 9, 0, 0, 0, 1, 0, 0],
                             [0, 0, 0, 0, 9, 2, 4, 0, 3],
                             [2, 4, 0, 3, 1, 5, 0, 8, 0],
                             [0, 0, 3, 0, 2, 0, 6, 0, 0],
                             [0, 0, 4, 1, 3, 8, 2, 0, 0],
                             [1, 9, 2, 5, 7, 6, 8, 3, 4],
                             [0, 7, 0, 0, 6, 0, 3, 0, 0],
                             [0, 0, 0, 2, 0, 0, 5, 0, 0],
                             [0, 2, 8, 0, 5, 0, 0, 4, 0]]

        Board board = new Board(input)

        when:
        BoardResolver boardResolver = new BoardResolver(board)
        boardResolver.fillCandidates()
        boardResolver.tryToResolve()

        then:
        board.emptyCells.size() == 0
        board.values == [[5, 3, 9, 4, 8, 7, 1, 6, 2],
                         [8, 1, 7, 6, 9, 2, 4, 5, 3],
                         [2, 4, 6, 3, 1, 5, 7, 8, 9],
                         [7, 8, 3, 9, 2, 4, 6, 1, 5],
                         [6, 5, 4, 1, 3, 8, 2, 9, 7],
                         [1, 9, 2, 5, 7, 6, 8, 3, 4],
                         [4, 7, 5, 8, 6, 9, 3, 2, 1],
                         [9, 6, 1, 2, 4, 3, 5, 7, 8],
                         [3, 2, 8, 7, 5, 1, 9, 4, 6]] as Integer[][]
    }

    def "TryToResolve extreme"() {
        given:
        Board board = new Board([[0, 3, 0, 0, 0, 5, 0, 0, 0],
                                 [0, 7, 0, 0, 9, 0, 0, 6, 1],
                                 [0, 0, 2, 0, 3, 0, 7, 0, 0],
                                 [4, 0, 0, 0, 0, 0, 0, 0, 0],
                                 [0, 8, 7, 0, 2, 0, 9, 5, 0],
                                 [0, 0, 0, 0, 0, 0, 0, 0, 6],
                                 [0, 0, 6, 0, 5, 0, 8, 0, 0],
                                 [9, 1, 0, 0, 8, 0, 0, 2, 0],
                                 [0, 0, 0, 4, 0, 0, 0, 1, 0]] as Integer[][])

        when:
        BoardResolver boardResolver = new BoardResolver(board)
        boardResolver.fillCandidates()
        boardResolver.tryToResolve()

        then:
        board.emptyCells.size() == 0
        board.values == [[6, 3, 1, 7, 4, 5, 2, 9, 8],
                         [5, 7, 4, 8, 9, 2, 3, 6, 1],
                         [8, 9, 2, 1, 3, 6, 7, 4, 5],
                         [4, 6, 9, 5, 7, 3, 1, 8, 2],
                         [1, 8, 7, 6, 2, 4, 9, 5, 3],
                         [2, 5, 3, 9, 1, 8, 4, 7, 6],
                         [7, 4, 6, 2, 5, 1, 8, 3, 9],
                         [9, 1, 5, 3, 8, 7, 6, 2, 4],
                         [3, 2, 8, 4, 6, 9, 5, 1, 7]] as Integer[][]
    }
}