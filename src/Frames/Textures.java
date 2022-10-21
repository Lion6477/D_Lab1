package Frames;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;

public class Textures {
    int intTexture; //посилання на текстуру
    Texture texture;
    int loadTexture(String fileName) {
        texture = null;
        try {
            texture = TextureIO.newTexture(new File(fileName), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return texture.getTextureObject();
    }
}
