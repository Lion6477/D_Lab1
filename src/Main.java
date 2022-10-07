import Frames.GLFrame1;
import Frames.GLFrame2;
import Frames.GLFrame3;
import Frames.GLFrame4;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;

public class Main {
    Scanner scan = new Scanner(System.in);
    public static float x;
    public static float y;
    public static float z;
    public static void main(String Args[]){

        //GLFrame1 frame1 = new GLFrame1();
        //GLFrame2 frame2 = new GLFrame2();
        //GLFrame3 frame3 = new GLFrame3();
        GLFrame4 frame4 = new GLFrame4();

        /* Frame Frame1 = new Frame();
        float result[][];

        float [][] matrix1=new float[][] {
                {11,12,13,14}, {21,22,23,24},
                {31,32,33,34}, {41,42,43,44}};

        float [][] matrix2=new float[][]{
                {11,12,13,14}, {21,22,23,24},
            {31,32,33,34}, {41,42,43,44}};

        result = Matrix.matrix(matrix1,matrix2);
        for (int i = 0; i< result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                System.out.println(result[i][j]);
            }
        }*/

        System.out.println("Successfully!");
    }
}

class Frame extends JFrame implements KeyListener {
    public Frame() {
        setSize(500,500);
        setLocation(100,100);
        setLocationRelativeTo(null);
        addKeyListener(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
        if(e.getKeyCode()==KeyEvent.VK_Q) {
            Main.x +=0.1;
            System.out.println(Main.x);
        }
        if(e.getKeyCode()==KeyEvent.VK_A) {
            Main.x -=0.1;
            System.out.println(Main.x);
        }
        if(e.getKeyCode()==KeyEvent.VK_W) {
            Main.y +=0.1;
            System.out.println(Main.y);
        }
        if(e.getKeyCode()==KeyEvent.VK_S) {
            Main.y -=0.1;
            System.out.println(Main.y);
        }
        if(e.getKeyCode()==KeyEvent.VK_E) {
            Main.z +=0.1;
            System.out.println(Main.z);
        }
        if(e.getKeyCode()==KeyEvent.VK_D) {
            Main.z -=0.1;
            System.out.println(Main.z);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

