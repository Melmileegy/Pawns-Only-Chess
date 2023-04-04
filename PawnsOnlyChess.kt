package chess

fun main() {
    println(" Pawns-Only Chess")
    println("First Player's name:")
    val firstPlayerName = readln()
    println("Second Player's name:")
    val secondPlayerName = readln()
    val chessboard = mutableListOf(
        mutableListOf(" ", " ", " ", " ", " ", " ", " ", " "),
        mutableListOf("W", "W", "W", "W", "W", "W", "W", "W"),
        mutableListOf(" ", " ", " ", " ", " ", " ", " ", " "),
        mutableListOf(" ", " ", " ", " ", " ", " ", " ", " "),
        mutableListOf(" ", " ", " ", " ", " ", " ", " ", " "),
        mutableListOf(" ", " ", " ", " ", " ", " ", " ", " "),
        mutableListOf("B", "B", "B", "B", "B", "B", "B", "B"),
        mutableListOf(" ", " ", " ", " ", " ", " ", " ", " ")
    )
    printChessboard(chessboard)
    makingMoves(firstPlayerName, secondPlayerName, chessboard)
}

fun printChessboard(chessboard: MutableList<MutableList<String>>) {
    val ranks = 8 downTo 1
    val files = 1..8
    for (rank in ranks) {
        println("  +---+---+---+---+---+---+---+---+")
        print("$rank |")
        for (f in files) {
            print(" ${chessboard[rank - 1][f - 1]} |")
        }
        println()
    }
    println("  +---+---+---+---+---+---+---+---+")
    println("    a   b   c   d   e   f   g   h")
    println()
}

