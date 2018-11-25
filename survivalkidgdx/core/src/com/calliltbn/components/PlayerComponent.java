package com.calliltbn.components;

import com.badlogic.ashley.core.Component;
import com.calliltbn.spritesdef.SpriteAnimationEnum;

public class PlayerComponent implements Component {

    public enum PlayerAnimation {
        STAND, RUN, JUMP, FALL, KNOCK_BACK;
    }

    public enum Perso {
        YUNA, YUGO;
    }

    public Perso perso;

    private float jumpDuration = 0;

    private boolean knockBack;

    public SpriteAnimationEnum getSpriteAnim(PlayerAnimation anim) {
        if (knockBack) {
            return (perso  == Perso.YUNA)? SpriteAnimationEnum.YUNA_KNOCKBACK :
                SpriteAnimationEnum.YUGO_KNOCKBACK;
        }
        switch(anim) {
            case STAND:
                return null;
            case RUN:
                return  (perso  == Perso.YUNA)? SpriteAnimationEnum.YUNA_RUN :
                    SpriteAnimationEnum.YUGO_RUN;
            case JUMP:
                return  (perso  == Perso.YUNA)? SpriteAnimationEnum.YUNA_JUMP :
                        SpriteAnimationEnum.YUGO_JUMP;
            case FALL:
                return  (perso  == Perso.YUNA)? SpriteAnimationEnum.YUNA_FALL :
                        SpriteAnimationEnum.YUGO_FALL;
         }
         return null;
    }


    public boolean isKnockBack() {
        return knockBack;
    }

    public void setKnockBack(boolean knockBack) {
        this.knockBack = knockBack;
    }

    public float getJumpDuration() {
        return jumpDuration;
    }

    public void setJumpDuration(float jumpDuration) {
        this.jumpDuration = jumpDuration;
    }
}
