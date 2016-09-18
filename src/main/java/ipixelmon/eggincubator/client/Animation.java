package ipixelmon.eggincubator.client;

import com.pixelmonmod.pixelmon.client.models.animations.EnumRotation;
import javafx.scene.transform.Translate;
import net.minecraft.util.Rotations;

import java.util.ArrayList;
import java.util.List;

public class Animation
{

    private List<Object> actions;
    private long startTime, waitTime;
    private float rotX, rotY, rotZ, posX, posY, posZ, scalar;

    private Animation()
    {
        actions = new ArrayList();
    }

    public static Animation newInstance()
    {
        return new Animation();
    }

    public Animation rotate(float rotX, float rotY, float rotZ)
    {
        actions.add(new SimpleRotation(rotX, rotY, rotZ));
        return this;
    }

    public Animation rotateTo(EnumRotation axis, float degree, float increment)
    {
        actions.add(new ComplexRotation(axis, degree, increment));
        return this;
    }

    public Animation translate(float x, float y, float z)
    {
        actions.add(new SimpleTranslation(x, y, z));
        return this;
    }

    public Animation translateTo(EnumRotation axis, float position, float increment)
    {
        actions.add(new ComplexTranslation(axis, position, increment));
        return this;
    }

    public Animation scale(float scalar)
    {
        actions.add(new SimpleScalar(scalar));
        return this;
    }

    public Animation scaleTo(float scalar, float increment)
    {
        actions.add(new ComplexScalar(scalar, increment));
        return this;
    }

    public Animation wait(int milliseconds)
    {
        actions.add(new Wait(milliseconds));
        return this;
    }

    public void animate()
    {
        if (actions.isEmpty())
        {
            return;
        }

        Object currentAnimation = actions.get(0);

        if (currentAnimation instanceof SimpleRotation)
        {
            SimpleRotation simpleRotation = (SimpleRotation) currentAnimation;
            simpleRotation.execute();
            actions.remove(0);
        } else if (currentAnimation instanceof ComplexRotation)
        {
            ComplexRotation complexRotation = (ComplexRotation) currentAnimation;
            complexRotation.execute();

            if (complexRotation.done)
            {
                actions.remove(0);
            }
        } else if (currentAnimation instanceof SimpleTranslation)
        {
            SimpleTranslation simpleTranslation = (SimpleTranslation) currentAnimation;
            simpleTranslation.execute();
            actions.remove(0);
        } else if (currentAnimation instanceof ComplexTranslation)
        {
            ComplexTranslation complexTranslation = (ComplexTranslation) currentAnimation;
            complexTranslation.execute();

            if(complexTranslation.done)
            {
                actions.remove(0);
            }
        } else if (currentAnimation instanceof SimpleScalar)
        {
            SimpleScalar simpleScalar = (SimpleScalar) currentAnimation;
            simpleScalar.execute();
            actions.remove(0);
        } else if (currentAnimation instanceof ComplexScalar)
        {
            ComplexScalar complexScalar = (ComplexScalar) currentAnimation;
            complexScalar.execute();

            if(complexScalar.done)
            {
                actions.remove(0);
            }
        } else if (currentAnimation instanceof Wait)
        {
            Wait wait = (Wait) currentAnimation;
            wait.start();
            if (wait.done)
            {
                actions.remove(0);
            }
        }

    }

    public float rotX()
    {
        return rotX;
    }

    public float rotY()
    {
        return rotY;
    }

    public float rotZ()
    {
        return rotZ;
    }

    public float posX()
    {
        return posX;
    }

    public float posY()
    {
        return posY;
    }

    public float posZ()
    {
        return posZ;
    }

    public float scalar()
    {
        return scalar;
    }

    private void setRotX(final float rotX)
    {
        this.rotX = rotX;
    }

    private void setRotY(final float rotY)
    {
        this.rotY = rotY;
    }

    private void setRotZ(final float rotZ)
    {
        this.rotZ = rotZ;
    }

    private void setPosX(final float posX)
    {
        this.posX = posX;
    }

    private void setPosY(final float posY)
    {
        this.posY = posY;
    }

    private void setPosZ(final float posZ)
    {
        this.posZ = posZ;
    }

    private void setScalar(final float scalar)
    {
        this.scalar = scalar;
    }

    private class SimpleRotation
    {
        private float rotX, rotY, rotZ;

        public SimpleRotation(float rotX, float rotY, float rotZ)
        {
            this.rotX = rotX;
            this.rotY = rotY;
            this.rotZ = rotZ;
        }

