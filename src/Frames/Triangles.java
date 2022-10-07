package Frames;

public class Triangles {
    int points_phi  = 30;
    int points_teta = 30;
    float phi = 0, phi1, phi2, phi3, phi4, phi5 = 0;
    float teta = 0, teta1, teta2, teta3, teta4, teta5 = 0;
    float x, y, z, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, x5, y5, z5;
    float r = 1;

    public float[] sphere(){
        float arrayPositions[] = new float[(points_phi + 1) * (points_teta + 1) * 6 * 3];
        float normals[] = new float[(points_phi + 1) * (points_teta + 1) * 6 * 3];

        int k = 0;
        float len = 0;
        for(int i = 0; i <= 30; i++){
            for(int j = 0; j <= 30; j++){
                x = r * (float) Math.cos(Math.toRadians(phi)) * (float) Math.sin(Math.toRadians(teta));
                y = r * (float) Math.sin(Math.toRadians(phi)) * (float) Math.sin(Math.toRadians(teta));
                z = r * (float) Math.cos(Math.toRadians(teta));

                phi1 = phi + 360 / points_phi;

                x1 = r * (float) Math.cos(Math.toRadians(phi1)) * (float) Math.sin(Math.toRadians(teta));
                y1 = r * (float) Math.sin(Math.toRadians(phi1)) * (float) Math.sin(Math.toRadians(teta));
                z1 = r * (float) Math.cos(Math.toRadians(teta));

                teta1 = teta + 180/points_teta;

                x2 = r * (float) Math.cos(Math.toRadians(phi)) * (float) Math.sin(Math.toRadians(teta1));
                y2 = r * (float) Math.sin(Math.toRadians(phi)) * (float) Math.sin(Math.toRadians(teta1));
                z2 = r * (float) Math.cos(Math.toRadians(teta1));

                x3 = x1;
                y3 = y1;
                z3 = z1;

                x4 = r * (float) Math.cos(Math.toRadians(phi1)) * (float) Math.sin(Math.toRadians(teta1));
                y4 = r * (float) Math.sin(Math.toRadians(phi1)) * (float) Math.sin(Math.toRadians(teta1));
                z4 = r * (float) Math.cos(Math.toRadians(teta1));

                x5 = x2;
                y5 = y2;
                z5 = z2;

                teta += 180/points_teta;

                arrayPositions[k] = x;
                len = (float) Math.sqrt(x * x + y * y + z * z);
                normals[k] = x/len;
                k += 1;
                arrayPositions[k] = y;
                normals[k] = y/len;
                k += 1;
                arrayPositions[k] = z;
                normals[k] = z/len;
                k += 1;
                arrayPositions[k] = x1;
                len = (float) Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
                normals[k] = x1/len;
                k += 1;
                arrayPositions[k] = y1;
                normals[k] = y1/len;
                k += 1;
                arrayPositions[k] = z1;
                normals[k] = z1/len;
                k += 1;
                arrayPositions[k] = x2;
                len = (float) Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2);
                normals[k] = x2/len;
                k += 1;
                arrayPositions[k] = y2;
                normals[k] = y2/len;
                k += 1;
                arrayPositions[k] = z2;
                normals[k] = z2/len;
                k += 1;
                arrayPositions[k] = x3;
                len = (float) Math.sqrt(x3 * x3 + y3 * y3 + z3 * z3);
                normals[k] = x3/len;
                k += 1;
                arrayPositions[k] = y3;
                normals[k] =y3/len;
                k += 1;
                arrayPositions[k] = z3;
                normals[k] = z3/len;
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
        return arrayPositions;
    }


}
