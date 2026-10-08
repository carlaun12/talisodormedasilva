#!/usr/bin/env python3
"""P03-A01-002: capture reproducible local Android validation evidence."""

import datetime
import json
import os
from pathlib import Path
import subprocess


def main():
    root = Path(__file__).resolve().parents[1]
    timestamp = datetime.datetime.now(datetime.timezone.utc).strftime("%Y%m%dT%H%M%SZ")
    logs = root / "build" / "validation" / timestamp
    logs.mkdir(parents=True)
    environment = {key: os.environ.get(key) for key in ("JAVA_HOME", "ANDROID_HOME", "ANDROID_SDK_ROOT")}
    commands = [
        ["git", "rev-parse", "HEAD"],
        ["java", "-version"],
        ["javac", "-version"],
        ["sdkmanager", "--list_installed"],
        ["adb", "devices", "-l"],
        ["./gradlew", "--version"],
        ["./gradlew", "projects"],
        ["./gradlew", "tasks"],
        ["./gradlew", ":app:assembleDebug"],
        ["./gradlew", ":app:testDebugUnitTest"],
        ["./gradlew", ":app:lintDebug"],
        ["./gradlew", ":app:assembleDebugAndroidTest"],
        ["./gradlew", ":app:connectedDebugAndroidTest"],
    ]
    report = {"task": "P03-A01-002", "started_utc": timestamp, "environment": environment, "commands": []}
    for index, command in enumerate(commands, 1):
        log = logs / f"{index:02d}.log"
        print("RUN " + " ".join(command), flush=True)
        started = datetime.datetime.now(datetime.timezone.utc)
        with log.open("w") as output:
            try:
                result = subprocess.run(command, cwd=root, stdout=output, stderr=subprocess.STDOUT, check=False)
                exit_code = result.returncode
            except FileNotFoundError as error:
                output.write(str(error) + "\n")
                exit_code = 127
        entry = {
            "command": command,
            "exit_code": exit_code,
            "started_utc": started.isoformat(),
            "seconds": (datetime.datetime.now(datetime.timezone.utc) - started).total_seconds(),
            "log": str(log.relative_to(root)),
        }
        report["commands"].append(entry)
        (logs / "commands.json").write_text(json.dumps(report, indent=2) + "\n")
        print(f"RESULT exit={exit_code} log={entry['log']}", flush=True)
    print(f"Evidence: {logs}")
    # Preserve raw failures: the handoff distinguishes defects from environment/device blockers.
    return int(any(entry["exit_code"] != 0 for entry in report["commands"]))


if __name__ == "__main__":
    raise SystemExit(main())
