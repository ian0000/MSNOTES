$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    $fuentesActivos = @(Get-ChildItem -Path src,test -Filter *.java -Recurse | ForEach-Object { $_.FullName })
    javac -encoding UTF-8 --release 11 -Xlint:all -d out $fuentesActivos
    if ($LASTEXITCODE -ne 0) { throw 'Fallo la compilacion' }
    java -cp out ejemplo.activos.Main
    if ($LASTEXITCODE -ne 0) { throw 'Fallo la demostracion' }
    java -cp out ejemplo.activos.ActivoFijoTest
    if ($LASTEXITCODE -ne 0) { throw 'Fallaron las pruebas' }
}
finally {
    Pop-Location
}
