"""Create redistributable 1.0.1 packages. Run after web and Android builds."""
from pathlib import Path
import hashlib
import shutil
import zipfile

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'release-assets'
OUT.mkdir(exist_ok=True)
VERSION = '1.0.1'
EXCLUDED = {'.git', 'node_modules', 'build', '.gradle', '__pycache__', 'dist', 'release-assets'}

def source_files(folder):
    return [p for p in (ROOT / folder).rglob('*') if p.is_file() and not any(x in EXCLUDED for x in p.relative_to(ROOT).parts) and p.name not in {'local.properties', '.DS_Store'} and not p.name.startswith('.env')]

def archive(name, files):
    with zipfile.ZipFile(OUT / name, 'w', zipfile.ZIP_DEFLATED) as z:
        for p in files:
            z.write(p, str(p.relative_to(ROOT)))

license = ROOT / 'LICENSE'
archive(f'm3-kit-{VERSION}-opensource-complete.zip', [license, ROOT/'README.md', ROOT/'CHANGELOG.md'] + sum([source_files(f) for f in ['web', 'android', 'docs', 'assets', 'scripts', '.github']], []))
archive(f'm3-kit-{VERSION}-web-source.zip', [license, ROOT/'README.md'] + source_files('web'))
archive(f'm3-kit-{VERSION}-android-source.zip', [license, ROOT/'android/README.md'] + source_files('android'))
archive(f'm3-kit-{VERSION}-web-example.zip', [license, ROOT/'docs/EXAMPLES.md'] + source_files('web'))
archive(f'm3-kit-{VERSION}-android-example.zip', [license, ROOT/'android/README.md', ROOT/'android/settings.gradle.kts', ROOT/'android/build.gradle.kts', ROOT/'android/gradle.properties'] + source_files('android/gradle') + source_files('android/example') + source_files('android/app'))
# The example source includes app as settings references both modules; both builds work unchanged.
with zipfile.ZipFile(OUT/f'm3-kit-{VERSION}-web-ready.zip', 'w', zipfile.ZIP_DEFLATED) as z:
    for p in (ROOT/'web/dist').rglob('*'):
        if p.is_file(): z.write(p, str(p.relative_to(ROOT/'web/dist')))
    z.write(license, 'LICENSE')
for module, label in [('app','complete'),('example','example')]:
    apk = ROOT/f'android/{module}/build/outputs/apk/debug/{module}-debug.apk'
    if not apk.exists(): raise SystemExit(f'Missing built APK: {apk}')
    shutil.copy2(apk, OUT/f'm3-kit-{VERSION}-android-{label}-debug.apk')
files = sorted(p for p in OUT.iterdir() if p.is_file() and p.name != 'SHA256SUMS.txt')
(OUT/'SHA256SUMS.txt').write_text(''.join(f'{hashlib.sha256(p.read_bytes()).hexdigest()}  {p.name}\n' for p in files))
print('\n'.join(p.name for p in OUT.iterdir()))
