package com.ufo.cart.utils.other;

import com.ufo.cart.Client;
import com.ufo.cart.event.EventBus;
import com.ufo.cart.event.events.*;
import com.ufo.cart.event.listeners.*;
import com.ufo.cart.utils.other.RotationUtils;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

import static com.ufo.cart.Client.mc;


public final class RotatorManager implements PacketSendListener, ItemUseListener, AttackListener, PacketReceiveListener {
    private boolean enabled;
    private boolean rotateBack;
    private boolean resetRotation;
    private final EventBus eventManager = Client.INSTANCE.EVENT_BUS;
    private Rotation currentRotation;
    private float clientYaw, clientPitch;
    private float serverYaw, serverPitch;

    public RotatorManager() {
        eventManager.unregister(PacketSendListener.class, this);
        eventManager.unregister(AttackListener.class, this);
        eventManager.unregister(ItemUseListener.class, this);
        eventManager.unregister(PacketReceiveListener.class, this);


        enabled = true;
        rotateBack = false;
        resetRotation = false;

        this.serverYaw = 0;
        this.serverPitch = 0;

        this.clientYaw = 0;
        this.clientPitch = 0;
    }

    public void shutDown() {
        eventManager.unregister(PacketSendListener.class, this);
        eventManager.unregister(AttackListener.class, this);
        eventManager.unregister(ItemUseListener.class, this);
        eventManager.unregister(PacketReceiveListener.class, this);
    }

    public Rotation getServerRotation() {
        return new Rotation(serverYaw, serverPitch);
    }

    public void enable() {
        enabled = true;
        rotateBack = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void disable() {
        if (isEnabled()) {
            enabled = false;
            if (!rotateBack) rotateBack = true;
        }
    }

    public void setRotation(Rotation rotation) {
        currentRotation = rotation;
    }

    public void setRotation(double yaw, double pitch) {
        setRotation(new Rotation(yaw, pitch));
    }

    private void resetClientRotation() {
        mc.player.setYaw(clientYaw);
        mc.player.setPitch(clientPitch);

        resetRotation = false;
    }

    public void setClientRotation(Rotation rotation) {
        this.clientYaw = mc.player.getYaw();
        this.clientPitch = mc.player.getPitch();

        mc.player.setYaw((float) rotation.yaw());
        mc.player.setPitch((float) rotation.pitch());

        resetRotation = true;
    }

    public void setServerRotation(Rotation rotation) {
        this.serverYaw = (float) rotation.yaw();
        this.serverPitch = (float) rotation.pitch();
    }

    private boolean wasDisabled;

    @Override
    public void onAttack(AttackEvent event) {
        if (!isEnabled() && wasDisabled) {
            enabled = true;
            wasDisabled = false;
        }
    }

    @Override
    public void onItemUse(ItemUseEvent event) {
        if (!event.isCancelled() && isEnabled()) {
            enabled = false;
            wasDisabled = true;
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.packet instanceof PlayerMoveC2SPacket packet) {
            serverYaw = packet.getYaw(serverYaw);
            serverPitch = packet.getPitch(serverPitch);
        }
    }

    public void onSendMovementPackets() {
        if (isEnabled() && currentRotation != null) {
            setClientRotation(currentRotation);
            setServerRotation(currentRotation);

            return;
        }

        if (rotateBack) {
            Rotation serverRot = new Rotation(serverYaw, serverPitch);
            Rotation clientRot = new Rotation(mc.player.getYaw(), mc.player.getPitch());

            if (RotationUtils.getTotalDiff(serverRot, clientRot) > 1) {
                Rotation smoothRotation = RotationUtils.getSmoothRotation(serverRot, clientRot, 0.2);

                setClientRotation(smoothRotation);
                setServerRotation(smoothRotation);
            } else {
                rotateBack = false;
            }
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.packet instanceof PlayerPositionLookS2CPacket packet) {
            serverYaw = packet.getYaw();
            serverPitch = packet.getPitch();
        }
    }
}