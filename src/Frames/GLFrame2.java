package Frames;//Рисування точки
//Заливається фон білим кольором. Рисується 1 точка у центрі червоним кольором
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.FloatBuffer;

import javax.swing.JFrame;
import javax.swing.Spring;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import static com.jogamp.opengl.GL4.*;// для GL_COLOR

public class GLFrame2 extends JFrame implements GLEventListener, KeyListener {
    GLCanvas Canvas;
    private int rendering_program;
    private int vao[]= {1};

    public GLFrame2() {
        Canvas=new GLCanvas();
        add(Canvas);
        Canvas.addGLEventListener(this);
        addKeyListener(this);
        setSize(500,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        Canvas.setFocusable(false);

    }
    private int CreateShaderProgram () {//створення програми, яка об`єднує 2 шейдери. Повертає посилання на програму
        GL4 gl=(GL4) GLContext.getCurrentGL();
        String VShaderSource[]= { //шейдер вершин - масив рядків
                "#version 430\n", //версія бібліотеки
                "void main(void){" //функція main, точка входу у шейдер
                        + "gl_Position=vec4(0.5, 0.5, 0, 1);"//положення вершини - вектор у однорідних координатах
                        + "}\n" //gl_Position - змінна out, яка визначена в бібліотеці. Оголошувати не треба
        },
                FShaderSource[]= { //шейдер фрагменів - після растерізації попіксельно замальовує кольором
                        "#version 430\n",
                        "out vec4 color;"//out - змінна спускається далі по конвеєру
                                + "void main(void){"//функція main, точка входу у шейдер
                                + "color=vec4(0, 0, 0, 1);"// RGB + 1 - у однорідних координатах
                                + "}\n"
                };
        int VShader=gl.glCreateShader(GL_VERTEX_SHADER);
        gl.glShaderSource(VShader, 2, VShaderSource, null, 0);
        gl.glCompileShader(VShader);
        int FShader=gl.glCreateShader(GL_FRAGMENT_SHADER);
        gl.glShaderSource(FShader, 2,FShaderSource,null,0);
        gl.glCompileShader(FShader);

        int vfProgram=gl.glCreateProgram();
        gl.glAttachShader(vfProgram, VShader);
        gl.glAttachShader(vfProgram, FShader);
        gl.glLinkProgram(vfProgram);

        gl.glDeleteShader(VShader);
        gl.glDeleteShader(FShader);

        return vfProgram;
    }

    @Override
    public void display(GLAutoDrawable arg0) {// метод, який викликається при перерисовці канви
        GL4 gl=(GL4) GLContext.getCurrentGL();// створення об'єкту OpenGL
        //зафарбовує кольором фон:
        float bkg[]= {1.0f,1.0f,1.0f,1.0f};//колір
        FloatBuffer bkgBuffer=Buffers.newDirectFloatBuffer(bkg);// створення буферу кольору
        gl.glClearBufferfv(GL_COLOR, 0, bkgBuffer);// очистка канви (залиття кольором)
        //рисування 1 точки
        gl.glUseProgram(rendering_program); //використовувати програму
        gl.glPointSize(10); // розмір точки
        gl.glDrawArrays(GL_POINTS, 0, 1); //нарисувати (поставити у чергу!!!) точки у кількості 1

    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(GLAutoDrawable arg0) {
        GL4 gl=(GL4) GLContext.getCurrentGL();
        rendering_program=CreateShaderProgram();// створення, компіляція шейдер-програми. Повертає посилання на програму
        gl.glGenVertexArrays(vao.length, vao, 0);
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
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE) { // при натисканні на Escape вийти
            System.exit(0);
        }

    }
    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

}