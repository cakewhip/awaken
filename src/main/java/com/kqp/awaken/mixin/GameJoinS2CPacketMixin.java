package com.kqp.awaken.mixin;

import com.kqp.awaken.data.AwakenLevelDataTagContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameJoinS2CPacket.class)
@Implements(@Interface(iface = AwakenLevelDataTagContainer.class, prefix = "vw$"))
public class GameJoinS2CPacketMixin implements AwakenLevelDataTagContainer {
    private CompoundTag awakenLevelDataTag;

    @Inject(method = "read", at = @At("TAIL"))
    private void readPacket(PacketByteBuf buf, CallbackInfo callbackInfo) {
        awakenLevelDataTag = buf.readCompoundTag();
    }

    @Inject(method = "write", at = @At("TAIL"))
    private void writePacket(PacketByteBuf buf, CallbackInfo callbackInfo) {
        buf.writeCompoundTag(awakenLevelDataTag);
    }

    @Override
    public CompoundTag getAwakenLevelDataTag() {
        return awakenLevelDataTag;
    }

    @Override
    public void setAwakenLevelDataTag(CompoundTag awakenLevelDataTag) {
        this.awakenLevelDataTag = awakenLevelDataTag;
    }
}
