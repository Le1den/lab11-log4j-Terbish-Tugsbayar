# Лаб 11 — Log4j ашиглан debug хийх

**Хичээл:** F.CSM202 Объект хандлагат программчлал  
**Лаб:** Лаб 11 — Log4j ашиглан debug хийх, шилдэг туршлагууд  
**Repository:** `lab11-log4j-<овог>-<нэр>`

---

## Төслийн бүтэц

```
lab11-logging/
├── pom.xml          
├── README.md        
├── analysis.md      
└── src/main/
    ├── java/com/lab11/
    │   ├── BankAccount.java  
    │   ├── Customer.java     
    │   └── Main.java         
    └── resources/
        └── log4j2.xml        
---

## BankAccount ангийн тайлбар

`BankAccount` анги нь банкны данс хийгдэх гүйлгээг дуурайж, Log4j 2-ийн **6 log түвшинг** бүгдийг харуулна:

| Түвшин | Хаана хэрэглэгдэж буй |
|--------|----------------------|
| `TRACE` | `deposit()`, `withdraw()`, `getBalance()` руу орох/гарахад |
| `DEBUG` | Гүйлгээний өмнөх үлдэгдэл |
| `INFO`  | Амжилттай deposit/withdraw |
| `WARN`  | Сөрөг эсвэл тэг хэмжээ оруулсан |
| `ERROR` | Үлдэгдэлээс их мөнгө авах гэж оролдсон |
| `FATAL` | Тохиргооны файл байхгүй — системийн критик алдаа |

**Шилдэг туршлага:** Нууц account дугаарыг маскалж логлоно:
```java
private static String mask(String s) {
    if (s == null || s.length() < 4) return "***";
    return s.substring(0, 2) + "***" + s.substring(s.length() - 2);
}
logger.info("Account access logged for user: id={}", mask(accountId));
```

---

## Customer ангийн тайлбар

`Customer` анги нь хэрэглэгчийн email-ийн домэйн гаргаж авна.

**Анхны алдаа:** `email` утга `null` үед `getDomain()` дотор `NullPointerException` гарна.

**Логоор хэрхэн олсон:** `TRACE`/`DEBUG` нэмж ажиллуулахад:
```
DEBUG Customer - getDomain called with email=null
```
гэж гарч, null email-аас үүссэн болохыг тодорхой харсан.

**Засвар:** `null` болон `@` байхгүй тохиолдолд `WARN` лог бичиж, аюулгүй утга буцаана.

---

## log4j2.xml тохиргоо

`src/main/resources/log4j2.xml` файлд:

- **Console appender** — консолд `%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %c{1}:%L - %msg%n` форматаар харуулна
- **RollingFile appender** — `logs/app.log` файлд бичнэ
  - **TimeBasedTriggeringPolicy** — өдөр бүр шинэ файл үүсгэнэ
  - **SizeBasedTriggeringPolicy (1 MB)** — **Даалгавар 1.1:** 1 MB хүрэхэд файл эргэдэг (ROLL_BY_SIZE)
- **Root logger**: `DEBUG` түвшин → хоёр аппендерт бичнэ

**Production-д** `level="info"` болгон `DEBUG`/`TRACE`-ийг унтраана.

---

## Алхам 2.1-ийн хариу

`main()` функцээс `deposit()`/`withdraw()`-г дараах 4+ хувилбараар дуудсан:

1. **Хэвийн deposit(200.0)** → INFO
2. **Хэвийн withdraw(100.0)** → INFO  
3. **Сөрөг deposit(-50.0)** → WARN (хасах тоо)
4. **Хэт их withdraw(99999.0)** → ERROR (үлдэгдэлээс их)
5. **Тэг deposit(0.0)** → WARN
6. **Тэг withdraw(0.0)** → WARN

`app.log`-д 6 түвшин бүгд байгааг шалгах:
```bash
awk '{print $3}' logs/app.log | sort | uniq
# Гаралт: DEBUG ERROR FATAL INFO TRACE WARN
```

---

## Алхам 4.1-ийн хариу: `tail -f` яагаад debug-д илүү хэрэгтэй вэ?

`tail -f` нь логийн файлд бичигдэж буй шинэ мэдээллийг **бодит цагт** харуулдаг тул програм ажиллаж байх үед шинж тэмдгийг шууд ажиглах боломжтой. `cat` болон `less` нь командыг ажиллуулах мөчид файлд байгаа агуулгыг нэг удаа харуулаад дуусдаг тул дараа нь нэмэгдэх лог мөрүүдийг харах боломжгүй. Debug хийх явцад алдаа хэзээ, ямар дарааллаар гарч байгааг **шууд, тасралтгүй** харах нь асуудлыг хурдан олоход шийдвэрлэх ач холбогдолтой.

---

## Ажиллуулах заавар

```bash
# 1. Клонлох
git clone https://github.com/<username>/lab11-log4j-<овог>-<нэр>.git
cd lab11-log4j-<овог>-<нэр>

# 2. Build хийх
mvn compile

# 3. Ажиллуулах
mvn exec:java -Dexec.mainClass="com.lab11.Main"

# 4. Лог харах
cat logs/app.log

# 5. ERROR шүүх
grep "ERROR" logs/app.log

# 6. Бодит цагийн лог
tail -f logs/app.log

# 7. Level тоолох
awk '{print $3}' logs/app.log | sort | uniq -c
```

---

## Ашигласан эх сурвалж

- [Apache Log4j 2 Manual](https://logging.apache.org/log4j/2.x/manual/)
- Хичээлийн слайд: Log4j-ийн Тусламжтайгаар debug хийх, шилдэг туршлагууд
