package de.legoshi.linkcraft.util;

import de.legoshi.linkcraft.util.message.Messages;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@Getter
@RequiredArgsConstructor
public class Cooldown {
    
    private final long used;
    private final long length;
    private final Messages message;

    public Cooldown(long length) {
        this(System.currentTimeMillis(), length, Messages.CMD_COOLDOWN_GLOBAL);
    }

    public Cooldown(long length, Messages message) {
        this(System.currentTimeMillis(), length, message);
    }

    public boolean isCooledDown() {
        return System.currentTimeMillis() > used + length;
    }

    public long timeLeft() {
        long time = TimeUnit.MILLISECONDS.toSeconds((used + length) - System.currentTimeMillis());
        return time < 1 ? 1 : time;
    }
}
