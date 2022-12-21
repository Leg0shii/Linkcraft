package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SaveableManager;
import de.legoshi.linkcraft.effectblock.*;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class EffectBlockManager implements SaveableManager<EffectBlock, Integer> {

    @Inject
    private DBManager dbManager;
    private final HashMap<Player, Effect> effectsToApply = new HashMap<>();
    private final ArrayList<Player> isRemoving = new ArrayList<>();


    public void storeEffect(Player player, Effect effect) {
        effectsToApply.put(player, effect);
    }

    public void storeRemoving(Player player) {
        if(!isRemoving.contains(player))
            isRemoving.add(player);
    }

    public boolean removeEffect(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();

        if(!isRemoving.contains(player))
            return false;

        isRemoving.remove(player);
        int blockId = isEffectBlock(block);

        if(blockId != -1) {
            EffectBlock effectBlock = requestObjectById(blockId);

            if (effectBlock.getCommands().size() == 1) {
                deleteObject(blockId);
            } else {
                // TODO remove specific effect from block...
                deleteObject(blockId);
            }

            player.sendMessage(MessageUtils.composeMessage(Messages.EFFECT_BLOCK_REMOVED, true));
        }
        else {
            player.sendMessage(MessageUtils.composeMessage(Messages.EFFECT_BLOCK_REMOVED_ERR, true));
        }
        return true;
    }

    public boolean addEffect(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Effect effect = getEffectToAdd(player);
        Block block = e.getClickedBlock();

        if(effect == null) {
            return false;
        }

        int blockId = isEffectBlock(block);

        if (blockId == -1) {
            EffectBlock effectBlock = new EffectBlock(block);
            effectBlock.getCommands().add(effect);
            initObject(effectBlock);
        } else {
            EffectBlock toAdd = requestObjectById(blockId);
            toAdd.getCommands().add(effect);
            updateObject(toAdd);

        }
        player.sendMessage(MessageUtils.composeMessage(Messages.EFFECT_BLOCK_ADDED, true));

        return true;
    }

    private Effect getEffectToAdd(Player player) {
        Effect effect = effectsToApply.get(player);

        if(effect != null)
            effectsToApply.remove(player);

        return effect;
    }

    // Possible race condition due to async sql queries?
    @Override
    public int initObject(EffectBlock blockEffect) {
        String sql = "INSERT INTO lc_effect_blocks (world, x, y, z) VALUES (?,?,?,?);";
        String world = blockEffect.getWorld();
        int x = blockEffect.getX();
        int y = blockEffect.getY();
        int z = blockEffect.getZ();
        ArrayList<Effect> effects = blockEffect.getCommands();

        try(PreparedStatement stmt = dbManager.getMySQL().prepare(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, world);
            stmt.setInt(2, x);
            stmt.setInt(3, y);
            stmt.setInt(4, z);
            stmt.execute();
            // return dbManager.getAutoGenID(stmt);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        // Select same block to get id... probably terribly logic especially with our concurrency issue
        EffectBlock block = requestObjectByLoc(world, x, y, z);

        for(Effect effect : effects) {
            initEffect(block, effect);
        }

        return -1;
    }

    public boolean initEffect(EffectBlock block, Effect effect) {
        String sql = "INSERT INTO lc_effects (block_id, command, executor) VALUES (?,?,?);";

        try(PreparedStatement stmt = dbManager.getMySQL().prepare(sql)) {
            stmt.setInt(1, block.getId());
            stmt.setString(2, effect.getCommand());
            stmt.setInt(3, ExecutorType.getExecutorPosition(effect.getExecutor()));
            stmt.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    // Lazy approach, but should work
    @Override
    public boolean updateObject(EffectBlock blockEffect) {
        EffectBlock old = requestObjectByLoc(blockEffect.getWorld(), blockEffect.getX(), blockEffect.getY(), blockEffect.getZ());
        deleteObject(old.getId());
        initObject(blockEffect);
        return true;
    }


    // Attached effects auto delete due to ON DELETE CASCADE constraint
    @Override
    public boolean deleteObject(Integer id) {
        try(PreparedStatement stmt = dbManager.getMySQL().prepare("DELETE FROM lc_effect_blocks WHERE id=?")) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public EffectBlock requestObjectByLoc(String world, int x, int y, int z) {
        EffectBlock effectBlock = new EffectBlock();
        String sql = "SELECT id, world, x, y, z " +
                     "FROM lc_effect_blocks " +
                     "WHERE world=? AND x=? AND y=? AND z=?;";

        try(PreparedStatement stmt = dbManager.getMySQL().prepare(sql)) {
            stmt.setString(1, world);
            stmt.setInt(2, x);
            stmt.setInt(3, y);
            stmt.setInt(4, z);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                int id = rs.getInt("id");

                effectBlock.setId(id);
                effectBlock.setWorld(rs.getString("world"));
                effectBlock.setX(rs.getInt("x"));
                effectBlock.setY(rs.getInt("y"));
                effectBlock.setZ(rs.getInt("z"));
                effectBlock.setCommands(requestEffectsByBlockId(id));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return effectBlock;
    }

    @Override
    public EffectBlock requestObjectById(Integer id) {
        EffectBlock effectBlock = new EffectBlock();
        String sql = "SELECT world, x, y, z " +
                "FROM lc_effect_blocks " +
                "WHERE id=?;";

        try(PreparedStatement stmt = dbManager.getMySQL().prepare(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                effectBlock.setId(id);
                effectBlock.setWorld(rs.getString("world"));
                effectBlock.setX(rs.getInt("x"));
                effectBlock.setY(rs.getInt("y"));
                effectBlock.setZ(rs.getInt("z"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        effectBlock.setCommands(requestEffectsByBlockId(id));

        return effectBlock;
    }

    public ArrayList<Effect> requestEffectsByBlockId(int id) {
        ArrayList<Effect> commands = new ArrayList<>();
        String sql = "SELECT id, command, executor " +
                     "FROM lc_effects " +
                     "WHERE block_id=?;";

        try(PreparedStatement stmt = dbManager.getMySQL().prepare(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Effect effect = new Effect();
                effect.setId(rs.getInt("id"));
                effect.setCommand(rs.getString("command"));
                effect.setExecutor(ExecutorType.values()[rs.getInt("executor")]);
                commands.add(effect);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return commands;
    }

    /**
     * @return -1 if block does not exist
     */
    public int isEffectBlock(String world, int x, int y, int z) {
        int result = -1;
        String sql = "SELECT id " +
                     "FROM lc_effect_blocks " +
                     "WHERE world=? AND x=? AND y=? AND z=?;";

        try(PreparedStatement stmt = dbManager.getMySQL().prepare(sql)) {
            stmt.setString(1, world);
            stmt.setInt(2, x);
            stmt.setInt(3, y);
            stmt.setInt(4, z);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                result = rs.getInt("id");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int isEffectBlock(Block block) {
        return isEffectBlock(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
    }

    public void executeEffects(PlayerInteractEvent e) {
        int blockId = isEffectBlock(e.getClickedBlock());
        if(blockId != -1) {
            ArrayList<Effect> effects = requestEffectsByBlockId(blockId);

            for(Effect effect : effects) {
                executeEffect(e, effect);
            }
        }
    }

    private void executeEffect(PlayerInteractEvent e, Effect effect) {
        String command = effect.getCommand();
        Player player = e.getPlayer();
        // formatted so this works: /rankup {0} evaluates to: /rankup dklink750
        String formatted = MessageUtils.composeMessage(command, false, player.getName());

        switch (effect.getExecutor()) {
            case PLAYER:
                Bukkit.dispatchCommand(player, formatted);
                break;
            case CONSOLE:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formatted);
                break;
        }
    }
}
