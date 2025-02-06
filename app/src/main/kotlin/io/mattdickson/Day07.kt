package io.mattdickson

import java.util.LinkedList

class Day07 : Solver {

	val lines: List<Pair<Long, List<Long>>>

	constructor(input: String) : super(input) {
		lines = input.trim().lines()
			.map { it.split(": ") }
			.map { Pair(it[0].toLong(), it[1].split(" ").map(String::toLong)) }
	}

	override fun part1(): String =
		lines
			.filter(::canMake)
			.map { it.first }
			.sum()
			.toString()

	override fun part2(): String =
		lines
			.filter { this.canMake(it, true) }
			.map { it.first }
			.sum()
			.toString()
	
	fun canMake(item: Pair<Long, List<Long>>, concat: Boolean = false): Boolean {
		val queue = LinkedList<Long>()
		queue.offer(item.second.first())
		item.second.listIterator(1).forEach {
			val queueSize = queue.size
			0.until(queueSize).forEach { _ ->
				val top = queue.pop()
				queue.offer(top + it)
				queue.offer(top * it)
				if (concat) queue.offer(concat(top, it))
			}
		}
		return queue.contains(item.first) 
	}

	 fun concat(first: Long, second: Long): Long =
	  // Wildly inefficent? Yes. Simple? Also yes 
		(first.toString() + second.toString()).toLong()
		
	
}
