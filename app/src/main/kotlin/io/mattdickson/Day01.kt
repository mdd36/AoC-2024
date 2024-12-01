package io.mattdickson

import kotlin.math.abs

class Day01 : Solver {

	val left: List<Int> 
	val right: List<Int> 

	val leftCounts: Map<Int, Int>
	val rightCounts: Map<Int, Int> 

	constructor(input: String) : super(input) {
		val parsedLines = input.lines()
			.map { it.split("\\s+".toRegex()) }
			.map { it.map(String::toInt) }

		left = parsedLines.map { it[0] }.sorted()
		right = parsedLines.map { it[1] }.sorted()

		leftCounts = left.groupingBy { it }.eachCount()
		rightCounts = right.groupingBy { it }.eachCount()
	}


	override fun part1(): String =
		left.zip(right)
			.map { abs(it.first - it.second) }
			.sum()
			.toString()

	override fun part2(): String = 
		leftCounts
			.map { it.value * it.key * rightCounts.getOrDefault(it.key, 0) }
			.sum()
			.toString()
}
