fun main() {
    fun inverse(original: List<String>): List<String> {
        val height = original.size
        val width = original[0].length

        val inversedArray = Array(width) { Array(height) { '.' } }
        for ((i, line) in original.withIndex()) {
            for ((j, s) in line.withIndex()) {
                inversedArray[j][i] = s
            }
        }

        val inversed = mutableListOf<String>()
        for (line in inversedArray) {
            var tmp = ""
            for (s in line) {
                tmp += s
            }
            inversed.add(tmp)
        }

        return inversed
    }

    fun hamming(s1: String, s2: String): Int {
        // strings must be of equal length
        if (s1.length != s2.length) return -1

        var distance = 0
        for (i in s1.indices) {
            if (s1[i] != s2[i]) {
                distance++
            }
        }

        return distance
    }

    fun newSymmetry(pattern: List<String>, diff: Int): Int {
        for (i in 0..<pattern.lastIndex) {
            var sum = 0
            for (j in pattern.indices) {
                if ((i - j) !in pattern.indices || (i + 1 + j) !in pattern.indices) {
                    break
                }
                sum += hamming(pattern[i - j], pattern[i + 1 + j])
            }
            if (sum == diff) {
                return i
            }
        }

        // no symmetry
        return -1
    }

    fun part1(input: List<String>, diff: Int): Int {
        var sum = 0

        val split = input.fold(mutableListOf(mutableListOf<String>())) {acc, string ->
            if (string.isBlank()) {
                acc.add(mutableListOf())
            }
            else  {
                acc.last().add(string)
            }
            acc
        }

        for (pattern in split) {
            val rowSym = newSymmetry(pattern, diff)
            if (rowSym == -1) {
                val colSym = newSymmetry(inverse(pattern), diff)

                sum += (colSym + 1)
            } else {
                sum += 100 * (rowSym + 1)
            }
        }
        return sum
    }

    fun part2(input: List<String>, diff: Int): Int {
        return part1(input, diff)
    }

    val input = readInput("Day13")
    part1(input, 0).println()
    part2(input, 1).println()
}