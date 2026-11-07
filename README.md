# 🐉 اژدهای پوچی — Void Dragon

**یک ماد کامل اژدها برای Minecraft 1.21.1 (NeoForge) — قابل رام کردن، قابل سوار شدن، با رشد و ارتقای مرحله‌ای.**

[![Build](https://github.com/aloArse/voiddragon/actions/workflows/build.yml/badge.svg)](https://github.com/aloArse/voiddragon/actions/workflows/build.yml)

---

## 🖼️ نمای اژدها

| روی زمین | در حال پرواز |
|---|---|
| ![اژدها](docs/dragon_showcase.png) | ![پرواز](docs/dragon_flying.png) |

| با زین و زره | از روبه‌رو |
|---|---|
| ![زره](docs/dragon_gear.png) | ![روبرو](docs/dragon_front.png) |

> این تصاویر با رندرر آفلاین خودِ پروژه (`tools/render_preview.py`) مستقیماً از همان مدل و تکسچری ساخته شده‌اند که در بازی استفاده می‌شود.

---

## 📥 ۱. دانلود فایل ماد (jar)

فایل نصبی ماد به‌صورت خودکار در GitHub Actions ساخته می‌شود:

1. این لینک را باز کن: **[آخرین ساخت‌های موفق](https://github.com/aloArse/voiddragon/actions/workflows/build.yml)**
2. روی اولین ساخت (سبز ✅) با نام شاخهٔ خودت بزن.
3. پایین صفحه، بخش **Artifacts** → روی **`voiddragon-mod`** بزن تا فایل zip دانلود شود.
4. zip را باز کن؛ داخلش فایل **`voiddragon-1.0.0.jar`** است. **همین فایل** را می‌خواهی.

> اگر فایل zip باز نشد، از اپ «Files by Google» یا ZArchiver استفاده کن.

---

## 📱 ۲. نصب روی گوشی (Poco X6 Pro)

برای اجرای مادهای NeoForge روی اندروید به یک لانچر جاوا مثل **PojavLauncher** یا **FCL** نیاز داری.

1. **PojavLauncher** را نصب کن (از F-Droid یا GitHub Releases).
2. داخل لانچر، Java 21 را انتخاب/نصب کن (برای 1.21.1 لازم است).
3. یک نسخهٔ **NeoForge 1.21.1** بساز:
   - در PojavLauncher: `Install jar` → از منوی نسخه‌ها، **1.21.1** را انتخاب کن → سپس از بخش **NeoForge** نسخهٔ **21.1.x** را نصب کن.
4. فایل `voiddragon-1.0.0.jar` را در این مسیر کپی کن:
   ```
   /storage/emulated/0/Android/data/net.kdt.pojavlaunch/files/.minecraft/mods/
   ```
   (اگر پوشهٔ `mods` نبود، خودت بسازش.)
5. در تنظیمات لانچر، حداقل **۲.۵ تا ۳ گیگ رم** به بازی بده (Poco X6 Pro راحت می‌کشد) و رندرر **Holy GL4ES** یا **Zink** را امتحان کن.
6. بازی را با پروفایل **NeoForge 1.21.1** اجرا کن. در منوی Mods باید «Void Dragon» را ببینی.

> ⚠️ ماد مخصوص **NeoForge** است؛ روی Forge یا Fabric کار نمی‌کند.

---

## 🎮 ۳. راهنمای بازی

### مرحله ۱ — تراشهٔ پوچی بساز
دو راه دارد:

| دستور ساخت (Crafting) | مواد |
|---|---|
| تراشهٔ پوچی ×۲ | echo shard + ender pearl + amethyst shard |
| تراشهٔ پوچی ×۱ | ender eye + obsidian |
| زین پوچی | ۴ چرم + ۴ تراشهٔ پوچی + پشم سیاه (وسط) |
| زرهٔ اژدها | ۴ تراشهٔ پوچی + ۴ obsidian + ۱ netherite ingot (وسط) |

راه سوم و هیجان‌انگیزتر: **تخم اژدها** (Dragon Egg) را با یک تراشهٔ پوچی **راست‌کلیک** کن → یک بچه‌اژدها بیرون می‌آید که از همان اول مال خودت است. 🐣

### مرحله ۲ — رام کردن
- تراشهٔ پوچی را در دست بگیر و روی اژدها **راست‌کلیک** کن.
- هر بار ۱ تراشه مصرف می‌شود؛ شانس رام شدن **۲۵٪** است (اگر اژدها زخمی باشد، **۵۰٪**).
- اژدها را می‌توانی با spawn egg هم پیدا کنی: **Void Dragon Spawn Egg** در تب خلاقیت «Void Dragon».

### مرحله ۳ — بزرگ کردن (Hatchling → Juvenile → Adult)
اژدها سه مرحله دارد و **فقط اژدهای بالغ** زین می‌شود و سوار می‌شود:

| کار | اثر |
|---|---|
| دادن تراشهٔ پوچی (راست‌کلیک) | +۴ امتیاز رشد، +۳ پیوند، ۴۰ جان درمان |
| کشتن دشمنان توسط اژدها | +۱ رشد، +۲ پیوند |
| سوار شدن/پرواز و همراهی | رشد آرام با گذشت زمان |

بچه‌اژدها کوچک و ضعیف است؛ با بزرگ شدن، جان، آسیب و زره‌اش زیاد می‌شود (۴۵ → ۱۲۰ → ۲۲۰ جان).

### مرحله ۴ — زین، زره، سوار شو
1. **زین پوچی** را روی اژدهای بالغ راست‌کلیک کن.
2. **زرهٔ اژدها** را روی همان اژدها راست‌کلیک کن (۱۰+ زره و ۲۵٪ کاهش آسیب).
3. دست خالی روی اژدها راست‌کلیک کن → سوار می‌شوی.
4. برای پیاده شدن: روی زمین دکمهٔ ** sneak (پنهان شدن)** را بزن.
5. برای درآوردن زین/زره: با دست خالی و در حالت sneak روی اژدها راست‌کلیک کن.

---

## ⌨️ ۴. کلیدهای توانایی (وقتی سوار اژدها هستی)

| کلید | توانایی | نیاز به پیوند | توضیح |
|---|---|---|---|
| **R** (نگه‌داشتن) | نفس پوچی | ۰ | پرتو سایه که دشمن را می‌سوزاند، کور می‌کند و برگ/گیاه را می‌پوساند. انرژی مصرف می‌کند. |
| **G** | حملهٔ پوچی | ۲۵ | یورش شاخ‌به‌شاخ؛ هر چیزی در مسیر پرواز را می‌درد و پرت می‌کند. |
| **X** | پرواز (روشن/خاموش) | ۴۵ | پرواز واقعی: بالا/پایین با نگاه کردن، شیفت = شیرجه، اسپرینت = پرواز سریع‌تر. |
| **V** | غرش پوچی | ۶۵ | همهٔ دشمنان تا ۲۰ بلوک را به سمت خودت می‌کشد، کور و ضعیف می‌کند. |
| **C** | گام پوچی | ۸۵ | پرش تله‌پورت تا ۴۴ بلوک از میان دیوار (با سوار) — فرار یا حملهٔ غافلگیرانه. |

روی صفحه (HUD پایین): نوار **انرژی پوچی**، نوار **پیوند** و آیکون توانایی‌ها با **کول‌داون زنده** و کلید هر کدام نمایش داده می‌شود. توانایی‌های قفل با ✖ و عدد پیوند لازم نشان داده می‌شوند.

### پیوند (Bond) چطور بالا می‌رود؟
- غذا دادن: +۳ | ضربه زدن به دشمن: +۲ | سوار شدن: +۱ در هر بار
- هر ۳۰ ثانیه سوار بودن: +۲ | همراهی (نه نشسته): +۱
- پیوند کامل (۱۰۰٪) → باز شدن همهٔ توانایی‌ها + شارژ سریع‌تر انرژی

### توانایی‌های همیشگی (passive)
- **پردهٔ سایه**: در تاریکی خودش را درمان می‌کند و به سوارش شب‌بینی می‌دهد.
- ایمنی به آتش، wither، نفس اژدها، افتادن و «پوچی» (خارج شدن از دنیا = پرتاب به بالا).
- ضربهٔ اژدها دشمن را در تاریکی فرو می‌برد.
- اگر اژدها جا بماند، خودش پیش صاحبش تله‌پورت می‌شود.

---

## ⚙️ ۵. تنظیمات

پس از اولین اجرا فایل `config/voiddragon-common.toml` ساخته می‌شود:

```toml
[gameplay]
  allowFlight = true                 # اجازهٔ پرواز
  voidBreathBreaksBlocks = true      # نفس پوچی بلوک‌های شکننده را نابود کند
  roarAffectsPlayers = true          # غرش به بازیکنان دیگر هم آسیب بزند
  hatchDragonEggs = true             # تبدیل تخم اژدها با تراشهٔ پوچی
  abilityCooldownScale = 1.0         # ضریب کول‌داون (۰.۵ = دو برابر سریع‌تر)
  naturalGrowthSeconds = 90          # رشد طبیعی بچه‌اژدها
```

---

## 🧱 ۶. ساخت (Build) از سورس

```bash
# نیاز: JDK 21
gradle build            # یا: ./gradlew build
# خروجی: build/libs/voiddragon-1.0.0.jar
```

ساخت خودکار در GitHub Actions انجام می‌شود (هر push → build → artifact).
| ابزار | کار |
|---|---|
| `tools/dragon_spec.py` | تنها منبع حقیقت هندسهٔ اژدها (۶۹ مکعب) |
| `tools/gen_model.py` | از spec، کلاس جاوا `DragonRig.java` و UVها را می‌سازد |
| `tools/gen_textures.py` | تکسچر ۵۱۲×۵۱۲ (اژدها، زره، زین، آیتم‌ها، آیکون HUD) را می‌کشد |
| `tools/render_preview.py` | رندرر آفلاین برای دیدن مدل بدون اجرای بازی |
| `tools/pack_source.sh` | آرشیو `voiddragon-source.zip` را که ورک‌فلوی CI از آن build می‌کند همگام می‌کند |

یعنی اگر روزی خواستی مدل یا تکسچر را تغییر بدهی، فقط `dragon_spec.py` را عوض کن و سه اسکریپت را اجرا کن.

### ساختار پروژه
```
src/main/java/com/voiddragon/
├── VoidDragonMod.java           # ورودی ماد + ثبت config
├── VoidDragonConfig.java        # تنظیمات گیم‌پلی
├── ModEntities.java / ModItems.java / ModTabs.java / ModTriggers.java
├── CommonModEvents.java         # attributeها، شبکه، هچ کردن تخم
├── LoreItem.java                # آیتم با توضیح
├── network/AbilityPayload.java  # بستهٔ کلید توانایی
├── entity/VoidDragonEntity.java # قلب ماد: حرکت، پرواز، توانایی‌ها، رشد، پیوند
└── client/                      # مدل، رندرر، کلیدها، HUD
src/main/resources/
├── assets/voiddragon/textures/  # تکسچرهای ساخته‌شده با اسکریپت
├── assets/voiddragon/lang/      # en_us + fa_ir
├── data/voiddragon/recipe/      # دستورهای ساخت
├── data/voiddragon/loot_table/  # دراپ اژدها
└── data/voiddragon/advancement/ # درخت پیشرفت ماد
```

---

## 🩺 ۷. عیب‌یابی

| مشکل | راه‌حل |
|---|---|
| بازی کرش می‌کند یا ماد در لیست نیست | مطمئن شو پروفایل **NeoForge 1.21.1** را اجرا می‌کنی، نه Forge/Fabric/Vanilla. |
| کلیدها کار نمی‌کنند | در `Options → Controls → Key Binds` دستهٔ «Void Dragon» را چک کن (R / G / X / V / C). |
| اژدها سوار نمی‌شود | فقط اژدهای **بالغ** و **زین‌شده** و **نشسته‌نشده** سوار می‌شود. |
| پرواز شروع نمی‌شود | پیوند باید ≥ ۴۵٪ باشد و در تنظیمات `allowFlight` روشن. |
| خیلی کند روی گوشی | در تنظیمات گرافیکی ذرات را کم کن؛ ماد ذرات زیادی برای جلوه‌ها تولید می‌کند. |

---

## 🇬🇧 English summary

**Void Dragon** is a complete dragon mod for **Minecraft 1.21.1 / NeoForge 21.1.x**.

- Tame with **Void Shards** (or hatch a **Dragon Egg** with one).
- Three growth stages; only adults can be saddled and ridden.
- **Void Saddle** + **Void Dragon Armor** craftables.
- Rider abilities, unlocked through a 0–100 **bond**: Void Breath (R, hold), Void Rush (G),
  Flight (X), Void Roar (V), Void Step portal blink (C).
- Hand-built animated model with glowing void eyes/veins, saddle & armour layers, custom HUD,
  four advancements, English + Persian localisation, and a config file for server owners.

Built automatically by GitHub Actions — grab `voiddragon-1.0.0.jar` from the run's **Artifacts**.
