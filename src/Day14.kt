fun main() {

    fun weightPerColumn(s: String, value: Int, sum: Long): Long {
        val sub = s.substringBefore("#")
        val rest = s.substringAfter("#")

        if (sub.isBlank() && rest.isBlank()) {
            return sum
        }

        val index = s.indexOf("#") + 1
        val c = sub.count { it == 'O' }

        var tmp = sum
        for (i in 0..<c) {
            tmp += (value - i)
        }


        if (sub == rest) {
            return tmp
        }
        return weightPerColumn(rest, value - index, tmp)
    }

    fun part1(input: List<String>): Long {
        val columns = MutableList(input[0].length) { "" }

        input.forEach {
            for (j in input.indices) {
                columns[j] = columns[j] + it[j]
            }
        }

        var sum = 0L
        for (column in columns) {
            sum += weightPerColumn(column, column.length, 0L)
        }

        return sum
    }

    fun transpose(grid: MutableList<String>): MutableList<String> {
        val transposed = MutableList(grid.size){""}

        grid.forEach {
            for (j in 0..<grid.size) {
                transposed[j] = transposed[j] + it[j]
            }
        }
        return transposed
    }

    fun north(grid: MutableList<String>): MutableList<String> {
        val new = MutableList(grid.size) { "" }
        val transposed = transpose(grid)
        transposed.forEachIndexed { i, item ->
            val sorted = mutableListOf<String>()
            val splitted = item.split("#")
            splitted.forEach {
                sorted.add(it.toCharArray().sorted().reversed().joinToString(""))
            }
            new[i] = sorted.joinToString("#")
        }
        return transpose(new)
    }

    fun south(grid: MutableList<String>): MutableList<String> {
        val new = MutableList(grid.size) { "" }
        val transposed = transpose(grid)
        transposed.forEachIndexed { i, item ->
            val sorted = mutableListOf<String>()
            val splitted = item.split("#")
            splitted.forEach {
                sorted.add(it.toCharArray().sorted().joinToString(""))
            }
            new[i] = sorted.joinToString("#")
        }
        return transpose(new)
    }

    fun west(grid: MutableList<String>): MutableList<String> {
        val new = MutableList(grid.size) { "" }

        grid.forEachIndexed { i, item ->
            val sorted = mutableListOf<String>()
            val splitted = item.split("#")
            splitted.forEach {
                sorted.add(it.toCharArray().sorted().reversed().joinToString(""))
            }
            new[i] = sorted.joinToString("#")
        }
        return new
    }

    fun east(grid: MutableList<String>): MutableList<String> {
        val new = MutableList(grid.size) { "" }

        grid.forEachIndexed { i, item ->
            val sorted = mutableListOf<String>()
            val splitted = item.split("#")
            splitted.forEach {
                sorted.add(it.toCharArray().sorted().joinToString(""))
            }
            new[i] = sorted.joinToString("#")
        }
        return new
    }

    fun cycle(grid: MutableList<String>): MutableList<String> {
        var new = north(grid)
        new = west(new)
        new = south(new)
        return east(new)
    }

    fun weight(grid: MutableList<String>): Long {
        var sum = 0L
        for (j in 0..<grid[0].length) {
            var k = grid.size
            for (i in 0..<grid.size) {
                if (grid[i][j] == 'O') {
                    sum += k
                }
                k--
            }
        }
        return sum
    }

    fun findRepitition(grid: MutableList<String>, len: Int): Long {
        var fin = grid
        val seen = mutableListOf<MutableList<String>>()
        val seenMap = mutableMapOf<MutableList<String>, Int>()
        var k = 0
        for (i in 1..len) {
            fin = cycle(fin)

            k++
            if (fin in seen) {
                // println("cycle $i has been seen already at cycle ${seen.indexOf(fin)}")
                // println(k - seen.indexOf(fin) - 1)
                val modulo = (1_000_000_000 - seen.indexOf(fin)) % (k - seen.indexOf(fin) - 1)
                val correctCycle = seen.indexOf(fin) + modulo

                // correctCycle - 1 because the list counts from 0 upwards
                return weight(seen[correctCycle - 1])
            }
            seen.add(fin)
            if (fin !in seenMap) {
                seenMap[fin] = 1
            } else {
                val tmp = seenMap[fin]!!
                seenMap[fin] = tmp + 1
            }
        }

        return weight(fin)
    }

    fun part2(input: List<String>): Long {
        val grid = MutableList(input.size) { "" }

        input.forEachIndexed { i, it ->
            grid[i] = it
        }

        // len for findRepition needs to be sufficiently big so a repitition can be found
        // it will stop after finding the first repition
        val sum = findRepitition(grid, 170)

        return sum
    }

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}