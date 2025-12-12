#!/usr/bin/env bash

# Runs unit tests for EvaluationApplicationServiceImpl
# Usage: ./scripts/run-evaluation-tests.sh

set -euo pipefail

mvn -Dtest=EvaluationApplicationServiceImplTest test
