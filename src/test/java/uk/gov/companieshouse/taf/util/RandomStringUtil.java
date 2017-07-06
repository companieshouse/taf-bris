package uk.gov.companieshouse.taf.util;

import java.util.Random;

public class RandomStringUtil {

    private static final String ALPHA_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz";
    private static final String ALPHA_NUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final String NUMERIC_CHARACTERS = "0123456789";

    public static String generateRandomAlpha(int length) {
        return generateString(new Random(), ALPHA_CHARACTERS, length);
    }

    public static String generateRandomNumeric(int length) {
        return generateString(new Random(), NUMERIC_CHARACTERS, length);
    }

    public static String generateRandomAlphaNumeric(int length) {
        return generateString(new Random(), ALPHA_NUMERIC_CHARACTERS, length);
    }

    private static String generateString(Random rng, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }
}
