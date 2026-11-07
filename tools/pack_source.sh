#!/usr/bin/env bash
# The repository's historical CI workflow builds from voiddragon-source.zip,
# so keep that archive in sync with the project root.
set -euo pipefail
cd "$(dirname "$0")/.."
rm -f voiddragon-source.zip
rm -rf /tmp/vd_pack && mkdir -p /tmp/vd_pack/voiddragon
cp -r build.gradle gradle.properties settings.gradle src /tmp/vd_pack/voiddragon/
(cd /tmp/vd_pack && zip -qr -0 "$OLDPWD/voiddragon-source.zip" voiddragon -x '*/build/*' '*.DS_Store')
unzip -l voiddragon-source.zip | tail -3
