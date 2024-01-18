fun main() {
	data class HandWithType(val hand: String, var type: String, val bid: Int)

	fun part1(input: List<String>, joker: Boolean): Long {
		val cardValue = mapOf(
			'1' to 14,
			'2' to 13,
			'3' to 12,
			'4' to 11,
			'5' to 10,
			'6' to 9,
			'7' to 8,
			'8' to 7,
			'9' to 6,
			'T' to 5,
			'Q' to 3,
			'K' to 2,
			'A' to 1,
			if (joker) {
				'J' to 15
			} else {
				'J' to 4
			}
		)
		val sortedHands = mutableListOf<HandWithType>()
		var jokerCount = 0
		for (line in input) {
			val (hand, bid) = line.split(" ")
			val chars = hand.toSortedSet()
			if (joker) {
				jokerCount = hand.count { it == 'J' }
				chars.remove('J')
			}
			val type = when (chars.size) {
				1 -> "0 Five of a kind"
				4 -> "5 One Pair"
				5 -> "6 High Card"
				2 -> {
					var max = 0
					for (char in chars) {
						var counter = 0
						hand.map { if (it == char) counter++ }
						if (counter > max) max = counter
					}
					if ((max + jokerCount) == 4) "1 Four of a kind"
					else "2 Full House"
				}

				3 -> { //"4 Two Pair or Three of a kind"
					var max = 0
					for (char in chars) {
						var counter = 0
						hand.map { if (it == char) counter++ }
						if (counter > max) max = counter
					}
					if (max == 3) "3 Three of a kind"
					else {
						if (jokerCount > 0) "3 Three of a kind"
						else "4 Two Pair"
					}
				}

				else -> "0 Only Jokers"
			}

			val handWithType = (HandWithType(hand, type, bid.toInt()))
			sortedHands.add(handWithType)
		}
		sortedHands.sortWith(compareBy<HandWithType> { it.type[0] }
			.thenBy { cardValue[it.hand[0]] }
			.thenBy { cardValue[it.hand[1]] }
			.thenBy { cardValue[it.hand[2]] }
			.thenBy { cardValue[it.hand[3]] }
			.thenBy { cardValue[it.hand[4]] })

		var sum: Long = 0
		var i = sortedHands.size
		for (hand in sortedHands) {
			sum += hand.bid * i
			i--
		}

		return sum
	}

	fun part2(input: List<String>): Long {
		return part1(input, true)
	}

	val input = readInput("Day07")
	part1(input, false).println()
	part2(input).println()
}
