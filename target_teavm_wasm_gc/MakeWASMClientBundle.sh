#!/bin/sh
chmod +x gradlew
./gradlew target_teavm_wasm_gc:makeMainWasmClientBundle
