#!/bin/bash

# Criar estrutura de diretórios base
mkdir -p src/main/java/com/torneios/{config,controller,dto,exception,model/enums,repository,service/impl}
mkdir -p src/test/java/com/torneios/{controller,service/impl}

# Mover arquivos para os diretórios corretos
find src/main/java -name "*.java" -not -path "*/com/torneios/*" -exec bash -c '
    file="$1"
    filename=$(basename "$file")
    if [[ "$file" == *"/config/"* ]]; then
        mv "$file" "src/main/java/com/torneios/config/"
    elif [[ "$file" == *"/controller/"* ]]; then
        mv "$file" "src/main/java/com/torneios/controller/"
    elif [[ "$file" == *"/dto/"* ]]; then
        mv "$file" "src/main/java/com/torneios/dto/"
    elif [[ "$file" == *"/exception/"* ]]; then
        mv "$file" "src/main/java/com/torneios/exception/"
    elif [[ "$file" == *"/model/enums/"* ]]; then
        mv "$file" "src/main/java/com/torneios/model/enums/"
    elif [[ "$file" == *"/model/"* ]]; then
        mv "$file" "src/main/java/com/torneios/model/"
    elif [[ "$file" == *"/repository/"* ]]; then
        mv "$file" "src/main/java/com/torneios/repository/"
    elif [[ "$file" == *"/service/impl/"* ]]; then
        mv "$file" "src/main/java/com/torneios/service/impl/"
    elif [[ "$file" == *"/service/"* ]]; then
        mv "$file" "src/main/java/com/torneios/service/"
    fi
' bash {} \;

# Mover arquivos de teste
find src/test/java -name "*.java" -not -path "*/com/torneios/*" -exec bash -c '
    file="$1"
    filename=$(basename "$file")
    if [[ "$file" == *"/controller/"* ]]; then
        mv "$file" "src/test/java/com/torneios/controller/"
    elif [[ "$file" == *"/service/impl/"* ]]; then
        mv "$file" "src/test/java/com/torneios/service/impl/"
    fi
' bash {} \; 