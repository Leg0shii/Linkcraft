package de.legoshi.linkcraft.util.message;

public enum Prefix {

    INFO("&6&l[Linkcraft]&7 "),
    ERROR("&c[Error]&7 "),
    SUCCESS("&a[Success]&7 ");

    private final String p;

    public String text() {
        return p;
    }

    Prefix(String prefix) {
        this.p = prefix;
    }
}
