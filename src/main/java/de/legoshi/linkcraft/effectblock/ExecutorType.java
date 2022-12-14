package de.legoshi.linkcraft.effectblock;

import de.legoshi.linkcraft.tag.TagType;

public enum ExecutorType {
    CONSOLE,
    PLAYER;

    public static int getExecutorPosition(ExecutorType type) {
        int typeCount = 0;
        for (ExecutorType executorType : ExecutorType.values()) {
            if (executorType.equals(type)) break;
            typeCount++;
        }
        return typeCount;
    }
}
