package Frames;

public class Triangles {
    float normalsVec[];
    float textXY[];
    int points_phi  = 30;
    int points_teta = 30;
    float X_1 = points_phi, Y_1 = points_teta;
    float phi = 0, phi1 = 0;
    float teta = 0, teta1 = 0;
    float x, y, z, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, x5, y5, z5;
    float X, Y, X1, Y1, X2, Y2, X3, Y3,  X4, Y4,  X5, Y5;
    float r = 1;


    public float[] sphere(){
        float arrayPositions[] = new float[(points_phi) * (points_teta) * 6 * 3];
        float normals[] = new float[(points_phi) * (points_teta) * 6 * 3];
        float textures[] = new float[(points_phi) * (points_teta) * 6 * 2];

        int k = 0;
        int l = 0;
        float len = 0;
        for(int i = 0; i < points_phi; i++){
            teta = 0;
            for(int j = 0; j < points_teta; j++){

                X = i/X_1;
                Y = j/Y_1;
                x = r * (float) Math.cos(Math.toRadians(phi)) * (float) Math.sin(Math.toRadians(teta));
                y = r * (float) Math.sin(Math.toRadians(phi)) * (float) Math.sin(Math.toRadians(teta));
                z = r * (float) Math.cos(Math.toRadians(teta));

                phi1 = phi + 360 / points_phi;

                X1 = (i + 1)/X_1;
                Y1 = j/Y_1;
                x1 = r * (float) Math.cos(Math.toRadians(phi1)) * (float) Math.sin(Math.toRadians(teta));
                y1 = r * (float) Math.sin(Math.toRadians(phi1)) * (float) Math.sin(Math.toRadians(teta));
                z1 = r * (float) Math.cos(Math.toRadians(teta));

                teta1 = teta + 180/points_teta;

                X2 = X1;
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

                teta += 180/points_teta;

                arrayPositions[k] = x;
                len = (float) Math.sqrt(x * x + y * y + z * z);
                normals[k] = x/len;
                textures[l] = X;
                l+=1;
                k += 1;
                arrayPositions[k] = y;
                normals[k] = y/len;
                textures[l] = Y;
                l+=1;
                k += 1;
                arrayPositions[k] = z;
                normals[k] = z/len;
                textures[l] = X1;
                l+=1;
                k += 1;
                arrayPositions[k] = x1;
                len = (float) Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
                normals[k] = x1/len;
                textures[l] = Y1;
                l+=1;
                k += 1;
                arrayPositions[k] = y1;
                normals[k] = y1/len;
                textures[l] = X2;
                l+=1;
                k += 1;
                arrayPositions[k] = z1;
                normals[k] = z1/len;
                textures[l] = Y2;
                l+=1;
                k += 1;
                arrayPositions[k] = x2;
                len = (float) Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2);
                normals[k] = x2/len;
                textures[l] = X3;
                l+=1;
                k += 1;
                arrayPositions[k] = y2;
                normals[k] = y2/len;
                textures[l] = Y3;
                l+=1;
                k += 1;
                arrayPositions[k] = z2;
                normals[k] = z2/len;
                textures[l] = X4;
                l+=1;
                k += 1;
                arrayPositions[k] = x3;
                len = (float) Math.sqrt(x3 * x3 + y3 * y3 + z3 * z3);
                normals[k] = x3/len;
                textures[l] = Y4;
                l+=1;
                k += 1;
                arrayPositions[k] = y3;
                normals[k] =y3/len;
                textures[l] = X5;
                l+=1;
                k += 1;
                arrayPositions[k] = z3;
                normals[k] = z3/len;
                textures[l] = Y5;
                l+=1;
                k += 1;
                arrayPositions[k] = x4;
                len = (float) Math.sqrt(x4 * x4 + y4 * y4 + z4 * z4);
                normals[k] = x4/len;
                k += 1;
                arrayPositions[k] = y4;
                normals[k] = y4/len;
                k += 1;
                arrayPositions[k] = z4;
                normals[k] = z4/len;
                k += 1;
                arrayPositions[k] = x5;
                len = (float) Math.sqrt(x5 * x5 + y5 * y5 + z5 * z5);
                normals[k] = x5/len;
                k += 1;
                arrayPositions[k] = y5;
                normals[k] = y5/len;
                k += 1;
                arrayPositions[k] = z5;
                normals[k] = z5/len;
                k += 1;
            }
            phi += 360 / points_phi;
        }

        textXY = textures;
        normalsVec = normals;
        return arrayPositions;
    }


}
