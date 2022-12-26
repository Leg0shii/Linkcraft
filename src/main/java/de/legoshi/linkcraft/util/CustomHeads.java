package de.legoshi.linkcraft.util;

// Comment the line below in if you use compatre
//import com.github.johnnyjayjay.compatre.NmsDependent;

import net.minecraft.server.v1_8_R3.MojangsonParser;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.UUID;

/**
 * Class with a utility method to get custom heads as ItemStacks with Spigot.
 * The code below can easily be adjusted to work with any version
 * (only the craftbukkit/nms package names and perhaps the material need to be changed).
 *
 * If you want to compile it once and be compatible with any version, you only need to
 * find a way around the obfuscated packages. You can either use reflection for this
 * (as is common practice), or <a href="https://github.com/johnnyjayjay/compatre">compatre</a>
 * (much simpler).
 *
 * As it stands, the code is compiled against 1.8.8.
 *
 * @author JohnnyJayJay (https://github.com/johnnyjayjay)
 */
// Comment the line below in if you use compatre
//@NmsDependent
public class CustomHeads {

    /* Since version 1.16.1, UUIDs in NBT tags are no longer serialised as strings,
       but as an int array of 4 x 32 bits, going from most significant to least significant
       bits of the UUID. */
    private static final boolean newStorageSystem;

    public static ItemStack commonTagHead = create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTViZjRkYjUxZmFiNGFhMjc5NjI4ZGZiMmExNzIwZTAxYmY3ZTliOTRiYjhjN2U3MDJmYjYzNzU3NDA1ZCJ9fX0===");
    public static ItemStack rareTagHead = create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQ2ODgxNDFlYzc4ZGVkN2JhZWU1ZmZjOTczOTAyMjRhZTllMjMyNzg1MTRkYmYyODQ1OGJmZWE2MiJ9fX0=");
    public static ItemStack epicTagHead = create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzQ1MTg5N2Q3Zjc0N2E5MDFjMTgzYmZlZTJlZDE3NGYzNTY1NWM5NjZmOWFkZjZlMmM3NjMwYTAzYTgxNTViZCJ9fX0===");
    public static ItemStack legendaryTagHead = create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNjNDE0ZmJiNmE5MzU5NzgzMzE4Y2EwYmYxN2E4Njg4ZjkyYmZhMzk4MDI3N2FiYmZkZjY5ZjFlZGViMzQifX19");
    public static ItemStack resetHead = create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMyYzE0NzJhN2JjNjk3NWRlZDdjMGM1MTY5Njk1OWI4OWFmNjFiNzVhZTk1NGNjNDAzNmJjMzg0YjNiODMwMSJ9fX0=");
    public static ItemStack victorHead = create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTM0YTU5MmE3OTM5N2E4ZGYzOTk3YzQzMDkxNjk0ZmMyZmI3NmM4ODNhNzZjY2U4OWYwMjI3ZTVjOWYxZGZlIn19fQ==");
    public static ItemStack hiddenHead = create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ5Y2M1OGFkMjVhMWFiMTZkMzZiYjVkNmQ0OTNjOGY1ODk4YzJiZjMwMmI2NGUzMjU5MjFjNDFjMzU4NjcifX19==");
    public static ItemStack eventSpringHead = create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM3MDQwNmU3NTM5NDIxMzU0MjRiYjk5NjkyYjc0NGM5YTAzNDVkM2YzYzgxOTNlOWQ4ZTMwZDU0NDEyMThlMyJ9fX0===");
    public static ItemStack eventSummerHead = create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE1YWIwNWVhMjU0YzMyZTNjNDhmM2ZkY2Y5ZmQ5ZDc3ZDNjYmEwNGU2YjVlYzJlNjhiM2NiZGNmYWMzZmQifX19===");
    public static ItemStack eventFallHead = create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFmMjRiYzU1M2Y2MWI1Y2U2OTc2MjNmN2MwOWQxOTBiOTAxYTk0NTAyNWEzY2ViZjBkNzhhM2IzNmM4ZjY3MCJ9fX0====");
    public static ItemStack eventWinterHead = create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IzNjA4ODA4MmYxYTQ2YjZmYjcyOThmMzQ0YjFjNjRhMGQzMzg3NmY1MTk3ZmQ4N2U3Mzk2NWU2NmM1NWQ4OCJ9fX0=====");
    public static ItemStack specialHead = create("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWIwYTA2MjlhNzRhMjBjZjQxOWMxYTEyZmNhNDlmODcyYjc4NTI0YmIyNWRkOGI5ZDc0YzFkMjZlN2IwOTI2OCJ9fX0======");

    static {
        String versionString = Bukkit.getBukkitVersion();
        int[] version = Arrays.stream(versionString.substring(0, versionString.indexOf('-')).split("\\."))
                .mapToInt(Integer::parseInt)
                .toArray();
        newStorageSystem = version[0] > 1
                || (version[0] == 1 && version[1] > 16)
                || (version[0] == 1 && version[1] == 16 && version[2] >= 1);
    }

    /**
     * Creates a skull item stack that uses the given base64-encoded texture
     *
     * @param texture The texture value. Can be found on e.g. https://minecraft-heads.com/custom-heads/
     *                in the "Value" field.
     * @return an ItemStack with this texture.
     */
    public static ItemStack create(String texture) {
        // for non-legacy api versions, use PLAYER_HEAD or LEGACY_SKULL_ITEM instead of SKULL_ITEM.
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
    /* This code is compiled against version 1.8.8, but works on any version if you use
       the correct package name. */
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        try {
            String nbtString = String.format(
                    "{SkullOwner:{Id:%s,Properties:{textures:[{Value:\"%s\"}]}}}",
                    serializeUuid(UUID.randomUUID()), texture
            );
            NBTTagCompound nbt = MojangsonParser.parse(nbtString);
            nmsItem.setTag(nbt);
        } catch (Exception e) { // Not catching a more specific exception here because that exception changes across versions
            throw new AssertionError("NBT Tag parsing failed - This should never happen.", e);
        }
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    private static String serializeUuid(UUID uuid) {
        if (newStorageSystem) {
            StringBuilder result = new StringBuilder();
            long msb = uuid.getMostSignificantBits();
            long lsb = uuid.getLeastSignificantBits();
            return result.append("[I;")
                    .append(msb >> 32)
                    .append(',')
                    .append(msb & Integer.MAX_VALUE)
                    .append(',')
                    .append(lsb >> 32)
                    .append(',')
                    .append(lsb & Integer.MAX_VALUE)
                    .append(']')
                    .toString();
        } else {
            return '"' + uuid.toString() + '"';
        }
    }
}
