@echo off
rem Wrapper otomatis ke Maven IntelliJ IDEA atau Maven lokal
if exist "C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.2\plugins\maven-plugin\lib\maven3\bin\mvn.cmd" (
    "C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.2\plugins\maven-plugin\lib\maven3\bin\mvn.cmd" %*
) else (
    mvn %*
)
