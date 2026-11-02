import '@material/web/button/filled-button.js';
import '@material/web/textfield/outlined-text-field.js';
document.querySelector('#createProject').addEventListener('click', () => {
  const name = document.querySelector('#projectName').value.trim();
  document.querySelector('#exampleStatus').textContent = name ? `Prévia local: ${name}. Nenhum projeto foi criado no servidor.` : 'Digite um nome para testar.';
});
