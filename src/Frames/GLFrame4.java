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

import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL4.*;// для GL_COLOR

public class GLFrame4 extends JFrame implements GLEventListener, KeyListener{
    private int vbo[]=new int[2];
    private int vao[]=new int[1];
    //private float VPositions1[]= {0.2f, 0.2f,   0f,   0.2f,	0.5f,  0f,   0.5f, 0.1f, 0f};
    //                            X1    Y1      Z1    X2    Y2     Z2    X3    Y3    Z3
    /*private float VPositions1[]= {0.3f,   0.3f,   0f,   0.3f,   0.8f,   0f,     0.8f,   0.3f,    0f,
                                  0.3f,   0.3f,   0f,   0.3f,   0.8f,   0f,     0.6f,   0.4f,    0.8f,
                                  0.3f,   0.3f,   0f,   0.6f,   0.4f,   0.8f,   0.8f,   0.3f,    0f,
                                  0.3f,   0.8f,   0f,   0.8f,   0.3f,   0f,     0.6f,    0.4f,   0.8f
    };*/

    private float VPositions1[] = {

        };
    private float VPositions2[]= {-0.2f, -0.2f, 0.0f, 0,    -0.2f, 0.0f, 0,    0f,   0.0f};
    private int program;
    private GLCanvas Canvas;
    private float x=0f;
    private float y=0f;
    private float z=0f;
    private float q = 1;
    private float w = 1;
    private float r = 1;
    float phi;
    float phi1;
    float phi2;

