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
println board
BoardResolver boardResolver = new BoardResolver(board)
boardResolver.fillCandidates()
boardResolver.tryToResolve()
board.validate()
