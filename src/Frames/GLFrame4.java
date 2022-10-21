package Frames;//рисує 2 трикутника. Один з них переміщується вздовж Ox, Oz при натисканні на клавіши
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.FloatBuffer;

import javax.swing.JFrame;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;

import static Frames.Matrix.inverseMat;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL4.*;// для GL_COLOR

public class GLFrame4 extends JFrame implements GLEventListener, KeyListener{
    private int[] vbo=new int[3];
    private int[] vao=new int[1];
    private float[] VPositions1;
    private int program, textureLink, textureLinkMercury, textureLinkVenus, textureLinkEarth, textureLinkMars, textureLinkJupiter, textureLinkSaturn, textureLinkUranus;//Текстуры планет
    private int textureLinkMoon;
    private GLCanvas Canvas;
    private final float x=0f, y=0f, z=0f;//Координаты звезды
    private final float q = 0.15f, w = 0.15f, r = 0.15f;//Размер звезды
    private final float xMercury = 0.12f, yMercury = 0.12f, zMercury = 0f;//Координаты Mercury
    private final float qMercury = 0.015f, wMercury = 0.015f, rMercury = 0.015f;//Размер Mercury
    private final float xVenus = 0.23f, yVenus = 0.23f, zVenus = 0f;//Координаты Venus
    private final float qVenus = 0.013f, wVenus = 0.013f, rVenus = 0.013f;//Размер Venus
    private final float xEarth = 0.32f, yEarth = 0.32f, zEarth = 0f;//Координаты Earth
    private final float qEarth = 0.013f, wEarth = 0.013f, rEarth = 0.013f;//Размер Earth
    private final float xMars = 0.5f, yMars = 0.5f, zMars = 0f;//Координаты Mars
    private final float qMars = 0.0073f, wMars = 0.0073f, rMars = 0.0073f;//Размер Mars
    private final float xJupiter = 0.17f, yJupiter = 0.17f, zJupiter = 0f;//Координаты Jupiter
    private final float qJupiter = 0.015f, wJupiter = 0.015f, rJupiter = 0.015f;//Размер Jupiter
    private final float xSaturn = 0.31f, ySaturn = 0.31f, zSaturn = 0f;//Координаты Saturn
    private final float qSaturn = 0.012f, wSaturn = 0.012f, rSaturn = 0.012f;//Размер Saturn
    private final float xUranus = 0.61f, yUranus = 0.61f, zUranus = 0f;//Координаты Uranus
    private final float qUranus = 0.0054f, wUranus = 0.0054f, rUranus = 0.0054f;//Размер Uranus
    private final float xMoon = 0.07f, yMoon = 0.07f, zMoon = 0f;//Координаты Moon
    private final float qMoon = 0.0037f, wMoon = 0.0037f, rMoon = 0.0037f;//Размер Moon


