package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.crafting.AdvancedCrafterRecipeRegistry;
import com.dyonovan.modernalchemy.crafting.ArcFurnaceRecipeRegistry;
import com.dyonovan.modernalchemy.crafting.OreDictStack;
import com.dyonovan.modernalchemy.helpers.OreDictHelper;
import com.dyonovan.modernalchemy.common.tileentity.machines.TileAdvancedCrafter;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.Arrays;

public class CraftingHandler {
    @SuppressWarnings("unchecked")
    public static void init() {

        //Advanced Crafter
        GameRegistry.addRecipe(new ItemStack(BlockHandler.blockAdvancedCrafter), "ABA", "CDC", "AEA",
                'A', Items.iron_ingot, 'B', Blocks.hopper, 'C', Blocks.piston,
                'D', Blocks.furnace, 'E', Blocks.redstone_block);

        //Elec Bellows
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockElectricBellows), "ABA", "CDE", "AFA",
                'A', "plateSteel", 'B', ItemHandler.itemEnergyAntenna, 'C', Blocks.piston,
                'D', ItemHandler.itemMachineFrame, 'E', ItemHandler.itemPumpModule, 'F', "circuitAdvanced"));

        //Amagamator
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockAmalgamator), "ABA", "CDE", "AFA",
                'A', "plateSteel", 'B', ItemHandler.itemEnergyAntenna, 'C', Blocks.crafting_table,
                'D', ItemHandler.itemMachineFrame, 'E', Items.cauldron, 'F', "circuitAdvanced"));

        //Pattern Recorder
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockPatternRecorder), "ABA", "CDC", "AEA",
                'A', "plateSteel", 'B', ItemHandler.itemEnergyAntenna, 'C', Items.book,
                'D', ItemHandler.itemMachineFrame, 'E', "circuitAdvanced"));

        //Arc Furnace Processor
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceCore), "ABA", "CDC", "ABA",
                'A', "plateSteel", 'B', "circuitAdvanced", 'C', "gearSteel",
                'D', BlockHandler.blockArcFurnaceDummy));

        //Arc Furnace Dummy
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummy), "ABA", "BCB", "ABA",
                'A', "plateSteel", 'B', "ingotCopper", 'C', ItemHandler.itemMachineFrame));

        //Arc Furnace Output Valve
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummyOutputValve), "ABA", "CDC", "AEA",
                'A', "plateSteel", 'B', "circuitAdvanced", 'C', Items.bucket, 'D', BlockHandler.blockArcFurnaceDummy,
                'E', ItemHandler.itemActiniumDust));

        //Arc Furnace Item IO
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummyAirValve), "ABA", "CDC", "AEA",
                'A', "plateSteel", 'B', "circuitAdvanced", 'C', Blocks.piston, 'D', BlockHandler.blockArcFurnaceDummy,
                'E', ItemHandler.itemActiniumDust));

        //Arc Furnace Energy Rec
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummyEnergy), "ABA", "CDC", "AEA",
                'A', "plateSteel", 'B', "circuitAdvanced", 'C', ItemHandler.itemCapacator, 'D', BlockHandler.blockArcFurnaceDummy,
                'E', "circuitAdvanced"));

        //Arc Furnace Compressed Air Valve
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockArcFurnaceDummyItemIO), "ABA", "CDC", "AEA",
                'A', "plateSteel", 'B', "circuitAdvanced", 'C', Blocks.hopper, 'D', BlockHandler.blockArcFurnaceDummy,
                'E', ItemHandler.itemActiniumDust));

        //Replicator Controller
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockReplicator), "ABA", "CDC", "CEC",
                'A', ItemHandler.itemSteelTube, 'B', Items.iron_ingot, 'C', ItemHandler.itemMachineFrame,
                'D', "circuitAdvanced", 'E', ItemHandler.itemCapacator));

        //Tesla Coil Top
        GameRegistry.addRecipe(new ItemStack(BlockHandler.blockTeslaCoil), "ABA", "CDC", " D ",
                'A', ItemHandler.itemDenseCopperCoil, 'B', Blocks.iron_bars, 'C', ItemHandler.itemCopperCoil,
                'D', ItemHandler.itemSteelTube);

        //Super Tesla Coil Top
        GameRegistry.addRecipe(new ItemStack(BlockHandler.blockSuperTeslaCoil), "AAA", "ABA", "AAA",
                'A', BlockHandler.blockTeslaCoil, 'B',  ItemHandler.itemTransformer);

        //Tesla Coil Stand
        GameRegistry.addRecipe(new ItemStack(BlockHandler.blockTeslaStand), "ABA", "ABA", "ABA",
                'A', ItemHandler.itemCopperCoil, 'B', ItemHandler.itemSteelTube);

        //Tesla Coil Base
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHandler.blockTeslaBase), "ABA", "CDC", "AEA",
                'A', "plateSteel", 'B', ItemHandler.itemSteelTube, 'C', ItemHandler.itemCopperCoil,
                'D', "circuitAdvanced", 'E', ItemHandler.itemCapacator));

        //Wrench
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemWrench), "A A", " A ", " A ",
                'A', "plateSteel"));

        //Blank Pattern
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemReplicatorPattern), "AAA", "ABA", "AAA",
                'A', ItemHandler.itemMemory, 'B', "circuitAdvanced"));

        //Graphene
        ItemStack flintStack = new ItemStack(Items.flint);
        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.itemGraphene),
                flintStack, flintStack, flintStack, flintStack);

        //Realtime Clock
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemRealClock), "ABA", "BCB", "ADA",
                'A', "wireCopper", 'B', Items.quartz, 'C', Items.clock, 'D', ItemHandler.itemBlankPCB));

        //Blank PCB
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemBlankPCB), "ABA", "BCB", "ABA",
                'A', "dyeGreen", 'B', "blockGlass", 'C', Blocks.wool));

        //Circuit
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemCircuit), "ABA", "BCB", "ADA",
                'A', ItemHandler.itemBlankPCB, 'B', "wireCopper", 'C', ItemHandler.itemRealClock, 'D', Items.gold_ingot));

        //Capacitor
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemCapacator), "ABA", "ABA", "C C",
                'A', ItemHandler.itemBlankPCB, 'B', "plateSteel", 'C', "wireCopper"));

        //Memory
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemMemory, 4), "ABA", "BBB", "CCC",
                'A', "wireCopper", 'B', "plateSteel", 'C', "circuitAdvanced"));

        //Copper Coil
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemCopperCoil), "AAA", "A A", "AAA",
                'A', "wireCopper"));

        //Dense Copper Coil
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemDenseCopperCoil), "AAA", "ABA", "AAA",
                'A', "wireCopper", 'B', "ingotCopper"));

        //Machine Frame
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemMachineFrame), "ABA", "B B", "ABA",
                'A', "plateSteel", 'B', Blocks.iron_bars));

        //Transformer
        GameRegistry.addRecipe(new ItemStack(ItemHandler.itemTransformer), "ABA", "ACA", "ABA",
                'A', ItemHandler.itemCopperCoil, 'B', ItemHandler.itemSteelTube, 'C', ItemHandler.itemCapacator);

        //Pump Module
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemPumpModule), "ABA", "ACA", "ABA",
                'A', "plateSteel", 'B', Items.leather, 'C', ItemHandler.itemSteelTube));

        //Energy Antenna
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemEnergyAntenna), " A ", " B ", "CCC",
                'A', ItemHandler.itemCopperCoil, 'B', ItemHandler.itemSteelTube, 'C', "plateSteel"));

        //Steel Tube
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandler.itemSteelTube), "A A", "A A", "A A",
                'A', "plateSteel"));

        //Book
        GameRegistry.addShapelessRecipe(new ItemStack(ItemHandler.manual),
                new ItemStack(ItemHandler.itemActiniumDust), new ItemStack(Items.book));

        //Faraday Armor
        GameRegistry.addRecipe(new ItemStack(ItemHandler.faradayHelm), "AAA", "ABA",
                'A', ItemHandler.itemFaradayWire, 'B', ItemHandler.itemCapacator);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.faradayChest), "ABA", "AAA", "AAA",
                'A', ItemHandler.itemFaradayWire, 'B', ItemHandler.itemCapacator);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.faradayLeg), "AAA", "ABA", "A A",
                'A', ItemHandler.itemFaradayWire, 'B', ItemHandler.itemCapacator);
        GameRegistry.addRecipe(new ItemStack(ItemHandler.faradayBoots), "A A", "ABA",
                'A', ItemHandler.itemFaradayWire, 'B', ItemHandler.itemCapacator);

        //Arc Furnace Recipes
        ArcFurnaceRecipeRegistry.instance.addRecipe(ItemHandler.itemActiniumDust, FluidContainerRegistry.BUCKET_VOLUME);
        ArcFurnaceRecipeRegistry.instance.addRecipe(ItemHandler.itemSlag,  FluidContainerRegistry.BUCKET_VOLUME / 4);
        ArcFurnaceRecipeRegistry.instance.addRecipe(Item.getItemFromBlock(BlockHandler.blockOreActinium), FluidContainerRegistry.BUCKET_VOLUME * 2);

        //Advanced Crafting Recipes
        AdvancedCrafterRecipeRegistry.instance.addRecipe(new ArrayList<Object>(Arrays.asList(new ItemStack(Items.coal), new ItemStack(Items.iron_ingot))),
                new OreDictStack("ingotSteel", 1), 1000, TileAdvancedCrafter.ENRICH);
        AdvancedCrafterRecipeRegistry.instance.addRecipe(new ArrayList<Object>(Arrays.asList(new ItemStack(ItemHandler.itemFaradayIngot))),
                new ItemStack(ItemHandler.itemFaradayWire, 3), 200, TileAdvancedCrafter.EXTRUDE);

        if (OreDictionary.getOres("oreCopper").isEmpty())
            GameRegistry.addSmelting(BlockHandler.blockOreCopper, new ItemStack(ItemHandler.itemCopperIngot), 0.1f);

        AdvancedCrafterRecipeRegistry.instance.addOreDictRecipe(new ArrayList<Object>(Arrays.asList(new OreDictStack("ingotCopper"))),
                new ItemStack(ItemHandler.itemCopperWire, 3), 200, TileAdvancedCrafter.EXTRUDE);
        AdvancedCrafterRecipeRegistry.instance.addOreDictRecipe(new ArrayList<Object>(Arrays.asList(new OreDictStack("ingotSteel"))),
                new ItemStack(ItemHandler.itemSteelPlate, 1), 200, TileAdvancedCrafter.BEND);
        AdvancedCrafterRecipeRegistry.instance.addOreDictRecipe(new ArrayList<Object>(Arrays.asList(new OreDictStack("ingotSteel"), new OreDictStack("ingotSteel"), new OreDictStack("ingotSteel"), new OreDictStack("ingotSteel"))),
                new ItemStack(ItemHandler.itemSteelGear, 1), 500, TileAdvancedCrafter.EXTRUDE);
        AdvancedCrafterRecipeRegistry.instance.addOreDictRecipe(new ArrayList<Object>(Arrays.asList(new OreDictStack("ingotCopper"), new OreDictStack("ingotSteel"))),
                new ItemStack(ItemHandler.itemFaradayIngot, 1), 500, TileAdvancedCrafter.ENRICH);

        if(OreDictHelper.hasOreDict("ingotTin") && OreDictHelper.hasOreDict("ingotBronze")) {
            AdvancedCrafterRecipeRegistry.instance.addOreDictRecipe(new ArrayList<Object>(Arrays.asList(new OreDictStack("ingotCopper"), new OreDictStack("ingotCopper"), new OreDictStack("ingotCopper"), new OreDictStack("ingotTin"))),
                    new OreDictStack("ingotBronze", 4), 200, TileAdvancedCrafter.ENRICH);
        }
    }
}
