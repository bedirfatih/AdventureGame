# AdventureGame ile OOP Öğrenme Rehberi

Bu rehber, kendi yazdığın kodu kullanarak OOP'yi (Nesne Yönelimli Programlama) anlamanı amaçlıyor. Soyut tanımlar yerine her kavramı **projendeki gerçek satırlarla** gösteriyorum.

---

## 0. Önce büyük resim: OOP aslında ne?

Klasik (prosedürel) yazımda "şunu yap, sonra bunu yap" diye **adımlar** düşünürsün. OOP'de ise önce **"Programda hangi varlıklar var?"** diye sorarsın:

| Oyundaki varlık | Koddaki karşılığı |
|---|---|
| Oyuncu | `Player` |
| Oyuncunun çantası | `Inventory` |
| Gidilen yerler | `Location` ve çocukları |
| Düşmanlar | `Obstcale` ve çocukları |
| Oyunun kendisi | `Game` |

Her varlığın:
- **Verisi** vardır (alan / field): `Player` için `healthy`, `money`, `damage`
- **Davranışı** vardır (metot): `Player` için `getTotalDamage()`, `selectCharacter()`

**Sınıf (class)** = kalıp / plan. **Nesne (object)** = o plandan üretilmiş gerçek parça.

```java
Player player = new Player("Ali");   // Player kalıbından bir nesne ürettik
```

> Benzetme: `Player` bir **kalıp kurabiye kesici**, `new Player("Ali")` ise kesilmiş **kurabiye**. Aynı kesiciden istediğin kadar kurabiye çıkar, her birinin kendi süslemesi (verisi) olur.

---

## 1. Projenin haritası

```
                        Main
                         │  (sadece başlatır)
                         ▼
                       Game ──────────────► Player ──► Inventory
                         │                    ▲
                         │ oluşturur          │ (her yer oyuncuyu tutar)
                         ▼                    │
                    ┌─ Location (abstract) ───┘
                    │      │
                    │      ├── NormalLoc (abstract)   ← düşmansız yerler
                    │      │      ├── SafeHouse
                    │      │      └── ToolStore
                    │      │
                    │      └── BattleLoc (abstract)   ← savaş yerleri
                    │             ├── Cave   (Zombie)
                    │             ├── Forest (Vampire)
                    │             └── River  (Bear)
                    │
                    └─ Obstcale (düşman kalıbı)
                           ├── Zombie
                           ├── Vampire
                           └── Bear
```

Bu şemada ok çizgilerinin iki farklı anlamı var; OOP'nin en önemli ayrımı bu:

- **"extends" (kalıtım)**: *"X bir Y'dir"* → `Cave` bir `BattleLoc`'tur.
- **Nesne tutma (kompozisyon)**: *"X bir Y'ye sahiptir"* → `Player`'ın bir `Inventory`'si **vardır**.

Kafan karıştığında kendine şunu sor: *"bir mi?" yoksa "sahip mi?"*

---

## 2. Dört temel OOP kavramı

### 2.1 Encapsulation (Kapsülleme)

**Fikir:** Verileri dışarıdan doğrudan değiştirtme; kontrollü kapıdan (getter/setter) geçir.

`Inventory.java`:
```java
public class Inventory {
    private boolean water, food, firewood;   // <-- private: dışarıdan erişilemez
    private int damage, armor;

    public boolean isWater() { return water; }              // okuma kapısı (getter)
    public void setWater(boolean water) { this.water = water; }  // yazma kapısı (setter)
}
```

Kullanım (`BattleLoc` içinde):
```java
player.getInv().setFood(true);   // inventory.food = true  DİYEMEZSİN, private
```

**Neden önemli?** Yarın "para asla negatif olamaz" kuralı koymak istesen, sadece `setMoney` metodunu değiştirirsin; projenin geri kalanı etkilenmez. Alan `public` olsaydı kuralı yüzlerce yere yazman gerekirdi.

**Projedeki erişim belirleyicileri:**

| Belirleyici | Kim görür? | Projede örnek |
|---|---|---|
| `private` | Sadece o sınıf | `Player.healthy`, `Obstcale.health` |
| `protected` | O sınıf + çocukları + aynı paket | `Location.player`, `Location.name` |
| `public` | Herkes | `getLocation()`, tüm getter/setter'lar |

