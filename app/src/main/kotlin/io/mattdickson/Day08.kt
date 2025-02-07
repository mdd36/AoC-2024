package io.mattdickson

class Day08 : Solver {

	val n: Int
	val m: Int
	val letterLocations: Map<String, List<Pair<Int, Int>>>

	constructor(input: String) : super(input) {
		val _letterLocations = mutableMapOf<String, MutableList<Pair<Int, Int>>>()
		input.lines().forEachIndexed { i, line -> 
			line.forEachIndexed { j, ch -> 
				if (ch != '.') {
					_letterLocations.getOrPut(ch.toString()) { mutableListOf() }.add(Pair(i, j))
				}
			}
		}

		letterLocations = _letterLocations
		n = input.lines().size
		m = input.lines()[0].length
	}

	override fun part1(): String = letterLocations.values.toList()
		.flatMap(::findAntinodes)
		.filter { it.within(n, m) }
		.distinct()
		.count()
		.toString()

	override fun part2(): String = letterLocations.values.toList()
		.flatMap(::harmonics)
		.distinct()
		.count()
		.toString()

	fun findAntinodes(coords: List<Pair<Int, Int>>) =
		coords.combinations().flatMap {
			val a = it.first
			val b = it.second
			val diff = b.sub(a)
			listOf(a.sub(diff), b.add(diff))
		}

	fun harmonics(coords: List<Pair<Int, Int>>) =
		coords.combinations().flatMap {
			var a = it.first
			var b = it.second
			val diff = b.sub(a)
			
			val antinodes = mutableListOf<Pair<Int,Int>>()

			var node = a
			while (node.within(n,m)) {
				antinodes.add(node)
				node = node.sub(diff)
			}

			node = b
			while (node.within(n,m)) {
				antinodes.add(node)
				node = node.add(diff)
			}
			antinodes
	}
}

fun Pair<Int, Int>.add(it: Pair<Int, Int>) =
	Pair(this.first + it.first, this.second + it.second)

fun Pair<Int, Int>.sub(it: Pair<Int, Int>) =
	Pair(this.first - it.first, this.second - it.second)

fun <T> List<T>.combinations() =
	this.flatMapIndexed { i, first ->
		this.subList(i + 1, size).map { second ->
			Pair(first, second)
		}
	}
