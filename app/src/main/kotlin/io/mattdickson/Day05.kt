package io.mattdickson

class Day05 : Solver {

	val prerequisites: Map<Int, List<Int>>
	val updates: List<List<Int>>

	constructor(input: String) : super(input) {
		val blankLineRegex = "^\\n".toRegex(RegexOption.MULTILINE)
		val rulesStrings = input.split(blankLineRegex)[0].trim()
		val updatesString = input.split(blankLineRegex)[1].trim()

		prerequisites = rulesStrings .lines()
			.map { it.split("|") }
			.groupBy({ it[1].toInt() }, { it[0].toInt() })

		updates = updatesString.lines()
			.map { it.split(",").map { it.toInt() } }
	}


	override fun part1(): String =
		updates
			.filter(::isWellOrdered) 
			.map { it[it.size / 2] }
			.sum()
			.toString()

	override fun part2(): String =
		updates
			.filter { !isWellOrdered(it) }
			.map(::reorder)
			.map { it[it.size / 2] }
			.sum()
			.toString()
	
	private fun isWellOrdered(update: List<Int>): Boolean {
		val remaining = update.toMutableSet()
		return update.none { 
			remaining.remove(it)
			prerequisites[it]?.any(remaining::contains) ?: false
		}
	}

	private fun reorder(update: List<Int>): List<Int> {
		val filteredPrereqs = update 
			.map { it.to(prerequisites.get(it)?.filter { it in update }?.toMutableSet() ?: mutableSetOf<Int>()) }
			.toMap()
			.toMutableMap()

		return 0.rangeUntil(update.size)
			.map { 
				val toAdd = filteredPrereqs
					.firstNotNullOf { if (it.value.isEmpty()) it.key else null }
				filteredPrereqs.remove(toAdd)
				filteredPrereqs.forEach { it.value.remove(toAdd) }
				toAdd	
			}
	}
}
