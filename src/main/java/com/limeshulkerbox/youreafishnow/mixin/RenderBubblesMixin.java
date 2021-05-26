package com.limeshulkerbox.youreafishnow.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ClientChatListener;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Mixin(InGameHud.class)
public abstract class RenderBubblesMixin extends DrawableHelper {

    @Shadow @Final private Random random;
    @Shadow @Final private MinecraftClient client;
    @Shadow private int ticks;
    @Shadow private int lastHealthValue;
    @Shadow private int renderHealthValue;
    @Shadow private long lastHealthCheckTime;
    @Shadow private long heartJumpEndTick;
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    @Shadow @Final private Map<MessageType, List<ClientChatListener>> listeners;

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow protected abstract LivingEntity getRiddenEntity();

    @Shadow protected abstract int getHeartCount(LivingEntity entity);

    @Shadow protected abstract int getHeartRows(int heartCount);

    @Inject(at = @At("HEAD"), method = "renderStatusBars", cancellable = true)
    private void renderStatusBars(MatrixStack matrices, CallbackInfo info) {
        //copied from base with changed lines marked with /*change*/

        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null) {
            int i = MathHelper.ceil(playerEntity.getHealth());
            boolean bl = this.heartJumpEndTick > (long)this.ticks && (this.heartJumpEndTick - (long)this.ticks) / 3L % 2L == 1L;
            long l = Util.getMeasuringTimeMs();
            if (i < this.lastHealthValue && playerEntity.timeUntilRegen > 0) {
                this.lastHealthCheckTime = l;
                this.heartJumpEndTick = (long)(this.ticks + 20);
            } else if (i > this.lastHealthValue && playerEntity.timeUntilRegen > 0) {
                this.lastHealthCheckTime = l;
                this.heartJumpEndTick = (long)(this.ticks + 10);
            }

            if (l - this.lastHealthCheckTime > 1000L) {
                this.lastHealthValue = i;
                this.renderHealthValue = i;
                this.lastHealthCheckTime = l;
            }

            this.lastHealthValue = i;
            int j = this.renderHealthValue;
            this.random.setSeed((long)(this.ticks * 312871));
            HungerManager hungerManager = playerEntity.getHungerManager();
            int k = hungerManager.getFoodLevel();
            int m = this.scaledWidth / 2 - 91;
            int n = this.scaledWidth / 2 + 91;
            int o = this.scaledHeight - 39;
            float f = (float)playerEntity.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH);
            int p = MathHelper.ceil(playerEntity.getAbsorptionAmount());
            int q = MathHelper.ceil((f + (float)p) / 2.0F / 10.0F);
            int r = Math.max(10 - (q - 2), 3);
            int s = o - (q - 1) * r - 10;
            int t = o - 10;
            int u = p;
            int v = playerEntity.getArmor();
            int w = -1;
            if (playerEntity.hasStatusEffect(StatusEffects.REGENERATION)) {
                w = this.ticks % MathHelper.ceil(f + 5.0F);
            }

            this.client.getProfiler().push("armor");

            int z;
            int aa;
            for(z = 0; z < 10; ++z) {
                if (v > 0) {
                    aa = m + z * 8;
                    if (z * 2 + 1 < v) {
                        this.drawTexture(matrices, aa, s, 34, 9, 9, 9);
                    }

                    if (z * 2 + 1 == v) {
                        this.drawTexture(matrices, aa, s, 25, 9, 9, 9);
                    }

                    if (z * 2 + 1 > v) {
                        this.drawTexture(matrices, aa, s, 16, 9, 9, 9);
                    }
                }
            }

            this.client.getProfiler().swap("health");

            int ai;
            int ad;
            int ae;
            for(z = MathHelper.ceil((f + (float)p) / 2.0F) - 1; z >= 0; --z) {
                aa = 16;
                if (playerEntity.hasStatusEffect(StatusEffects.POISON)) {
                    aa += 36;
                } else if (playerEntity.hasStatusEffect(StatusEffects.WITHER)) {
                    aa += 72;
                }

                int ab = 0;
                if (bl) {
                    ab = 1;
                }

                ai = MathHelper.ceil((float)(z + 1) / 10.0F) - 1;
                ad = m + z % 10 * 8;
                ae = o - ai * r;
                if (i <= 4) {
                    ae += this.random.nextInt(2);
                }

                if (u <= 0 && z == w) {
                    ae -= 2;
                }

                int af = 0;
                if (playerEntity.world.getLevelProperties().isHardcore()) {
                    af = 5;
                }

                this.drawTexture(matrices, ad, ae, 16 + ab * 9, 9 * af, 9, 9);
                if (bl) {
                    if (z * 2 + 1 < j) {
                        this.drawTexture(matrices, ad, ae, aa + 54, 9 * af, 9, 9);
                    }

                    if (z * 2 + 1 == j) {
                        this.drawTexture(matrices, ad, ae, aa + 63, 9 * af, 9, 9);
                    }
                }

                if (u > 0) {
                    if (u == p && p % 2 == 1) {
                        this.drawTexture(matrices, ad, ae, aa + 153, 9 * af, 9, 9);
                        --u;
                    } else {
                        this.drawTexture(matrices, ad, ae, aa + 144, 9 * af, 9, 9);
                        u -= 2;
                    }
                } else {
                    if (z * 2 + 1 < i) {
                        this.drawTexture(matrices, ad, ae, aa + 36, 9 * af, 9, 9);
                    }

                    if (z * 2 + 1 == i) {
                        this.drawTexture(matrices, ad, ae, aa + 45, 9 * af, 9, 9);
                    }
                }
            }

            LivingEntity livingEntity = this.getRiddenEntity();
            aa = this.getHeartCount(livingEntity);
            int ah;
            int al;
            if (aa == 0) {
                this.client.getProfiler().swap("food");

                for(ah = 0; ah < 10; ++ah) {
                    ai = o;
                    ad = 16;
                    int ak = 0;
                    if (playerEntity.hasStatusEffect(StatusEffects.HUNGER)) {
                        ad += 36;
                        ak = 13;
                    }

                    if (playerEntity.getHungerManager().getSaturationLevel() <= 0.0F && this.ticks % (k * 3 + 1) == 0) {
                        ai = o + (this.random.nextInt(3) - 1);
                    }

                    al = n - ah * 8 - 9;
                    this.drawTexture(matrices, al, ai, 16 + ak * 9, 27, 9, 9);
                    if (ah * 2 + 1 < k) {
                        this.drawTexture(matrices, al, ai, ad + 36, 27, 9, 9);
                    }

                    if (ah * 2 + 1 == k) {
                        this.drawTexture(matrices, al, ai, ad + 45, 27, 9, 9);
                    }
                }

                t -= 10;
            }

            this.client.getProfiler().swap("air");
            ah = playerEntity.getMaxAir();
            ai = Math.min(playerEntity.getAir(), ah);
            if (!playerEntity.isSubmergedIn(FluidTags.WATER) || ai < ah) {/*change*/
                ad = this.getHeartRows(aa) - 1;
                t -= ad * 10;
                ae = MathHelper.ceil((double)(ai - 2) * 10.0D / (double)ah);
                al = MathHelper.ceil((double)ai * 10.0D / (double)ah) - ae;

                for(int ar = 0; ar < ae + al; ++ar) {
                    if (ar < ae) {
                        this.drawTexture(matrices, n - ar * 8 - 9, t, 16, 18, 9, 9);
                    } else {
                        this.drawTexture(matrices, n - ar * 8 - 9, t, 25, 18, 9, 9);
                    }
                }
            }

            this.client.getProfiler().pop();
        }

        info.cancel();
    }
}