    public GLFrame4() {
        Canvas=new GLCanvas();
        add(Canvas);
        Canvas.addGLEventListener(this);
        Canvas.setFocusable(false);
        addKeyListener(this);
        setSize(500,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private int MakeProgram() {
        int program;
        GL4 gl=(GL4) GLContext.getCurrentGL();
        String vSource[]= {
                "#version 430\n",
                "layout (location=0) in vec3 pos;" //змінна pos приймає дані з активного буфера
                        + "uniform mat4 model;" //matB -- матриця 4х4, яка передається з Java

                        + "uniform float x;"
                        + "uniform float y;"
                        + "uniform float z;"

                        + "out vec4 varyingColor;"
                        + "void main(void){"
                        //+ "gl_Position=matA*vec4(pos, 1);"// множення матриці на вектор. Дія "*" перевизначена у GLSL

                        + "gl_Position=model* vec4(pos, 1);"
                        
                        + "varyingColor=vec4(0.0, 0.0+pos.z, 1.0, 1);"
                        + "}\n"
        },
                fSource[]= {
                        "#version 430\n",
                        "out vec4 Color;"
                                + "in vec4 varyingColor;"
                                + "void main(void){"
                                + "Color=varyingColor;"
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
        float Col[]= {0.0f, 0.0f, 0.0f, 1f};
        FloatBuffer bkg= Buffers.newDirectFloatBuffer(Col);
        gl.glClearBufferfv(GL_COLOR, 0, bkg);

        float  [] B = Matrix.rotate(phi, phi1, phi2, 0.3f, 0.3f, 0f);

        float  [] X = Matrix.zooming(q, w, r);
        float  [] F = Matrix.moving(x, y, z);

        float  [] model =
                Matrix.matrix(
                        Matrix.matrix(X, B), F);

        float []A=new float[] {1f,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};//одинична матриця

//передача змінної у шейдер		
        int X_location=gl.glGetUniformLocation(program, "x"); // посилання на (положення) "x" у шейдері
        gl.glProgramUniform1f(program, X_location, x);  //передача значення Java змінної x у шейдер змінну за посиланням X_location

        gl.glUseProgram(program);//використовуєму програму program. Програм може бути багато!
//передача матриці у шейдер. Матриця 4х4 - це ОДНОМІРНИЙ масив з 16 елементів
        int matModel_location=gl.glGetUniformLocation(program, "model"); //посилання на положення матриці matA у шейдері
        gl.glUniformMatrix4fv(matModel_location, 1, true, model, 0);//передача матриці A за посиланням matA_location у кількості 1, без транспонування


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

        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        gl.glDrawArrays(GL_TRIANGLES, 0, 12); //постановка у чергу на рисування 3 вершин з АКТИВНОГО буфера vbo[0]

//активація буфера		
        gl.glBindBuffer(GL_ARRAY_BUFFER,  vbo[1]); //активізація буфера vbo[1]
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(0); //layout (location=0) -- 0 - номер змінної у шейдері, яка приймає дані з буфера

//передача змінної у шейдер		
        gl.glProgramUniform1f(program, X_location, 0);// записуємо 0 у шейдер змінну за посиланням X_location. Посилання на змінну вже отримали раніше
        //gl.glDisable(GL_DEPTH_TEST);//відключення Z-буферизації

        gl.glDrawArrays(GL_TRIANGLES, 0, 3); //постановка у чергу на рисування 3 вершин з активного буфера vbo[1]
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(GLAutoDrawable arg0) {
        GL4 gl=(GL4) GLContext.getCurrentGL();
        program=MakeProgram();
        gl.glGenVertexArrays(1, vao,0);
        gl.glBindVertexArray(vao[0]);
        gl.glGenBuffers(2, vbo, 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);// 0-й буфер АКТИВНИЙ vbo[0]
        FloatBuffer f= Buffers.newDirectFloatBuffer(VPositions1);
        gl.glBufferData(GL_ARRAY_BUFFER, f.limit()*4, f, GL_STATIC_DRAW);//записуємо у АКТИВНИЙ буфер vbo[0]

        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);// 1-й буфер АКТИВНИЙ vbo[1]
        FloatBuffer f2=Buffers.newDirectFloatBuffer(VPositions2);
        gl.glBufferData(GL_ARRAY_BUFFER, f2.limit()*4, f2,GL_STATIC_DRAW);//записуємо у АКТИВНИЙ буфер vbo[1]

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
        if(e.getKeyCode()==KeyEvent.VK_I) {
            x=x-0.05f;
        }
        if(e.getKeyCode()==KeyEvent.VK_O) {
            x=x+0.05f;
        }
        if(e.getKeyCode()==KeyEvent.VK_K) {
            y=y-0.05f;
        }
        if(e.getKeyCode()==KeyEvent.VK_L) {
            y=y+0.05f;
        }
        if(e.getKeyCode()==KeyEvent.VK_P) {
            z=z-0.05f;
        }
        if(e.getKeyCode()==KeyEvent.VK_U) {
            z=z+0.05f;
        }
        if(e.getKeyCode()==KeyEvent.VK_A) {
            phi+=1;
        }
        if(e.getKeyCode()==KeyEvent.VK_D) {
            phi-=1;
        }
        if(e.getKeyCode()==KeyEvent.VK_Z) {
            phi1+=1;
        }
        if(e.getKeyCode()==KeyEvent.VK_X) {
            phi1-=1;
        }
        if(e.getKeyCode()==KeyEvent.VK_C) {
            phi2+=1;
        }
        if(e.getKeyCode()==KeyEvent.VK_V) {
            phi2-=1;
        }
        if(e.getKeyCode()==KeyEvent.VK_T) {
            q+=0.1;
        }
        if(e.getKeyCode()==KeyEvent.VK_Y) {
            q-=0.1;
        }
        if(e.getKeyCode()==KeyEvent.VK_G) {
            w+=0.1;
        }
        if(e.getKeyCode()==KeyEvent.VK_H) {
            w-=0.1;
        }
        if(e.getKeyCode()==KeyEvent.VK_B) {
            r+=0.1;
        }
        if(e.getKeyCode()==KeyEvent.VK_N) {
            r-=0.1;
        }




        Canvas.display();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

}