import { argbFromHex, themeFromSourceColor, applyTheme, themeFromImage } from '@material/material-color-utilities';

(() => {
  const root = document.documentElement;
  const toggle = document.querySelector('#themeToggle');
  const icon = document.querySelector('#themeIcon');
  const color = document.querySelector('#seedColor');
  const imageInput = document.querySelector('#wallpaperInput');
  const toast = document.querySelector('#toast');
  const status = document.querySelector('#paletteStatus');
  let timer;
  let currentImageUrl = null;
  let activeSeed = '#6750a4';
  const store = {get(k){try{return localStorage.getItem(k)}catch{return null}},set(k,v){try{localStorage.setItem(k,v)}catch{}}};
  const saved = store.get('m3-demo-theme');
  const preference = matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
  root.dataset.theme = saved === 'dark' || saved === 'light' ? saved : preference;

  function renderTheme(seed = activeSeed) {
    activeSeed = seed;
    try {
      const theme = themeFromSourceColor(argbFromHex(seed));
      applyTheme(theme, {target: root, dark: root.dataset.theme === 'dark', paletteTones: [0,10,20,30,40,50,60,70,80,90,95,99,100]});
      status.textContent = 'Esquema tonal gerado com Material Color Utilities.';
      document.querySelector('meta[name="theme-color"]').content = theme.schemes[root.dataset.theme].toJSON().primary ? `#${(theme.schemes[root.dataset.theme].toJSON().primary & 0xffffff).toString(16).padStart(6,'0')}` : seed;
    } catch (error) {
      console.error('Falha ao gerar esquema Material:', error);
      status.textContent = 'Não foi possível carregar o gerador de cores; usando os tokens CSS de fallback.';
    }
  }
  const updateTheme = () => {const dark=root.dataset.theme==='dark';icon.textContent=dark?'☼':'☾';toggle.setAttribute('aria-label',dark?'Ativar tema claro':'Ativar tema escuro');renderTheme(activeSeed);};
  updateTheme();
  toggle.addEventListener('click',()=>{root.dataset.theme=root.dataset.theme==='dark'?'light':'dark';store.set('m3-demo-theme',root.dataset.theme);updateTheme();});
  color.addEventListener('input',()=>{if(currentImageUrl){URL.revokeObjectURL(currentImageUrl);currentImageUrl=null;}renderTheme(color.value);});
  document.querySelector('#resetColor').addEventListener('click',()=>{if(currentImageUrl){URL.revokeObjectURL(currentImageUrl);currentImageUrl=null;}color.value='#6750a4';imageInput.value='';renderTheme('#6750a4');});
  imageInput.addEventListener('change',async()=>{
    const file=imageInput.files?.[0]; if(!file)return;
    if(!file.type.startsWith('image/')){status.textContent='Escolha um arquivo de imagem.';return;}
    if(file.size>12*1024*1024){status.textContent='A imagem precisa ter menos de 12 MB.';imageInput.value='';return;}
    try{const img=new Image();currentImageUrl=URL.createObjectURL(file);img.src=currentImageUrl;await img.decode();const theme=await themeFromImage(img);activeSeed=`#${(theme.source&0xffffff).toString(16).padStart(6,'0')}`;color.value=activeSeed;applyTheme(theme,{target:root,dark:root.dataset.theme==='dark',paletteTones:[0,10,20,30,40,50,60,70,80,90,95,99,100]});status.textContent='Tema criado a partir da imagem local. A imagem não é enviada.';}catch(error){console.error(error);status.textContent='Não foi possível analisar esta imagem. Tente outra.';}
  });
  document.querySelectorAll('.chip').forEach(chip=>chip.addEventListener('click',()=>{document.querySelectorAll('.chip').forEach(c=>{c.classList.remove('selected');c.setAttribute('aria-pressed','false')});chip.classList.add('selected');chip.setAttribute('aria-pressed','true')}));
  document.querySelectorAll('[data-message]').forEach(button=>button.addEventListener('click',()=>{toast.textContent=button.dataset.message;toast.classList.add('show');clearTimeout(timer);timer=setTimeout(()=>toast.classList.remove('show'),2400)}));
  document.querySelector('#shapeToggle').addEventListener('click',e=>{const button=e.currentTarget;button.classList.toggle('morphed');button.setAttribute('aria-pressed',button.classList.contains('morphed')?'true':'false')});
  matchMedia('(prefers-color-scheme: dark)').addEventListener?.('change',e=>{if(!store.get('m3-demo-theme')){root.dataset.theme=e.matches?'dark':'light';updateTheme()}});
})();
