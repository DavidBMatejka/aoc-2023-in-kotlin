fun main() {

    data class Lens(val label: String) {
        var value: Int = -1

        override fun toString(): String {
            return "$label $value"
        }
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        val items = input[0].split(",")

        items.forEach {
            var hash = 0
            it.forEach {s ->
                hash += s.code
                hash *= 17
                hash %= 256
            }
            sum += hash
        }

        return sum
    }


    fun part2(input: List<String>): Int {
        val items = input[0].split(",")
        val boxes = mutableMapOf<Int, MutableList<Lens>>()

        for (item in items) {
            if (item.contains("-")) {
                val lens = Lens(item.substringBefore("-"))

                var hash = 0
                lens.label.forEach { s ->
                    hash += s.code
                    hash *= 17
                    hash %= 256
                }

                val box = boxes[hash]
                if (box.isNullOrEmpty()) {
                    continue
                }
                if (box.contains(lens)) {
                    box.remove(lens)
                }
            } else {
                val lens = Lens(item.substringBefore("="))
                lens.value = item.substringAfter("=").toInt()

                var hash = 0
                lens.label.forEach { s ->
                    hash += s.code
                    hash *= 17
                    hash %= 256
                }

                val box = boxes[hash]
                if (box.isNullOrEmpty()) {
                    boxes[hash] = mutableListOf(lens)
                } else if (lens !in box) {
                    box.add(lens)
                } else if (lens in box) {
                    box.forEach {
                        if (it.label == lens.label) {
                            it.value = lens.value
                        }
                    }
                }
            }
        }

        println(boxes)

        var sum  = 0
        boxes.forEach { (index, box) ->
            box.forEachIndexed { slot, lens ->
                sum += (index + 1) * (slot + 1) * lens.value
            }
        }

        return sum
    }

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}