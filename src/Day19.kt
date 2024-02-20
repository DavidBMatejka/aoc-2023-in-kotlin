fun main() {

    data class Part(val x: Long, val m:Long, val a: Long, val s: Long) {
        fun sum(): Long {
            return x + m + a + s
        }
    }

    data class Rule(val name: String, val lessOrGreater: String, val value: String, val dest: String)

    fun process(part: Part, ruleBook: MutableMap<String, List<Rule>>): Boolean {
        var current = "in"

        while (true) {
            val rules = ruleBook[current]
            if (current == "A") {
                return true
            }
            if (current == "R") {
                return false
            }
            for (rule in rules!!) {

                if (rule.name == "x" || rule.name == "m" || rule.name == "a" || rule.name == "s") {
                    val ruleValue = when (rule.name) {
                        "x" -> part.x
                        "m" -> part.m
                        "a" -> part.a
                        "s" -> part.s
                        else -> {
                            println("error in when block")
                            -1
                        }
                    }

                    if (rule.lessOrGreater == "<") {
                        if (ruleValue < rule.value.toLong()) {
                            current = rule.dest
                            break
                        }
                    } else {
                        if (ruleValue > rule.value.toLong()) {
                            current = rule.dest
                            break
                        }
                    }
                } else if (rule.name == "other") {
                    current = rule.dest
                    break
                }
            }
        }
    }

    fun part1(input: List<String>): Long {
        val parts = mutableListOf<Part>()
        val workflows = mutableMapOf<String, List<String>>()

        var newLine = false
        for (line in input) {
            if (line.isBlank()) {
                newLine = true
                continue
            }

            if (!newLine) {
                workflows[line.substringBefore("{")] =
                    line.substringAfter("{")
                        .dropLast(1)
                        .split(",")
            }
            else {
                val (x, m, a, s) =
                    line.split(",")
                    .map { it.substringAfter("=") }
                    .map { it.substringBefore("}") }
                    .map { it.toLong() }
                parts.add(Part(x, m, a , s))
            }
        }

        val ruleBook = mutableMapOf<String, List<Rule>>()

        for (workflow in workflows) {
            val rules = mutableListOf<Rule>()
            for (rule in workflow.value) {
                if (!rule.contains("<") && !rule.contains(">")) {
                    rules.add(Rule("other","", "", rule))
                }
                else {
                    rules.add(Rule(
                        rule.substring(0,1),
                        rule[1].toString(),
                        rule.substringBefore(":").drop(2),
                        rule.substringAfter(":")
                    ))
                }
            }
            ruleBook[workflow.key] = rules
        }

        var sumOfParts = 0L
        for (part in parts) {
            val accepted = process(part, ruleBook)
            if (accepted) {
                sumOfParts += part.sum()
            }
        }

        return sumOfParts
    }

    fun LongRange.length(): Long {
        return last - first + 1
    }

    fun countRanges(ranges: Map<Char, LongRange>, workflows: MutableMap<String, List<Rule>>, workflowName: String): Long {
        @Suppress("NAME_SHADOWING") val ranges = ranges.toMutableMap()
        if (workflowName == "R") {
            return 0
        }
        if (workflowName == "A") {
            return ranges.values.map { it.length() }.reduce(Long::times)
        }
        var sum = 0L
        val workflow = workflows[workflowName]!!
        for ((name, op, cutoff, dest) in workflow) {
            if (name in "xmas") {
                val range: LongRange = ranges[name.first()]!!
                val trueRange = if (op == "<") {
                    range.first..<cutoff.toLong()
                } else {
                    cutoff.toLong() + 1..range.last
                }
                val falseRange = if (op == "<") {
                    cutoff.toLong()..range.last
                } else {
                    range.first..cutoff.toLong()
                }
                if (trueRange.length() > 0) {
                    val rangesCopy = ranges.toMutableMap()
                    rangesCopy[name.first()] = trueRange
                    sum += countRanges(rangesCopy, workflows, dest)
                }
                if (falseRange.length() > 0) {
                    ranges[name.first()] = falseRange
                } else {
                    println(name)
                    break
                }
            } else {
                sum += countRanges(ranges, workflows, dest)
            }
        }

        return sum
    }

    fun part2(input: List<String>): Long {
        val workflows = mutableMapOf<String, List<String>>()

        for (line in input) {
            if (line.isBlank()) {
                break
            }

            workflows[line.substringBefore("{")] =
                line.substringAfter("{")
                    .dropLast(1)
                    .split(",")

        }

        val ruleBook = mutableMapOf<String, List<Rule>>()

        for (workflow in workflows) {
            val rules = mutableListOf<Rule>()
            for (rule in workflow.value) {
                if (!rule.contains("<") && !rule.contains(">")) {
                    rules.add(Rule("other","", "", rule))
                }
                else {
                    rules.add(Rule(
                        rule.substring(0,1),
                        rule[1].toString(),
                        rule.substringBefore(":").drop(2),
                        rule.substringAfter(":")
                    ))
                }
            }
            ruleBook[workflow.key] = rules
        }

        val ranges = "xmas".associateWith { 1L..4000L }
        return countRanges(ranges, ruleBook, "in")
    }

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}