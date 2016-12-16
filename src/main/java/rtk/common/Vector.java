package rtk.common;

public class Vector {
    public float x, y, z;

    public Vector(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector random(float length){
        Vector result = new Vector(
                (float) Common.random.nextGaussian(),
                (float) Common.random.nextGaussian(),
                (float) Common.random.nextGaussian()
        );
        result.multiply(length / result.length());
        return result;
    }

    public float lengthSq(){
        return x * x + y * y + z * z;
    }

    public float length(){
        return (float)Math.sqrt(lengthSq());
    }

    public void normalize(){
        divide(length());
    }

    public void multiply(float amount){
        x *= amount;
        y *= amount;
        z *= amount;
    }

    public void divide(float amount){
        x /= amount;
        y /= amount;
        z /= amount;
    }
}
