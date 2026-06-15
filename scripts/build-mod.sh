#!/bin/sh

set -eu

ROOT_DIR=$(CDPATH= cd -- "$(dirname "$0")/.." && pwd)
BUILD_DIR="$ROOT_DIR/build/manual"
CLASSES_DIR="$BUILD_DIR/classes"
TMP_JAR_DIR="$BUILD_DIR/jar"

STS_JAR=${STS_JAR:-"$HOME/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/desktop-1.0.jar"}
MTS_JAR=${MTS_JAR:-"$HOME/Library/Application Support/Steam/steamapps/workshop/content/646570/1605060445/ModTheSpire.jar"}
MODS_DIR=${MODS_DIR:-"$HOME/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/mods"}
OUTPUT_JAR="$MODS_DIR/SpireMod.jar"

if [ ! -f "$STS_JAR" ]; then
  echo "Slay the Spire jar not found: $STS_JAR" >&2
  exit 1
fi

if [ ! -f "$MTS_JAR" ]; then
  echo "ModTheSpire jar not found: $MTS_JAR" >&2
  exit 1
fi

rm -rf "$BUILD_DIR"
mkdir -p "$CLASSES_DIR" "$TMP_JAR_DIR" "$MODS_DIR"

javac --release 8 \
  -cp "$STS_JAR:$MTS_JAR" \
  -d "$CLASSES_DIR" \
  $(find "$ROOT_DIR/src/main/java" -type f | sort)

cp -R "$CLASSES_DIR"/. "$TMP_JAR_DIR"/
cp "$ROOT_DIR/src/main/resources/ModTheSpire.json" "$TMP_JAR_DIR"/

(cd "$TMP_JAR_DIR" && jar cf "$OUTPUT_JAR" .)

echo "Built mod jar: $OUTPUT_JAR"
