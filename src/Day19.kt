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

    fun part2(input: List<String>): Int {

        return -1
    }

    val input = readInput("Day19")
    part1(input).println()
    // part2(input).println()
}