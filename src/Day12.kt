fun main() {


	fun possibilities(s: String?, rules: List<String>): Int {

		if (s.isNullOrBlank() && rules.isEmpty()) {
			return 1
		}
		if (s.isNullOrBlank()) {
			return 0
		}
		if (rules.isEmpty() && s.contains("#")) {
			return 0
		}


		if (s.contains('?')) {
			return possibilities(s.replaceFirst('?', '.'), rules) + possibilities(s.replaceFirst('?', '#'), rules)
		}

		if (s.first() == '.') {
			return possibilities(s.drop(1), rules)
		}
		if (s.first() == '#') {
			var i = 0
			while (i in s.indices && s[i] == '#') {
				i++
			}
			if (i == rules[0].toInt()) {
				return possibilities(s.drop(i), rules.drop(1))
			}
		}
		return 0
	}

	fun part1(input: List<String>): Int {
		var sum = 0
        for (line in input) {
            val (s, rules) = line.split(" ")
			sum += possibilities(s, rules.split(","))
        }
		return sum
	}


	fun part2(input: List<String>): Int {
		var sum = 0

		for (line in input) {
			val (s, rules) = line.split(" ")
			var firstAext = ""
			var firstBext = ""
			for (i in 0..4) {
				firstAext += "$s?"
				firstBext += "$rules,"
			}
			firstAext = firstAext.dropLast(1)
			firstBext = firstBext.dropLast(1)
			println("$firstAext $firstBext")
		}

		for (line in input) {
			val (s, rules) = line.split(" ")
			val tmp = possibilities(s, rules.split(","))
			println(tmp)
			sum += tmp
		}

		return sum
	}

	val input = readInput("Day12")
	part1(input).println()
//    part2(input).println()
}