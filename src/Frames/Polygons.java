package Frames;

public class Polygons {
    float[] normalsVec, textXY;
    public float[] sphere(){
        float points_phi  = 30f, points_teta = 30f;
        float x, y, z, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, x5, y5, z5;
        float X, Y, X1, Y1, X2, Y2, X3, Y3,  X4, Y4,  X5, Y5;
        float r = 1;
        float X_1 = points_phi, Y_1 = points_teta;
        float phi = 0f, phi1 = 0f, teta = 0f, teta1 = 0f;

        int sphereArrayPositionsLength = (int) ((points_phi) * (points_teta) * 6 * 3);
        int sphereArrayNormalsLength = (int) ((points_phi) * (points_teta) * 6 * 3);
        int sphereArrayTexturesLength = (int) ((points_phi) * (points_teta) * 6 * 2);
        float[] arrayPositions = new float[sphereArrayPositionsLength];
        float[] arrayNormals = new float[sphereArrayNormalsLength];
        float[] arrayTextures = new float[sphereArrayTexturesLength];

        int k = 0, l = 0;
        float len;
        for(int i = 0; i < points_phi; i++){
            teta = 0;
            for(int j = 0; j < points_teta; j++){

                X = i/X_1;
                Y = j/Y_1;
                x = r * (float) Math.cos(Math.toRadians(phi)) * (float) Math.sin(Math.toRadians(teta));
                y = r * (float) Math.sin(Math.toRadians(phi)) * (float) Math.sin(Math.toRadians(teta));
                z = r * (float) Math.cos(Math.toRadians(teta));

                phi1 = phi + 360f / points_phi;

                X1 = (i + 1)/X_1;
                Y1 = j/Y_1;
                x1 = r * (float) Math.cos(Math.toRadians(phi1)) * (float) Math.sin(Math.toRadians(teta));
                y1 = r * (float) Math.sin(Math.toRadians(phi1)) * (float) Math.sin(Math.toRadians(teta));
                z1 = r * (float) Math.cos(Math.toRadians(teta));

                teta1 = teta + 180f / points_teta;

                X2 = X;
                Y2 = (j + 1)/Y_1;
                x2 = r * (float) Math.cos(Math.toRadians(phi)) * (float) Math.sin(Math.toRadians(teta1));
                y2 = r * (float) Math.sin(Math.toRadians(phi)) * (float) Math.sin(Math.toRadians(teta1));
                z2 = r * (float) Math.cos(Math.toRadians(teta1));

                X3 = X1;
                Y3 = Y1;
                x3 = x1;
                y3 = y1;
                z3 = z1;

                X4 = (i + 1)/X_1;
                Y4 = (j + 1)/Y_1;
                x4 = r * (float) Math.cos(Math.toRadians(phi1)) * (float) Math.sin(Math.toRadians(teta1));
                y4 = r * (float) Math.sin(Math.toRadians(phi1)) * (float) Math.sin(Math.toRadians(teta1));
                z4 = r * (float) Math.cos(Math.toRadians(teta1));

                X5 = X2;
                Y5 = Y2;
                x5 = x2;
                y5 = y2;
                z5 = z2;

                teta += 180f / points_teta;

                arrayPositions[k] = x;
                len = (float) Math.sqrt(x * x + y * y + z * z);
                arrayNormals[k] = x/len;
                arrayTextures[l] = X;
                l+=1;
                k += 1;
                arrayPositions[k] = y;
                arrayNormals[k] = y/len;
                arrayTextures[l] = Y;
                l+=1;
                k += 1;
                arrayPositions[k] = z;
                arrayNormals[k] = z/len;
                arrayTextures[l] = X1;
                l+=1;
                k += 1;
                arrayPositions[k] = x1;
                len = (float) Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
                arrayNormals[k] = x1/len;
                arrayTextures[l] = Y1;
                l+=1;
                k += 1;
                arrayPositions[k] = y1;
                arrayNormals[k] = y1/len;
                arrayTextures[l] = X2;
                l+=1;
                k += 1;
                arrayPositions[k] = z1;
                arrayNormals[k] = z1/len;
                arrayTextures[l] = Y2;
                l+=1;
                k += 1;
                arrayPositions[k] = x2;
                len = (float) Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2);
                arrayNormals[k] = x2/len;
                arrayTextures[l] = X3;
                l+=1;
                k += 1;
                arrayPositions[k] = y2;
                arrayNormals[k] = y2/len;
                arrayTextures[l] = Y3;
                l+=1;
                k += 1;
                arrayPositions[k] = z2;
                arrayNormals[k] = z2/len;
                arrayTextures[l] = X4;
                l+=1;
                k += 1;
                arrayPositions[k] = x3;
                len = (float) Math.sqrt(x3 * x3 + y3 * y3 + z3 * z3);
                arrayNormals[k] = x3/len;
                arrayTextures[l] = Y4;
                l+=1;
                k += 1;
                arrayPositions[k] = y3;
                arrayNormals[k] =y3/len;
                arrayTextures[l] = X5;
                l+=1;
                k += 1;
                arrayPositions[k] = z3;
                arrayNormals[k] = z3/len;
                arrayTextures[l] = Y5;
                l+=1;
                k += 1;
                arrayPositions[k] = x4;
                len = (float) Math.sqrt(x4 * x4 + y4 * y4 + z4 * z4);
                arrayNormals[k] = x4/len;
                k += 1;
                arrayPositions[k] = y4;
                arrayNormals[k] = y4/len;
                k += 1;
                arrayPositions[k] = z4;
                arrayNormals[k] = z4/len;
                k += 1;
                arrayPositions[k] = x5;
                len = (float) Math.sqrt(x5 * x5 + y5 * y5 + z5 * z5);
                arrayNormals[k] = x5/len;
                k += 1;
                arrayPositions[k] = y5;
                arrayNormals[k] = y5/len;
                k += 1;
                arrayPositions[k] = z5;
                arrayNormals[k] = z5/len;
                k += 1;
            }
            phi += 360f / points_phi;
        }

        textXY = arrayTextures;
        normalsVec = arrayNormals;
        return arrayPositions;
    }


}
