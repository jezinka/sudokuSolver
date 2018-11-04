Board board = new Board()
println board
BoardResolver boardResolver = new BoardResolver(board)
boardResolver.fillCandidates()
boardResolver.tryToResolve()
