import spock.lang.Specification
import spock.lang.Unroll

class BoardTest extends Specification {

    Board bigBoard

    def setup() {
        bigBoard = new Board()
    }

    def "test constructor"() {
        when:
        print bigBoard

        then:
        noExceptionThrown()
    }

    def "getColumn"() {
        when:
        Cell[] column = bigBoard.getColumn(1)

        then:
        column.join(' ') == '- - - 4 - - 9 3 -'
    }

    def "getRecord"() {
        when:
        Cell[] record = bigBoard.getRecord(2)

        then:
        record.join(' ') == '9 - - - - - - - -'

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
        assert bigBoard.emptyCells.size() == 55
    }

    def "get values from column"() {
        when:
        List column = bigBoard.getValuesForColumn(1)

        then:
        column == [4, 9, 3]
    }

    def "get values for record"() {
        when:
        List record = bigBoard.getValuesForRecord(2)

        then:
        record == [9]

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

    def "fill candidates test"() {
        expect:
        bigBoard.board.flatten().every { Cell it -> it.candidates.size() > 0 || it.value }
        bigBoard.board[1][0].candidates == []
        bigBoard.board[0][0].candidates == [1, 5, 7]
        bigBoard.board[8][8].candidates == [1, 6, 8]

    }
}
