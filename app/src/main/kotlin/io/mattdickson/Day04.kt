package io.mattdickson

class Day04(input: String) : Solver(input) {

	val grid = super.input.lines().map(String::toCharArray)
	val offsets = (-1..1).flatMap { 
		rowOffset -> (-1..1).map { colOffset -> Pair(rowOffset, colOffset) } 
	}

	override fun part1(): String = 
		PrefixTrieNode().apply { addPrefix("XMAS") }.let { trie ->
			0.rangeUntil(grid.size).map { row ->
				0.rangeUntil(grid[0].size).map { col -> 
					offsets.map { dfs(row, col, trie, it) }.sum()
				}.sum()
			}.sum()
			.toString()
	}	

	fun dfs(i: Int, j: Int, prefix: PrefixTrieNode, offset: Pair<Int, Int>): Int {
		if (i < 0 || i >= grid.size || j < 0 || j >= grid[0].size) {
			return 0 
		}

		val ch = grid[i][j]
		val nextPrefix = prefix.getChild(ch) ?: return 0
		if (nextPrefix.isLeaf()) return 1 
 
		return dfs(i + offset.first, j + offset.second, nextPrefix, offset)
	}

	override fun part2(): String =
			1.rangeUntil(grid.size-1).map { row ->
				1.rangeUntil(grid[0].size-1).filter { col -> 
					isXMas(row, col)
				}.count()
			}.sum()
			.toString()
	

	fun isXMas(i: Int, j: Int): Boolean = 
		grid[i][j] == 'A'
		&& (grid[i-1][j-1] == 'M' && grid[i+1][j+1] == 'S' || grid[i-1][j-1] == 'S' && grid[i+1][j+1] == 'M') 
		&& (grid[i+1][j-1] == 'M' && grid[i-1][j+1] == 'S' || grid[i+1][j-1] == 'S' && grid[i-1][j+1] == 'M') 
}

class PrefixTrieNode {

	val children: MutableMap<Char, PrefixTrieNode> = mutableMapOf()

	fun addPrefix(prefix: String) {
		if (prefix.isNotBlank()) {
			children
				.getOrPut(prefix[0], ::PrefixTrieNode)
				.addPrefix(prefix.substring(1))
		}
	}

	fun getChild(char: Char): PrefixTrieNode? = children.get(char)

	fun isLeaf(): Boolean = children.isEmpty()

	override fun toString(): String =
		children.toString()
}