> `Location.player` neden `protected`? Çünkü `Cave`, `SafeHouse` gibi **çocuk sınıfların** oyuncuya doğrudan ulaşması gerekiyor. `private` olsaydı çocuklar da göremezdi.

---

### 2.2 Inheritance (Kalıtım)

**Fikir:** Ortak şeyleri bir kez **ebeveyn** sınıfa yaz, çocuklar `extends` ile otomatik miras alsın. Kod tekrarını önler.

#### Örnek A — Düşmanlar

`Obstcale` tüm düşmanların ortak kalıbı: ad, hasar, can, ödül, en fazla sayı.

```java
public class Zombie extends Obstcale {
    public Zombie() {
        super("Zombi", 3, 10, 4, 3);   // ebeveynin constructor'ını çağır
    }
}
```

`Zombie`'nin içinde **hiç alan ya da getter yok**, ama şunu yapabilirsin:

```java
Zombie z = new Zombie();
z.getHealth();   // Obstcale'den miras geldi
z.count();       // Obstcale'den miras geldi
```

Üç düşman sınıfı da (`Zombie`, `Vampire`, `Bear`) sadece **farklı sayılar** veriyor. Yeni düşman eklemek 5 satır.

**`super(...)` nedir?** "Önce ebeveynin kurulumunu yap." Ebeveynin constructor'ı parametre istiyorsa, çocuk bunları `super(...)` ile vermek **zorunda**.

#### Örnek B — Mekânlar (zincirleme kalıtım)

```
Location  →  BattleLoc  →  Cave
```

`Cave` şunlara sahip, ama **hiçbirini kendisi yazmadı**:
- `Location`'dan: `player`, `name`, `scan`, `getName()`...
- `BattleLoc`'tan: `combat()`, `afterHit()`, `playerStats()`, `getLocation()`...

`Cave` sınıfının tamamı:
```java
public class Cave extends BattleLoc {
    public Cave(Player player) {
        super(player, "Zombi", new Zombie(), "Food");
    }
}
```
Dört satır! Savaş mantığının tamamı `BattleLoc`'ta **bir kez** yazıldı; `Cave`, `Forest`, `River` hepsi onu kullanıyor. Savaşta bir hatayı düzeltsen, üçü birden düzelir.

---

### 2.3 Abstraction (Soyutlama)

**Fikir:** "Ne yapılacağını" söyle, "nasıl yapılacağını" çocuğa bırak.

`Location.java`:
```java
public abstract class Location {
    public abstract boolean getLocation();   // gövde yok! Sadece söz veriyor.
}
```

- `abstract class` → **doğrudan `new Location()` yapamazsın.** "Genel bir yer" diye bir şey yok; ya güvenli evdir ya mağaza ya mağara.
- `abstract` metot → "Bu metodun gövdesi yok; **her çocuk kendi yazmak zorunda**."

Bu bir **sözleşmedir**: *"Location olmak istiyorsan `getLocation()` yazmalısın."* Yazmayan sınıf derlenmez.

Projede soyut sınıflar: `Location`, `NormalLoc`, `BattleLoc`.

> `NormalLoc` ve `BattleLoc` abstract ama `getLocation()`'ı kendileri yazıyor (yarı soyut). Bu normal: soyut sınıf hem hazır kod hem de boş sözleşme içerebilir.

---

### 2.4 Polymorphism (Çok biçimlilik) ⭐ En kritik kısım

**Fikir:** Aynı çağrı, nesnenin gerçek türüne göre farklı davranır.

`Game.java` içinde:

```java
Location location;            // değişken tipi: Location (genel)

switch (selLoc) {
    case 1: location = new SafeHouse(player); break;
    case 2: location = new Cave(player);      break;
    case 5: location = new ToolStore(player); break;
}

location.getLocation();        // ← sihir burada
```

`location` değişkeni **tek bir tip** (`Location`) ama içine farklı çocuklar koyabiliyorsun. `location.getLocation()` çağrıldığında Java **çalışma anında** gerçek nesneye bakar:

