fun main() {

	val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

	fun possibilities(s: String?, rules: List<Int>): Long {
		if (s.isNullOrEmpty()) {
			return if (rules.isEmpty()) 1
			else 0
		}
		if (rules.isEmpty()) {
			return if (s.contains("#")) {
				0
			} else {
				1
			}
		}

		if (Pair(s, rules) in cache.keys) {
			return cache[Pair(s, rules)]!!
		}

		// adapted from HyperNeutrino's youtube-video which is more efficient than mine
		var sum = 0L
		if (s[0] in "?.") {
			sum += possibilities(s.drop(1), rules)
		}
		if (s[0] in "#?") {
			if (rules[0] <= s.length && "." !in s.substring(0..<rules[0]) && (rules[0] == s.length || s[rules[0]] != '#')) {
				sum += possibilities(s.drop(rules[0] + 1), rules.drop(1))
			}
		}

		cache[Pair(s, rules)] = sum
		return sum
	}

	fun part1(input: List<String>): Long {
		var sum = 0L

		for (line in input) {
			val (s, rules) = line.split(" ")
			sum += possibilities(s, rules.split(",").map { it.toInt() })
		}
		return sum
	}

	fun part2(input: List<String>): Long {
		var sum = 0L

		for (line in input) {
			val (s, rules) = line.split(" ")
			val sb1 = StringBuilder(s)
			val sb2 = StringBuilder(rules)
			for (i in 0..3) {
				sb1.append("?$s")
				sb2.append(",$rules")
			}
			val tmp = possibilities(sb1.toString(), sb2.split(",").map { it.toInt() })
			sum += tmp
		}
		return sum
	}

	val input = readInput("Day12")
	part1(input).println()
	part2(input).println()
}