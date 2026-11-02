import { detectLanguage, translate } from './i18n.js';
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
  const languageSelect = document.querySelector('#languageSelect');
  const langStore = { get(){try{return localStorage.getItem('m3-demo-language')}catch{return null}}, set(v){try{localStorage.setItem('m3-demo-language',v)}catch{}} };
  let latestStatusKey = 'Paleta Material gerada no seu navegador.';
  let language = langStore.get() || 'auto';
  const effectiveLanguage = () => language === 'auto' ? detectLanguage() : language;
  function applyLanguage() {
    const current = effectiveLanguage();
    document.documentElement.lang = current;
    document.title = translate('Material You Starter Kit — Componentes', current);
    const description = document.querySelector('meta[name="description"]');
    if (description) description.content = translate('Explore os componentes, cores e exemplos do Material You Starter Kit para web e Android.', current);
    const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT);
    let node; while ((node = walker.nextNode())) {
      if (!node.__sourceText) node.__sourceText = node.nodeValue;
      const source = node.__sourceText; const trimmed = source.trim();
      if (!trimmed) continue;
      const translated = translate(trimmed, current);
      node.nodeValue = source.replace(trimmed, translated);
    }
    document.querySelectorAll('[aria-label]').forEach(el => { if (!el.dataset.sourceAria) el.dataset.sourceAria = el.getAttribute('aria-label'); el.setAttribute('aria-label', translate(el.dataset.sourceAria, current)); });
    document.querySelectorAll('[data-message]').forEach(el => { if (!el.dataset.sourceMessage) el.dataset.sourceMessage = el.dataset.message; });
    if (status) status.textContent = translate(latestStatusKey, current);
    if (languageSelect) languageSelect.value = language;
    if (icon && toggle) toggle.setAttribute('aria-label', translate(root.dataset.theme === 'dark' ? 'Ativar tema claro' : 'Ativar tema escuro', current));
  }
  applyLanguage();
  languageSelect?.addEventListener('change', () => { language = languageSelect.value; langStore.set(language); applyLanguage(); });

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
      latestStatusKey = 'Esquema tonal gerado com Material Color Utilities.'; status.textContent = translate(latestStatusKey, effectiveLanguage());
      document.querySelector('meta[name="theme-color"]').content = theme.schemes[root.dataset.theme].toJSON().primary ? `#${(theme.schemes[root.dataset.theme].toJSON().primary & 0xffffff).toString(16).padStart(6,'0')}` : seed;
    } catch (error) {
      console.error('Falha ao gerar esquema Material:', error);
      latestStatusKey = 'Não foi possível carregar o gerador de cores; usando os tokens CSS de fallback.'; status.textContent = translate(latestStatusKey, effectiveLanguage());
    }
  }
  const updateTheme = () => {const dark=root.dataset.theme==='dark';icon.textContent=dark?'☼':'☾';toggle.setAttribute('aria-label',translate(dark?'Ativar tema claro':'Ativar tema escuro',effectiveLanguage()));renderTheme(activeSeed);};
  updateTheme();
  toggle.addEventListener('click',()=>{root.dataset.theme=root.dataset.theme==='dark'?'light':'dark';store.set('m3-demo-theme',root.dataset.theme);updateTheme();});
  color.addEventListener('input',()=>{if(currentImageUrl){URL.revokeObjectURL(currentImageUrl);currentImageUrl=null;}renderTheme(color.value);});
  document.querySelector('#resetColor').addEventListener('click',()=>{if(currentImageUrl){URL.revokeObjectURL(currentImageUrl);currentImageUrl=null;}color.value='#6750a4';imageInput.value='';renderTheme('#6750a4');});
  imageInput.addEventListener('change',async()=>{
    const file=imageInput.files?.[0]; if(!file)return;
    if(!file.type.startsWith('image/')){latestStatusKey='Escolha um arquivo de imagem.'; status.textContent=translate(latestStatusKey,effectiveLanguage());return;}
    if(file.size>12*1024*1024){latestStatusKey='A imagem precisa ter menos de 12 MB.'; status.textContent=translate(latestStatusKey,effectiveLanguage());imageInput.value='';return;}
    try{const img=new Image();currentImageUrl=URL.createObjectURL(file);img.src=currentImageUrl;await img.decode();const theme=await themeFromImage(img);activeSeed=`#${(theme.source&0xffffff).toString(16).padStart(6,'0')}`;color.value=activeSeed;applyTheme(theme,{target:root,dark:root.dataset.theme==='dark',paletteTones:[0,10,20,30,40,50,60,70,80,90,95,99,100]});latestStatusKey='Tema criado a partir da imagem local. A imagem não é enviada.'; status.textContent=translate(latestStatusKey,effectiveLanguage());}catch(error){console.error(error);latestStatusKey='Não foi possível analisar esta imagem. Tente outra.'; status.textContent=translate(latestStatusKey,effectiveLanguage());}
  });
  document.querySelectorAll('.chip').forEach(chip=>chip.addEventListener('click',()=>{document.querySelectorAll('.chip').forEach(c=>{c.classList.remove('selected');c.setAttribute('aria-pressed','false')});chip.classList.add('selected');chip.setAttribute('aria-pressed','true')}));
  document.querySelectorAll('[data-message]').forEach(button=>button.addEventListener('click',()=>{toast.textContent=translate(button.dataset.sourceMessage || button.dataset.message,effectiveLanguage());toast.classList.add('show');clearTimeout(timer);timer=setTimeout(()=>toast.classList.remove('show'),2400)}));
  document.querySelector('#shapeToggle').addEventListener('click',e=>{const button=e.currentTarget;button.classList.toggle('morphed');button.setAttribute('aria-pressed',button.classList.contains('morphed')?'true':'false')});
  matchMedia('(prefers-color-scheme: dark)').addEventListener?.('change',e=>{if(!store.get('m3-demo-theme')){root.dataset.theme=e.matches?'dark':'light';updateTheme()}});
  window.addEventListener('languagechange', () => { if (language === 'auto') applyLanguage(); });
})();
