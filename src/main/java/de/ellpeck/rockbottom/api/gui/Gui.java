/*
 * This file ("Gui.java") is part of the RockBottomAPI by Ellpeck.
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

package de.ellpeck.rockbottom.api.gui;

import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.gui.component.GuiComponent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

public class Gui{

    public static final Color GRADIENT = new Color(0F, 0F, 0F, 0.7F);
    public static final Color HOVER_INFO_BACKGROUND = new Color(0F, 0F, 0F, 0.8F);
    protected final Gui parent;
    public int sizeX;
    public int sizeY;

    public int guiLeft;
    public int guiTop;
    protected List<GuiComponent> components = new ArrayList<>();

    public Gui(int sizeX, int sizeY){
        this(sizeX, sizeY, null);
    }

    public Gui(int sizeX, int sizeY, Gui parent){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.parent = parent;
    }

    public void onOpened(IGameInstance game){
        Keyboard.enableRepeatEvents(true);
    }

    public void onClosed(IGameInstance game){
        Keyboard.enableRepeatEvents(false);
    }

    public void initGui(IGameInstance game){
        if(!this.components.isEmpty()){
            this.components.clear();
        }

        this.initGuiVars(game);
    }

    protected void initGuiVars(IGameInstance game){
        this.guiLeft = (int)game.getWidthInGui()/2-this.sizeX/2;
        this.guiTop = (int)game.getHeightInGui()/2-this.sizeY/2;
    }

    public void update(IGameInstance game){
        this.components.forEach(component -> component.update(game));
    }

    public boolean onMouseAction(IGameInstance game, int button, float x, float y){
        for(GuiComponent component : this.components){
            if(component.onMouseAction(game, button, x, y)){
                return true;
            }
        }
        return false;
    }

    public boolean onKeyboardAction(IGameInstance game, int button, char character){
        for(GuiComponent component : this.components){
            if(component.onKeyboardAction(game, button, character)){
                return true;
            }
        }

        if(button == game.getSettings().keyMenu.key || (button == game.getSettings().keyInventory.key && this instanceof GuiContainer)){
            if(this.tryEscape(game)){
                return true;
            }
        }

        return false;
    }

    public void render(IGameInstance game, IAssetManager manager, Graphics g){
        this.components.forEach(component -> component.render(game, manager, g));
    }

    public void renderOverlay(IGameInstance game, IAssetManager manager, Graphics g){
        this.components.forEach(component -> component.renderOverlay(game, manager, g));
    }

    protected boolean tryEscape(IGameInstance game){
        if(this.parent != null){
            game.getGuiManager().openGui(this.parent);
        }
        else{
            game.getGuiManager().closeGui();
        }

        return true;
    }

    public boolean doesPauseGame(){
        return true;
    }

    public boolean isMouseOverComponent(IGameInstance game){
        return this.components.stream().anyMatch(component -> component.isMouseOver(game));
    }

    public boolean isMouseOver(IGameInstance game){
        if(Mouse.isInsideWindow()){
            int mouseX = (int)game.getMouseInGuiX();
            int mouseY = (int)game.getMouseInGuiY();

            boolean overSelf = mouseX >= this.guiLeft && mouseX < this.guiLeft+this.sizeX && mouseY >= this.guiTop && mouseY < this.guiTop+this.sizeY;
            return overSelf || this.isMouseOverComponent(game);
        }
        else{
            return false;
        }
    }

    public boolean onButtonActivated(IGameInstance game, int button){
        return false;
    }

    public boolean hasGradient(){
        return true;
    }
}
