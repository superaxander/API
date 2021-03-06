/*
 * This file ("Texture.java") is part of the RockBottomAPI by Ellpeck.
 * View the source code at <https://github.com/Ellpeck/RockBottomAPI>.
 *
 * The RockBottomAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The RockBottomAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the RockBottomAPI. If not, see <http://www.gnu.org/licenses/>.
 */

package de.ellpeck.rockbottom.api.render;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.renderer.SGL;

import java.io.InputStream;

public class Texture extends Image{

    public Texture(Image other){
        super(other);
    }

    public Texture(){
    }

    public Texture(String ref) throws SlickException{
        super(ref);
    }

    public Texture(String ref, Color trans) throws SlickException{
        super(ref, trans);
    }

    public Texture(String ref, boolean flipped) throws SlickException{
        super(ref, flipped);
    }

    public Texture(String ref, boolean flipped, int filter) throws SlickException{
        super(ref, flipped, filter);
    }

    public Texture(String ref, boolean flipped, int f, Color transparent) throws SlickException{
        super(ref, flipped, f, transparent);
    }

    public Texture(int width, int height) throws SlickException{
        super(width, height);
    }

    public Texture(int width, int height, int f) throws SlickException{
        super(width, height, f);
    }

    public Texture(InputStream in, String ref, boolean flipped) throws SlickException{
        super(in, ref, flipped);
    }

    public Texture(InputStream in, String ref, boolean flipped, int filter) throws SlickException{
        super(in, ref, flipped, filter);
    }

    public Texture(ImageData data){
        super(data);
    }

    public Texture(ImageData data, int f){
        super(data, f);
    }

    public Texture getSubTexture(int x, int y, int width, int height){
        this.init();

        float newTextureOffsetX = ((x/(float)this.width)*this.textureWidth)+this.textureOffsetX;
        float newTextureOffsetY = ((y/(float)this.height)*this.textureHeight)+this.textureOffsetY;
        float newTextureWidth = ((width/(float)this.width)*this.textureWidth);
        float newTextureHeight = ((height/(float)this.height)*this.textureHeight);

        Texture sub = new Texture();
        sub.inited = true;
        sub.texture = this.texture;
        sub.textureOffsetX = newTextureOffsetX;
        sub.textureOffsetY = newTextureOffsetY;
        sub.textureWidth = newTextureWidth;
        sub.textureHeight = newTextureHeight;

        sub.width = width;
        sub.height = height;
        sub.ref = this.ref;
        sub.centerX = width/2;
        sub.centerY = height/2;

        return sub;
    }

    public void drawWithLight(float x, float y, float width, float height, Color[] light){
        this.drawWithLight(x, y, width, height, light, null);
    }

    public void drawWithLight(float x, float y, float width, float height, Color[] light, Color filter){
        if(filter != null){
            filter.bind();
        }

        this.texture.bind();

        GL.glTranslatef(x, y, 0);
        if(this.angle != 0){
            GL.glTranslatef(this.centerX, this.centerY, 0F);
            GL.glRotatef(this.angle, 0F, 0F, 1F);
            GL.glTranslatef(-this.centerX, -this.centerY, 0F);
        }

        GL.glBegin(SGL.GL_QUADS);
        this.drawEmbeddedWithLight(0, 0, width, height, light);
        GL.glEnd();

        if(this.angle != 0){
            GL.glTranslatef(this.centerX, this.centerY, 0F);
            GL.glRotatef(-this.angle, 0F, 0F, 1F);
            GL.glTranslatef(-this.centerX, -this.centerY, 0F);
        }
        GL.glTranslatef(-x, -y, 0);
    }

    public void drawEmbeddedWithLight(float x, float y, float width, float height, Color[] light){
        this.init();

        light[TOP_LEFT].bind();
        GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY);
        GL.glVertex3f(x, y, 0);
        light[BOTTOM_LEFT].bind();
        GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY+this.textureHeight);
        GL.glVertex3f(x, y+height, 0);
        light[BOTTOM_RIGHT].bind();
        GL.glTexCoord2f(this.textureOffsetX+this.textureWidth, this.textureOffsetY+this.textureHeight);
        GL.glVertex3f(x+width, y+height, 0);
        light[TOP_RIGHT].bind();
        GL.glTexCoord2f(this.textureOffsetX+this.textureWidth, this.textureOffsetY);
        GL.glVertex3f(x+width, y, 0);
    }
}
