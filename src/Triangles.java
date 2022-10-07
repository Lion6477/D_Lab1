
public class Triangles {
    static int points_phi  = 30;
    static int points_teta = 30;
    static float phi = 0, phi1, phi2, phi3, phi4, phi5 = 0;
    static float teta = 0, teta1, teta2, teta3, teta4, teta5 = 0;
    static float x, y, z, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, x5, y5, z5;
    static float r = 1;

    public static float[] sphere(){
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
            }


            phi += 360 / points_phi;
        }
        float arrayPositions[] = new float[(points_phi + 1) * (points_teta + 1) * 6 * 3];

        return null;
    }


}
