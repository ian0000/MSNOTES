$ErrorActionPreference = 'Stop'
$codificacionAnterior = [Console]::OutputEncoding
[Console]::OutputEncoding = [System.Text.UTF8Encoding]::new($false)
Push-Location $PSScriptRoot
try {
    $nombreJar = 'jackson-core-2.21.4.jar'
    $hashEsperado = '4B40A06396F239F8DE2DA57419ADDE6E94E5EDC18A2171D471EA05EEED4E5C2D'
    $rutaJar = Join-Path $PSScriptRoot "lib/$nombreJar"
    if (-not (Test-Path -LiteralPath $rutaJar)) {
        New-Item -ItemType Directory -Path (Join-Path $PSScriptRoot 'lib') -Force | Out-Null
        $cacheJar = Join-Path ([Environment]::GetFolderPath('UserProfile')) ".m2/repository/com/fasterxml/jackson/core/jackson-core/2.21.4/$nombreJar"
        if (Test-Path -LiteralPath $cacheJar) {
            Copy-Item -LiteralPath $cacheJar -Destination $rutaJar
        } else {
            Invoke-WebRequest -Uri "https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-core/2.21.4/$nombreJar" -OutFile $rutaJar
        }
    }
    if ((Get-FileHash -LiteralPath $rutaJar -Algorithm SHA256).Hash -ne $hashEsperado) {
        throw 'La biblioteca JSON no coincide con la versión verificada.'
    }
    $fuentesEjercicio = @(Get-ChildItem -Path src,test -Filter *.java -Recurse | ForEach-Object { $_.FullName })
    New-Item -ItemType Directory -Path out -Force | Out-Null
    $salidaClases = (New-Item -ItemType Directory -Path (Join-Path $PSScriptRoot ('out/clases-' + [guid]::NewGuid().ToString('N')))).FullName
    javac -encoding UTF-8 --release 11 -Xlint:all -cp $rutaJar -d $salidaClases $fuentesEjercicio
    if ($LASTEXITCODE -ne 0) { throw 'Fallo la compilacion' }
    $classPath = $salidaClases + [IO.Path]::PathSeparator + $rutaJar
    java '-Dfile.encoding=UTF-8' '-Dstdout.encoding=UTF-8' '-Dstderr.encoding=UTF-8' -cp $classPath ejemplo.template.Main
    if ($LASTEXITCODE -ne 0) { throw 'Fallo el ejemplo' }
    java '-Dfile.encoding=UTF-8' '-Dstdout.encoding=UTF-8' '-Dstderr.encoding=UTF-8' -cp $classPath ejemplo.template.ImportacionTest
    if ($LASTEXITCODE -ne 0) { throw 'Fallaron las pruebas' }
    Set-Content -LiteralPath 'out/classpath.txt' -Value $classPath -Encoding UTF8
}
finally {
    Pop-Location
    [Console]::OutputEncoding = $codificacionAnterior
}