        public void execute()
        {
            setRotX(rotX);
            setRotY(rotY);
            setRotZ(rotZ);
        }

    }

    private class ComplexRotation
    {
        private EnumRotation axis;
        private float degree, increment;
        private boolean done = false;
        private boolean isLess = false;
        private boolean started = false;

        public ComplexRotation(EnumRotation axis, float degree, float increment)
        {
            this.axis = axis;
            this.degree = degree;
            this.increment = increment;
        }

        public void execute()
        {
            float rotation = axis == EnumRotation.x ? rotX() : axis == EnumRotation.y ? rotY() : rotZ();
            if (!started)
            {
                isLess = rotation < degree;
                started = true;
            }

            if (isLess && rotation < degree)
            {
                if (axis == EnumRotation.x)
                {
                    setRotX(rotation + increment > degree ? degree : rotation + increment);
                } else if (axis == EnumRotation.y)
                {
                    setRotY(rotation + increment > degree ? degree : rotation + increment);
                } else
                {
                    setRotZ(rotation + increment > degree ? degree : rotation + increment);
                }

            }

            if (!isLess && rotation > degree)
            {
                if (axis == EnumRotation.x)
                {
                    setRotX(rotation - increment < degree ? degree : rotation - increment);
                } else if (axis == EnumRotation.y)
                {
                    setRotY(rotation - increment < degree ? degree : rotation - increment);
                } else
                {
                    setRotZ(rotation - increment < degree ? degree : rotation - increment);
                }
            }

            if (rotation == degree)
            {
                done = true;
            }
        }
    }

    private class SimpleTranslation
    {
        private float posX, posY, posZ;

        public SimpleTranslation(float posX, float posY, float posZ)
        {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
        }

        public void execute()
        {
            setPosX(posX);
            setPosY(posY);
            setPosZ(posZ);
        }
    }

    private class ComplexTranslation
    {
        private EnumRotation axis;
        private float position, increment;
        private boolean done = false;
        private boolean isLess = false;
        private boolean started = false;

        public ComplexTranslation(EnumRotation axis, float position, float increment)
        {
            this.axis = axis;
            this.position = position;
            this.increment = increment;
        }

        public void execute()
        {
            float rotation = axis == EnumRotation.x ? posX() : axis == EnumRotation.y ? posY() : posZ();
            if (!started)
            {
                isLess = rotation < position;
                started = true;
            }

            if (isLess && rotation < position)
            {
                if (axis == EnumRotation.x)
                {
                    setPosX(rotation + increment > position ? position : rotation + increment);
                } else if (axis == EnumRotation.y)
                {
                    setPosY(rotation + increment > position ? position : rotation + increment);
                } else
                {
                    setPosZ(rotation + increment > position ? position : rotation + increment);
                }

            }

            if (!isLess && rotation > position)
            {
                if (axis == EnumRotation.x)
                {
                    setPosX(rotation - increment < position ? position : rotation - increment);
                } else if (axis == EnumRotation.y)
                {
                    setPosY(rotation - increment < position ? position : rotation - increment);
                } else
                {
                    setPosZ(rotation - increment < position ? position : rotation - increment);
                }
            }

            if (rotation == position)
            {
                done = true;
            }
        }
    }

    private class SimpleScalar
    {
        private float scalar;

        public SimpleScalar(float scalar)
        {
            this.scalar = scalar;
        }

        public void execute()
        {
            setScalar(scalar);
        }
    }

    private class ComplexScalar
    {
        private float scalar;
        private float increment;
        private boolean done = false;
        private boolean isLess = false;
        private boolean started = false;

        public ComplexScalar(float scalar, float increment)
        {
            this.scalar = scalar;
            this.increment = increment;
        }

        public void execute()
        {

            if (!started)
            {
                isLess = scalar() < scalar;
                started = true;
            }

            if (isLess && scalar() < scalar)
            {
                setScalar(scalar() + increment > scalar ? scalar : scalar() + increment);
            }

            if (!isLess && scalar() > scalar)
            {
                setScalar(scalar() - increment < scalar ? scalar : scalar() - increment);
            }

            if (scalar() == scalar)
            {
                done = true;
            }
        }
    }

    private class Wait
    {
        private long duration;
        private boolean started = false;
        private long startTime;
        private boolean done = false;

        public Wait(long duration)
        {
            this.duration = duration;
        }

        public void start()
        {
            if (!started)
            {
                started = true;
                startTime = System.currentTimeMillis();
            } else
            {
                done = System.currentTimeMillis() - startTime > duration;
            }
        }

    }
}
