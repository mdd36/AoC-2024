package io.mattdickson

class Day03 : Solver {
	
	constructor(input: String) : super(input) 

	override fun part1(): String = 
		"mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex().findAll(input)	
			.map { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
			.sum()
			.toString()
	
	override fun part2(): String { 
		val enabledRanges = "do\\(\\)|don't\\(\\)".toRegex().findAll(input)
			.map { Pair(it.value, it.range.start) }
			.fold(mutableListOf(Pair("do", 0))) { acc, r ->
				if (acc.last().first != r.first) { acc.apply{ add(r) } } else { acc }
			}
			.chunked(2) { Pair(it[0].second, it.getOrNull(1)?.let { it.second} ?: input.length) }
		val intervalQueryTree = IntervalTreeNode(enabledRanges, 0, enabledRanges.size)

		return "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex().findAll(input)
			.filter { intervalQueryTree.contains(it.range.start) }
			.map { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
			.sum()
			.toString()
	}
}

class IntervalTreeNode {
	
	val start: Int
	val end: Int

	val left: IntervalTreeNode?
	val right: IntervalTreeNode?

	constructor(ranges: List<Pair<Int, Int>>, lo: Int, hi: Int) {
		val middle = (hi + lo) / 2
		start = ranges.get(middle).first
		end = ranges.get(middle).second
		
		left = if (lo < middle) { IntervalTreeNode(ranges, lo, middle) } else { null }
		right = if (middle + 1 < hi) { IntervalTreeNode(ranges, middle+1, hi) } else { null }
	}

	public fun contains(query: Int): Boolean =
		start <= query && query <= end 
			|| query < start && left?.let { it.contains(query) } ?: false
			|| query > end && right?.let { it.contains(query) } ?: false
}

