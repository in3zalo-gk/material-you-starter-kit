"""Capture real m3 kit browser components for the README.
Requires: pip install playwright pillow; python -m playwright install chromium
Run from the repository root: python scripts/capture_readme.py
The hero rearranges existing catalog panels for a documentation composition.
It does not implement substitute controls or simulate Android screenshots.
"""
from pathlib import Path
from io import BytesIO
import time
from PIL import Image, ImageDraw, ImageFont
from playwright.sync_api import sync_playwright

BASE = 'https://in3zalo-gk.github.io/material-you-starter-kit/'
OUT = Path(__file__).resolve().parents[1] / 'assets'
OUT.mkdir(exist_ok=True)
FONT = '/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf'
def font(n): return ImageFont.truetype(FONT, n)
def shot(page): return Image.open(BytesIO(page.screenshot())).convert('RGB')
def seed(page, selector, color):
    page.locator(selector).evaluate('(e,v)=>{e.value=v;e.dispatchEvent(new Event("input",{bubbles:true}));}', color)
def text(draw, xy, value, size, color): draw.text(xy, value, font=font(size), fill=color)

with sync_playwright() as p:
    browser = p.chromium.launch(headless=True)
    ctx = browser.new_context(locale='pt-BR', color_scheme='dark', reduced_motion='no-preference')
    catalog = ctx.new_page()
    catalog.set_viewport_size({'width':540,'height':500})
    catalog.goto(BASE+'catalog.html', wait_until='networkidle')
    catalog.wait_for_function('!!document.querySelector("md-filled-button").shadowRoot')
    catalog.evaluate('''() => {
      window.captureSeed=document.querySelector('#catalogSeed');window.captureToggle=document.querySelector('#catalogTheme');
      const grid=document.querySelector('.catalog-grid');
      const keys=['botoes buttons','icon icons icones fab','checkbox switch radio selecao','progress loading progresso'];
      const chosen=keys.map(k=>document.querySelector('[data-search="'+k+'"]'));
      grid.replaceChildren(...chosen);document.body.replaceChildren(grid);
    }''')
    catalog.add_style_tag(content='''html,body{margin:0!important;padding:0!important;overflow:hidden;background:var(--md-sys-color-surface)!important}.catalog-grid{display:grid;grid-template-columns:1fr 1fr;gap:14px;height:500px;padding:0}.catalog-grid section{height:243px;box-sizing:border-box;padding:20px 16px;border-radius:24px;background:var(--md-sys-color-surface-container)}.catalog-grid h2{font-size:17px;margin:0 0 18px}.catalog-grid p{display:none}.samples{gap:9px;margin:12px 0}.samples label{font-size:12px;gap:4px}md-fab,md-branded-fab{margin-top:4px}md-linear-progress{margin:30px 0}md-circular-progress{width:42px;height:42px}md-filled-button,md-filled-tonal-button,md-elevated-button,md-outlined-button,md-text-button{--md-filled-button-container-height:36px;--md-filled-tonal-button-container-height:36px;--md-outlined-button-container-height:36px;--md-elevated-button-container-height:36px;--md-text-button-container-height:36px;font-size:12px}''')
    gallery = ctx.new_page()
    gallery.set_viewport_size({'width':344,'height':500})
    gallery.goto(BASE,wait_until='networkidle')
    gallery.select_option('#languageSelect','pt-BR')
    gallery.evaluate('''()=>{window.captureSeed=document.querySelector('#seedColor');window.captureToggle=document.querySelector('#themeToggle');const preview=document.querySelector('#pagePreview');const parent=document.querySelector('#galeria');parent.replaceChildren(preview);document.querySelector('main').replaceChildren(parent);document.querySelectorAll('header,footer,.skip-link').forEach(e=>e.remove());}''')
    gallery.add_style_tag(content='''html,body{padding:0!important;margin:0!important;overflow:hidden!important}body .shell,body .site-shell,main{width:100%!important;margin:0!important;padding:0!important}.section.gallery{padding:0!important;margin:0!important;width:344px!important}.gallery-preview{height:500px;box-sizing:border-box;border-radius:24px}.mini-pages{padding:14px}.mini-page{box-sizing:border-box;min-height:393px;padding:20px}.mini-page h3{font-size:27px}.shared-rail{padding:14px}.preview-nav button{padding:0 12px}.mini-page .metric{margin-top:20px}.mini-page .mini-chart{height:80px}''')
    # Real handlers and web-component animations stay attached to the original nodes.
    errors=[]
    catalog.on('pageerror',lambda e:errors.append(str(e)))
    gallery.on('pageerror',lambda e:errors.append(str(e)))
    nav=gallery.locator('.preview-nav button')
    frames=[]
    for i in range(96):
        if i in (0,32,64):
            col=['#2f6b46','#6750a4','#136b89'][i//32]
            for page in [catalog,gallery]:
                page.evaluate('(v)=>{window.captureSeed.value=v;window.captureSeed.dispatchEvent(new Event("input",{bubbles:true}));}',col)
            if i==32:
                catalog.evaluate('window.captureToggle.click()')
                gallery.evaluate('window.captureToggle.click()')
            if i==64:
                catalog.evaluate('window.captureToggle.click()')
                gallery.evaluate('window.captureToggle.click()')
        if i in (8,40,72):
            catalog.locator('md-filled-icon-button').click()
            catalog.locator('md-switch').click()
        if i in (16,48,80):
            nav.nth(1).click()
        if i in (24,56,88):
            nav.nth(2).click()
        if i in (0,32,64):
            nav.nth(0).click()
        canvas=Image.new('RGB',(960,690),'#111613')
        d=ImageDraw.Draw(canvas)
        text(d,(26,24),'m3 kit',34,'#e3eee4')
        text(d,(27,72),'COR, ESTADO E MOVIMENTO.',15,'#a7cdb1')
        text(d,(672,40),'WEB / CAPTURA REAL',12,'#a3b4a6')
        canvas.paste(shot(catalog),(24,120))
        canvas.paste(shot(gallery),(592,120))
        text(d,(26,648),'Material Web + seleção conectada do kit',13,'#b8c6bb')
        text(d,(683,648),'Dados demonstrativos',12,'#8fa695')
        frames.append(canvas)
        time.sleep(1/12)
    frames[0].save(OUT/'showcase-animated.gif',save_all=True,append_images=frames[1:],duration=84,loop=0,optimize=True)
    frames[4].save(OUT/'showcase-poster.png',optimize=True)
    ctx.close()

    # Three actual responsive browser views, not Android device captures.
    mobile=browser.new_context(viewport={'width':390,'height':760},device_scale_factor=1,locale='pt-BR',color_scheme='dark',reduced_motion='reduce',is_mobile=True,has_touch=True)
    views=[]
    for target in ['laboratorio','catalog','galeria']:
        page=mobile.new_page()
        page.goto(BASE+('catalog.html' if target=='catalog' else ''),wait_until='networkidle')
        if target!='catalog':
            page.select_option('#languageSelect','pt-BR')
            if target=='laboratorio':
                page.locator('#themeToggle').click()
                seed(page,'#seedColor','#6750a4')
            page.locator('#'+target).evaluate('(e)=>e.scrollIntoView({block:"start"})')
        else:
            seed(page,'#catalogSeed','#136b89')
            page.locator('.catalog-grid').evaluate('(e)=>e.scrollIntoView({block:"start"})')
        page.wait_for_timeout(400)
        views.append(shot(page))
        page.close()
    board=Image.new('RGB',(1280,970),'#111613');d=ImageDraw.Draw(board)
    text(d,(28,24),'Uma base. Diferentes contextos.',32,'#e3eee4')
    text(d,(29,73),'Capturas reais na web · layout mobile · dados fictícios',16,'#a7cdb1')
    for i,(im,label) in enumerate(zip(views,['01 / Laboratório de temas','02 / Catálogo de componentes','03 / Galeria de interação'])):
        x=27+i*417
        text(d,(x,124),label,17,'#d5e7d9')
        mask=Image.new('L',im.size,0);ImageDraw.Draw(mask).rounded_rectangle((0,0,389,759),radius=22,fill=255)
        board.paste(im,(x,170),mask)
    board.save(OUT/'screenshots-web.png',optimize=True)
    browser.close()
    assert not errors, errors
    gif=Image.open(OUT/'showcase-animated.gif')
    assert gif.n_frames>1
    print('Verified:',gif.n_frames,'GIF frames;',gif.size)
    for f in ['showcase-animated.gif','showcase-poster.png','screenshots-web.png']:
        print(f, (OUT/f).stat().st_size,'bytes')
