package io.mattdickson

import kotlin.math.abs
import kotlin.math.max

class Day02 : Solver {

	val reports: List<List<Int>>

	constructor(input: String) : super(input) {
		reports = input.lines()
			.map { it.split("\\s+".toRegex()).map(String::toInt) }
	}

	override fun part1(): String =
		reports
			.map { it.zipWithNext { x, y -> x - y } }
			.filter { it.all { diff -> diff > -4 && diff < 0 } || it.all { diff -> diff < 4 && diff > 0 } }
			.count()
			.toString()
	
	override fun part2(): String =
		reports
			.filter { longestMonotonicSubset(it) >= it.size - 1 }
			.count()
			.toString()

	fun longestMonotonicSubset(seq: List<Int>): Int {
		val dp = List(seq.size) { mutableListOf(1,1) }	
		for (i in 1.rangeUntil(seq.size)) {
			val doublePrev = seq.getOrNull(i-2)
			val prev = seq[i-1]
			val curr = seq[i]

			if (prev < curr && curr - prev < 4) {
				dp[i][0] = dp[i-1][0] + 1 
			}

			if (prev > curr && prev - curr < 4) {
				dp[i][1] = dp[i-1][1] + 1 
			}

			if (doublePrev != null && doublePrev < curr && curr - doublePrev < 4) {
				dp[i][0] = max(dp[i][0], dp[i-2][0] + 1)
			}

			if (doublePrev != null && curr < doublePrev && doublePrev - curr < 4) {
				dp[i][1] = max(dp[i][1], dp[i-2][1] + 1)
			}
		}
		return dp.map(List<Int>::max).max() 
	}

}

