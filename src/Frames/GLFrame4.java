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
    //private float VPositions1[]= {0.2f, 0.2f,   0f,   0.2f,	0.5f,  0f,   0.5f, 0.1f, 0f};
    //                            X1    Y1      Z1    X2    Y2     Z2    X3    Y3    Z3
    /*private float VPositions1[]= {0.3f,   0.3f,   0f,   0.3f,   0.8f,   0f,     0.8f,   0.3f,    0f,
                                  0.3f,   0.3f,   0f,   0.3f,   0.8f,   0f,     0.6f,   0.4f,    0.8f,
                                  0.3f,   0.3f,   0f,   0.6f,   0.4f,   0.8f,   0.8f,   0.3f,    0f,
                                  0.3f,   0.8f,   0f,   0.8f,   0.3f,   0f,     0.6f,    0.4f,   0.8f
    };*/
    private float[] VPositions1;
//    private float[] VPositions2= {-0.2f, -0.2f, 0.0f, 0,    -0.2f, 0.0f, 0,    0f,   0.0f};
    private int program, textureLink, textureLink1, textureLink2;
    private GLCanvas Canvas;
    private final float x=0f, y=0f, z=0f;
    private final float xP = 0.7f, yP = 0.7f, zP = 0f;
    private final float xS = 0.3f, yS = 0.3f, zS = 0f;
    private final float q = 0.3f, w = 0.3f, r = 0.3f;
    private final float qP = 0.2f, wP = 0.2f, rP = 0.2f;
    private final float qS = 0.1f, wS = 0.1f, rS = 0.1f;
    float phiP, phiPP, phiS, phiSS;
    public GLFrame4() {
        Canvas=new GLCanvas();
        add(Canvas);
        Canvas.addGLEventListener(this);
        Canvas.setFocusable(false);
        addKeyListener(this);
        setSize(1920,1080);
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
        FloatBuffer bkg= Buffers.newDirectFloatBuffer(Col);
        gl.glClearBufferfv(GL_COLOR, 0, bkg);
        //активація буфера
        gl.glBindBuffer(GL_ARRAY_BUFFER,  vbo[2]); //активізація буфера vbo[2]
        gl.glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(2); //layout (location=0) -- 0 - номер змінної у шейдері, яка приймає дані з буфера

        //Звезда
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

        //Планета Земля
        //активуємо texture unit No0 і зв’язуємо з texture object
        gl.glActiveTexture(GL_TEXTURE0);// номер текстури = 0, кількість -обмежена
        gl.glBindTexture(GL_TEXTURE_2D, textureLink1);

        float  [] XP = Matrix.zooming(qP, wP, rP);
        float  [] BP = Matrix.rotate(0, 0, phiP, 0, 0, 0);
        float  [] FP = Matrix.moving(xP, yP, zP);
        float  [] BPP = Matrix.rotate(0, 0, phiPP, 0, 0, 0);

        float  [] modelP = Matrix.matrix(Matrix.matrix(Matrix.matrix(BPP, FP), BP), XP);
        normalMat = Matrix.inverse(modelP);

        matNormal_location=gl.glGetUniformLocation(program, "normalMat"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matNormal_location, 1, false, normalMat, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        int matModel_locationP=gl.glGetUniformLocation(program, "model"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matModel_locationP, 1, true, modelP, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування

        gl.glDrawArrays(GL_TRIANGLES, 0, VPositions1.length / 3);//постановка у чергу на рисування 3 вершин з АКТИВНОГО буфера vbo[0]

        //Спутник-Луна
        //активуємо texture unit No0 і зв’язуємо з texture object
        gl.glActiveTexture(GL_TEXTURE0);// номер текстури = 0, кількість -обмежена
        gl.glBindTexture(GL_TEXTURE_2D, textureLink2);
        float  [] XS = Matrix.zooming(qS, wS, rS);
        float  [] BS = Matrix.rotate(0, 0, phiS, 0, 0, 0);
        float  [] FS = Matrix.moving(xS, yS, zS);
        float  [] BSS = Matrix.rotate(0, 0, phiSS, 0, 0, 0);

//        float  [] FSS = Matrix.moving(xSS, ySS, zSS);
//        float  [] BSSS = Matrix.rotate(0, 0, phiPP, 0, 0, 0);

        float  [] modelS = Matrix.matrix(BPP,
                Matrix.matrix(FP,
                        Matrix.matrix(BSS,
                                Matrix.matrix(FS,
                                        Matrix.matrix(BS, XS)))));
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
        textureLink1 = texters.loadTexture("EarthTexture.jpg");
        textureLink2 = texters.loadTexture("MoonTexture.jpg");

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