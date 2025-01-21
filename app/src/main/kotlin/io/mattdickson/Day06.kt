package io.mattdickson

import kotlin.collections.mutableMapOf
import kotlin.collections.getOrPut
import kotlin.collections.mutableSetOf

class Day06 : Solver {
	
	val maze: Array<Array<Boolean>>
	val startingPosition: Pair<Int, Int>
	val n: Int
	val m: Int

	constructor(input: String) : super(input) {
		val inputLines = input.lines()
		n = inputLines.size
		m = inputLines.first().length

		maze = Array(n) { Array(m) { false } }
		var start: Pair<Int, Int>? = null;

		for (i in 0..<n) {
			val line = inputLines[i]
			for (j in 0..<m) {
				val space = line[j];
				if (space == '#') {
					maze[i][j] = true
				}
				if (space == '^') {
					start = Pair(i, j)
				}
			}
		}

		if (start != null) {
			startingPosition = start
		} else {
			throw Error("Failed to find starting position")
		}
	} 

	override fun part1(): String {
		val seen = mutableSetOf(startingPosition)

		var direction = "up"
		var location = startingPosition
		
		while (location.within(n, m)) {
			var next = nextTile(location, direction)	
			if (next.within(n, m) && maze[next.first][next.second]) {
				direction = clockwise(direction)
				next = nextTile(location, direction)
			}
			seen.add(next)
			location = next
		}

		return (seen.size - 1).toString()
	}

	override fun part2(): String {
		val seen = mutableSetOf(startingPosition)
		var ways = 0

		var direction = "up"
		var location = startingPosition
		
		while (location.within(n, m)) {
			var next = nextTile(location, direction)	
			if (next.within(n, m) && maze[next.first][next.second]) {
				direction = clockwise(direction)
				next = nextTile(location, direction)
			}
			seen.add(next)
			location = next
		}
		
		for (point in seen) {
			if (point == startingPosition || !point.within(n,m)) continue 
			maze[point.first][point.second] = true
			location = startingPosition
			direction = "up"
			val visited = mutableMapOf<Pair<Int, Int>, MutableSet<String>>()

			while (location.within(n, m)) {
				val previousDirections = visited.getOrPut(location) { mutableSetOf() }
				if (previousDirections.contains(direction)) {
					ways += 1
					break
				}
				previousDirections.add(direction)
				var next = nextTile(location, direction)
				while (next.within(n, m) && maze[next.first][next.second]) {
					direction = clockwise(direction)
					next = nextTile(location, direction)
				}
				location = next
			}

			maze[point.first][point.second] = false
		}

		return ways.toString() 
	}



	fun nextTile(location: Pair<Int, Int>, direction: String) =
		when(direction) {
			"up" -> location.copy(first = location.first - 1)
			"right" -> location.copy(second = location.second + 1) 
			"down" -> location.copy(first = location.first + 1)
			"left" -> location.copy(second = location.second - 1)
			else -> throw Error("Unknown direction ${direction}")
		}

	fun clockwise(current: String) =
		when (current) {
			"up" -> "right"
			"right" -> "down"
			"down" -> "left"
			"left" -> "up"
			else -> throw Error("Unknown direction ${current}")
		}
}

fun Pair<Int, Int>.within(i: Int, j: Int) =
	(0.until(i)).contains(first) && (0.until(j)).contains(second)	
