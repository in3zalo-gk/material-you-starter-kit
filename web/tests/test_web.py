#!/usr/bin/env python3
"""Browser smoke tests for the Vite demo. Run after `npm run build` or `npm run dev`."""
from __future__ import annotations

import contextlib
import http.server
import os
import socketserver
import threading
import time
import unittest
from pathlib import Path

from playwright.sync_api import sync_playwright

WEB_ROOT = Path(__file__).resolve().parents[1]
ROOT = WEB_ROOT / 'dist'
PORT = 4173

class QuietHandler(http.server.SimpleHTTPRequestHandler):
    def log_message(self, *_args):
        pass

class ReusableTCPServer(socketserver.TCPServer):
    allow_reuse_address = True

class WebDemoTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        os.chdir(ROOT)
        cls.server = ReusableTCPServer(('127.0.0.1', 0), QuietHandler)
        cls.port = cls.server.server_address[1]
        cls.thread = threading.Thread(target=cls.server.serve_forever, daemon=True)
        cls.thread.start()
        time.sleep(.1)
        cls.playwright = sync_playwright().start()
        cls.browser = cls.playwright.chromium.launch()

    @classmethod
    def tearDownClass(cls):
        cls.browser.close()
        cls.playwright.stop()
        cls.server.shutdown()
        cls.server.server_close()

    def setUp(self):
        self.context = self.browser.new_context(viewport={'width': 1280, 'height': 900})
        self.page = self.context.new_page()
        self.page.goto(f'http://127.0.0.1:{self.port}/index.html')
        self.page.wait_for_selector('#seedValue')

    def tearDown(self):
        self.context.close()

    def test_theme_and_seed_are_persisted(self):
        self.page.locator('#themeToggle').click()
        self.page.locator('#seedColor').evaluate("el => { el.value = '#004f7a'; el.dispatchEvent(new Event('input', {bubbles:true})) }")
        self.assertEqual(self.page.locator('html').get_attribute('data-theme'), 'dark')
        self.assertEqual(self.page.locator('#seedValue').inner_text(), '#004F7A')
        self.assertEqual(self.page.evaluate("localStorage.getItem('m3-kit-theme')"), 'dark')
        self.assertEqual(self.page.evaluate("localStorage.getItem('m3-kit-seed')"), '#004f7a')

    def test_tabs_support_keyboard_and_dialog_validates(self):
        self.page.locator('#languageSelect').select_option('pt-BR')
        self.page.locator('#tab-controls').press('ArrowRight')
        self.assertEqual(self.page.locator('#tab-feedback').get_attribute('aria-selected'), 'true')
        self.page.locator('#tab-feedback').press('ArrowRight')
        self.page.locator('#openDialog').click()
        self.page.locator('#confirmDialog').click()
        self.assertIn('ao menos dois', self.page.locator('#draftError').inner_text())
        self.page.locator('#draftName').fill('Kit')
        self.page.locator('#confirmDialog').click()
        self.assertTrue('is-visible' in (self.page.locator('#toast').get_attribute('class') or ''))

    def test_mobile_has_no_overflow_or_script_errors(self):
        errors = []
        self.page.on('pageerror', lambda error: errors.append(str(error)))
        for width in [360, 390, 768, 1440]:
            self.page.set_viewport_size({'width': width, 'height': 850})
            self.page.reload()
            self.page.wait_for_selector('#seedValue')
            self.assertTrue(self.page.evaluate('document.documentElement.scrollWidth <= innerWidth'), f'overflow at {width}')
        self.assertEqual(errors, [])

    def test_language_switch_and_minimal_page(self):
        self.page.locator('#languageSelect').select_option('en')
        self.assertEqual(self.page.locator('html').get_attribute('lang'), 'en')
        self.assertEqual(self.page.locator('#tab-controls').inner_text(), 'Controls')
        self.page.goto(f'http://127.0.0.1:{self.port}/examples/minimal.html')
        self.assertIn('EXEMPLO MÍNIMO', self.page.locator('main').inner_text())

if __name__ == '__main__':
    unittest.main(verbosity=2)
