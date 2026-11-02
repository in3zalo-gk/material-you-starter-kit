import { detectLanguage, translate } from './i18n.js';
import { argbFromHex, themeFromSourceColor, applyTheme, themeFromImage } from '@material/material-color-utilities';

const DEFAULT_SEED = '#2f6b46';
const PALETTE_TONES = [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 100];

(() => {
  const root = document.documentElement;
  const $ = (selector) => document.querySelector(selector);
  const $$ = (selector) => [...document.querySelectorAll(selector)];
  const storage = {
    get(key) { try { return localStorage.getItem(key); } catch { return null; } },
    set(key, value) { try { localStorage.setItem(key, value); } catch { /* storage can be unavailable */ } },
    remove(key) { try { localStorage.removeItem(key); } catch { /* storage can be unavailable */ } }
  };

  const toggle = $('#themeToggle');
  const seedInput = $('#seedColor');
  const seedValue = $('#seedValue');
  const imageInput = $('#wallpaperInput');
  const fileName = $('#fileName');
  const status = $('#paletteStatus');
  const toast = $('#toast');
  const languageSelect = $('#languageSelect');
  let toastTimer;
  let imageUrl;
  let currentTheme;
  let activeSeed = storage.get('m3-kit-seed') || DEFAULT_SEED;
  let language = storage.get('m3-kit-language') || 'auto';
  const savedTheme = storage.get('m3-kit-theme');
  root.dataset.theme = savedTheme || (matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');

  const currentLanguage = () => language === 'auto' ? detectLanguage() : language;
  const t = (key) => translate(key, currentLanguage());
  const setStatus = (key) => { if (status) status.textContent = t(key); };
  const showToast = (key) => {
    toast.textContent = t(key);
    toast.classList.add('is-visible');
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => toast.classList.remove('is-visible'), 2800);
  };

  function applyLanguage() {
    root.lang = currentLanguage();
    document.title = t('pageTitle');
    $('meta[name="description"]')?.setAttribute('content', t('description'));
    $$('[data-i18n]').forEach((element) => { element.textContent = t(element.dataset.i18n); });
    $$('[data-i18n-aria]').forEach((element) => { element.setAttribute('aria-label', t(element.dataset.i18nAria)); });
    languageSelect.value = language;
    updateThemeButton();
    setStatus('paletteReady');
  }

  function hexFromArgb(argb) { return `#${(argb & 0xffffff).toString(16).padStart(6, '0')}`; }
  function updateThemeButton() {
    const dark = root.dataset.theme === 'dark';
    toggle?.setAttribute('aria-label', t(dark ? 'enableLight' : 'enableDark'));
    const icon = $('#themeIcon');
    if (icon) icon.setAttribute('d', dark ? 'M12 3v2m0 14v2M3 12h2m14 0h2M5.6 5.6 7 7m10 10 1.4 1.4M18.4 5.6 17 7M7 17l-1.4 1.4M12 8a4 4 0 1 1 0 8 4 4 0 0 1 0-8Z' : 'M12 3a9 9 0 1 0 9 9 7 7 0 0 1-9-9Z');
  }
  function updateSeedLabel(seed) { seedValue.textContent = seed.toUpperCase(); }
  function updateMetaTheme() {
    const primary = getComputedStyle(root).getPropertyValue('--md-sys-color-primary').trim();
    $('meta[name="theme-color"]')?.setAttribute('content', primary || activeSeed);
  }
  function applyMaterialTheme(theme) {
    currentTheme = theme;
    applyTheme(theme, { target: root, dark: root.dataset.theme === 'dark', paletteTones: PALETTE_TONES });
    root.style.colorScheme = root.dataset.theme;
    updateMetaTheme();
  }
  function renderSeed(seed, announce = true) {
    try {
      activeSeed = seed;
      seedInput.value = seed;
      updateSeedLabel(seed);
      applyMaterialTheme(themeFromSourceColor(argbFromHex(seed)));
      storage.set('m3-kit-seed', seed);
      if (announce) setStatus('paletteReady');
    } catch (error) {
      console.error('Material color generation failed', error);
      setStatus('paletteFailed');
    }
  }
  function resetImageUrl() { if (imageUrl) URL.revokeObjectURL(imageUrl); imageUrl = undefined; }
  function copyTokens() {
    const values = ['primary', 'on-primary', 'primary-container', 'on-primary-container', 'surface', 'on-surface', 'secondary-container', 'on-secondary-container'];
    const css = `:root {\n${values.map((role) => `  --md-sys-color-${role}: ${getComputedStyle(root).getPropertyValue(`--md-sys-color-${role}`).trim()};`).join('\n')}\n}`;
    if (!navigator.clipboard?.writeText) { showToast('copyFailed'); return; }
    navigator.clipboard.writeText(css).then(() => showToast('tokensCopied')).catch(() => showToast('copyFailed'));
  }

  applyLanguage();
  renderSeed(activeSeed, false);
  languageSelect.addEventListener('change', () => { language = languageSelect.value; storage.set('m3-kit-language', language); applyLanguage(); });
  toggle.addEventListener('click', () => { root.dataset.theme = root.dataset.theme === 'dark' ? 'light' : 'dark'; storage.set('m3-kit-theme', root.dataset.theme); updateThemeButton(); currentTheme ? applyMaterialTheme(currentTheme) : renderSeed(activeSeed, false); });
  seedInput.addEventListener('input', () => { resetImageUrl(); imageInput.value = ''; fileName.textContent = t('imageHint'); renderSeed(seedInput.value); });
  $('#resetTheme').addEventListener('click', () => { resetImageUrl(); imageInput.value = ''; fileName.textContent = t('imageHint'); storage.remove('m3-kit-seed'); renderSeed(DEFAULT_SEED); setStatus('themeReset'); });
  $('#copyTokens').addEventListener('click', copyTokens);
  imageInput.addEventListener('change', async () => {
    const file = imageInput.files?.[0];
    if (!file) return;
    if (!file.type.startsWith('image/') || file.size > 12 * 1024 * 1024) { imageInput.value = ''; fileName.textContent = t('imageHint'); setStatus(file.size > 12 * 1024 * 1024 ? 'imageTooLarge' : 'imageInvalid'); return; }
    resetImageUrl(); imageUrl = URL.createObjectURL(file); fileName.textContent = file.name;
    try {
      const image = new Image(); image.src = imageUrl; await image.decode();
      const theme = await themeFromImage(image);
      activeSeed = hexFromArgb(theme.source); seedInput.value = activeSeed; updateSeedLabel(activeSeed); storage.set('m3-kit-seed', activeSeed); applyMaterialTheme(theme); setStatus('imageApplied');
    } catch (error) { console.error('Image palette extraction failed', error); setStatus('imageFailed'); }
  });

  const tabs = $$('[role="tab"]');
  function selectTab(tab) {
    tabs.forEach((item) => { const selected = item === tab; item.setAttribute('aria-selected', String(selected)); item.tabIndex = selected ? 0 : -1; $(`#panel-${item.dataset.tab}`).hidden = !selected; });
  }
  tabs.forEach((tab, index) => {
    tab.addEventListener('click', () => selectTab(tab));
    tab.addEventListener('keydown', (event) => { if (!['ArrowLeft', 'ArrowRight', 'Home', 'End'].includes(event.key)) return; event.preventDefault(); const next = event.key === 'Home' ? 0 : event.key === 'End' ? tabs.length - 1 : (index + (event.key === 'ArrowRight' ? 1 : -1) + tabs.length) % tabs.length; tabs[next].focus(); selectTab(tabs[next]); });
  });
  $('#intensity').addEventListener('input', (event) => { $('#intensityValue').textContent = `${event.target.value}%`; });
  $$('.chip').forEach((chip) => chip.addEventListener('click', () => { $$('.chip').forEach((item) => { item.classList.toggle('is-selected', item === chip); item.setAttribute('aria-pressed', String(item === chip)); }); }));
  $('#shapeToggle').addEventListener('click', (event) => { const button = event.currentTarget; button.classList.toggle('is-morphed'); button.setAttribute('aria-pressed', String(button.classList.contains('is-morphed'))); });
  $$('[data-message]').forEach((button) => button.addEventListener('click', () => showToast(button.dataset.message === 'Ação demonstrativa concluída.' ? 'actionDone' : button.dataset.message)));

  const dialog = $('#exampleDialog');
  const form = $('#dialogForm');
  const draftName = $('#draftName');
  $('#openDialog').addEventListener('click', () => { form.reset(); $('#draftError').textContent = ''; dialog.showModal(); draftName.focus(); });
  form.addEventListener('submit', (event) => { if (event.submitter?.value === 'cancel') return; if (!draftName.validity.valid) { event.preventDefault(); $('#draftError').textContent = t('nameError'); draftName.focus(); } });
  form.addEventListener('invalid', (event) => { if (event.target === draftName) $('#draftError').textContent = t('nameError'); }, true);
  draftName.addEventListener('input', () => { if (draftName.validity.valid) $('#draftError').textContent = ''; });
  dialog.addEventListener('close', () => { if (dialog.returnValue === 'default' && draftName.validity.valid) showToast('dialogConfirmed'); });
  matchMedia('(prefers-color-scheme: dark)').addEventListener?.('change', (event) => { if (!storage.get('m3-kit-theme')) { root.dataset.theme = event.matches ? 'dark' : 'light'; updateThemeButton(); currentTheme ? applyMaterialTheme(currentTheme) : renderSeed(activeSeed, false); } });
  window.addEventListener('languagechange', () => { if (language === 'auto') applyLanguage(); });
})();