| `location` içinde... | `getLocation()` ne yapar? |
|---|---|
| `SafeHouse` | Canı fuller, "güvenli evdesiniz" yazar |
| `ToolStore` | Mağaza menüsünü açar |
| `Cave` / `Forest` / `River` | Savaş sorar, dövüşü başlatır |

**`Game` sınıfı bunların hangisi olduğunu umursamıyor.** Sadece `getLocation()` çağırıyor. Yarın `Castle` adlı yeni bir yer eklesen, `Game`'in ana döngüsünü **hiç değiştirmen gerekmez**. İşte OOP'nin gücü bu.

**Bunu polimorfizm olmadan yazsaydın:**
```java
if (location is SafeHouse) { ...canı fulle... }
else if (location is ToolStore) { ...mağaza... }
else if (location is Cave) { ...savaş... }
// her yeni yer için buraya yeni else-if
```

#### `@Override` nedir?

`SafeHouse.java`:
```java
@Override
public boolean getLocation() { ... }
```
"Ebeveynin metodunu **kendi versiyonumla değiştiriyorum**" demektir. Yazım hatası yaparsan derleyici seni uyarır, o yüzden hep yaz.

#### Polimorfizmin ikinci örneği: `BattleLoc` ve `Obstcale`

```java
protected Obstcale obstcale;     // Zombie mi Vampire mı Bear mı? BattleLoc bilmiyor.
...
obstcale.getHealth();
obstcale.getDamage();
```
`BattleLoc` düşmanın türünü bilmeden savaşı yönetiyor. `Cave` içine `new Zombie()` koyarsan zombi savaşı olur, `new Bear()` koyarsan ayı savaşı.

---

## 3. Constructor (Yapıcı metot) zinciri — `new Cave(player)` çağrılınca ne olur?

Bu, en çok kafa karıştıran yerlerden biri. Adım adım:

```java
location = new Cave(player);
```

```
1. Cave(Player player) çalışır
      └─ super(player, "Zombi", new Zombie(), "Food")   ← hemen ebeveyne git
            │
2.          ▼  BattleLoc(player, name, obstcale, award) çalışır
               └─ super(player)                          ← bir üste git
                     │
3.                   ▼  Location(player) çalışır
                        this.player = player;            ← en üstten başla
                     │
4.               ◄───┘  BattleLoc devam eder:
                        this.name = name;
                        this.obstcale = obstcale;
                        this.award = award;
5.         ◄───┘  Cave devam eder (başka işi yok)
```

**Kural:** Kurulum **yukarıdan aşağıya** olur. Önce en üst ebeveyn hazırlanır, sonra çocuk. (Çocuk, ebeveynin ürettiği şeylere güvenerek çalışabilsin diye.)

`this` = "şu an oluşturulan nesnenin kendisi". `this.name = name` → "nesnenin `name` alanına, parametredeki `name`'i ata".

---

## 4. Kompozisyon ("sahip olma") örnekleri

Kalıtım dışında sınıflar birbirini **alan olarak tutarak** da bağlanır:

```java
public class Player {
    private Inventory inv;                    // Player'ın bir Inventory'si VAR

    public Player(String name) {
        this.inv = new Inventory();           // oyuncu doğunca çantası da doğar
    }
}
```

```java
public abstract class Location {
    protected Player player;                  // her Location bir Player'ı tanır
}
```

```java
public abstract class BattleLoc extends Location {
    protected Obstcale obstcale;              // her savaş yerinin bir düşmanı VAR
}
```

**Zincirleme çağrı okuma:** `player.getInv().setFood(true)`

```
player          → Player nesnesi
.getInv()       → onun Inventory nesnesini ver
.setFood(true)  → o envanterde yemek = true yap
```
Soldan sağa, her nokta "şunun içindeki şuna git" demek.

**Önemli:** Hepsi **aynı** `Player` nesnesini paylaşıyor. `Game` oyuncuyu bir kez oluşturur, her yeni `Location`'a verir. Savaşta `BattleLoc` canı azaltınca, `SafeHouse` aynı oyuncunun azalmış canını görüp fullüyor. Çünkü nesneler **referansla** paylaşılır (kopya yoktur).

---

## 5. Bir oyun turunun baştan sona izi

