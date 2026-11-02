# Programar neste projeto pelo Termux com assistência de IA

Há duas formas simples: usar a IA no chat do navegador copiando contexto e diff (mais segura), ou usar um agente de programação local compatível com Termux. A disponibilidade e os comandos de instalação do agente variam; não existe um único comando oficial para todas as IAs. Não passe tokens em prompts nem aceite permissões irrestritas.

## Preparar o Termux
Instale o Termux de uma fonte oficial mantida e abra uma sessão. Os comandos abaixo deixam o shell aberto:

```sh
pkg update && pkg upgrade
pkg install git nodejs-lts python
termux-setup-storage
```

Autorize o acesso ao armazenamento apenas se for útil. Para trabalhar em uma pasta privada do Termux:

```sh
mkdir -p ~/projetos
cd ~/projetos
git clone https://github.com/in3zalo-gk/material-you-starter-kit.git
cd material-you-starter-kit
```

Se `nodejs-lts` não existir no espelho atual, confira `pkg search nodejs` e instale o pacote Node disponível. Não instale APKs ou scripts aleatórios para “ativar IA”.

## Web: executar e validar

```sh
cd ~/projetos/material-you-starter-kit/web
npm install
npm run dev -- --host 0.0.0.0
```

Abra o endereço local mostrado pelo Vite no navegador do aparelho (normalmente `http://127.0.0.1:5173`). Para validar a build:

```sh
cd ~/projetos/material-you-starter-kit/web
npm run build
```

## Android: compilar na nuvem pelo GitHub Actions

Compilar Android no telefone consome bastante espaço/RAM e requer JDK, Android SDK e Gradle. O caminho recomendado aqui é editar no Termux, fazer push da branch e deixar o workflow `Verificar app Android` compilar o APK. No app de GitHub, abra Actions e baixe o artefato `material-you-starter-debug-apk` quando a execução terminar.

Para compilar localmente, Android Studio/SDK é o caminho mais confiável. Se Gradle e JDK 17 já estiverem disponíveis:

```sh
cd ~/projetos/material-you-starter-kit
gradle -p android assembleDebug
```

Não instale SDK enorme no telefone sem espaço suficiente. Este repositório não inclui wrapper binário nem `local.properties`.

## Ciclo Git seguro
Confira primeiro que está no repositório certo e veja o que mudou:

```sh
cd ~/projetos/material-you-starter-kit
git status --short
git diff --check
git diff
```

Crie uma branch, faça uma mudança por vez, rode a validação adequada e prepare apenas os arquivos da tarefa:

```sh
git switch -c feat/minha-melhoria
# edite e teste os arquivos
 git add web/ android/ docs/ AGENTS.md
 git diff --cached --check
git diff --cached
```

Revise cuidadosamente o que foi staged; não inclua arquivos pessoais, `.env`, `local.properties`, keystores ou chaves. Se estiver tudo certo:

```sh
git commit -m "feat: descreva a melhoria"
git push -u origin feat/minha-melhoria
```

Depois, abra um Pull Request no GitHub. Não use `git add .` sem inspecionar o estado. Não cole token GitHub no chat nem no prompt do agente; use autenticação segura do próprio Git/credential manager.

## Usar a IA sem agente local
No chat de IA, anexe os arquivos relevantes ou cole o pedido junto com `AGENTS.md`. Peça primeiro um plano e os arquivos que pretende alterar. Faça você a edição no Termux ou aplique uma sugestão por vez. Depois rode `git diff`, build/testes e decida o commit.

Prompt inicial copiável:

```text
Leia AGENTS.md e docs/COMPONENTES.md. Quero implementar: [descreva uma função concreta]. Antes de editar, explique o plano e os arquivos que mudará. Use APIs oficiais compatíveis com as versões do projeto, mantenha acessibilidade e não altere dependências sem justificar. Não toque em segredos nem rode comandos destrutivos. Ao final, liste o diff e os testes que realmente executou; se não puder executar algum, diga isso claramente.
```

## Cuidado com credenciais
- Nunca compartilhe API keys, senhas, OTP, cookies, `~/.ssh`, tokens GitHub, keystore ou `.env` com a IA.
- Dê ao agente acesso somente ao diretório do projeto; não conceda acesso geral ao armazenamento do telefone.
- Revise comandos `rm`, `reset --hard`, limpeza de cache e scripts remotos antes de executá-los.
- Prefira branch e Pull Request. Commits locais são reversíveis; dados apagados e segredos vazados podem não ser.
