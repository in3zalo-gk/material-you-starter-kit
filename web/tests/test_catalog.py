#!/usr/bin/env python3
"""Behavior and coverage checks for the official Material Web showcase."""
import http.server
import os
import re
import socketserver
import threading
import unittest
from pathlib import Path
from playwright.sync_api import sync_playwright

WEB = Path(__file__).resolve().parents[1]

class QuietHandler(http.server.SimpleHTTPRequestHandler):
    def log_message(self, *_): pass

class CatalogTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        os.chdir(WEB / 'dist')
        cls.server = socketserver.TCPServer(('127.0.0.1', 0), QuietHandler)
        threading.Thread(target=cls.server.serve_forever, daemon=True).start()
        cls.pw = sync_playwright().start()
        cls.browser = cls.pw.chromium.launch()
        cls.base = f'http://127.0.0.1:{cls.server.server_address[1]}'
    @classmethod
    def tearDownClass(cls):
        cls.browser.close(); cls.pw.stop(); cls.server.shutdown(); cls.server.server_close()
    def setUp(self):
        self.context = self.browser.new_context(viewport={'width': 1280, 'height': 900})
        self.page = self.context.new_page(); self.errors = []
        self.page.on('pageerror', lambda error: self.errors.append(str(error)))
        self.page.goto(self.base + '/catalog.html')
        self.page.wait_for_function("customElements.get('md-filled-button') !== undefined")
    def tearDown(self):
        self.assertEqual(self.errors, []); self.context.close()
    def test_stable_export_coverage_and_registration(self):
        expected = set(re.findall(r"import './([^']+)';", (WEB/'node_modules/@material/web/all.js').read_text()))
        actual = set(re.findall(r"import '@material/web/([^']+)';", (WEB/'catalog.js').read_text()))
        self.assertEqual(expected, actual); self.assertEqual(len(actual), 43)
        missing = self.page.evaluate("[...new Set([...document.querySelectorAll('*')].map(el=>el.localName).filter(n=>n.startsWith('md-')))].filter(n=>!customElements.get(n))")
        self.assertEqual(missing, [])
    def test_search_and_theme(self):
        self.page.locator('#catalogSearch').fill('diálogo')
        self.assertEqual(self.page.locator('[data-search]:visible').count(), 1)
        self.page.locator('#catalogSearch').fill('zzzzzz')
        self.assertTrue(self.page.locator('#catalogEmpty').is_visible())
        self.page.locator('#catalogSearch').fill('')
        before = self.page.locator('html').get_attribute('data-theme')
        self.page.locator('#catalogTheme').click()
        self.assertNotEqual(before, self.page.locator('html').get_attribute('data-theme'))
        self.page.locator('#catalogSeed').evaluate("el=>{el.value='#004f7a';el.dispatchEvent(new Event('input'))}")
        self.assertTrue(self.page.evaluate("document.documentElement.style.getPropertyValue('--md-sys-color-primary').length > 0"))
    def test_menu_dialog_tabs_and_controls(self):
        self.page.locator('#menuAnchor').click()
        self.page.wait_for_function("document.querySelector('#catalogMenu').open")
        self.page.keyboard.press('Escape')
        self.page.locator('#showCatalogDialog').click()
        self.page.wait_for_function("document.querySelector('#catalogDialog').open")
        self.page.locator('#confirmCatalogDialog').click()
        self.page.wait_for_function("!document.querySelector('#catalogDialog').open")
        self.assertIn('Nada foi criado', self.page.locator('#catalogStatus').inner_text())
        self.page.locator('#catalogTabDetails').click()
        self.assertIn('Detalhes:', self.page.locator('#catalogTabPanel').inner_text())
        switch = self.page.locator('md-switch'); switch.click()
        self.assertFalse(switch.evaluate('el=>el.selected'))
        self.page.locator('md-outlined-text-field').first.locator('input').fill('teste@exemplo.com')
        self.assertEqual(self.page.locator('md-outlined-text-field').first.evaluate('el=>el.value'), 'teste@exemplo.com')
    def test_mobile_overflow_and_local_example(self):
        for width in (360,390,768,1440):
            self.page.set_viewport_size({'width':width,'height':900})
            self.assertLessEqual(self.page.evaluate('document.documentElement.scrollWidth'),width, f'overflow {width}')
        self.page.goto(self.base+'/examples/material-web.html')
        self.page.locator('#projectName').locator('input').fill('Minha marca')
        self.page.locator('#createProject').click()
        self.assertIn('Minha marca',self.page.locator('#exampleStatus').inner_text())
        self.assertEqual(self.page.request.get(self.base+'/THIRD_PARTY_NOTICES.md').status,200)

if __name__ == '__main__': unittest.main(verbosity=2)
