package Frames;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.FloatBuffer;

import javax.swing.JFrame;

import com.jogamp.common.nio.*;
import com.jogamp.opengl.GL4;
import static com.jogamp.opengl.GL4.*;// для GL_COLOR
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;


public class GLFrame1 extends JFrame implements KeyListener, GLEventListener{
    //клас нащадок JFrame з інтерфейсами обробки натискання на клавіатуру та подіями OpenGL
    private GLCanvas Canvas; //OpenGL канва
    public GLFrame1() { //конструктор класа
        Canvas=new GLCanvas(); //виклик конструктора канви. Ініціалізація OpenGL. При цьому створюється об'єкт GL4. Виклик init() --> display()
        Canvas.addGLEventListener(this);// (додавання слухача подій OpenGL канві)
        add(Canvas); // додавання канви на фрейм
        setSize(500,500); // розміри фрейму
        setLocationRelativeTo(null);// положення -- посередині головного вікна (монітору)
        addKeyListener(this); // додавання слухача подій
        setDefaultCloseOperation(EXIT_ON_CLOSE); //визначення операції при закритті вікна (фрейму)
        setVisible(true); //показати фрейм
    }

    @Override
    public void display(GLAutoDrawable drawable) {// метод, який викликається при перерисовці канви
        // TODO Auto-generated method stub



        GL4 gl = (GL4) GLContext.getCurrentGL(); // створення об'єкту OpenGL
        //GL4	gl	=	(GL4) drawable.getGL();
        float bkg[]= {1, 0,7, 0,8, 1f}; //колір
        FloatBuffer bkgBuffer=Buffers.newDirectFloatBuffer(bkg);// створення буфера кольору
        gl.glClearBufferfv(GL_COLOR, 0, bkgBuffer); // очистка канви (залиття кольором)
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {//викликається при закритті
        // TODO Auto-generated method stub

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int arg3, int arg4) {
        //викликаэться при зміні розмірів
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
            System.exit(1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }


}
