package com.lab11;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main — Лаб 11-ийн бүх сценарийг ажиллуулна.
 *
 * Алхам 2.1: deposit/withdraw-г дор хаяж 4 удаа дуудна
 *   (хэвийн, сөрөг, хэт их, тэг).
 *
 * Алхам 3: Customer-ийн NullPointerException-г логоор илрүүлнэ.
 */
public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("========== Лаб 11: Log4j Demo эхлэв ==========");

        // -------------------------------------------------------
        // АЛХАМ 2 — BankAccount: 6 log level туршилт
        // -------------------------------------------------------
        logger.info("--- BankAccount туршилт эхлэв ---");

        BankAccount account = new BankAccount("ACC-1001", 500.0);

        // 1. Хэвийн deposit
        logger.info("Тест 1: Хэвийн deposit (200.0)");
        account.deposit(200.0);

        // 2. Хэвийн withdraw
        logger.info("Тест 2: Хэвийн withdraw (100.0)");
        account.withdraw(100.0);

        // 3. Сөрөг deposit — WARN гарна
        logger.info("Тест 3: Сөрөг deposit (-50.0) — WARN хүлээгдэж буй");
        account.deposit(-50.0);

        // 4. Хэт их withdraw — ERROR гарна
        logger.info("Тест 4: Хэт их withdraw (99999.0) — ERROR хүлээгдэж буй");
        account.withdraw(99999.0);

        // 5. Тэг deposit — WARN гарна
        logger.info("Тест 5: Тэг deposit (0.0) — WARN хүлээгдэж буй");
        account.deposit(0.0);

        // 6. Тэг withdraw — WARN гарна
        logger.info("Тест 6: Тэг withdraw (0.0) — WARN хүлээгдэж буй");
        account.withdraw(0.0);

        // Эцсийн үлдэгдэл
        logger.info("Эцсийн үлдэгдэл: {}", account.getBalance());

        // FATAL жишээ — системийн критик алдаа
        logger.info("Тест 7: FATAL — критик тохиргооны алдааг дуурайна");
        BankAccount.simulateFatalConfigError();

        // Маскалалт жишээ — Алхам 5
        account.logMaskedAccountInfo();

        logger.info("--- BankAccount туршилт дууслаа ---");

        // -------------------------------------------------------
        // АЛХАМ 3 — Customer: NullPointerException debug
        // -------------------------------------------------------
        logger.info("--- Customer debug туршилт эхлэв ---");

        // Тест 1: Хэвийн хэрэглэгч — email зөв
        logger.info("Customer Тест 1: Зөв email-тэй хэрэглэгч");
        Customer c1 = new Customer("Батболд", "batbold@gmail.com");
        logger.info("Domain: {}", c1.getDomain());
        c1.logSafeInfo();

        // Тест 2: email = null — WARN гарч, NPE-гүйгээр ажиллана (засагдсан)
        logger.info("Customer Тест 2: email=null — WARN хүлээгдэж буй (NPE гарахгүй)");
        Customer c2 = new Customer("Дорж", null);
        logger.info("Domain: {}", c2.getDomain()); // "UNKNOWN" буцаана

        // Тест 3: "@" тэмдэгтгүй email — WARN гарна
        logger.info("Customer Тест 3: Буруу email форматтай хэрэглэгч");
        Customer c3 = new Customer("Сарнай", "notanemail");
        logger.info("Domain: {}", c3.getDomain()); // "INVALID" буцаана

        logger.info("--- Customer debug туршилт дууслаа ---");

        logger.info("========== Лаб 11: Demo амжилттай дууслаа ==========");
    }
}
