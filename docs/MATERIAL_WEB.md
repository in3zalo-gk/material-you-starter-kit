# Material Web no m3 kit 1.0.2

O fork `in3zalo-gk/material-web` é referência. A integração usa o pacote publicado e fixado `@material/web@2.5.0`, sem copiar o repositório inteiro, testes upstream ou sua identidade visual. Isso permite instalar com `npm ci` e preservar atualizações separadamente.

## Catálogo estável

`web/catalog.js` importa individualmente os mesmos 43 módulos de `@material/web/all.js`: cinco botões, checkbox, cinco módulos de chips (incluindo container), diálogo, divisor, elevação, dois FABs, dois fields primitivos, foco, ícone, quatro icon buttons, lista/item, menu/item/submenu, dois progressos, radio, ripple, três módulos select, slider, switch, três módulos tabs e dois text fields.

Alguns módulos são containers ou primitivos, não 43 famílias diferentes de controles. O catálogo demonstra combinações reais, estados, busca textual, seed e tema. O catálogo inteiro é uma vitrine; em produção importe só o necessário, como no exemplo `web/examples/material-web.js`.

## Camadas

- `web/index.html`: laboratório e galeria autorais, CSS próprio + Material Color Utilities, pt/en/es.
- `web/catalog.html`: componentes oficiais Material Web, em português, estado temporário.
- `web/examples/material-web.html`: botão e campo, exemplo reduzido oficial.
- Android: Compose Material3 1.4.0, não web components ou WebView.

O catálogo lê a preferência de tema/seed do laboratório quando abre, mas os ajustes locais do catálogo não sobrescrevem preferências do laboratório. Não carrega fontes de ícones externas; símbolos são texto. Dados são fictícios, menus de exportação não geram arquivos e ações não criam projetos no servidor.

## Limites

Material Web está em manutenção. Seu catálogo estável não implementa toda a especificação Material 3/Expressive. Date/time pickers, sheets, busca de negócio, navegação adaptativa e serviços exigem implementação própria ou outras bibliotecas. O Android inclui amostras nativas de pickers e sheets; isso não implica equivalentes web.

## Licença

Código próprio MIT, Material Web Apache-2.0. Veja `THIRD_PARTY_NOTICES.md` para as licenças das dependências distribuídas. Não remova os avisos ao hospedar o build. Não há afiliação ou endosso da Google.
