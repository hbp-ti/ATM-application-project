#!/bin/bash

# Obtém o nome do usuário atual
CURRENT_USER=$(whoami)

# Instala o Git se não estiver instalado
if ! command -v git &> /dev/null; then
    echo "Git não encontrado, instalando..."
    sudo apt update
    sudo apt install git -y
fi

# Cria a pasta ByteBankData no diretório do usuário atual e a torna oculta
mkdir /home/$CURRENT_USER/ByteBankData
chmod +h /home/$CURRENT_USER/ByteBankData

# Cria a pasta Resources dentro da pasta ByteBankData
mkdir /home/$CURRENT_USER/ByteBankData/Resources

# Navega para a pasta Resources
cd /home/$CURRENT_USER/ByteBankData/Resources

# Baixa apenas a pasta ByteBank Linux do repositório do GitHub usando git
git init
git remote add -f origin https://github.com/hbp-ti/ByteBank.git
git config core.sparseCheckout true
echo ByteBank\ Linux\/* >> .git/info/sparse-checkout
git pull origin main

# Move os arquivos da pasta ByteBank Linux para a pasta Resources
cp -r "ByteBank Linux/"* .

# Remove a pasta do repositório temporário e o .git
rm -rf "ByteBank Linux" .git

# Retorna para a pasta ByteBankData
cd /home/$CURRENT_USER/ByteBankData

# Atualiza o script para executar o arquivo jar com as opções da JVM
echo '#!/bin/bash' > runByteBank.sh
echo "java --module-path '/home/$CURRENT_USER/ByteBankData/Resources/openjfx-21.0.1_linux-x64_bin-sdk/javafx-sdk-21.0.1/lib' --add-modules javafx.controls,javafx.fxml -jar '/home/$CURRENT_USER/ByteBankData/Resources/ByteBank.jar'" >> runByteBank.sh
chmod +x runByteBank.sh

# Cria um atalho para o script na área de trabalho (o método pode variar dependendo do ambiente gráfico)
cp /home/$CURRENT_USER/ByteBankData/runByteBank.sh /home/$CURRENT_USER/Desktop/ByteBank.sh
chmod +x /home/$CURRENT_USER/Desktop/ByteBank.sh

# Define o ícone para o atalho na área de trabalho
gio set /home/$CURRENT_USER/Desktop/ByteBank.sh metadata::custom-icon file:///home/$CURRENT_USER/ByteBankData/Resources/logo2.ico