Kavramları birleştirelim. Oyuncu "2 – Mağara" seçti:

```
Main.main()
  └─ new Game().login()
       ├─ new Player(isim)                    → Player nesnesi, içinde new Inventory()
       ├─ player.selectCharacter()            → can/hasar/para atanır
       └─ start()  (sonsuz döngü)
            ├─ kullanıcı 2 seçer
            ├─ location = new Cave(player)    → [Polimorfizm: tip Location, nesne Cave]
            │       ├─ Location(player)       → player saklanır
            │       ├─ BattleLoc(...)         → name, obstcale(Zombie), award saklanır
            │       └─ Cave
            └─ location.getLocation()         → [Cave'in miras aldığı BattleLoc.getLocation()]
                  ├─ obstcale.count()         → Obstcale'den miras: 1-3 arası rastgele zombi
                  ├─ "Savaş mı kaç mı?"
                  ├─ combat(sayı)             → döngü: oyuncu vurur, zombi vurur
                  │     ├─ obstcale.getHealth()/setHealth()   (encapsulation)
                  │     └─ player.getTotalDamage()  → Player damage + Inventory damage
                  └─ kazandıysa player.getInv().setFood(true)
```

---

## 6. Kendi kodundan "OOP tasarım kararları" tablosu

| Karar | Neden iyi? | Hangi kavram? |
|---|---|---|
| `Zombie/Vampire/Bear` ayrı sınıf | Yeni düşman = 5 satır | Kalıtım |
| `BattleLoc.combat()` tek yerde | 3 yer aynı kodu paylaşıyor | Kalıtım / DRY |
| `Location.getLocation()` abstract | Her yer kendi davranışını yazmak zorunda | Soyutlama |
| `Game` içinde `Location location` | Yeni yer eklemek döngüyü bozmaz | Polimorfizm |
| `Inventory` private alanlar | Çantaya kontrollü erişim | Kapsülleme |
| `Player` → `Inventory` tutuyor | "Oyuncunun çantası var" gerçeğini modeller | Kompozisyon |

---

## 7. Projede gördüğüm iyileştirme fırsatları (öğrenmek için ideal)

Bunlar "hata" değil; OOP'yi daha iyi uygulamak için çalışabileceğin yerler. Her birini düzeltmek bir kavramı pekiştirir.

### 7.1 Yazım: `Obstcale` → `Obstacle`
Sınıf adı yanlış yazılmış (`Obstcale`). IDE'de sınıfa sağ tıkla → *Refactor → Rename* yap, tüm referanslar otomatik güncellenir.

### 7.2 Tür kontrolünü string ile yapıyorsun (`Game.java`)
```java
if (location.getClass().getName().equals("SafeHouse")) { ... }
```
Bu, polimorfizmin ruhuna ters ve kırılgan (sınıf adı değişirse sessizce bozulur). Daha iyisi: `instanceof` kullan:
```java
if (location instanceof SafeHouse) { ... }
```
Daha da iyisi: "oyunu kazandı mı?" kontrolünü `Player`/`Inventory` içine metot olarak koy (`inv.hasAllItems()`).

### 7.3 `award` bir `String` ("Food", "Water", "Firewood")
`BattleLoc` içinde uzun `if / else if` zinciri var; `"Food"` yerine `"Fod"` yazsan derleyici uyarmaz. İyi bir alternatif: **enum**.
```java
enum Award { FOOD, WATER, FIREWOOD }
```

### 7.4 Kod tekrarı: `Player` ve `Inventory` içindeki çift getter
`getInventory()` ile `getInv()` aynı işi yapıyor. Birini sil.

### 7.5 Her sınıf kendi `Scanner`'ını açıyor
`Game`, `Player`, `Location` hepsi `new Scanner(System.in)` yapıyor. Tek bir paylaşılan `Scanner` kullanmak daha temiz.

### 7.6 `Obstcale` nesnesi savaşta değiştiriliyor
`combat()` içinde düşmanın canı `setHealth` ile düşürülüp sonra eski değere sıfırlanıyor. Bunun yerine her düşman için **yeni nesne** oluşturmak daha güvenli (nesneler kendi durumunu kendi taşımalı).

