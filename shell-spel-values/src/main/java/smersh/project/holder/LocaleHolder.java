package smersh.project.holder;

import java.util.Locale;

public class LocaleHolder {

    private static final ThreadLocal<Locale> localeStorage = new ThreadLocal<>();

    public static void setLocale(Locale locale) {
        localeStorage.set(locale);
    }

    public static Locale getCurrentLocale() {
        return localeStorage.get();
    }

    public static void remove() {
        localeStorage.remove();
    }

}
