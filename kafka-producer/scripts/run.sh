#!/bin/bash
set +x

JAVA_OPTS="${JAVA_OPTS} -Dvertx.cacheDirBase=/tmp/vertx-cache"

exec java $JAVA_OPTS -jar $1 $@
