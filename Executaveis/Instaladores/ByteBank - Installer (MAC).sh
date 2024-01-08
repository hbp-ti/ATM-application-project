#!/bin/bash

# Obtém o nome do usuário atual
CURRENT_USER=$(whoami)

# Verifica se o Git está instalado e o instala se não estiver
if ! command -v git &> /dev/null; then
    echo "Git não encontrado, instalando..."
    brew install git
fi

# Cria a pasta ByteBankData no diretório do usuário atual e a torna oculta
mkdir /Users/$CURRENT_USER/ByteBankData
chflags hidden /Users/$CURRENT_USER/ByteBankData

# Cria a pasta Resources dentro da pasta ByteBankData
mkdir /Users/$CURRENT_USER/ByteBankData/Resources

# Navega para a pasta Resources
cd /Users/$CURRENT_USER/ByteBankData/Resources

# Baixa apenas a pasta ByteBank MAC do repositório do GitHub usando git
git init
git remote add -f origin https://github.com/hbp-ti/ByteBank.git
git config core.sparseCheckout true
echo ByteBank\ MAC\/* >> .git/info/sparse-checkout
git pull origin main

# Move os arquivos da pasta ByteBank MAC para a pasta Resources
cp -r "ByteBank MAC/"* .

# Remove a pasta do repositório temporário e o .git
rm -rf "ByteBank MAC" .git

# Retorna para a pasta ByteBankData
cd /Users/$CURRENT_USER/ByteBankData

# Atualiza o script para executar o arquivo jar com as opções da JVM
echo '#!/bin/bash' > runByteBank.sh
echo "java --module-path '/Users/$CURRENT_USER/ByteBankData/Resources/openjfx-21.0.1_osx-aarch64_bin-sdk/javafx-sdk-21.0.1/lib' --add-modules javafx.controls,javafx.fxml -jar '/Users/$CURRENT_USER/ByteBankData/Resources/ByteBank.jar'" >> runByteBank.sh
chmod +x runByteBank.sh

# Cria um atalho para o script na área de trabalho (o método pode variar dependendo do ambiente gráfico)
ln -s /Users/$CURRENT_USER/ByteBankData/runByteBank.sh /Users/$CURRENT_USER/Desktop/ByteBank.sh
chmod +x /Users/$CURRENT_USER/Desktop/ByteBank.sh

# Verifica se o ícone logo2.ico está presente na pasta Resources
if [ -f "/Users/$CURRENT_USER/ByteBankData/Resources/logo2.ico" ]; then
    # Cria o ícone no formato .icns
    iconutil -c icns -o /Users/$CURRENT_USER/ByteBankData/Resources/logo2.icns /Users/$CURRENT_USER/ByteBankData/Resources/logo2.ico

    # Atribui o ícone ao atalho na área de trabalho
    sips -i /Users/$CURRENT_USER/Desktop/ByteBank.sh
    DeRez -only icns /Users/$CURRENT_USER/ByteBankData/Resources/logo2.icns > /tmp/logo2.rsrc
    Rez -append /tmp/logo2.rsrc -o /Users/$CURRENT_USER/Desktop/ByteBank.sh
fi
