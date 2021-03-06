/*
 * This file ("MultiTile.java") is part of the RockBottomAPI by Ellpeck.
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

package de.ellpeck.rockbottom.api.tile;

import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.assets.font.FormattingCode;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.render.tile.ITileRenderer;
import de.ellpeck.rockbottom.api.render.tile.MultiTileRenderer;
import de.ellpeck.rockbottom.api.util.Pos2;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;
import de.ellpeck.rockbottom.api.world.IWorld;
import de.ellpeck.rockbottom.api.world.TileLayer;

import java.util.List;

public abstract class MultiTile extends TileBasic{

    private boolean[][] structure;

    public MultiTile(IResourceName name){
        super(name);
    }

    @Override
    protected ITileRenderer createRenderer(IResourceName name){
        return new MultiTileRenderer(name, this);
    }

    protected abstract boolean[][] makeStructure();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract int getMainX();

    public abstract int getMainY();

    public boolean isStructurePart(int x, int y){
        if(this.structure == null){
            this.structure = this.makeStructure();

            if(!this.areDimensionsValid()){
                throw new RuntimeException("MultiTile with name "+this.name+" has invalid structure dimensions!");
            }
        }

        return this.structure[this.getHeight()-1-y][x];
    }

    @Override
    public boolean canPlace(IWorld world, int x, int y, TileLayer layer){
        if(super.canPlace(world, x, y, layer)){
            int startX = x-this.getMainX();
            int startY = y-this.getMainY();

            for(int addX = 0; addX < this.getWidth(); addX++){
                for(int addY = 0; addY < this.getHeight(); addY++){
                    if(this.isStructurePart(addX, addY)){
                        int theX = startX+addX;
                        int theY = startY+addY;

                        if(!world.getTile(layer, theX, theY).canReplace(world, theX, theY, layer, this)){
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void doPlace(IWorld world, int x, int y, TileLayer layer, ItemInstance instance, AbstractEntityPlayer placer){
        int startX = x-this.getMainX();
        int startY = y-this.getMainY();

        for(int addX = 0; addX < this.getWidth(); addX++){
            for(int addY = 0; addY < this.getHeight(); addY++){
                if(this.isStructurePart(addX, addY)){
                    int meta = this.getMeta(addX, addY);
                    world.setTile(startX+addX, startY+addY, this, meta);
                }
            }
        }
    }

    @Override
    public void doBreak(IWorld world, int x, int y, TileLayer layer, AbstractEntityPlayer breaker, boolean isRightTool){
        Pos2 start = this.getBottomLeft(x, y, world.getMeta(x, y));

        for(int addX = 0; addX < this.getWidth(); addX++){
            for(int addY = 0; addY < this.getHeight(); addY++){
                if(this.isStructurePart(addX, addY)){
                    boolean isMain = addX == this.getMainX() && addY == this.getMainY();
                    world.destroyTile(start.getX()+addX, start.getY()+addY, layer, breaker, isMain && (this.forceDrop || isRightTool));
                }
            }
        }
    }

    public boolean isMainPos(int x, int y, int meta){
        Pos2 main = this.getMainPos(x, y, meta);
        return main.getX() == x && main.getY() == y;
    }

    public Pos2 getInnerCoord(int meta){
        return new Pos2(meta%this.getWidth(), meta/this.getWidth());
    }

    public int getMeta(Pos2 coord){
        return this.getMeta(coord.getX(), coord.getY());
    }

    public int getMeta(int x, int y){
        return y*this.getWidth()+x;
    }

    public Pos2 getMainPos(int x, int y, int meta){
        return this.getBottomLeft(x, y, meta).add(this.getMainX(), this.getMainY());
    }

    public Pos2 getBottomLeft(int x, int y, int meta){
        Pos2 inner = this.getInnerCoord(meta);
        return inner.set(x-inner.getX(), y-inner.getY());
    }

    private boolean areDimensionsValid(){
        if(this.structure.length != this.getHeight()){
            return false;
        }
        else{
            for(boolean[] row : this.structure){
                if(row.length != this.getWidth()){
                    return false;
                }
            }
        }
        return this.isStructurePart(this.getMainX(), this.getMainY());
    }

    @Override
    public void describeItem(IAssetManager manager, ItemInstance instance, List<String> desc, boolean isAdvanced){
        super.describeItem(manager, instance, desc, isAdvanced);

        if(isAdvanced){
            desc.add("");
            desc.add(FormattingCode.GRAY+manager.localize(RockBottomAPI.createInternalRes("info.size"), this.getWidth(), this.getHeight()));
        }
    }
}