    float phiP, phiPP, phiS, phiSS;
    public GLFrame4() {
        Canvas = new GLCanvas();
        add(Canvas);
        Canvas.addGLEventListener(this);
        Canvas.setFocusable(false);
        addKeyListener(this);
        setSize(1000,1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private int MakeProgram() {
        int program;
        GL4 gl=(GL4) GLContext.getCurrentGL();
        String[] vSource = {
                "#version 430\n",
                "layout (location=0) in vec3 pos;"//змінна pos приймає дані з активного буфера
                + "layout (location=1) in vec3 normal;"
                        +"layout (location=2) in vec2 texturee;"
                        +"layout (binding=0) uniform sampler2D samp;"

                        + "uniform mat4 model;" //matB -- матриця 4х4, яка передається з Java
                        + "uniform mat4 normalMat;"

                        + "uniform float x;"
                        + "uniform float y;"
                        + "uniform float z;"

                        + "vec3 normalVec;"
                        + "out vec3 light;"
                        + "vec3 light_Ambient;"
                        + "vec3 material;"
                        + "vec3 light_Diffuse;"
                        + "vec3 light_intens;"
                        + "vec3 vecT;"
                        + "vec4 vecTemp;"

                        + "out vec4 varyingColor;"
                        + "out vec2 helpingTc;"

                        + "void main(void){"

                        + "helpingTc = texturee;"
                        + "light_Ambient = vec3(0.1, 0.1, 0.1);"
                        + "material = vec3(1, 0, 0);"
                        + "light_Diffuse = vec3(1, 1, 1);"
                        + "light_intens = vec3(1, 1, 1);"

                        //+ "gl_Position=matA*vec4(pos, 1);"// множення матриці на вектор. Дія "*" перевизначена у GLSL
                        + "gl_Position = model * vec4(pos, 1);"
                        + "vecT = vec3(0, 0, 0) - gl_Position.xyz;"
                        + "vecTemp = normalMat * vec4(normal, 1);"
                        + "normalVec = normalize(vecTemp.xyz);"
                        + "light_Diffuse = light_intens * max(dot(normalVec, vecT), 0);"
                        + "light = light_Ambient + light_Diffuse;"
                        + "varyingColor=vec4(light * material, 1);"
                        //+ "varyingColor=vec4(1, 0, 0, 1);"
                        + "}\n"
        },
                fSource = {
                        "#version 430\n",
                        "out vec4 Color;"
                                +"layout (binding=0) uniform sampler2D samp;"
                                + "in vec4 varyingColor;"
                                + "in vec2 helpingTc;"
                                + "in vec3 light;"
                                + "void main(void){"
                                + "Color = texture(samp, helpingTc) * vec4(light, 1);"
//                                + "Color=varyingColor;"
                                //+ "if(Color.b>0.5){discard;}"//якщо значення синього кольору >0.5, не рисувати

                                + "}\n"
                };
        int VShader=gl.glCreateShader(GL_VERTEX_SHADER);
        gl.glShaderSource(VShader, 2, vSource, null, 0);
        gl.glCompileShader(VShader);
        int FShader=gl.glCreateShader(GL_FRAGMENT_SHADER);
        gl.glShaderSource(FShader, 2, fSource, null,0);
        gl.glCompileShader(FShader);

        program=gl.glCreateProgram();
        gl.glAttachShader(program, VShader);
        gl.glAttachShader(program,FShader);
        gl.glLinkProgram(program);
        gl.glDeleteShader(VShader);
        gl.glDeleteShader(FShader);

        return program;
    }

    @Override
    public void display(GLAutoDrawable arg0) {
        GL4 gl=(GL4) GLContext.getCurrentGL();
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        float[] Col = {0.0f, 0.0f, 0.0f, 1f};
        FloatBuffer bkg = Buffers.newDirectFloatBuffer(Col);
        gl.glClearBufferfv(GL_COLOR, 0, bkg);
        //активація буфера
        gl.glBindBuffer(GL_ARRAY_BUFFER,  vbo[2]); //активізація буфера vbo[2]
        gl.glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(2); //layout (location=0) -- 0 - номер змінної у шейдері, яка приймає дані з буфера

        //TODO Звезда
        //активуємо texture unit No0 і зв’язуємо з texture object
        gl.glActiveTexture(GL_TEXTURE0);// номер текстури = 0, кількість -обмежена
        gl.glBindTexture(GL_TEXTURE_2D, textureLink);
        //float  [] B = Matrix.rotate(phi, phi1, phi2, 0.3f, 0.3f, 0f);
        float  [] X = Matrix.zooming(q, w, r);
        //float  [] F = Matrix.moving(x, y, z);

        float [] model = X;
        float [] modelInv = inverseMat(model);
        float [] normalMat = Matrix.inverse(modelInv);

        //передача змінної у шейдер
        int X_location=gl.glGetUniformLocation(program, "x"); // посилання на (положення) "x" у шейдері
        gl.glProgramUniform1f(program, X_location, x);  //передача значення Java змінної x у шейдер змінну за посиланням X_location
        gl.glUseProgram(program);//використовуєму програму program. Програм може бути багато!

        //передача матриці у шейдер. Матриця 4х4 - це ОДНОМІРНИЙ масив з 16 елементів
        int matModel_location=gl.glGetUniformLocation(program, "model"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matModel_location, 1, true, model, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        int matNormal_location=gl.glGetUniformLocation(program, "normalMat"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matNormal_location, 1, false, normalMat, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        int Y_location=gl.glGetUniformLocation(program, "y"); // посилання на (положення) "x" у шейдері
        gl.glProgramUniform1f(program, Y_location, y);  //передача значення Java змінної x у шейдер змінну за посиланням X_location

        int Z_location=gl.glGetUniformLocation(program, "z"); // посилання на (положення) "x" у шейдері
        gl.glProgramUniform1f(program, Z_location, z);  //передача значення Java змінної x у шейдер змінну за посиланням X_location

        //активація буфера
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]); //активізація буфера vbo[0]
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);//3 -- кількість елементів у приймаючій зміннії (vec3 - вектор ТРЬОХ елементів). Для vec2 необхідно писати "2"
        gl.glEnableVertexAttribArray(0); //layout (location=0) -- 0 - номер змінної у шейдері, яка приймає дані з буфера
        //Z-буферизація
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        //gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        gl.glDrawArrays(GL_TRIANGLES, 0, VPositions1.length / 3);//постановка у чергу на рисування 3 вершин з АКТИВНОГО буфера vbo[0]

        //TODO Планета Mercury
        //активуємо texture unit No0 і зв’язуємо з texture object
        gl.glActiveTexture(GL_TEXTURE0);// номер текстури = 0, кількість -обмежена
        gl.glBindTexture(GL_TEXTURE_2D, textureLinkMercury);

        float  [] XPMercury = Matrix.zooming(qMercury, wMercury, rMercury);
        float  [] BPMercury = Matrix.rotate(0, 0, phiP, 0, 0, 0);
        float  [] FPMercury = Matrix.moving(xMercury, yMercury, zMercury);
        float  [] BPPMercury = Matrix.rotate(0, 0, phiPP, 0, 0, 0);

        float  [] modelPMercury = Matrix.matrix(Matrix.matrix(Matrix.matrix(BPPMercury, FPMercury), BPMercury), XPMercury);
        normalMat = Matrix.inverse(modelPMercury);

        matNormal_location = gl.glGetUniformLocation(program, "normalMat"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matNormal_location, 1, false, normalMat, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        int matModel_locationPMercury=gl.glGetUniformLocation(program, "model"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matModel_locationPMercury, 1, true, modelPMercury, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        gl.glDrawArrays(GL_TRIANGLES, 0, VPositions1.length / 3);//постановка у чергу на рисування 3 вершин з АКТИВНОГО буфера vbo[0]

        //TODO Планета Venus
        //активуємо texture unit No0 і зв’язуємо з texture object
        gl.glActiveTexture(GL_TEXTURE0);// номер текстури = 0, кількість -обмежена
        gl.glBindTexture(GL_TEXTURE_2D, textureLinkVenus);

        float  [] XPVenus = Matrix.zooming(qVenus, wVenus, rVenus);
        float  [] BPVenus = Matrix.rotate(0, 0, phiP, 0, 0, 0);
        float  [] FPVenus = Matrix.moving(xVenus, yVenus, zVenus);
        float  [] BPPVenus = Matrix.rotate(0, 0, phiPP, 0, 0, 0);

        float  [] modelPVenus = Matrix.matrix(Matrix.matrix(Matrix.matrix(BPPVenus, FPVenus), BPVenus), XPVenus);
        normalMat = Matrix.inverse(modelPVenus);

        matNormal_location = gl.glGetUniformLocation(program, "normalMat"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matNormal_location, 1, false, normalMat, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        int matModel_locationPVenus=gl.glGetUniformLocation(program, "model"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matModel_locationPVenus, 1, true, modelPVenus, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        gl.glDrawArrays(GL_TRIANGLES, 0, VPositions1.length / 3);//постановка у чергу на рисування 3 вершин з АКТИВНОГО буфера vbo[0]

        //TODO Планета Земля
        //активуємо texture unit No0 і зв’язуємо з texture object
        gl.glActiveTexture(GL_TEXTURE0);// номер текстури = 0, кількість -обмежена
        gl.glBindTexture(GL_TEXTURE_2D, textureLinkEarth);

        float  [] XPEarth = Matrix.zooming(qEarth, wEarth, rEarth);
        float  [] BPEarth = Matrix.rotate(0, 0, phiP, 0, 0, 0);
        float  [] FPEarth = Matrix.moving(xEarth, yEarth, zEarth);
        float  [] BPPEarth = Matrix.rotate(0, 0, phiPP, 0, 0, 0);

        float  [] modelPEarth = Matrix.matrix(Matrix.matrix(Matrix.matrix(BPPEarth, FPEarth), BPEarth), XPEarth);
        normalMat = Matrix.inverse(modelPEarth);

        matNormal_location = gl.glGetUniformLocation(program, "normalMat"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matNormal_location, 1, false, normalMat, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        int matModel_locationPEarth = gl.glGetUniformLocation(program, "model"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matModel_locationPEarth, 1, true, modelPEarth, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        gl.glDrawArrays(GL_TRIANGLES, 0, VPositions1.length / 3);//постановка у чергу на рисування 3 вершин з АКТИВНОГО буфера vbo[0]

        //TODO Планета Mars
        //активуємо texture unit No0 і зв’язуємо з texture object
        gl.glActiveTexture(GL_TEXTURE0);// номер текстури = 0, кількість -обмежена
        gl.glBindTexture(GL_TEXTURE_2D, textureLinkMars);

        float  [] XPMars = Matrix.zooming(qMars, wMars, rMars);
        float  [] BPMars = Matrix.rotate(0, 0, phiP, 0, 0, 0);
        float  [] FPMars = Matrix.moving(xMars, yMars, zMars);
        float  [] BPPMars = Matrix.rotate(0, 0, phiPP, 0, 0, 0);

        float  [] modelPMars = Matrix.matrix(Matrix.matrix(Matrix.matrix(BPPMars, FPMars), BPMars), XPMars);
        normalMat = Matrix.inverse(modelPMars);

        matNormal_location = gl.glGetUniformLocation(program, "normalMat"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matNormal_location, 1, false, normalMat, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        int matModel_locationPMars=gl.glGetUniformLocation(program, "model"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matModel_locationPMars, 1, true, modelPMars, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        gl.glDrawArrays(GL_TRIANGLES, 0, VPositions1.length / 3);//постановка у чергу на рисування 3 вершин з АКТИВНОГО буфера vbo[0]

        //TODO Планета Jupiter
        //активуємо texture unit No0 і зв’язуємо з texture object
        gl.glActiveTexture(GL_TEXTURE0);// номер текстури = 0, кількість -обмежена
        gl.glBindTexture(GL_TEXTURE_2D, textureLinkJupiter);

        float  [] XPJupiter = Matrix.zooming(qJupiter, wJupiter, rJupiter);
        float  [] BPJupiter = Matrix.rotate(0, 0, phiP, 0, 0, 0);
        float  [] FPJupiter = Matrix.moving(xJupiter, yJupiter, zJupiter);
        float  [] BPPJupiter = Matrix.rotate(0, 0, phiPP, 0, 0, 0);

        float  [] modelPJupiter = Matrix.matrix(Matrix.matrix(Matrix.matrix(BPPJupiter, FPJupiter), BPJupiter), XPJupiter);
        normalMat = Matrix.inverse(modelPJupiter);

        matNormal_location = gl.glGetUniformLocation(program, "normalMat"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matNormal_location, 1, false, normalMat, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        int matModel_locationPJupiter=gl.glGetUniformLocation(program, "model"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matModel_locationPJupiter, 1, true, modelPJupiter, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        gl.glDrawArrays(GL_TRIANGLES, 0, VPositions1.length / 3);//постановка у чергу на рисування 3 вершин з АКТИВНОГО буфера vbo[0]

        //TODO Планета Saturn
        //активуємо texture unit No0 і зв’язуємо з texture object
        gl.glActiveTexture(GL_TEXTURE0);// номер текстури = 0, кількість -обмежена
        gl.glBindTexture(GL_TEXTURE_2D, textureLinkSaturn);

        float  [] XPSaturn = Matrix.zooming(qSaturn, wSaturn, rSaturn);
        float  [] BPSaturn = Matrix.rotate(0, 0, phiP, 0, 0, 0);
        float  [] FPSaturn = Matrix.moving(xSaturn, ySaturn, zSaturn);
        float  [] BPPSaturn = Matrix.rotate(0, 0, phiPP, 0, 0, 0);

        float  [] modelPSaturn = Matrix.matrix(Matrix.matrix(Matrix.matrix(BPPSaturn, FPSaturn), BPSaturn), XPSaturn);
        normalMat = Matrix.inverse(modelPSaturn);

        matNormal_location = gl.glGetUniformLocation(program, "normalMat"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matNormal_location, 1, false, normalMat, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        int matModel_locationPSaturn=gl.glGetUniformLocation(program, "model"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matModel_locationPSaturn, 1, true, modelPSaturn, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        gl.glDrawArrays(GL_TRIANGLES, 0, VPositions1.length / 3);//постановка у чергу на рисування 3 вершин з АКТИВНОГО буфера vbo[0]

        //TODO Планета Uranus
        //активуємо texture unit No0 і зв’язуємо з texture object
        gl.glActiveTexture(GL_TEXTURE0);// номер текстури = 0, кількість -обмежена
        gl.glBindTexture(GL_TEXTURE_2D, textureLinkUranus);

        float  [] XPUranus = Matrix.zooming(qUranus, wUranus, rUranus);
        float  [] BPUranus = Matrix.rotate(0, 0, phiP, 0, 0, 0);
        float  [] FPUranus = Matrix.moving(xUranus, yUranus, zUranus);
        float  [] BPPUranus = Matrix.rotate(0, 0, phiPP, 0, 0, 0);

        float  [] modelPUranus = Matrix.matrix(Matrix.matrix(Matrix.matrix(BPPUranus, FPUranus), BPUranus), XPUranus);
        normalMat = Matrix.inverse(modelPUranus);

        matNormal_location = gl.glGetUniformLocation(program, "normalMat"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matNormal_location, 1, false, normalMat, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        int matModel_locationPUranus=gl.glGetUniformLocation(program, "model"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matModel_locationPUranus, 1, true, modelPUranus, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        gl.glDrawArrays(GL_TRIANGLES, 0, VPositions1.length / 3);//постановка у чергу на рисування 3 вершин з АКТИВНОГО буфера vbo[0]

        //TODO Спутник-Луна
        //активуємо texture unit No0 і зв’язуємо з texture object
        gl.glActiveTexture(GL_TEXTURE0);// номер текстури = 0, кількість -обмежена
        gl.glBindTexture(GL_TEXTURE_2D, textureLinkMoon);
        float  [] XMoon = Matrix.zooming(qMoon, wMoon, rMoon);
        float  [] BMoon = Matrix.rotate(0, 0, phiS, 0, 0, 0);
        float  [] FMoon = Matrix.moving(xMoon, yMoon, zMoon);
        float  [] BMoonMoon = Matrix.rotate(0, 0, phiSS, 0, 0, 0);

//        float  [] FSS = Matrix.moving(xSS, ySS, zSS);
//        float  [] BSSS = Matrix.rotate(0, 0, phiPP, 0, 0, 0);

        float  [] modelS = Matrix.matrix(BPPEarth,
                Matrix.matrix(FPEarth,
                        Matrix.matrix(BMoonMoon,
                                Matrix.matrix(FMoon,
                                        Matrix.matrix(BMoon, XMoon)))));
        normalMat = Matrix.inverse(modelS);
        matNormal_location=gl.glGetUniformLocation(program, "normalMat"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matNormal_location, 1, false, normalMat, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        int matModel_locationS=gl.glGetUniformLocation(program, "model"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matModel_locationS, 1, true, modelS, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        gl.glDrawArrays(GL_TRIANGLES, 0, VPositions1.length / 3);//постановка у чергу на рисування 3 вершин з АКТИВНОГО буфера vbo[0]

    //активація буфера
        gl.glBindBuffer(GL_ARRAY_BUFFER,  vbo[1]); //активізація буфера vbo[1]
        gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(1); //layout (location=0) -- 0 - номер змінної у шейдері, яка приймає дані з буфера

////передача змінної у шейдер
//        gl.glProgramUniform1f(program, X_location, 0);// записуємо 0 у шейдер змінну за посиланням X_location. Посилання на змінну вже отримали раніше
//        //gl.glDisable(GL_DEPTH_TEST);//відключення Z-буферизації
//
//        gl.glDrawArrays(GL_TRIANGLES, 0, 3); //постановка у чергу на рисування 3 вершин з активного буфера vbo[1]
    }
    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void init(GLAutoDrawable arg0) {
        Polygons sphere = new Polygons();
        Textures texters = new Textures();
        VPositions1 = sphere.sphere();
        textureLink = texters.loadTexture("SunTexture.jpg");
        textureLinkMercury = texters.loadTexture("Mercury.jpg");
        textureLinkVenus = texters.loadTexture("Venus.jpg");
        textureLinkEarth = texters.loadTexture("EarthTexture.jpg");
        textureLinkMars = texters.loadTexture("Mars.jpg");
        textureLinkJupiter = texters.loadTexture("Jupiter.jpg");
        textureLinkSaturn = texters.loadTexture("Saturn.jpg");
        textureLinkUranus = texters.loadTexture("Uranus.jpg");

        textureLinkMoon = texters.loadTexture("MoonTexture.jpg");

        GL4 gl=(GL4) GLContext.getCurrentGL();
        program=MakeProgram();
        gl.glGenVertexArrays(1, vao,0);
        gl.glBindVertexArray(vao[0]);
        gl.glGenBuffers(3, vbo, 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);// 0-й буфер АКТИВНИЙ vbo[0]
        FloatBuffer f= Buffers.newDirectFloatBuffer(VPositions1);
        gl.glBufferData(GL_ARRAY_BUFFER, f.limit()*4, f, GL_STATIC_DRAW);//записуємо у АКТИВНИЙ буфер vbo[0]

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);// 1-й буфер АКТИВНИЙ vbo[1]
        FloatBuffer f2=Buffers.newDirectFloatBuffer(sphere.normalsVec);
        gl.glBufferData(GL_ARRAY_BUFFER, f2.limit()*4, f2,GL_STATIC_DRAW);//записуємо у АКТИВНИЙ буфер vbo[1]

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[2]);// 2-й буфер АКТИВНИЙ vbo[2]
        FloatBuffer f3=Buffers.newDirectFloatBuffer(sphere.textXY);
        gl.glBufferData(GL_ARRAY_BUFFER, f3.limit()*4, f3,GL_STATIC_DRAW);//записуємо у АКТИВНИЙ буфер vbo[2]

    }
    @Override
    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
        // TODO Auto-generated method stub

    }
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
            System.exit(1);
        }
        if(e.getKeyCode()==KeyEvent.VK_SPACE) {
            phiP += 1f ;
            phiPP += 5f;
            phiS += 10f;
            phiSS += 7f;

        }
        Canvas.display();
    }
    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

}