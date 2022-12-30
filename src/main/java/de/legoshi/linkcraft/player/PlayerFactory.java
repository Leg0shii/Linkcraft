package de.legoshi.linkcraft.player;

import de.legoshi.linkcraft.player.playertype.*;

public class PlayerFactory {

    public static AbstractPlayer getPlayerByType(AbstractPlayer abstractPlayer, Class<?> clazz) {
        String[] stringClazzes = clazz.toString().split("\\.");
        String stringClazz = stringClazzes[stringClazzes.length-1];
        AbstractPlayer aPlayer;
        
        switch (stringClazz) {
            case "MazePlayer":
                aPlayer = constructMazePlayer(abstractPlayer);
                break;
            case "RankUpPlayer":
                aPlayer = constructRankUpPlayer(abstractPlayer);
                break;
            case "SegmentedPlayer":
                aPlayer = constructSegmentedPlayer(abstractPlayer);
                break;
            case "PracticePlayer":
                aPlayer = constructPracticePlayer(abstractPlayer);
                break;
            case "StaffPlayer":
                aPlayer = constructStaffPlayer(abstractPlayer);
                break;
            default:
                aPlayer = constructSpawnPlayer(abstractPlayer);
        };

        aPlayer.setPlayThrough(abstractPlayer.getPlayThrough());
        aPlayer.setPlayThroughManager(abstractPlayer.getPlayThroughManager());
        aPlayer.setSaveStateManager(abstractPlayer.getSaveStateManager());
        return aPlayer;
    }

    private static MazePlayer constructMazePlayer(AbstractPlayer aPlayer) {
        return new MazePlayer(aPlayer.getPlayer(), aPlayer.getPlayerTag());
    }

    private static RankUpPlayer constructRankUpPlayer(AbstractPlayer aPlayer) {
        return new RankUpPlayer(aPlayer.getPlayer(), aPlayer.getPlayerTag());
    }

    private static SegmentedPlayer constructSegmentedPlayer(AbstractPlayer aPlayer) {
        return new SegmentedPlayer(aPlayer.getPlayer(), aPlayer.getPlayerTag());
    }

    private static SpawnPlayer constructSpawnPlayer(AbstractPlayer aPlayer) {
        return new SpawnPlayer(aPlayer.getPlayer(), aPlayer.getPlayerTag());
    }

    private static PracticePlayer constructPracticePlayer(AbstractPlayer aPlayer) {
        return new PracticePlayer(aPlayer.getPlayer(), aPlayer.getPlayerTag(), aPlayer.getClass());
    }

    private static StaffPlayer constructStaffPlayer(AbstractPlayer aPlayer) {
        return new StaffPlayer(aPlayer.getPlayer(), aPlayer.getPlayerTag());
    }

}
