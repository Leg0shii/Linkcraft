package de.legoshi.linkcraft.util.message;

public enum Prefix {

    INFO("&3&l[&b&lLinkcraft&3&l]&7&l "),
    ERROR("&c[Error]&7 "),
    SUCCESS("&a[Success]&7 "),
    TEST("&6&l[Test]&7" );

    private final String p;

    public String text() {
        return p;
    }

    Prefix(String prefix) {
        this.p = prefix;
    }
}
