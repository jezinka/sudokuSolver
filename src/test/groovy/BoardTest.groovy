import spock.lang.Specification
import spock.lang.Unroll

class BoardTest extends Specification {

    Board board
    Board bigBoard

    def setup() {
        Integer[][] sampleDataSmall = [[0, 0, 0, 3],
                                       [2, 3, 0, 0],
                                       [4, 1, 3, 2],
                                       [3, 0, 4, 1]]

        Integer[][] sampleDataBig = [[0, 0, 0, 0, 0, 2, 0, 0, 3],
                                     [6, 0, 0, 0, 3, 0, 0, 0, 9],
                                     [9, 0, 0, 0, 0, 0, 0, 0, 0],
                                     [0, 4, 5, 0, 0, 0, 6, 0, 0],
                                     [8, 0, 0, 6, 0, 0, 3, 0, 7],
                                     [0, 0, 0, 0, 0, 0, 8, 5, 2],
                                     [4, 9, 0, 0, 6, 3, 0, 0, 5],
                                     [0, 3, 0, 5, 0, 8, 0, 0, 0],
                                     [0, 0, 0, 4, 9, 0, 0, 0, 0]]

        board = new Board(sampleDataSmall)
        bigBoard = new Board(sampleDataBig)
    }

    def "test constructor"() {
        when:
        print board

        then:
        noExceptionThrown()
    }

    def "getColumn"() {
        when:
        Cell[] column = board.getColumn(1)

        then:
        column.join(' ') == '- 3 1 -'
    }

    def "getRecord"() {
        when:
        Cell[] record = board.getRecord(2)

        then:
        record.join(' ') == '4 1 3 2'

    }

    @Unroll
    def "getSquare #x #y"() {
        when:
        Cell[] square = bigBoard.getSquare(x, y)

        then:
        square.toString() == expectedResult

        where:
        x | y || expectedResult
        0 | 0 || '[-, -, -, 6, -, -, 9, -, -]'
        0 | 1 || '[-, -, -, 6, -, -, 9, -, -]'
        2 | 2 || '[-, -, -, 6, -, -, 9, -, -]'
        4 | 4 || '[-, -, -, 6, -, -, -, -, -]'
        8 | 8 || '[-, -, 5, -, -, -, -, -, -]'
    }

    def "get empty cell"() {
        expect:
        assert board.emptyCells.size() == 6
    }

    def "get values from column"() {
        when:
        List column = board.getValuesForColumn(1)

        then:
        column == [3, 1]
    }

    def "get values for record"() {
        when:
        List record = board.getValuesForRecord(2)

        then:
        record == [4, 1, 3, 2]

    }

    @Unroll
    def "get values for square #x #y"() {
        when:
        List square = bigBoard.getValuesForSquare(x, y)

        then:
        square == expectedResult

        where:
        x | y || expectedResult
        0 | 0 || [6, 9]
        0 | 1 || [6, 9]
        2 | 2 || [6, 9]
        4 | 4 || [6]
        8 | 8 || [5]
    }
}
