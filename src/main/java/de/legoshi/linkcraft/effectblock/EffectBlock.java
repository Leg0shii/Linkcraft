package de.legoshi.linkcraft.effectblock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.block.Block;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EffectBlock {

    private int id;
    private String world;
    private int x;
    private int y;
    private int z;
    private ArrayList<Effect> commands;

    public EffectBlock(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.commands = new ArrayList<>();
    }

    public EffectBlock(Block block) {
        this(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
    }

}
