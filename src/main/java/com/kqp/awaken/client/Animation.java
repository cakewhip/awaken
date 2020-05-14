package com.kqp.awaken.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.entity.Entity;

public abstract class Animation<T extends Entity, M extends Model> {
    public float duration;
    public boolean loop;

    private State state = State.STOPPED;

    private float tickTimer;

    public Animation(boolean loop) {
        this.loop = loop;
    }

    public Animation<T, M> setDuration(float duration) {
        this.duration = duration;

        return this;
    }

    public void render(T entity, M model) {
        if (state != State.STOPPED) {
            float tickDelta = MinecraftClient.getInstance().getTickDelta();

            if (state == State.PLAYING) {
                tickTimer += tickDelta;
                tickTimer %= duration;
            } else if (state == State.STOPPING) {
                if (tickTimer + tickDelta > duration) {
                    tickTimer = 0;
                    state = State.STOPPED;
                }
            }

            this.render(entity, model, MinecraftClient.getInstance().getTickDelta());
        }
    }

    public void stop() {
        state = State.STOPPING;
    }

    public void play() {
        state = State.PLAYING;
    }

    public void reset() {
        tickTimer = 0;
    }

    public State getState() {
        return state;
    }

    protected abstract void render(T entity, M model, float tickDelta);

    public enum State {
        STOPPED,
        STOPPING,
        PLAYING;
    }
}
