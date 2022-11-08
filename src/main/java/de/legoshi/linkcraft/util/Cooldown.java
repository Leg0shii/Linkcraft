package de.legoshi.linkcraft.util;

import de.legoshi.linkcraft.util.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@Getter
@RequiredArgsConstructor
public class Cooldown {
    
    private final long used;
    private final long length;
    private final Message message;

    public Cooldown(long length) {
        this(System.currentTimeMillis(), length, Message.CMD_COOLDOWN_GLOBAL);
    }

    public Cooldown(long length, Message message) {
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
