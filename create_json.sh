#!/bin/bash

echo "creating the json for github release"

F=release.json
REL_NOTE="$(while IFS= read -r line; do printf '%s\\n' "$line" ; done < changelog.md)"

echo '{
  "tag_name": "'$FULL_VERSION'",
  "target_commitish": "master",
  "name": "'$FULL_VERSION'",
	"body": "'$REL_NOTE'",
  "draft": false
}' > $F