### 7.7 Küçük mantık hataları (OOP dışı ama göze çarptı)
- `ToolStore.buyWeapon` içinde `player.getMoney() > price`; `buyArmor`'da `>=`. Tutarsız. Parası tam yetiyorsa silah alınamıyor.
- `case 4:` sonrası `break` yok → "Çıkış yapılıyor" yazıp hemen "Geçersiz işlem!" yazıyor (fall-through).
- Menüde "Silah Seçiniz" zırh menüsünde de yazıyor.
- `BattleLoc.getLocation()`: oyuncu savaştan **kaçınca** `true` dönüyor ve oyun devam ediyor (bu doğru), ama `combat()` hem "kaçtı" hem "öldü" durumunda `false` döndüğü için ikisini ayırt edemiyorsun; bu yüzden ölüm kontrolü dolaylı yapılıyor.

---

## 8. Kendini sına: alıştırmalar

Aşağıdakileri **sırayla** yap. Her biri bir kavramı oturtur.

1. **(Kalıtım)** Yeni bir düşman ekle: `Skeleton` (`Obstcale`'den türesin). Kaç satır yazdın? Kaç tane başka dosyaya dokunmak zorunda kaldın?
2. **(Kalıtım + Polimorfizm)** Yeni bir savaş yeri ekle: `Mine` (Maden), içinde `Skeleton` olsun, ödülü "Coal" olsun. `Game` menüsüne 6. seçenek olarak ekle. Ana döngüye (`while(true)`) dokunman gerekti mi?
3. **(Soyutlama)** `Location`'dan türeyen ama `getLocation()` yazmayan bir sınıf yazmayı dene. Derleyici ne diyor?
4. **(Kapsülleme)** `Player.setMoney` içine "para negatif olamaz" kuralı ekle. Kuralı tek yerde yazmak yetti mi?
5. **(Polimorfizm)** `Location`'a `abstract` olmayan `describe()` metodu ekle, sonra sadece `Forest` içinde `@Override` ile değiştir. Üç yeri çağırıp farkı gözle.
6. **(Kompozisyon)** `Weapon` adlı yeni bir sınıf yaz (ad, hasar, fiyat). `Inventory` içindeki `wName` + `damage` ikilisini bu sınıfla değiştir. Neden daha temiz oldu?
7. **(Zor)** `ToolStore`'daki iki `switch`'i (silah/zırh) `Weapon` ve `Armor` nesneleri dizisiyle değiştir. Yeni silah eklemek tek satıra düştü mü?

---

## 9. Hızlı sözlük

| Terim | Tek cümle |
|---|---|
| Sınıf | Nesneler için kalıp |
| Nesne | Kalıptan üretilmiş somut örnek (`new` ile) |
| Alan (field) | Sınıfın içindeki değişken (`healthy`) |
| Metot | Sınıfın içindeki fonksiyon (`combat()`) |
| Constructor | `new` denince çalışan kurulum metodu |
| `this` | "Şu anki nesne" |
| `super` | "Ebeveyn sınıf" |
| `extends` | "...'ın çocuğudur" |
| `abstract` | "Eksik/yarım, çocuk tamamlasın; doğrudan `new` yapılamaz" |
| `@Override` | "Ebeveynin metodunu ezip kendiminkini yazdım" |
| `private/protected/public` | Erişim sınırları |
| Getter/Setter | Private alana kontrollü erişim kapısı |
| Referans | Nesneye işaret eden "ok"; kopya değil, aynı nesne |

---

## 10. Son söz: tek cümlelik özet

> **Kalıtım** kod tekrarını azaltır, **soyutlama** sözleşme koyar, **kapsülleme** veriyi korur, **polimorfizm** ise `Game` gibi yöneten sınıfların detayları bilmeden çalışmasını sağlar.

Kafan karıştığında bu soruları sor:
1. **Bu varlık ne?** (sınıf)
2. **Ne bilir, ne yapar?** (alan, metot)
3. **"Bir"mi yoksa "sahip"mi?** (`extends` mi, alan mı)
4. **Çağıran, tam olarak kimin çağrıldığını bilmek zorunda mı?** (hayırsa → polimorfizm)
