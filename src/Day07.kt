fun main() {

    data class HandWithType(val replaced: String, val original: String, val type: String, val bid: Int)

    fun part1(input: List<String>): Long {
        val sortedHands = emptyList<HandWithType>().toMutableList()

        for (line in input) {
            val (originalHand, bid) = line.split(" ")
            val (hand, _) = line
                .replace("T", "E")
                .replace("J", "D")
                .replace("Q", "C")
                .replace("K", "B")
                .replace("A", "A")
                //these need to be 1,2,3 -> z,x,y...
                .replace("1", "Z")
                .replace("2", "Y")
                .replace("3", "X")
                .replace("4", "W")
                .replace("5", "V")
                .replace("6", "U")
                .replace("7", "T")
                .replace("8", "S")
                .replace("9", "R")
                .split(" ")
           // println(hand)
            val chars = hand.toSortedSet()
            chars.println()
            val type = when (chars.size) {
                1 -> "0 Five"
                4 -> "5 One Pair"
                5 -> "6 High Card"
                2 -> {
                    var max = 0
                    for (char in chars) {
                        var counter = 0
                        hand.map { if (it == char) counter++ }
                        if (counter > max) max = counter
                    }
                    if (max == 4) "1 Four"
                    else "2 Full House"
                }
                3 -> { //"4 Two Pair or Three of a kind"
                    var max = 0
                    for (char in chars) {
                        var counter = 0
                        hand.map { if (it == char) counter++ }
                        if (counter > max) max = counter
                    }
                    if (max == 3) "3 Three"
                    else "4 Two Pair"
                }
                else -> "this shouldn't happen"
            }

            val new = (HandWithType(hand, originalHand, type, bid.toInt()))
            sortedHands.add(new)
        }
        sortedHands.sortWith(compareBy<HandWithType> { it.type[0] }.thenBy { it.replaced[0] }.thenBy { it.replaced[1] }.thenBy { it.replaced[2] }.thenBy { it.replaced[3] }.thenBy { it.replaced[4] })

        var sum: Long = 0
        var i = sortedHands.size
        for (hand in sortedHands) {
            println("$hand rank: $i")
            sum += hand.bid * i
            i--
        }

        return sum
    }

    fun part2(input: List<String>): Long {

        return -1
    }

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
