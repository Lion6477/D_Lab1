package Frames;

class Matrix {
    public static float[] matrix(float[] matrix1, float[] matrix2){
        float result[] = new float[16];
    // l = 4 * i + j

        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                for(int k=0; k<4; k++){
                    result[4 * i + j] += matrix1[4 * i + k] * matrix2[4 * k + j];
                }
            }
        }
        return result;
    }

    static float [] Ox (float phi) {
        float [] A = new float[] {
                1,  0,  0,  0,
                0,  (float) Math.cos(Math.toRadians(phi)), -(float) Math.sin(Math.toRadians(phi)),   0,
                0,  (float) Math.sin(Math.toRadians(phi)),  (float) Math.cos(Math.toRadians(phi)),  0,
                0,  0,  0,  1
        };
        return A; //Тіло методу
    };
    static float [] Oy (float phi1) {
        float [] B = new float[] {
                (float) Math.cos(Math.toRadians(phi1)),  0,  -(float) Math.sin(Math.toRadians(phi1)),  0,
                0,  1,  0,   0,
                (float) Math.sin(Math.toRadians(phi1)),  0,  (float) Math.cos(Math.toRadians(phi1)),  0,
                0,  0,  0,  1
        };
        return B; //Тіло методу
    };

    static float [] Oz (float phi2) {
        float [] Z = new float[]{
                (float) Math.cos(Math.toRadians(phi2)),  -(float) Math.sin(Math.toRadians(phi2)),  0,  0,
                (float) Math.sin(Math.toRadians(phi2)),  (float) Math.cos(Math.toRadians(phi2)),  0,   0,
                0,  0,  1,  0,
                0,  0,  0,  1
        };
        return Z;
    };
    static float [] zooming (float B, float C, float D) {
        float [] X = new float[]{
                B,  0,  0,  0,
                0,  C,  0,  0,
                0,  0,  D,  0,
                0,  0,  0,  1
        };
        return X;
    };
    static float [] moving (float x, float y, float z) {
        float [] F = new float[]{
                1,  0,  0,  x,
                0,  1,  0,  y,
                0,  0,  1,  z,
                0,  0,  0,  1
        };
        return F;
    };
    static float [] rotate (float phi, float phi1, float phi2, float x, float y, float z){
        float result [] = moving(-x, -y, -z);

        result = matrix(Ox(phi), result);
        result = matrix(Oy(phi1), result);
        result = matrix(Oz(phi2), result);

        result = matrix(moving(x, y, z), result);

        return result;
    };

    void outputArr(float [] A) {
        for(int i=0;i<A.length; i++) {
            System.out.println(A[i]);
        }
    }

}