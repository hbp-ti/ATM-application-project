@echo off

REM Obtém o nome do usuário atual
set "CURRENT_USER=%USERNAME%"

REM Verifica se Chocolatey está instalado
where /q choco
if %ERRORLEVEL% neq 0 (
    echo Chocolatey não encontrado, instalando...
    @"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -InputFormat None -ExecutionPolicy Bypass -Command "iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))" && SET "PATH=%PATH%;%ALLUSERSPROFILE%\chocolatey\bin"
)

REM Verifica se git está instalado
where /q git
if %ERRORLEVEL% neq 0 (
    echo Git não encontrado, instalando...
    choco install git
)

REM Cria a pasta ByteBankData no diretório do usuário atual e a torna oculta
mkdir C:\Users\%CURRENT_USER%\ByteBankData
attrib +h C:\Users\%CURRENT_USER%\ByteBankData

REM Cria a pasta Resources dentro da pasta ByteBankData
mkdir C:\Users\%CURRENT_USER%\ByteBankData\Resources

REM Navega para a pasta Resources
cd /d C:\Users\%CURRENT_USER%\ByteBankData\Resources

REM Baixa apenas a pasta ByteBank WIN do repositório do GitHub usando git
git init
git remote add -f origin https://github.com/hbp-ti/ByteBank.git
git config core.sparseCheckout true
echo ByteBank\ WIN\/* >> .git/info/sparse-checkout
git pull origin main

REM Move os arquivos da pasta ByteBank WIN para a pasta Resources
xcopy /s /e "ByteBank WIN\*" .

REM Remove a pasta do repositório temporário e o .git
rd /s /q "ByteBank WIN" ".git"

REM Retorna para a pasta ByteBankData
cd /d C:\Users\%CURRENT_USER%\ByteBankData

REM Atualiza o script em batch para executar o arquivo jar com as opções da JVM
(
    echo @echo off
    echo java --module-path "C:\Users\%USERNAME%\ByteBankData\Resources\openjfx-21.0.1_windows-x64_bin-sdk\javafx-sdk-21.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar "C:\Users\%USERNAME%\ByteBankData\Resources\ByteBank.jar"
) > runByteBank.bat

REM Cria um atalho para o script em batch com o nome ByteBank na área de trabalho
echo Set oWS = WScript.CreateObject("WScript.Shell") > createShortcut.vbs
echo sLinkFile = "%USERPROFILE%\Desktop\ByteBank.lnk" >> createShortcut.vbs
echo Set oLink = oWS.CreateShortcut(sLinkFile) >> createShortcut.vbs
echo oLink.TargetPath = "%USERPROFILE%\ByteBankData\runByteBank.bat" >> createShortcut.vbs
echo oLink.WorkingDirectory = "%USERPROFILE%\ByteBankData\Resources" >> createShortcut.vbs
echo oLink.IconLocation = "%USERPROFILE%\ByteBankData\Resources\logo2.ico" >> createShortcut.vbs
echo oLink.Save >> createShortcut.vbs
cscript createShortcut.vbs
del createShortcut.vbs

REM Cria um atalho para o script em batch com o nome ByteBank na área de trabalho do oneDrive
if exist "%UserProfile%\OneDrive" (
    echo Set oWS = WScript.CreateObject("WScript.Shell") > createShortcut.vbs
    echo sLinkFile = "%USERPROFILE%\OneDrive\Desktop\ByteBank.lnk" >> createShortcut.vbs
    echo Set oLink = oWS.CreateShortcut(sLinkFile) >> createShortcut.vbs
    echo oLink.TargetPath = "%USERPROFILE%\ByteBankData\runByteBank.bat" >> createShortcut.vbs
    echo oLink.WorkingDirectory = "%USERPROFILE%\ByteBankData\Resources" >> createShortcut.vbs
    echo oLink.IconLocation = "%USERPROFILE%\ByteBankData\Resources\logo2.ico" >> createShortcut.vbs
    echo oLink.Save >> createShortcut.vbs
    cscript createShortcut.vbs
    del createShortcut.vbs
)