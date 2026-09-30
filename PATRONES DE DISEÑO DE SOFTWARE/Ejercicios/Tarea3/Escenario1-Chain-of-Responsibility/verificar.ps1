$ErrorActionPreference = 'Stop'
$codificacionAnterior = [Console]::OutputEncoding
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new($false)
Push-Location $PSScriptRoot
try {
    $fuentesEjercicio = @(Get-ChildItem -Path src,test -Filter *.java -Recurse | ForEach-Object { $_.FullName })
    if (Test-Path -LiteralPath out) { Remove-Item -LiteralPath out -Recurse -Force }
    javac -encoding UTF-8 --release 11 -Xlint:all -d out $fuentesEjercicio
    if ($LASTEXITCODE -ne 0) { throw 'Fallo la compilacion' }
    java '-Dfile.encoding=UTF-8' '-Dstdout.encoding=UTF-8' '-Dstderr.encoding=UTF-8' -cp out ejemplo.chain.Main
    if ($LASTEXITCODE -ne 0) { throw 'Fallo el ejemplo' }
    java '-Dfile.encoding=UTF-8' '-Dstdout.encoding=UTF-8' '-Dstderr.encoding=UTF-8' -cp out ejemplo.chain.ChainOfResponsibilityTest
    if ($LASTEXITCODE -ne 0) { throw 'Fallaron las pruebas' }
}
finally {
    Pop-Location
    [Console]::OutputEncoding = $codificacionAnterior
}
