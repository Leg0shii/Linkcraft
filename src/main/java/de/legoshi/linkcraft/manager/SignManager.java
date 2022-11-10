package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.sign.EndSign;
import de.legoshi.linkcraft.sign.ISign;

public class SignManager {

    public static ISign loadSign(String[] args) {
        // depending on the sign click, get sign and further information
        return new EndSign();
    }

}
