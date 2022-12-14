package de.legoshi.linkcraft.effectblock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Effect {
    private int id;
    private String command;
    private ExecutorType executor;

    public Effect(String command, ExecutorType executor) {
        this.command = command;
        this.executor = executor;
    }
}
