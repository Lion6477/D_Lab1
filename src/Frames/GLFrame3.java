package Frames;

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
import static com.jogamp.opengl.GL4.*;// для GL_COLOR

public class GLFrame3 extends JFrame implements GLEventListener, KeyListener {
    float Y;
    float X;
    GLCanvas Canvas;
    int rendering_program, vao[]= new int[1];

    public GLFrame3() {
        Y=0;
        X=0;
        Canvas=new GLCanvas();
        add(Canvas);
        Canvas.addGLEventListener(this);
        addKeyListener(this);
        setSize(500,500);
        setLocationRelativeTo(null);
        Canvas.setFocusable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    private int CreateShaderProgram() {
        GL4 gl= (GL4) GLContext.getCurrentGL();
        String vShaderSource[]= {//шейдер вершин
                "#version 430\n",
                "out	vec4	varyingColor;" // out змінна передається далі по конвеєру (у шейдер фрагментів)
                        + "uniform float y1;"//uniform змінні, які "приймаються" з Java
                        + "uniform float x1;"
                        + "void main(void){"
                        + "if (gl_VertexID == 0){gl_Position= vec4(0.1+x1, 0.0+y1, 0.0, 1);"
                        + "varyingColor=vec4(1.0, 0.0, 0.0, 1);}"
                        + "if (gl_VertexID == 1){gl_Position= vec4(0.0+x1, 0.0+y1, 0.0, 1);"
                        + "varyingColor=vec4(1.0, 0.0, 0.0, 1);}"
                        + "if (gl_VertexID == 2){gl_Position= vec4(0.0+x1, 0.2+y1, 0.0, 1);"
                        + "varyingColor=vec4(0.0, 0.0, 1.0, 1);}"


                        + "if (gl_VertexID == 3){gl_Position= vec4(0.1+x1, 0+y1, 0.0, 1);"
                        + "varyingColor=vec4(1.0, 0.0, 0.0, 1);}"
                        + "if (gl_VertexID == 4){gl_Position= vec4(0.1+x1, 0.2+y1, 0.0, 1);"
                        + "varyingColor=vec4(1.0, 0.0, 0.0, 1);}"
                        + "if (gl_VertexID == 5){gl_Position= vec4(0.0+x1, 0.2+y1, 0.0, 1);"
                        + "varyingColor=vec4(0.0, 0.0, 1.0, 1);}"


                        + "}\n"
        }, fShaderSource[]= {//шейдер фрагментів. Навіть якщо тут нічого не відбувається, він повинен бути
                "#version 430\n",
                "out vec4 color;"
                        + "in vec4 varyingColor;"
                        + "void main (void){"
                        + "color= varyingColor;"//колір їде даль по конвеєру
                        + "}\n"
        };
        int vShader=gl.glCreateShader(GL_VERTEX_SHADER);
        gl.glShaderSource(vShader,2,vShaderSource, null,0);
        gl.glCompileShader(vShader);
        int fShader=gl.glCreateShader(GL_FRAGMENT_SHADER);
        gl.glShaderSource(fShader,2,fShaderSource,null,0);
        gl.glCompileShader(fShader);
        int vfProgram=gl.glCreateProgram();
        gl.glAttachShader(vfProgram, vShader);
        gl.glAttachShader(vfProgram, fShader);
        gl.glLinkProgram(vfProgram);
        gl.glDeleteShader(vShader);
        gl.glDeleteShader(fShader);
        return vfProgram;
    }

    @Override
    public void display(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub
        int i,j;
        float y1f=Y;
        GL4 gl=(GL4) GLContext.getCurrentGL();
        float bkg[]= {1.0f,1.0f,1.0f,1.0f};
        FloatBuffer bkgBuffer=Buffers.newDirectFloatBuffer(bkg);
        gl.glClearBufferfv(GL_COLOR, 0, bkgBuffer);

        int y1_location=gl.glGetUniformLocation(rendering_program, "y1");//посилання на y1 у шейдері
        gl.glProgramUniform1f(rendering_program, y1_location,y1f); //запис значення Java-змінної y1f за посиланням
        int x1_location=gl.glGetUniformLocation(rendering_program, "x1");//посилання на x1 у шейдері
        gl.glProgramUniform1f(rendering_program, x1_location,X); //запис значення Java-змінної x1f за посиланням

        gl.glUseProgram(rendering_program);
        gl.glDrawArrays(GL_TRIANGLES, 0, 6); //рисуємо 3 вершини, об'єднані у трикутник
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(GLAutoDrawable arg0) {
        GL4 gl=(GL4) GLContext.getCurrentGL();
        rendering_program=CreateShaderProgram();
        gl.glGenVertexArrays(vao.length, vao,0);
        gl.glBindVertexArray(vao[0]);
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
            System.exit(0);
        }
        if(e.getKeyCode()==KeyEvent.VK_UP) {//при натисканні на клавішу ДОГОРИ значення змінної Y змінюється
            Y+=0.005;
            Canvas.display(); //виклик методу рисування
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN) {//при натисканні на клавішу ДОГОРИ значення змінної Y змінюється
            Y-=0.005;
            Canvas.display(); //виклик методу рисування
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT) {//при натисканні на клавішу ДОГОРИ значення змінної Y змінюється
            X-=0.005;
            Canvas.display(); //виклик методу рисування
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT) {//при натисканні на клавішу ДОГОРИ значення змінної Y змінюється
            X+=0.005;
            Canvas.display(); //виклик методу рисування
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }
}