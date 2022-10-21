package Frames;

import java.util.Arrays;

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

    static public float [] inverse(float [] A){
        float [] B = Arrays.copyOf(A, A.length);
        float [] res = new float[] {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
        float max,max1,d;
        int i,j,k,num,NumOfElGauss =4;
        for (i = 0;i<= NumOfElGauss-1;i++){
            max = B[4*i+i];
            num = i;
            for(j=i;j<=NumOfElGauss-1;j++){
                if (Math.abs(max) < Math.abs(B[4*j+i])) {
                    max = B[4*j+i];
                    num = j;
                }
            }
            max1=1f/max;
            for (j = 0;j<= NumOfElGauss-1;j++){
                d = B[4*i+j];
                B[4*i+j] = B[4*num+j];
                B[4*num+j] = d;
                B[4*i+j] = B[4*i+j] * max1;
                d = res[4*i+j];
                res[4*i+j] = res[4*num+j];
                res[4*num+j] = d;
                res[4*i+j] = res[4*i+j] * max1;
            }

            for (j = 0;j<= NumOfElGauss-1;j++){
                if (j != i){
                    d = B[4*j+i];
                    for (k = i;k<=NumOfElGauss-1;k++){
                        B[4*j+k] = B[4*j+k] - B[4*i+k]*d;
                    }
                    for (k = 0;k<=NumOfElGauss-1;k++){
                        res[4*j+k] = res[4*j+k] - res[4*i+k]*d;
                    }
                }
            }
        }

        return res;
    }

    static public float[] inverseMat(float[] matrix){
        float [] matrixNew = new float[matrix.length];
        for(int i = 0; i < matrix.length - 1; i++){
            matrixNew[i] = - matrix[i];
        }
        matrixNew[matrixNew.length -1] = 1;
        return matrixNew;
    }
    void outputArr(float [] A) {
        for(int i=0;i<A.length; i++) {
            System.out.println(A[i]);
        }
    }

}