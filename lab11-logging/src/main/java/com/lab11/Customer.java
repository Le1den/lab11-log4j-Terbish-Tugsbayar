package com.lab11;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Customer — Алхам 3: NullPointerException-ийг логоор илрүүлж засах.
 *
 * АНХНЫ АЛДАА: email null үед getDomain() дотор
 *   email.indexOf("@") дуудахад NullPointerException үүсдэг.
 *
 * ЗАСВАР: null шалгалт + WARN лог нэмсэн.
 */
public class Customer {

    private static final Logger logger = LogManager.getLogger(Customer.class);

    private String name;
    private String email;

    public Customer(String name, String email) {
        logger.trace("Entering Customer constructor: name={}, email={}", name, email);

        this.name = name;
        this.email = email;

        // DEBUG — конструктор дуусч объект үүссэнийг баталгаажуулах
        logger.debug("Customer object created: name={}, email={}", name, email);
        logger.trace("Exiting Customer constructor");
    }

    /**
     * Email-ийн доменийг буцаана (жишээ: "user@gmail.com" → "GMAIL.COM").
     *
     * АЛДААНЫ ТАЙЛБАР:
     *   - Анхны хувилбарт null шалгалт байгаагүй.
     *   - email=null үед email.indexOf("@") дуудахад NullPointerException гарна.
     *   - TRACE логоор "getDomain called with email=null" гарж алдааг олсон.
     *
     * ЗАСВАР:
     *   - null болон "@" агуулаагүй тохиолдлыг шалгаж WARN лог бичнэ.
     *   - Аюулгүй утга буцаана.
     */
    public String getDomain() {
        logger.trace("Entering getDomain() for customer={}", name);

        // DEBUG — параметрийн утгыг логлох (алдааг олоход тусалсан мөр)
        logger.debug("getDomain called with email={}", email);

        // --- ЗАСВАР ЭХЛЭЛ ---
        if (email == null) {
            // WARN — null email: алдааг бичиж, аюулгүй утга буцаана
            logger.warn("Email is null for customer={}. Cannot extract domain. Returning 'UNKNOWN'.", name);
            logger.trace("Exiting getDomain() — email was null");
            return "UNKNOWN";
        }

        int atIndex = email.indexOf("@");
        logger.debug("Index of '@' in email: atIndex={}", atIndex);

        if (atIndex < 0) {
            // WARN — "@" тэмдэгт байхгүй, буруу форматтай email
            logger.warn("Email '{}' does not contain '@' for customer={}. Returning 'INVALID'.", email, name);
            logger.trace("Exiting getDomain() — invalid email format");
            return "INVALID";
        }
        // --- ЗАСВАР ТӨГСГӨЛ ---

        String domain = email.substring(atIndex + 1).toUpperCase();
        logger.debug("Extracted domain={} for customer={}", domain, name);
        logger.trace("Exiting getDomain() with domain={}", domain);
        return domain;
    }

    public String getName() {
        logger.trace("getName() called, returning name={}", name);
        return name;
    }

    public String getEmail() {
        // Нууц мэдээлэл маскалж логлоно — Алхам 5
        logger.debug("getEmail() called for customer={}", name);
        return email;
    }

    /**
     * Нууц мэдээллийг маскалах — Алхам 5: шилдэг туршлага
     */
    private static String maskEmail(String email) {
        if (email == null) return "***";
        int at = email.indexOf("@");
        if (at < 0) return "***";
        String local = email.substring(0, at);
        String domain = email.substring(at); // "@gmail.com"
        if (local.length() <= 2) return "**" + domain;
        return local.substring(0, 2) + "***" + domain;
    }

    public void logSafeInfo() {
        // Имэйлийг маскалж логлоно — нууц мэдээллийг бүү лог (Алхам 5)
        logger.info("Customer logged in: name={}, email={}", name, maskEmail(email));
    }
}
