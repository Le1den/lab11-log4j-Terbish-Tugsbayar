package com.lab11;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * BankAccount — Log4j 2-ийн 6 түвшинг харуулсан жишээ анги.
 *
 * Алхам 2 — TRACE, DEBUG, INFO, WARN, ERROR, FATAL бүгдийг ашиглана.
 */
public class BankAccount {

    // Encapsulation: private static final logger
    private static final Logger logger = LogManager.getLogger(BankAccount.class);

    private double balance;
    private final String accountId;

    public BankAccount(String accountId, double initialBalance) {
        this.accountId = accountId;
        this.balance = initialBalance;
        logger.info("BankAccount created: id={}, initialBalance={}", accountId, initialBalance);
    }

    /**
     * Мөнгө хийх — deposit
     */
    public void deposit(double amount) {
        logger.trace("Entering deposit() with amount={}", amount);

        // WARN — сөрөг утга оруулсан
        if (amount < 0) {
            logger.warn("Invalid input: deposit amount {} is negative for account={}", amount, accountId);
            logger.trace("Exiting deposit() early — invalid amount");
            return;
        }

        if (amount == 0) {
            logger.warn("Deposit amount is zero for account={}. No action taken.", accountId);
            logger.trace("Exiting deposit() early — zero amount");
            return;
        }

        // DEBUG — өмнөх үлдэгдэл
        logger.debug("Balance before deposit: account={}, balance={}", accountId, balance);

        balance += amount;

        // INFO — амжилттай гүйлгээ
        logger.info("Deposited amount={} into account={}. New balance={}", amount, accountId, balance);

        logger.trace("Exiting deposit()");
    }

    /**
     * Мөнгө авах — withdraw
     */
    public void withdraw(double amount) {
        logger.trace("Entering withdraw() with amount={}", amount);

        // WARN — сөрөг утга
        if (amount < 0) {
            logger.warn("Invalid input: withdrawal amount {} is negative for account={}", amount, accountId);
            logger.trace("Exiting withdraw() early — invalid amount");
            return;
        }

        if (amount == 0) {
            logger.warn("Withdrawal amount is zero for account={}. No action taken.", accountId);
            logger.trace("Exiting withdraw() early — zero amount");
            return;
        }

        // DEBUG — өмнөх үлдэгдэл
        logger.debug("Balance before withdraw: account={}, balance={}", accountId, balance);

        // ERROR — үлдэгдэлээс их мөнгө авах гэж оролдсон
        if (amount > balance) {
            logger.error(
                "Withdrawal failed: requested={} exceeds balance={} for account={}",
                amount, balance, accountId
            );
            logger.trace("Exiting withdraw() — insufficient funds");
            return;
        }

        balance -= amount;

        // INFO — амжилттай гүйлгээ
        logger.info("Withdrew amount={} from account={}. New balance={}", amount, accountId, balance);

        logger.trace("Exiting withdraw()");
    }

    /**
     * Үлдэгдэл харах — getBalance
     */
    public double getBalance() {
        logger.trace("Entering getBalance() for account={}", accountId);
        logger.debug("Current balance queried: account={}, balance={}", accountId, balance);
        logger.trace("Exiting getBalance()");
        return balance;
    }

    /**
     * FATAL — системийн критик нөхцөлийн жишээ
     * Тохиргоо (config) алдаатай бол систем унана гэж дуурайна.
     */
    public static void simulateFatalConfigError() {
        logger.fatal("CRITICAL: Banking configuration file is missing or corrupt! " +
                     "System cannot initialize security module. Shutting down.");
    }

    /**
     * Нууц мэдээллийг маскалах — Алхам 5: шилдэг туршлага
     */
    private static String mask(String s) {
        if (s == null || s.length() < 4) return "***";
        return s.substring(0, 2) + "***" + s.substring(s.length() - 2);
    }

    /**
     * Маскалсан account ID-тай лог жишээ
     */
    public void logMaskedAccountInfo() {
        // Нууц дугаарыг маскалж логлоно — Алхам 5
        logger.info("Account access logged for user: id={}", mask(accountId));
    }
}
