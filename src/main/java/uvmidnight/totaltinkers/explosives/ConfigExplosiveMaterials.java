package uvmidnight.totaltinkers.explosives;


import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import uvmidnight.totaltinkers.ModConfig;
import uvmidnight.totaltinkers.TotalTinkers;
import uvmidnight.totaltinkers.explosives.materials.ExplosiveMaterialStats;
import uvmidnight.totaltinkers.explosives.materials.ExplosivePartType;

import javax.annotation.Nullable;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiConsumer;

public class ConfigExplosiveMaterials
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void dumpMissing(File saveLoc)
    {
        saveLoc.mkdirs();
        Collection<Material> materials = TinkerRegistry.getAllMaterials();

        for (Material material : materials)
        {
            File materialSave = new File(saveLoc, material.identifier + ".json");

            JsonObject materialJson = new JsonObject();

            JsonObject explosivecoreStats = dumpMissingExplosivecoreStats(material);

            if (explosivecoreStats == null)
                continue;

            if (explosivecoreStats != null)
                materialJson.add("body", explosivecoreStats);

//            if (axleStats != null)
//                materialJson.add("axle", axleStats);

            try
            {
                try (FileWriter writer = new FileWriter(materialSave))
                {
                    writer.write(GSON.toJson(materialJson));
                }
            }
            catch (Exception e)
            {
                TotalTinkers.logger.error(String.format("Error writing material \"%s\" to file: ", material.identifier), e);
            }
        }
    }

    @Nullable
    private static JsonObject dumpMissingExplosivecoreStats(Material material)
    {
        HeadMaterialStats head = material.getStats(ExplosivePartType.HEAD);
        if (head == null || material.getStats(ExplosivePartType.EXPLOSIVE_CORE) != null) {
            return null;
        }

        JsonObject statJson = new JsonObject();

        statJson.addProperty("radius", 0);
        statJson.addProperty("attack", head.attack);
        statJson.addProperty("durability", head.durability);

        return statJson;
    }

//    @Nullable
//    private static JsonObject dumpMissingAxleStats(Material material)
//    {
//        HandleMaterialStats handle = material.getStats(YoyoMaterialTypes.HANDLE);
//        if (handle == null || material.getStats(YoyoMaterialTypes.AXLE) != null)
//            return null;
//
//        JsonObject statJson = new JsonObject();
//        statJson.addProperty("friction", 0);
//        statJson.addProperty("modifier", handle.modifier);
//
//        return statJson;
//    }

    public static void load()
    {
        System.out.println("loading materials");
        File materialsLoc = new File(TotalTinkers.directory, "/totaltinkers/materials");

        if (Explosives.explosiveCoresFromConfig.getBoolean())
        {
            if (materialsLoc.mkdirs())
                resourceMaterialsFor((name, json) ->
                {
                    File to = new File(materialsLoc, name + ".json");

                    if (to.exists())
                        return;

                    try (FileWriter writer = new FileWriter(to))
                    {
                        writer.write(GSON.toJson(json));
                    }
                    catch (Exception e)
                    {
                        TotalTinkers.logger.error(e);
                    }
                });

            loadConfigMaterials(materialsLoc);
        }
        else
        {
            resourceMaterialsFor(ConfigExplosiveMaterials::processJson);
        }
    }

    private static void resourceMaterialsFor(BiConsumer<String, JsonObject> action)
    {
        FileSystem filesystem = null;

        try
        {
            URL url = ConfigExplosiveMaterials.class.getResource("/assets/totaltinkers/materials");

            if (url != null)
            {
                URI uri = url.toURI();
                Path path;

                if ("file".equals(uri.getScheme()))
                {
                    path = Paths.get(ConfigExplosiveMaterials.class.getResource("/assets/totaltinkers/materials").toURI());
                }
                else
                {
                    if (!"jar".equals(uri.getScheme()))
                    {
                        TotalTinkers.logger.error("Unsupported scheme " + uri + " trying to list all materials");
                        return;
                    }

                    filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    path = filesystem.getPath("/assets/totaltinkers/materials");
                }

                Iterator<Path> iterator = Files.walk(path).iterator();

                while (iterator.hasNext())
                {
                    Path path1 = iterator.next();

                    if ("json".equals(FilenameUtils.getExtension(path1.toString())))
                    {
                        String name = FilenameUtils.removeExtension(path1.getFileName().toString());
                        try (BufferedReader bufferedreader = Files.newBufferedReader(path1))
                        {
                            action.accept(name, JsonUtils.fromJson(GSON, bufferedreader, JsonObject.class));
                        }
                    }
                }
            }
        }
        catch (IOException | URISyntaxException e)
        {
            TotalTinkers.logger.error("Couldn't get a list of all material files", e);
        }
        finally
        {
            IOUtils.closeQuietly(filesystem);
        }
    }

    private static void loadConfigMaterials(File file)
    {
        File[] files = file.listFiles();

        if (files == null)
            return;

        for (File file1 : files)
            if (file1 != null && file1.getAbsolutePath().endsWith(".json"))
                loadMaterialStatsFromFile(file1);
    }

    private static void loadMaterialStatsFromFile(File file)
    {
        String name = file.getName();

        if(name.endsWith(".json"))
            name = name.substring(0, name.indexOf(".json"));
        else
            return;

        try
        {
            FileReader reader = new FileReader(file);
            JsonObject materialStats = GSON.fromJson(reader, JsonObject.class);
            reader.close();
            processJson(name, materialStats);
        }
        catch (Exception e)
        {
            TotalTinkers.logger.error(String.format("Error reading material \"%s\" from file: ", name), e);
        }
    }

    private static void processJson(String name, JsonObject materialStats)
    {
        try
        {
            addStats(name, ExplosiveMaterialStats.deserialize(materialStats));
        }
        catch (JsonParseException e)
        {
            TotalTinkers.logger.error(String.format("Error parsing explosive core stats for material \"%s\", ", name), e);
        }
    }

    private static void addStats(String material, @Nullable IMaterialStats stats)
    {
        if (stats == null)
            return;

        Set<IMaterialStats> statsSet = Explosives.MASTER_STATS.getOrDefault(material, Sets.newHashSet());

        statsSet.add(stats);

        if (!Explosives.MASTER_STATS.containsKey(material))
            Explosives.MASTER_STATS.put(material, statsSet);
    }
}
