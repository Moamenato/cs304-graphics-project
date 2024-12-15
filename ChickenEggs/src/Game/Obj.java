package Game;
import javax.media.opengl.GL;
import com.sun.opengl.util.GLUT;
import static Game.Anim_Listener.*;

public class Obj {

    public Obj() {}

    public double sqrDistance(int x1, int y1, int x2, int y2) {
        return Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
    }

    public void drawString(GL gl, GLUT glut, String status, float xPos, float yPos) {
        gl.glRasterPos2f(xPos, yPos);
        for (char c : status.toCharArray()) {
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
        }
    }

    public void setOrgCoOrdVertex(GL gl) {
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
    }

    public void drawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textures.length - 1]);
        gl.glPushMatrix();
        setOrgCoOrdVertex(gl);
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    public void drawSprite(GL gl, float x, float y, int index, float scaleX, float scaleY) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scaleX, 0.1 * scaleY, 1);
        setOrgCoOrdVertex(gl);
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }
}