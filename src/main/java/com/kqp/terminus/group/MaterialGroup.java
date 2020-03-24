package com.kqp.terminus.group;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.item.TerminusToolMaterial;
import com.kqp.terminus.item.tool.TerminusAxeItem;
import com.kqp.terminus.item.tool.TerminusPickaxeItem;
import com.kqp.terminus.item.tool.TerminusShovelItem;
import com.kqp.terminus.item.tool.TerminusSwordItem;
import net.minecraft.item.Item;

public class MaterialGroup {
    public final Item SWORD;
    public final Item SHOVEL;
    public final Item PICKAXE;
    public final Item AXE;

    public MaterialGroup(String name) {
        SWORD = new TerminusSwordItem(TerminusToolMaterial.CELESTIAL);
        SHOVEL = new TerminusShovelItem(TerminusToolMaterial.CELESTIAL);
        PICKAXE = new TerminusPickaxeItem(TerminusToolMaterial.CELESTIAL);
        AXE = new TerminusAxeItem(TerminusToolMaterial.CELESTIAL);

        Terminus.TItems.register(SWORD, name + "_sword");
        Terminus.TItems.register(SHOVEL, name + "_shovel");
        Terminus.TItems.register(PICKAXE, name + "_pickaxe");
        Terminus.TItems.register(AXE, name + "_axe");
    }
}