fun makingMoves(firstPlayer: String, secondPlayer: String, chessboard: MutableList<MutableList<String>>) {
    val regex = Regex("[a-h][1-8][a-h][1-8]")
    println("$firstPlayer's turn:")
    var move = readln()
    var playerTurn = 1
    val whitePawnFirstMove = MutableList(8) { false }
    val blackPawnFirstMove = MutableList(8) { false }

    while (move != "exit") {
        val startingFile = move[0] - 'a'
        val startingRank = move[1].digitToInt()
        val endingFile = move[2] - 'a'
        val endingRank = move[3].digitToInt()

        if (regex.matches(move)
            && chessboard[startingRank - 1][startingFile] == "W"
            && chessboard[endingRank - 1][endingFile] == "B"
            && playerTurn == 1
            && endingRank - startingRank == 1
            && (endingFile - startingFile == 1 || startingFile - endingFile == 1)
        ) {
            for (file in whitePawnFirstMove.indices) {
                whitePawnFirstMove[file] = false
            }
            if (startingRank == 2 && endingRank == 4) {
                whitePawnFirstMove[startingFile] = true     // valid EN PASSANT
            }
            updateChessboard(chessboard, startingFile, startingRank, endingFile, endingRank, playerTurn)
            printChessboard(chessboard)
            if (checkWhiteWinConditions(chessboard)) break
            if (checkBlackStalemateConditions(chessboard)) break
            playerTurn = 2
            println("$secondPlayer's turn:")
            move = readln()
        } else if (regex.matches(move)
            && chessboard[startingRank - 1][startingFile] == "B"
            && chessboard[endingRank - 1][endingFile] == "W"
            && playerTurn == 2
            && endingRank - startingRank == -1
            && (endingFile - startingFile == 1 || startingFile - endingFile == 1)
        ) {
            for (file in blackPawnFirstMove.indices) {
                blackPawnFirstMove[file] = false
            }
            if (startingRank == 7 && endingRank == 5) {
                blackPawnFirstMove[startingFile] = true     // valid EN PASSANT
            }
            updateChessboard(chessboard, startingFile, startingRank, endingFile, endingRank, playerTurn)
            printChessboard(chessboard)
            if (checkBlackWinConditions(chessboard)) break
            if (checkWhiteStalemateConditions(chessboard)) break
            playerTurn = 1
            println("$firstPlayer's turn:")
            move = readln()
        } else if (regex.matches(move)
            && chessboard[startingRank - 1][startingFile] == "W"
            && playerTurn == 1
            && endingRank - startingRank == 1
            && (endingFile - startingFile == 1 || startingFile - endingFile == 1)
            && blackPawnFirstMove[endingFile]
        ) {
            for (file in whitePawnFirstMove.indices) {
                whitePawnFirstMove[file] = false
            }
            if (startingRank == 2 && endingRank == 4) {
                whitePawnFirstMove[startingFile] = true     // valid EN PASSANT
            }
            captureEnPassant(chessboard, startingFile, startingRank, endingFile, endingRank, playerTurn)
            printChessboard(chessboard)
            if (checkWhiteWinConditions(chessboard)) break
            if (checkBlackStalemateConditions(chessboard)) break
            playerTurn = 2
            println("$secondPlayer's turn:")
            move = readln()
        } else if (regex.matches(move)
            && chessboard[startingRank - 1][startingFile] == "B"
            && playerTurn == 2
            && endingRank - startingRank == -1
            && (endingFile - startingFile == 1 || startingFile - endingFile == 1)
            && whitePawnFirstMove[endingFile]
        ) {
            for (file in blackPawnFirstMove.indices) {
                blackPawnFirstMove[file] = false
            }
            if (startingRank == 7 && endingRank == 5) {
                blackPawnFirstMove[startingFile] = true     // valid EN PASSANT
            }
            captureEnPassant(chessboard, startingFile, startingRank, endingFile, endingRank, playerTurn)
            printChessboard(chessboard)
            if (checkBlackWinConditions(chessboard)) break
            if (checkWhiteStalemateConditions(chessboard)) break
            playerTurn = 1
            println("$firstPlayer's turn:")
            move = readln()
        } else if (chessboard[startingRank - 1][startingFile] != "W" && playerTurn == 1) {
            println("No white pawn at ${move[0]}${move[1]}")
            println("$firstPlayer's turn:")
            move = readln()
        } else if (chessboard[startingRank - 1][startingFile] != "B" && playerTurn == 2) {
            println("No black pawn at ${move[0]}${move[1]}")
            println("$secondPlayer's turn:")
            move = readln()
        } else if ((!regex.matches(move) && playerTurn == 1)
            || (move[3].digitToInt() - move[1].digitToInt() != 1 && move[1] != '2' && playerTurn == 1)
            || (move[3].digitToInt() - move[1].digitToInt() !in 1..2 && move[1] == '2' && playerTurn == 1)
            || (chessboard[endingRank - 1][endingFile] == "B" && playerTurn == 1)
            || (startingFile != endingFile && playerTurn == 1)
        ) {
            println("Invalid Input")
            println("$firstPlayer's turn:")
            move = readln()
        } else if (!regex.matches(move) && playerTurn == 2
            || (move[1].digitToInt() - move[3].digitToInt() != 1 && move[1] != '7' && playerTurn == 2)
            || (move[1].digitToInt() - move[3].digitToInt() !in 1..2 && move[1] == '7' && playerTurn == 2)
            || (chessboard[endingRank - 1][endingFile] == "W" && playerTurn == 2)
            || (startingFile != endingFile && playerTurn == 2)
        ) {
            println("Invalid Input")
            println("$secondPlayer's turn:")
            move = readln()
        } else if (regex.matches(move) && playerTurn == 1) {
            for (file in whitePawnFirstMove.indices) {
                whitePawnFirstMove[file] = false
            }
            if (startingRank == 2 && endingRank == 4) {
                whitePawnFirstMove[startingFile] = true     // valid EN PASSANT
            }
            updateChessboard(chessboard, startingFile, startingRank, endingFile, endingRank, playerTurn)
            printChessboard(chessboard)
            if (checkWhiteWinConditions(chessboard)) break
            if (checkBlackStalemateConditions(chessboard)) break
            playerTurn = 2
            println("$secondPlayer's turn:")
            move = readln()
        } else if (regex.matches(move) && playerTurn == 2) {
            for (file in blackPawnFirstMove.indices) {
                blackPawnFirstMove[file] = false
            }
            if (startingRank == 7 && endingRank == 5) {
                blackPawnFirstMove[startingFile] = true     // valid EN PASSANT
            }
            updateChessboard(chessboard, startingFile, startingRank, endingFile, endingRank, playerTurn)
            printChessboard(chessboard)
            if (checkBlackWinConditions(chessboard)) break
            if (checkWhiteStalemateConditions(chessboard)) break
            playerTurn = 1
            println("$firstPlayer's turn:")
            move = readln()
        }
    }
    print("Bye!")
}

fun updateChessboard(
    chessboard: MutableList<MutableList<String>>,
    startingFile: Int, startingRank: Int,
    endingFile: Int, endingRank: Int,
    playerTurn: Int
) {
    chessboard[startingRank - 1][startingFile] = " "
    if (playerTurn == 1) {
        chessboard[endingRank - 1][endingFile] = "W"
    } else if (playerTurn == 2) {
        chessboard[endingRank - 1][endingFile] = "B"
    }
}

