#! /usr/bin/env sh

set -eux

if [[ "$#" -ne 1 ]]; then
	echo "Must provide a day to run"
	exit 1
fi

DAY="$1"
EXEC_PATH="$HOME/workplace/AdventOfCode/2024/$DAY"

kotlinc "$EXEC_PATH/solution.kt" -d "$EXEC_PATH/solution.jar"
kotlin -classpath "$EXEC_PATH/solution.jar" SolutionKt
