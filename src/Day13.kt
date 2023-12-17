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

    // finds a pair of symbols, puts the line into chunks with a size according to the found index
    // then checks if potential symmetry holds within the whole line by comparing the first to elements in the chunk
    // and then checks for each line in the string
    fun findSymmetry(strings: MutableList<String>): Int {
        val firstLine = strings[0]
        for (i in firstLine.indices) {
            if (i + 1 == firstLine.length) { break }
            if (firstLine[i] == firstLine[i + 1]) {
                println("i = $i : ${firstLine[i]} ${firstLine[i + 1]}")
                val chunk = firstLine.chunked(i+1).toMutableList()
                if (chunk[0].length < chunk[1].length) {
                    chunk[1] = chunk[1].dropLast(chunk[1].length - chunk[0].length)
                }
                else if (chunk[0].length > chunk[1].length) {
                    chunk[0] = chunk[0].drop(chunk[0].length - chunk[1].length)
                }
                chunk[0] = chunk[0].reversed()
                println("new chunk $chunk")

                if (chunk[0] == chunk[1]) {
                    println("potential symmetry at $i")
                    var candidate = true
                    for (j in 1..<strings.size) {
                        val chunkForEachLine = strings[j].chunked(i+1).toMutableList()
                        //chunkForEachLine.println()
                        if (chunkForEachLine[0].length < chunkForEachLine[1].length) {
                            chunkForEachLine[1] = chunkForEachLine[1].dropLast(chunkForEachLine[1].length - chunkForEachLine[0].length)
                        }
                        else if (chunkForEachLine[0].length > chunkForEachLine[1].length) {
                            chunkForEachLine[0] = chunkForEachLine[0].drop(chunkForEachLine[0].length - chunkForEachLine[1].length)
                        }
                        chunkForEachLine[0] = chunkForEachLine[0].reversed()
                        if (chunkForEachLine[0] != chunkForEachLine[1]) {
                            candidate = false
                        }
                    }
                    if (candidate) return i
                }
            }
        }

        return -1
    }

    fun part1(input: List<String>): Int {
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

        for (i in split.indices) {
            split[i].forEach(::println)
            var mirror = findSymmetry(split[i])

            if (mirror == -1) {
                val inverse = inverse(split[i]).toMutableList()

                inverse.forEach(::println)
                mirror = findSymmetry(inverse)

                sum += 100 * (mirror + 1) // + 1 because I count from index 0 instead of index 1 like in the problem description
            } else {
                sum += (mirror + 1) // // + 1 because I count from index 0 instead of index 1 like in the problem description
            }
        }
        return sum
    }


    fun part2(input: List<String>): Int {
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

        for (i in split.indices) {
            val brute = split[i].toMutableList()
            for ((index, line) in split[i].withIndex()) {
                for (j in line.indices) {
                    val b = if (line[j] == '#') {
                        line.replaceRange(j,j + 1,".")
                    } else line.replaceRange(j,j + 1,"#")
                    brute[index] = b

                    var mirror = findSymmetry(brute)
                    println("found symmetry: $mirror")
                    if (mirror == -1) {
                        val inverse = inverse(brute).toMutableList()

                        inverse.forEach(::println)
                        mirror = findSymmetry(inverse)

                        sum += 100 * (mirror + 1) // + 1 because I count from index 0 instead of index 1 like in the problem description
                    } else {
                        sum += (mirror + 1) // // + 1 because I count from index 0 instead of index 1 like in the problem description
                    }
                }
            }
        }
        return sum
    }

    val input = readInput("Day13_test")
    part1(input).println()
    //part2(input).println()
}