fun captureEnPassant(
    chessboard: MutableList<MutableList<String>>,
    startingFile: Int, startingRank: Int,
    endingFile: Int, endingRank: Int,
    playerTurn: Int
) {
    chessboard[startingRank - 1][startingFile] = " "
    if (playerTurn == 1) {
        chessboard[endingRank - 1][endingFile] = "W"
        chessboard[endingRank - 2][endingFile] = " "
    } else if (playerTurn == 2) {
        chessboard[endingRank - 1][endingFile] = "B"
        chessboard[endingRank][endingFile] = " "
    }
}

fun checkWhiteWinConditions(chessboard: MutableList<MutableList<String>>): Boolean {
    var allBlackPawnsCaptured = true

    noPawnsLoop@ for (rank in chessboard) {
        for (pawn in rank) {
            if (pawn == "B") {
                allBlackPawnsCaptured = false
                break@noPawnsLoop
            }
        }
    }
    if (allBlackPawnsCaptured) {
        println("White Wins!")
        return true
    }

    var whitePawnInOppositeRank = false

    for (pawn in chessboard.last()) {
        if (pawn == "W") {
            whitePawnInOppositeRank = true
            break
        }
    }

    if (whitePawnInOppositeRank) {
        println("White Wins!")
        return true
    }
    return false
}

fun checkBlackWinConditions(chessboard: MutableList<MutableList<String>>): Boolean {
    var allWhitePawnsCaptured = true

    noPawnsLoop@ for (rank in chessboard) {
        for (pawn in rank) {
            if (pawn == "W") {
                allWhitePawnsCaptured = false
                break@noPawnsLoop
            }
        }
    }
    if (allWhitePawnsCaptured) {
        println("Black Wins!")
        return true
    }

    var blackPawnInOppositeRank = false

    for (pawn in chessboard.first()) {
        if (pawn == "B") {
            blackPawnInOppositeRank = true
            break
        }
    }

    if (blackPawnInOppositeRank) {
        println("Black Wins!")
        return true
    }
    return false
}

fun checkWhiteStalemateConditions(chessboard: MutableList<MutableList<String>>): Boolean {
    var whitePawnsCannotMove = true

    whiteStalemateLoop@ for (rank in chessboard.indices) {
        if (rank == 0 || rank == 7) continue        // to prevent index exception
        for (file in chessboard[rank].indices) {
            if (file != 0 && file != 7) {
                if (chessboard[rank][file] == "W"
                    && (chessboard[rank + 1][file] != "B"
                            || chessboard[rank + 1][file + 1] == "B"
                            || chessboard[rank + 1][file - 1] == "B")
                ) {       // index exception
                    whitePawnsCannotMove = false
                    break@whiteStalemateLoop
                }
            } else if (file == 0) {
                if (chessboard[rank][file] == "W"
                    && (chessboard[rank + 1][file] != "B"
                            || chessboard[rank + 1][1] == "B")
                ) {
                    whitePawnsCannotMove = false
                    break@whiteStalemateLoop
                }
            } else {
                if (chessboard[rank][file] == "W"
                    && (chessboard[rank + 1][file] != "B"
                            || chessboard[rank + 1][6] == "B")
                ) {
                    whitePawnsCannotMove = false
                    break@whiteStalemateLoop
                }
            }

        }
    }

    if (whitePawnsCannotMove) {
        println("Stalemate!")
        return true
    }
    return false
}

fun checkBlackStalemateConditions(chessboard: MutableList<MutableList<String>>): Boolean {
    var blackPawnsCannotMove = true

    blackStalemateLoop@ for (rank in chessboard.indices) {
        if (rank == 0 || rank == 7) continue        // to prevent index exception
        for (file in chessboard[rank].indices) {
            if (file != 0 && file != 7) {
                if (chessboard[rank][file] == "B"
                    && (chessboard[rank - 1][file] != "W"
                            || chessboard[rank - 1][file + 1] == "W"        // index exception
                            || chessboard[rank - 1][file - 1] == "W")        // index exception
                ) {
                    blackPawnsCannotMove = false
                    break@blackStalemateLoop
                }
            } else if (file == 0) {
                if (chessboard[rank][file] == "B"
                    && (chessboard[rank - 1][file] != "W"
                            || chessboard[rank - 1][1] == "W")        // index exception
                ) {
                    blackPawnsCannotMove = false
                    break@blackStalemateLoop
                }
            } else {
                if (chessboard[rank][file] == "B"
                    && (chessboard[rank - 1][file] != "W"
                            || chessboard[rank - 1][6] == "W")        // index exception
                ) {
                    blackPawnsCannotMove = false
                    break@blackStalemateLoop
                }
            }

        }
    }

    if (blackPawnsCannotMove) {
        println("Stalemate!")
        return true
    }
    return false
}
