package com.droidblossom.archive.util


object CapsuleSkinUtils {

    fun stringToMotionEnum(motionString: String): Motion {
        return Motion.valueOf(motionString.uppercase())
    }

    fun stringToRetargetEnum(retargetString: String): Retarget {
        return Retarget.valueOf(retargetString.uppercase())
    }

    fun mapMotionToRetarget(motion: Motion): Retarget {
        return when (motion) {
            Motion.JUMPING_JACKS -> Retarget.CMU
            Motion.DAB, Motion.JUMPING, Motion.WAVE_HELLO, Motion.ZOMBIE -> Retarget.FAIR
            Motion.JESSE_DANCE -> Retarget.ROKOKO
        }
    }

    fun mapMotionStringToRetarget(motionString: String): Retarget {
        val motion = stringToMotionEnum(motionString)
        return mapMotionToRetarget(motion)
    }
}

enum class Motion {
    JUMPING_JACKS,
    DAB,
    JESSE_DANCE,
    JUMPING,
    WAVE_HELLO,
    ZOMBIE
}

enum class Retarget {
    CMU, FAIR, ROKOKO
}