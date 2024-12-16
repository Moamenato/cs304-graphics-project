package Game;

import Texture.TextureReader;

import javax.media.opengl.GLEventListener;
import java.awt.event.KeyListener;

public abstract class Anim_Listener implements GLEventListener, KeyListener {
    public static int maxWidth = 100, maxHeight = 100;
    public static String[] textureNames = {"Chicken.png", "Egg.png", "Egg 2.png", "Basket.png", "Heart 1.png", "Heart 2.png", "Back.png"};
    public static TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    public static int[] textures = new int[textureNames.length];
    public static String assetsFolder = ".//assets";
}