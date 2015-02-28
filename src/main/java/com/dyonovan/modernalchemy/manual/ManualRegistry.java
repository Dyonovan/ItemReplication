package com.dyonovan.modernalchemy.manual;

import com.dyonovan.modernalchemy.ModernAlchemy;
import com.dyonovan.modernalchemy.handlers.GuiHandler;
import com.dyonovan.modernalchemy.helpers.LogHelper;
import com.dyonovan.modernalchemy.manual.pages.GuiManual;
import com.dyonovan.modernalchemy.util.ReplicatorUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

public class ManualRegistry {
    /**
     * Out Manual instance
     */
    public static ManualRegistry instance = new ManualRegistry();

    /**
     * The list of {@link com.dyonovan.modernalchemy.manual.pages.GuiManual} that have been opened, the stack
     */
    public static Stack<GuiManual> visitedPages;

    /**
     * All registered {@link com.dyonovan.modernalchemy.manual.pages.GuiManual}, built from files on preInit
     */
    public static HashMap<String, GuiManual> pages;

    /**
     * Creates our registry
     */
    public ManualRegistry() {
        pages = new HashMap<String, GuiManual>();
        visitedPages = new Stack<GuiManual>();
        init();
    }

    /**
     * Fills the pages registry with all {@link com.dyonovan.modernalchemy.manual.pages.GuiManual} from files
     */
    public void init() {
        File[] files = getFilesForPages();
        for(File f : files) {
            if(buildManualFromFile(f) != null)
                pages.put(f.getName().split(".json")[0], buildManualFromFile(f));
        }
    }

    /**
     * Adds a {@link com.dyonovan.modernalchemy.manual.pages.GuiManual} to the registered pages
     * @param page The built {@link com.dyonovan.modernalchemy.manual.pages.GuiManual} to add
     */
    public void addPage(GuiManual page) {
        pages.put(page.getID(), page);
    }

    /**
     * Get the {@link com.dyonovan.modernalchemy.manual.pages.GuiManual} in the registry
     * @param id The string representing our page
     * @return The {@link com.dyonovan.modernalchemy.manual.pages.GuiManual}, null if not found
     */
    public GuiManual getPage(String id) {
        return pages.get(id);
    }

    /**
     * Gets the page that was open on top of the visited stack
     * @return The top {@link com.dyonovan.modernalchemy.manual.pages.GuiManual} in the stack
     */
    public GuiManual getOpenPage() {
        if(visitedPages.isEmpty()) {
            try {
                return buildManualFromFile(new File(URLDecoder.decode(ModernAlchemy.class.getResource("/manualPages").getFile(), "UTF-8") + File.separator + ManualLib.MAINPAGE));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return visitedPages.get(visitedPages.size() - 1);
    }

    /**
     * Pushes a new page onto the visited stack
     * @param page The {@link com.dyonovan.modernalchemy.manual.pages.GuiManual} to add (must be registered)
     */
    public void visitNewPage(GuiManual page) {
        if(pages.containsKey(page.getID())) {
            visitedPages.push(page);
            openManual();
        }
        else
            LogHelper.warning("Could not load page: " + page.getID());
    }

    /**
     * Get the page below the current one
     * @return The last {@link com.dyonovan.modernalchemy.manual.pages.GuiManual}, landing page if not found
     */
    public GuiManual getLastPage() {
        return visitedPages.size() > 2 ? visitedPages.get(visitedPages.size() - 2) : new GuiManual(ManualLib.MAINPAGE);
    }

    /**
     * Pops the visited page stack
     */
    public void deleteLastPage() {
        try {
            visitedPages.pop();
        } catch(EmptyStackException e) {
            visitedPages.push(new GuiManual(ManualLib.MAINPAGE));
            LogHelper.warning("Tried to delete last page with no stack");
        }
    }

    /**
     * Opens the manual gui with the current page
     */
    @SideOnly(Side.CLIENT)
    public void openManual() {
        visitedPages.clear();
        if(visitedPages.empty()) {
            try {
                visitedPages.push(buildManualFromFile(new File(URLDecoder.decode(ModernAlchemy.class.getResource("/manualPages").getFile(), "UTF-8") + File.separator + ManualLib.MAINPAGE)));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Minecraft.getMinecraft().thePlayer.openGui(ModernAlchemy.instance, GuiHandler.MANUAL_GUI_ID, Minecraft.getMinecraft().theWorld, (int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ);
    }

    /**
     * Builds the page from the file provided
     * @param input The file with the context
     * @return A built {@link com.dyonovan.modernalchemy.manual.pages.GuiManual}
     */
    public GuiManual buildManualFromFile(File input) {
        GuiManual page = new GuiManual(input.getName().split(".json")[0]);
        ManualJson json;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(input.getAbsoluteFile()));
            json = readJson(bufferedReader);
        } catch (FileNotFoundException e) {
            LogHelper.severe("Could not find file: " + input.getName() + " at " + input.getAbsoluteFile() + ".json");
            return null;
        }

        page.setTitle(json.title);
        return page;
    }

    /**
     * Gets all the files in the manual pages directory ("resources/manualPages")
     * @return An array of {@link java.io.File}s containing our info
     */
    public File[] getFilesForPages() {
        File directory = null;
        try {
            directory = new File(URLDecoder.decode(ModernAlchemy.class.getResource("/manualPages").getFile(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            LogHelper.severe("Could not find Manual Pages");
        }
        return directory.listFiles();
    }

    public  ManualJson readJson(BufferedReader br) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ManualJson.class, new MJDeserializer());
        Gson gson = gsonBuilder.create();

        ManualJson json = gson.fromJson(br, ManualJson.class);

        return json;
    }

    public void writeManJson(ArrayList<ManualJson> values) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(values);

        try {
            FileWriter fw = new FileWriter(ReplicatorUtils.fileDirectory + "test.json");
            fw.write(json);
            fw.close();
        } catch (IOException e) {

        }
    }
}
