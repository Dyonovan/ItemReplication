package com.dyonovan.modernalchemy.handlers;

import com.dyonovan.modernalchemy.lib.Constants;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    public static String folderLocation;

    public static boolean generateActinium, machineExplodes, machineSounds;
    public static int actiniumMaxLevel, actiniumVeinSize, actiniumVeinsPerChunk, actiniumMinLevel;
    public static int searchRange;
    public static int rfPerTesla, maxCoilGenerate, maxCoilTransfer;

    public static void init(Configuration config) {

        folderLocation = config.getConfigFile().getAbsolutePath();

        config.load();

        generateActinium        = config.get(Constants.CONFIG_ORE_GENERATION, "Generate Actinium", true).getBoolean();
        actiniumVeinSize        = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Vein Size", 3).getInt();
        actiniumVeinsPerChunk   = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Veins Per Chunk", 10).getInt();
        actiniumMinLevel        = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Min Level", 5).getInt();
        actiniumMaxLevel        = config.get(Constants.CONFIG_ORE_GENERATION, "Actinium Max Level", 64).getInt();

        rfPerTesla              = config.get(Constants.CONFIG_TESLA, "RFs per Tesla", 10).getInt();
        maxCoilTransfer         = config.get(Constants.CONFIG_TESLA, "Max Tesla a coil can transfer per tick", 1).getInt();
        maxCoilGenerate         = config.get(Constants.CONFIG_TESLA, "Max Tesla a coil can generate per tick", 1).getInt();
        searchRange             = config.get(Constants.CONFIG_TESLA, "How many blocks (cubed) to search for coils", 10).getInt();

        machineExplodes         = config.get(Constants.CONFIG_GENERAL, "Do Machines Explode?", true).getBoolean();
        machineSounds           = config.get(Constants.CONFIG_GENERAL, "Disable Machine Sounds?", false).getBoolean();

        config.save();
    }
}
