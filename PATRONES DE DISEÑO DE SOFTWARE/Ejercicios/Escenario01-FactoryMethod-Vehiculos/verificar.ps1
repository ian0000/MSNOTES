$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    $fuentesVehiculos = @(Get-ChildItem -Path src,test -Filter *.java -Recurse | ForEach-Object { $_.FullName })
    javac -encoding UTF-8 --release 11 -Xlint:all -d out $fuentesVehiculos
    if ($LASTEXITCODE -ne 0) { throw 'Fallo la compilacion' }
    java -cp out ejemplo.vehiculos.Main auto ABC-123 Marca Modelo 2020 20000 2026
    if ($LASTEXITCODE -ne 0) { throw 'Fallo el ejemplo Auto' }
    java -cp out ejemplo.vehiculos.Main camioneta DEF-456 Marca Modelo 2021 30000 2026 800
    if ($LASTEXITCODE -ne 0) { throw 'Fallo el ejemplo Camioneta' }
    java -cp out ejemplo.vehiculos.Main camion GHI-789 Marca Modelo 2018 50000 2026 12 8
    if ($LASTEXITCODE -ne 0) { throw 'Fallo el ejemplo Camion' }
    java -cp out ejemplo.vehiculos.VehiculosTest
    if ($LASTEXITCODE -ne 0) { throw 'Fallaron las pruebas' }
}
finally {
    Pop-Location
}
