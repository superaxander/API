/*
 * This file ("ComponentSlot.java") is part of the RockBottomAPI by Ellpeck.
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

package de.ellpeck.rockbottom.api.gui.component;

import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.gui.GuiContainer;
import de.ellpeck.rockbottom.api.gui.container.ContainerSlot;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import org.newdawn.slick.Graphics;

public class ComponentSlot extends GuiComponent{

    public final ContainerSlot slot;
    public final int componentId;
    public final GuiContainer container;

    public ComponentSlot(GuiContainer container, ContainerSlot slot, int componentId, int x, int y){
        super(container, x, y, 18, 18);
        this.container = container;
        this.slot = slot;
        this.componentId = componentId;
    }

    @Override
    public boolean onMouseAction(IGameInstance game, int button, float x, float y){
        return RockBottomAPI.getApiHandler().doDefaultSlotMovement(game, button, x, y, this);
    }

    @Override
    public void render(IGameInstance game, IAssetManager manager, Graphics g){
        RockBottomAPI.getApiHandler().renderSlotInGui(game, manager, g, this.slot.get(), this.x, this.y, 1F);
    }

    @Override
    public void renderOverlay(IGameInstance game, IAssetManager manager, Graphics g){
        if(this.container.holdingInst == null && this.isMouseOver(game)){
            ItemInstance instance = this.slot.get();
            if(instance != null){
                RockBottomAPI.getApiHandler().describeItem(game, manager, g, instance);
            }
        }
    }
}
