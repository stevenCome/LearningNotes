package utils;
import java.security.SecureRandom;
import java.util.UUID;
/**
 * @author hexushuai@jd.com
 * @version 1.0.0
 * @description UUID-Utils
 * @date 2022/4/17 2:39 下午
 */
public class Identities {
    private static SecureRandom random = new SecureRandom();

    public Identities() {
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String uuid(String name) {
        return UUID.nameUUIDFromBytes(name.getBytes()).toString();
    }

    public static String uuid32() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String uuid32(String name) {
        return UUID.nameUUIDFromBytes(name.getBytes()).toString().replaceAll("-", "");
    }

    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    public static long randomLong18() {
        long ctime = System.currentTimeMillis();
        return ctime * 100000L + (long)random.nextInt(100000);
    }
}
