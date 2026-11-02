#!/usr/bin/env python3
"""Deterministic browser checks for the compiled connected-selection gallery."""
from __future__ import annotations
import http.server
import os
import socketserver
import threading
import unittest
from pathlib import Path
from playwright.sync_api import sync_playwright

ROOT = Path(__file__).resolve().parents[1] / 'dist'
class Quiet(http.server.SimpleHTTPRequestHandler):
    def log_message(self, *_): pass
class Server(socketserver.TCPServer): allow_reuse_address = True

class GalleryTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        os.chdir(ROOT); cls.server = Server(('127.0.0.1', 0), Quiet)
        cls.thread = threading.Thread(target=cls.server.serve_forever, daemon=True); cls.thread.start()
        cls.pw = sync_playwright().start(); cls.browser = cls.pw.chromium.launch()
        cls.url = f'http://127.0.0.1:{cls.server.server_address[1]}/index.html'
    @classmethod
    def tearDownClass(cls):
        cls.browser.close(); cls.pw.stop(); cls.server.shutdown(); cls.server.server_close()
    def page(self, **context_args):
        context_args.setdefault('viewport', {'width': 768, 'height': 900})
        context = self.browser.new_context(**context_args)
        page = context.new_page(); errors = []
        page.on('pageerror', lambda error: errors.append(str(error)))
        page.goto(self.url); page.locator('#galeria').scroll_into_view_if_needed()
        return context, page, errors
    def pill_box(self, page):
        return page.locator('.connected-pill').bounding_box()
    def test_pill_matches_selection_and_has_intermediate_motion(self):
        context, page, errors = self.page()
        page.locator('#motionDuration').fill('700')
        buttons = page.locator('.preview-nav button')
        initial = self.pill_box(page); target = buttons.nth(2).bounding_box()
        self.assertAlmostEqual(initial['width'], buttons.nth(0).bounding_box()['width'], delta=1)
        buttons.nth(2).click()
        page.wait_for_timeout(140)
        middle = self.pill_box(page)
        self.assertGreater(middle['x'], initial['x'] + 1)
        self.assertLess(middle['x'], target['x'] - 1)
        page.wait_for_timeout(760)
        final = self.pill_box(page)
        self.assertAlmostEqual(final['x'], target['x'], delta=1)
        self.assertAlmostEqual(final['width'], target['width'], delta=1)
        self.assertEqual(buttons.nth(2).get_attribute('aria-pressed'), 'true')
        self.assertEqual(buttons.nth(0).get_attribute('aria-pressed'), 'false')
        self.assertFalse(page.locator('.mini-page').nth(2).is_hidden())
        self.assertEqual(errors, []); context.close()
    def test_keyboard_rapid_clicks_and_pointer_origin_ripple(self):
        context, page, errors = self.page()
        buttons = page.locator('.preview-nav button')
        buttons.nth(1).focus(); page.keyboard.press('Enter')
        self.assertEqual(buttons.nth(1).get_attribute('aria-pressed'), 'true')
        buttons.nth(2).click(position={'x': 4, 'y': 5})
        self.assertTrue(page.locator('.preview-nav button').nth(2).locator('.pointer-ripple').count() >= 1)
        ripple = page.locator('.preview-nav button').nth(2).locator('.pointer-ripple').bounding_box()
        self.assertLess(ripple['x'], buttons.nth(2).bounding_box()['x'] + 12)
        buttons.nth(0).click(); buttons.nth(1).click(); buttons.nth(2).click()
        page.wait_for_timeout(350)
        self.assertEqual(buttons.nth(2).get_attribute('aria-pressed'), 'true')
        self.assertEqual(errors, []); context.close()
    def test_translation_resize_reduced_motion_and_mobile_overflow(self):
        context, page, errors = self.page(reduced_motion='reduce')
        page.locator('#languageSelect').select_option('en')
        self.assertEqual(page.locator('.preview-nav button').first.inner_text(), 'Dashboard')
        page.locator('#languageSelect').select_option('es')
        self.assertEqual(page.locator('.preview-nav button').first.inner_text(), 'Panel')
        before = self.pill_box(page)
        page.locator('.preview-nav button').nth(1).click(); page.wait_for_timeout(20)
        after = self.pill_box(page); expected = page.locator('.preview-nav button').nth(1).bounding_box()
        self.assertNotEqual(before['x'], after['x'])
        self.assertAlmostEqual(after['x'], expected['x'], delta=1)
        self.assertEqual(page.locator('.mini-page:not([hidden])').evaluate("el => el.getAnimations().filter(a => a.playState === 'running').length"), 0)
        context.close()
        for width in (360, 390, 768, 1440):
            context, page, errors = self.page(viewport={'width': width, 'height': 900})
            page.locator('#languageSelect').select_option('en')
            self.assertLessEqual(page.evaluate('document.documentElement.scrollWidth'), width, width)
            page.set_viewport_size({'width': width + 80, 'height': 900}); page.wait_for_timeout(40)
            pill = self.pill_box(page); selected = page.locator('.preview-nav button[aria-pressed="true"]').bounding_box()
            self.assertAlmostEqual(pill['x'], selected['x'], delta=1)
            self.assertAlmostEqual(pill['width'], selected['width'], delta=1)
            self.assertEqual(errors, []); context.close()
if __name__ == '__main__': unittest.main(verbosity=2)
