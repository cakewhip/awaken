package com.kqp.awaken.entity.attribute;

public interface RangedWeaponProjectile {
    Type getType();

    void setType(Type rangedWeaponType);

    enum Type {
        BOW,
        CROSSBOW,
        TRIDENT,
        NONE
    }
}
