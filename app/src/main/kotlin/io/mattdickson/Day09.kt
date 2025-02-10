package io.mattdickson

import kotlin.math.max
import kotlin.math.min
import java.util.SortedSet
import java.util.TreeSet

class Day09 : Solver {

	val freeSpace: List<Segment>
	val files: List<Segment>

	constructor(input: String) : super(input) {
		var block = 0L
		var id = 0L
		val _freeSpace = mutableListOf<Segment>()
		val _files = mutableListOf<Segment>()

		input.forEachIndexed { i, ch ->
			val size = (ch - '0').toLong()
			if (i % 2 == 0) {
				_files.add(Segment(block, size, id++))
			} else {
				_freeSpace.add(Segment(block, size, -1))
			}
			block += size
		}
		
		freeSpace = _freeSpace
		files = _files
	}

	override fun part1(): String {
		val spaceQueue = ArrayDeque(freeSpace)
		val fileStack = ArrayDeque(files)
		var checksum = 0L

		while (
			fileStack.isNotEmpty() && spaceQueue.isNotEmpty() &&
			fileStack.last().start > spaceQueue.first().start
		) {
			val slot = spaceQueue.removeFirst()
			val file = fileStack.removeLast()
			checksum += sumConsumedBlocks(slot.start, min(file.size, slot.size)) * file.id

			if (slot.size > file.size) {
				spaceQueue.addFirst(Segment(slot.start + file.size, slot.size - file.size, -1))
			} else if (file.size > slot.size) {
				fileStack.add(file.copy(size=(file.size - slot.size)))
			}
		}

		while (fileStack.isNotEmpty()) {
			val file = fileStack.removeFirst()
			checksum += file.id * sumConsumedBlocks(file.start, file.size) 
		}

		return checksum.toString()
	}

	override fun part2(): String {
		val spaceManager = FragmentManager().also {
			freeSpace
				.filter { it.size > 0 }
				.forEach { s -> it.add(s) }
		}
		val fileStack = ArrayDeque(files)
		var checksum = 0L

		while (fileStack.isNotEmpty()) {
			val file = fileStack.removeLast()
			val slots = spaceManager.slotsFor(file.size)
			
			if (slots.isEmpty() || slots.first().start > file.start) {
				checksum += file.id * sumConsumedBlocks(file.start, file.size) 
				continue
			}

			val slot = slots.first()
			spaceManager.remove(slot)
			if (slot.size > file.size) {
				spaceManager.add(Segment(
					start = slot.start + file.size,
					size = slot.size - file.size,
					id = -1
				))
			}

			checksum += file.id * sumConsumedBlocks(slot.start, file.size)
		}

		return checksum.toString()
	}

	fun sumConsumedBlocks(start: Long, size: Long): Long {
			val fileEnd = start + size - 1
			val lastFileEnd = start - 1
			return ((fileEnd * (fileEnd + 1)) - (lastFileEnd * (lastFileEnd + 1))) / 2
	}
}

data class Segment(val start: Long, val size: Long, val id: Long)


// A quick and dirty malloc-esque Map wrapper
typealias FragmentManager = Map<Long, SortedSet<Segment>>

fun FragmentManager(): FragmentManager = 
	1L.rangeTo(9)
		.map { it to TreeSet<Segment>() { a, b -> a.start.compareTo(b.start) } }
		.toMap()

fun FragmentManager.slotsFor(size: Long): SortedSet<Segment> =
	this.get(size)!!

fun FragmentManager.remove(segment: Segment) =
	1L.rangeTo(segment.size)
		.forEach { this.get(it)!!.remove(segment) }

fun FragmentManager.add(segment: Segment) =
	1L.rangeTo(segment.size)
		.forEach { this.get(it)!!.add(segment) }
