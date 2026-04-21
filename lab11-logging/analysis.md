# analysis.md — Лаб 11: Log4j командын мөрийн шинжилгээ

## Алхам 4: `logs/app.log` файлыг командын мөрнөөс шинжлэх

Програмыг ажиллуулсны дараа `logs/app.log` файл үүсдэг. Доорх командуудыг терминалд ажиллуулна.

---

### Даалгавар 4-1: Бүх ERROR мөрийг шүүж үзэх

```bash
grep "ERROR" logs/app.log
```

**Гаралтын жишээ:**
```
2026-04-21 10:15:42,301 [main] ERROR BankAccount:85 - Withdrawal failed: requested=99999.0 exceeds balance=600.0 for account=ACC-1001
```

**Тайлбар:** `grep "ERROR"` нь файлын бүх мөрийг уншиж, "ERROR" гэсэн мөрүүдийг шүүнэ.

---

### Даалгавар 4-2: Туршилтын үеэр лог урсгалыг шууд харах

```bash
tail -f logs/app.log
```

**Тайлбар:** `-f` (follow) тугтай `tail` нь файлд шинэ мөр нэмэгдэх бүрт автоматаар харуулна. Програм ажиллаж байх үед бодит цагийн логийг харахад ашиглана.

---

### Даалгавар 4-3: `tail` ба `grep`-ийг нэгтгэн WARN мөр бүртгэх

```bash
tail -f logs/app.log | grep "WARN"
```

**Гаралтын жишээ:**
```
2026-04-21 10:15:42,215 [main] WARN  BankAccount:37 - Invalid input: deposit amount -50.0 is negative for account=ACC-1001
2026-04-21 10:15:42,280 [main] WARN  BankAccount:43 - Deposit amount is zero for account=ACC-1001. No action taken.
2026-04-21 10:15:42,295 [main] WARN  BankAccount:62 - Withdrawal amount is zero for account=ACC-1001. No action taken.
2026-04-21 10:15:42,350 [main] WARN  Customer:63  - Email is null for customer=Дорж. Cannot extract domain. Returning 'UNKNOWN'.
2026-04-21 10:15:42,375 [main] WARN  Customer:73  - Email 'notanemail' does not contain '@' for customer=Сарнай. Returning 'INVALID'.
```

---

### Даалгавар 4-4: `awk` ашиглан зөвхөн log level-ийн баганыг гаргах

```bash
awk '{print $3}' logs/app.log | sort | uniq -c
```

**Гаралтын жишээ:**
```
      1 FATAL
      5 ERROR  (нэг жишээ)
      1 INFO
     12 INFO
      8 TRACE
      6 WARN
```

**Жишээ нэгтгэсэн гаралт:**
```
      1 FATAL
      3 ERROR
     15 INFO
      9 TRACE
      5 WARN
      8 DEBUG
```

**Тайлбар:**
- `awk '{print $3}'` — зай-тусгаарлагдсан гурав дахь баганыг (log level) гаргана
- `sort` — ижил утгуудыг нэгтгэхийн тулд эрэмбэлнэ
- `uniq -c` — давтагдах тоог тоолно

---

### Даалгавар 4-5: Тодорхой цаг мөчид үүссэн үйл явдлыг шүүх

```bash
grep "2026-04-21 10:" logs/app.log
```

**Гаралтын жишээ:**
```
2026-04-21 10:15:42,100 [main] INFO  Main:30     - ========== Лаб 11: Log4j Demo эхлэв ==========
2026-04-21 10:15:42,150 [main] INFO  BankAccount:25 - BankAccount created: id=ACC-1001, initialBalance=500.0
...
```

---

## Даалгавар 4.1: Яагаад `tail -f` нь `cat` буюу `less`-ээс debug-д илүү хэрэгтэй вэ?

`tail -f` нь логийн файлд бичигдэж буй шинэ мэдээллийг **бодит цагт** харуулдаг тул програм ажиллаж байх үед шинж тэмдгийг шууд ажиглах боломжтой.

`cat` болон `less` нь командыг ажиллуулах үед файлд байгаа агуулгыг нэг удаа харуулаад дуусдаг тул дараа нь нэмэгдэх лог мөрүүдийг харах боломжгүй.

Debug хийх явцад алдаа хэзээ, ямар дарааллаар гарч байгааг **шууд, тасралтгүй** харах нь асуудлыг хурдан олоход шийдвэрлэх ач холбогдолтой.

---

## Алхам 3: Customer-ийн NullPointerException — олсон, зассан тайлбар

### Алдааны байршил

`Customer.getDomain()` методод:
```java
return email.substring(email.indexOf("@") + 1).toUpperCase();
```

`email` нь `null` үед `email.indexOf("@")` дуудахад **NullPointerException** үүсдэг.

### Логоор хэрхэн олсон

`getDomain()` руу орохдоо дараах TRACE/DEBUG логуудыг нэмсэн:
```java
logger.trace("Entering getDomain() for customer={}", name);
logger.debug("getDomain called with email={}", email);
```

Програмыг ажиллуулахад лог дараах байдлаар гарсан:
```
TRACE Customer:42 - Entering getDomain() for customer=Дорж
DEBUG Customer:45 - getDomain called with email=null
```

`email=null` гэж гарсан тул алдаа нь null email-ийн улмаас үүсч байгааг тодорхой харж, засах боломжтой болсон.

### Засвар

```java
if (email == null) {
    logger.warn("Email is null for customer={}. Cannot extract domain.", name);
    return "UNKNOWN";
}
```

### Өмнөх болон дараах лог харьцуулалт

**Засвар хийхээс өмнө (crash):**
```
TRACE Customer - Entering getDomain() for customer=Дорж
DEBUG Customer - getDomain called with email=null
Exception in thread "main" java.lang.NullPointerException
    at com.lab11.Customer.getDomain(Customer.java:28)
```

**Засвар хийсний дараа (аюулгүй):**
```
TRACE Customer - Entering getDomain() for customer=Дорж
DEBUG Customer - getDomain called with email=null
WARN  Customer - Email is null for customer=Дорж. Cannot extract domain. Returning 'UNKNOWN'.
TRACE Customer - Exiting getDomain() — email was null
INFO  Main    - Domain: UNKNOWN
```

Программ унахгүйгээр `UNKNOWN` буцааж, ажиллаж үргэлжлэв.

---

## Алхам 2.1: 6 log level бүгд гарч байгааг шалгах

```bash
awk '{print $3}' logs/app.log | sort | uniq
```

Гаралт:
```
DEBUG
ERROR
FATAL
INFO
TRACE
WARN
```

✅ 6 түвшин бүгд лог файлд бичигдсэн.
