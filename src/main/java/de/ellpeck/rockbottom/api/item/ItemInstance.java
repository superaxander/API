/*
 * This file ("ItemInstance.java") is part of the RockBottomAPI by Ellpeck.
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

package de.ellpeck.rockbottom.api.item;

import de.ellpeck.rockbottom.api.Constants;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.data.set.DataSet;
import de.ellpeck.rockbottom.api.tile.Tile;
import org.newdawn.slick.util.Log;

public class ItemInstance{

    private final Item item;

    private short meta;
    private int amount;
    private DataSet additionalData;

    public ItemInstance(Tile tile){
        this(tile, 1);
    }

    public ItemInstance(Tile tile, int amount){
        this(tile, amount, 0);
    }

    public ItemInstance(Tile tile, int amount, int meta){
        this(tile.getItem(), amount, meta);
    }

    public ItemInstance(Item item){
        this(item, 1);
    }

    public ItemInstance(Item item, int amount){
        this(item, amount, 0);
    }

    public ItemInstance(Item item, int amount, int meta){
        if(item == null){
            throw new NullPointerException("Tried to create an ItemInstance with null item!");
        }
        if((meta < 0 || meta > Short.MAX_VALUE) && meta != Constants.META_WILDCARD){
            throw new IndexOutOfBoundsException("Tried assigning meta "+meta+" to item instance with item "+item+" and amount "+amount+" which is less than 0 or greater than max "+Short.MAX_VALUE+"!");
        }

        this.item = item;
        this.amount = amount;
        this.meta = (short)meta;
    }

    public static ItemInstance load(DataSet set){
        String name = set.getString("item_name");
        Item item = RockBottomAPI.ITEM_REGISTRY.get(RockBottomAPI.createRes(name));

        if(item != null){
            int amount = set.getInt("amount");
            short meta = set.getShort("meta");

            ItemInstance instance = new ItemInstance(item, amount, meta);

            if(set.hasKey("data")){
                instance.additionalData = set.getDataSet("data");
            }

            return instance;
        }
        else{
            Log.info("Could not load item instance from data set "+set+" because name "+name+" is missing!");
            return null;
        }
    }

    public void save(DataSet set){
        set.addString("item_name", RockBottomAPI.ITEM_REGISTRY.getId(this.item).toString());
        set.addInt("amount", this.amount);
        set.addShort("meta", this.meta);
        if(this.additionalData != null){
            set.addDataSet("data", this.additionalData);
        }
    }

    public Item getItem(){
        return this.item;
    }

    public int getMeta(){
        return this.meta;
    }

    public int getAmount(){
        return this.amount;
    }

    public void setMeta(int meta){
        this.meta = (short)meta;
    }

    public boolean fitsAmount(int amount){
        return this.getAmount()+amount <= this.getMaxAmount();
    }

    public int getMaxAmount(){
        return this.item.getMaxAmount();
    }

    public ItemInstance setAmount(int amount){
        this.amount = amount;
        return this;
    }

    public ItemInstance addAmount(int amount){
        return this.setAmount(this.amount+amount);
    }

    public ItemInstance removeAmount(int amount){
        return this.setAmount(this.amount-amount);
    }

    public ItemInstance copy(){
        ItemInstance instance = new ItemInstance(this.item, this.amount, this.meta);
        if(this.additionalData != null){
            instance.additionalData = this.additionalData.copy();
        }
        return instance;
    }

    public DataSet getAdditionalData(){
        return this.additionalData;
    }

    public void setAdditionalData(DataSet set){
        this.additionalData = set;
    }

    public boolean isEffectivelyEqual(ItemInstance instance){
        return compare(this, instance, true, true, true, false);
    }

    public boolean isEffectivelyEqualWithWildcard(ItemInstance instance){
        return compare(this, instance, true, true, true, true);
    }

    public static boolean compare(ItemInstance one, ItemInstance other, boolean item, boolean meta, boolean data, boolean wildcard){
        if(one == null && other == null){
            return true;
        }
        else if(one == null || other == null){
            return false;
        }
        else{
            if(item){
                if(one.item != other.item){
                    return false;
                }
            }

            if(meta){
                if(one.meta != other.meta){
                    if(wildcard){
                        if(one.meta != Constants.META_WILDCARD && other.meta != Constants.META_WILDCARD){
                            return false;
                        }
                    }
                    else{
                        return false;
                    }
                }
            }

            if(data){
                if(one.item.isDataSensitive(one) || other.item.isDataSensitive(other)){
                    if(one.additionalData == null ? other.additionalData != null : !one.additionalData.equals(other.additionalData)){
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public String getDisplayName(){
        return RockBottomAPI.getGame().getAssetManager().localize(this.item.getUnlocalizedName(this));
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null || this.getClass() != o.getClass()){
            return false;
        }

        ItemInstance instance = (ItemInstance)o;
        return this.meta == instance.meta && this.amount == instance.amount && this.item.equals(instance.item) && (this.additionalData != null ? this.additionalData.equals(instance.additionalData) : instance.additionalData == null);
    }

    @Override
    public int hashCode(){
        int result = this.item.hashCode();
        result = 31*result+(int)this.meta;
        result = 31*result+this.amount;
        result = 31*result+(this.additionalData != null ? this.additionalData.hashCode() : 0);
        return result;
    }
}
