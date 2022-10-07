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
                k += 1;
                arrayPositions[k] = y2;
                k += 1;
                arrayPositions[k] = z2;
                k += 1;
                arrayPositions[k] = x3;
                k += 1;
                arrayPositions[k] = y3;
                k += 1;
                arrayPositions[k] = z3;
                k += 1;
                arrayPositions[k] = x4;
                k += 1;
                arrayPositions[k] = y4;
                k += 1;
                arrayPositions[k] = z4;
                k += 1;
                arrayPositions[k] = x5;
                k += 1;
                arrayPositions[k] = y5;
                k += 1;
                arrayPositions[k] = z5;
                k += 1;
            }
            phi += 360 / points_phi;
        }
        return arrayPositions;
    }


}
