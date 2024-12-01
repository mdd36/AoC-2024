package io.mattdickson

import java.io.File

fun main(args: Array<String>) {
    val day = args[0]

		val dataFilePath = "/Users/matthew/workplace/AdventOfCode/2024/data/$day"
		val fileContents = File(dataFilePath).readText().trim()


		val solverImpl = Class.forName("io.mattdickson.Day$day").kotlin

		val solver: Solver = solverImpl.constructors.first().call(fileContents) as Solver

		println("Part 1: ${solver.part1()}")
		println("Part 2: ${solver.part2()}")
}

abstract class Solver(val input: String) {
	open fun part1(): String = "Part 1 unimplemented"
	open fun part2(): String = "Part 2 unimplemented"
}

