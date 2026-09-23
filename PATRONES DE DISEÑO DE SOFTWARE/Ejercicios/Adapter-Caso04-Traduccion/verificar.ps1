$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    $fuentesTraduccion = @(Get-ChildItem -Path src,test -Filter *.java -Recurse | ForEach-Object { $_.FullName })
    javac -encoding UTF-8 --release 11 -Xlint:all -d out $fuentesTraduccion
    if ($LASTEXITCODE -ne 0) { throw 'Fallo la compilacion' }
    java -cp out ejemplo.adapter.Main
    if ($LASTEXITCODE -ne 0) { throw 'Fallo el ejemplo' }
    java -cp out ejemplo.adapter.AdapterTest
    if ($LASTEXITCODE -ne 0) { throw 'Fallaron las pruebas' }
}
finally {
    Pop-Location
}
