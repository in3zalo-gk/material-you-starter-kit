export function initGallery({ t }) {
  const $ = (selector) => document.querySelector(selector);
  const $$ = (selector) => [...document.querySelectorAll(selector)];
  const gallery = $('#galeria');
  if (!gallery) return;

  const live = $('#galleryStatus');
  const preview = $('#pagePreview');
  const duration = $('#motionDuration');
  const durationValue = $('#motionDurationValue');
  const elevation = $('#elevation');
  const elevationValue = $('#elevationValue');
  const reduceMotion = matchMedia('(prefers-reduced-motion: reduce)');
  let currentPage = 0;
  let currentMotion = 'fade';
  let frame;

  const announce = (key) => { if (live) live.textContent = t(key); };
  const applyDuration = () => {
    const value = Number(duration.value);
    gallery.style.setProperty('--gallery-duration', `${value}ms`);
    durationValue.textContent = `${value} ms`;
  };
  const applyElevation = () => {
    const value = elevation.value;
    gallery.style.setProperty('--gallery-elevation', value);
    elevationValue.textContent = value;
    const stageValue = $('#elevationStageValue'); if (stageValue) stageValue.textContent = value;
  };
  const movePill = (button, immediate = false) => {
    const group = button?.closest('.connected-group');
    const pill = group?.querySelector('.connected-pill');
    if (!group || !pill || !button) return;
    const groupBox = group.getBoundingClientRect();
    const buttonBox = button.getBoundingClientRect();
    pill.classList.toggle('is-instant', immediate || reduceMotion.matches);
    pill.style.width = `${buttonBox.width}px`;
    pill.style.transform = `translateX(${buttonBox.left - groupBox.left}px)`;
  };
  const queuePill = (immediate = false) => {
    cancelAnimationFrame(frame);
    frame = requestAnimationFrame(() => movePill($('.preview-nav button[aria-pressed="true"]'), immediate));
  };
  const selectPage = (next, replay = false) => {
    const pages = $$('.mini-page');
    currentPage = (next + pages.length) % pages.length;
    pages.forEach((page, index) => {
      const selected = index === currentPage;
      page.hidden = !selected;
      page.classList.remove('is-entering');
      if (selected && !reduceMotion.matches) {
        void page.offsetWidth;
        page.classList.add('is-entering');
      }
    });
    $$('.preview-nav button').forEach((button, index) => {
      const selected = index === currentPage;
      button.setAttribute('aria-pressed', String(selected));
      button.classList.toggle('is-selected', selected);
      if (selected) movePill(button);
    });
    pages[currentPage]?.addEventListener('animationend', (event) => {
      if (event.target === pages[currentPage]) event.target.classList.remove('is-entering');
    }, { once: true });
    announce(replay ? 'galleryReplayed' : 'galleryPageChanged');
  };
  const addRipple = (event) => {
    if (event.button !== 0 && event.pointerType !== 'touch') return;
    const button = event.currentTarget;
    const bounds = button.getBoundingClientRect();
    const ripple = document.createElement('span');
    ripple.className = 'pointer-ripple';
    const size = Math.max(bounds.width, bounds.height) * 1.8;
    ripple.style.width = ripple.style.height = `${size}px`;
    ripple.style.left = `${event.clientX - bounds.left - size / 2}px`;
    ripple.style.top = `${event.clientY - bounds.top - size / 2}px`;
    button.append(ripple);
    ripple.addEventListener('animationend', () => ripple.remove());
  };

  $$('.preview-nav button').forEach((button, index) => {
    button.addEventListener('click', () => selectPage(index));
    button.addEventListener('pointerdown', addRipple);
  });
  $$('.motion-choice').forEach((button) => {
    button.addEventListener('click', () => {
      currentMotion = button.dataset.motion;
      gallery.dataset.motion = currentMotion;
      $$('.motion-choice').forEach((item) => item.setAttribute('aria-pressed', String(item === button)));
      announce('galleryMotionChanged');
    });
    button.addEventListener('pointerdown', addRipple);
  });
  $('#replayMotion')?.addEventListener('click', () => {
    preview?.classList.remove('replay');
    void preview?.offsetWidth;
    if (!reduceMotion.matches) preview?.classList.add('replay');
    selectPage(currentPage, true);
  });
  preview?.addEventListener('animationend', () => preview.classList.remove('replay'));
  duration?.addEventListener('input', applyDuration);
  elevation?.addEventListener('input', applyElevation);

  applyDuration();
  applyElevation();
  queuePill(true);
  new ResizeObserver(() => queuePill(true)).observe($('.connected-group'));
  document.fonts?.ready.then(() => queuePill(true));
  addEventListener('resize', () => queuePill(true));
  reduceMotion.addEventListener?.('change', () => queuePill(true));
}
