$ErrorActionPreference = 'Stop'
$codificacionAnterior = [Console]::OutputEncoding
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new($false)
Push-Location $PSScriptRoot
try {
    $fuentesEjercicio = @(Get-ChildItem -Path src,test -Filter *.java -Recurse | ForEach-Object { $_.FullName })
    New-Item -ItemType Directory -Path out -Force | Out-Null
    $salidaClases = (New-Item -ItemType Directory -Path (Join-Path $PSScriptRoot ('out/clases-' + [guid]::NewGuid().ToString('N')))).FullName
    javac -encoding UTF-8 --release 11 -Xlint:all -d $salidaClases $fuentesEjercicio
    if ($LASTEXITCODE -ne 0) { throw 'Fallo la compilacion' }
    java '-Dfile.encoding=UTF-8' '-Dstdout.encoding=UTF-8' '-Dstderr.encoding=UTF-8' -cp $salidaClases ejemplo.observer.Main
    if ($LASTEXITCODE -ne 0) { throw 'Fallo el ejemplo' }
    java '-Dfile.encoding=UTF-8' '-Dstdout.encoding=UTF-8' '-Dstderr.encoding=UTF-8' -cp $salidaClases ejemplo.observer.ObserverTest
    if ($LASTEXITCODE -ne 0) { throw 'Fallaron las pruebas' }
}
finally {
    Pop-Location
    [Console]::OutputEncoding = $codificacionAnterior
}
