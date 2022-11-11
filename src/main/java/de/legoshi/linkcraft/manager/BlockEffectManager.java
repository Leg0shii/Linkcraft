package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.blockeffect.BlockCheckpointEffect;
import de.legoshi.linkcraft.blockeffect.BlockEffect;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.ArrayList;

public class BlockEffectManager {

    public boolean isEffectBlock(Block block) {
        return isEffectBlock(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    public boolean isEffectBlock(World world, int x, int y, int z) {
        boolean result = false;
        String worldUID = world.getUID().toString();

        // Check if exists in db
        // SELECT * FROM {p}effect_blocks WHERE world=? AND x=? AND y=? AND z=?;
        return result;
    }

    public void executeEffects(PlayerInteractEvent e) {
        ArrayList<BlockEffect> effects = getEffects(e.getClickedBlock());
        for(BlockEffect blockEffect : effects) {
            blockEffect.executeEffect(e);
        }
    }

    private BlockEffect getEffectData(String id, String type) {
        BlockEffect blockEffect = null;
        String location = "location";
        // Don't know if this set up is great considering im unsure of the database implementation (at least how it will work)
        // Plan to do SELECT * FROM {p}type_effects (where type is the passed in type)
        // Then in switch statements we can get the relevant data so we wouldnt need to write a different query for each effect type
        switch(type) {
            case "CHECKPOINT":
                blockEffect = new BlockCheckpointEffect(location);

        }
        return blockEffect;
    }

    private ArrayList<BlockEffect> getEffects(Block b) {
        ArrayList<BlockEffect> effects = new ArrayList<>();
        // SELECT e.id, e.type FROM {p}effects e JOIN {p}effect_blocks eb ON e.set_id = eb.set_id
        // WHERE eb.world=? AND eb.x=? AND eb.y=? AND eb.z=?
        while(b.isLiquid()) { // rs.next
            //effects.add(getEffectData(id, type))
        }
        return effects;
    }
}
