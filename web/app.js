(() => {
  const root = document.documentElement;
  const toggle = document.querySelector('#themeToggle');
  const icon = document.querySelector('#themeIcon');
  const color = document.querySelector('#seedColor');
  const toast = document.querySelector('#toast');
  const variables = ['primary','on-primary','primary-container','on-primary-container','secondary-container','on-secondary-container'];
  let timer;
  let changed = false;
  const store = { get(key) { try { return localStorage.getItem(key); } catch { return null; } }, set(key,value) { try { localStorage.setItem(key,value); } catch {} } };
  const saved = store.get('m3-demo-theme');
  root.dataset.theme = saved === 'dark' || saved === 'light' ? saved : (matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
  const updateTheme = () => { const dark = root.dataset.theme === 'dark'; icon.textContent = dark ? '☼' : '☾'; toggle.setAttribute('aria-label', dark ? 'Ativar tema claro' : 'Ativar tema escuro'); if(changed) applyColor(); };
  function applyColor() {
    const hex = color.value.slice(1);
    const rgb = [0,2,4].map(i => parseInt(hex.slice(i,i+2),16)/255);
    const [r,g,b] = rgb; const max=Math.max(...rgb),min=Math.min(...rgb),d=max-min; let h=0,s=0;
    const v=(max+min)/2;
    if(d){s=d/(1-Math.abs(2*v-1)); if(max===r)h=((g-b)/d)%6; else if(max===g)h=(b-r)/d+2;else h=(r-g)/d+4; h=(Math.round(h*60)+360)%360;}
    s=Math.round(s*100); const dark=root.dataset.theme==='dark';
    const tint=(lightness,saturation=Math.min(80,Math.max(25,s)))=>`hsl(${h} ${saturation}% ${lightness}%)`;
    const linear=x=>x<=.04045?x/12.92:((x+.055)/1.055)**2.4;
    const lum=.2126*linear(r)+.7152*linear(g)+.0722*linear(b);
    const foreground = (lum+.05)/.05 >= 1.05/(lum+.05) ? '#000000' : '#ffffff';
    const pairs = {
      'primary': dark ? tint(82) : color.value,
      'on-primary': dark ? tint(15) : foreground,
      'primary-container': dark ? tint(30) : tint(91),
      'on-primary-container': dark ? tint(94) : tint(16),
      'secondary-container': dark ? tint(28,30) : tint(91,30),
      'on-secondary-container': dark ? tint(94,30) : tint(16,30)
    };
    Object.entries(pairs).forEach(([name,value])=>root.style.setProperty(`--md-sys-color-${name}`,value));
  }
  updateTheme();
  toggle.addEventListener('click',()=>{root.dataset.theme=root.dataset.theme==='dark'?'light':'dark';store.set('m3-demo-theme',root.dataset.theme);updateTheme();});
  color.addEventListener('input',()=>{changed=true;applyColor();});
  document.querySelector('#resetColor').addEventListener('click',()=>{changed=false;color.value='#6750a4';variables.forEach(v=>root.style.removeProperty(`--md-sys-color-${v}`));});
  document.querySelectorAll('.chip').forEach(chip=>chip.addEventListener('click',()=>{document.querySelectorAll('.chip').forEach(c=>{c.classList.remove('selected');c.setAttribute('aria-pressed','false');});chip.classList.add('selected');chip.setAttribute('aria-pressed','true');}));
  document.querySelectorAll('[data-message]').forEach(button=>button.addEventListener('click',()=>{toast.textContent=button.dataset.message;toast.classList.add('show');clearTimeout(timer);timer=setTimeout(()=>toast.classList.remove('show'),2400);}));
})();
