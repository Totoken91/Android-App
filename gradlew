#!/bin/sh

#
# Gradle startup script for POSIX systems (Linux, macOS, etc.)
#

# Attempt to set APP_HOME
APP_HOME="${APP_HOME:-$(dirname "$(readlink -f "$0" 2>/dev/null || realpath "$0" 2>/dev/null || echo "$0")")"}"

APP_NAME="Gradle"
APP_BASE_NAME="$(basename "$0")"

# Default JVM options for Gradle
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use JAVA_HOME if set, otherwise just use java
if [ -n "$JAVA_HOME" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi

if ! command -v "$JAVACMD" > /dev/null 2>&1; then
    echo "ERROR: JAVA_HOME is not set and 'java' could not be found in PATH." >&2
    exit 1
fi

CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

exec "$JAVACMD" \
    $DEFAULT_JVM_OPTS \
    $JAVA_OPTS \
    $GRADLE_OPTS \
    "-Dorg.gradle.appname=$APP_BASE_NAME" \
    -classpath "$CLASSPATH" \
    org.gradle.wrapper.GradleWrapperMain \
    "$@"
