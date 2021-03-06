/*
 * This file ("WorldRenderEvent.java") is part of the RockBottomAPI by Ellpeck.
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

package de.ellpeck.rockbottom.api.event.impl;

import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.event.Event;
import de.ellpeck.rockbottom.api.world.IWorld;
import org.newdawn.slick.Graphics;

/**
 * This event is fired after the world is rendered
 * <br> It is not cancellable
 */
public class WorldRenderEvent extends Event{

    public final IGameInstance game;
    public final IAssetManager assetManager;
    public final Graphics graphics;
    public final IWorld world;
    public final AbstractEntityPlayer player;
    public final float translationX;
    public final float translationY;

    public WorldRenderEvent(IGameInstance game, IAssetManager assetManager, Graphics graphics, IWorld world, AbstractEntityPlayer player, float translationX, float translationY){
        this.game = game;
        this.assetManager = assetManager;
        this.graphics = graphics;
        this.world = world;
        this.player = player;
        this.translationX = translationX;
        this.translationY = translationY;
    }
}
