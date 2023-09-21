package com.codecool.marsexploration.service.input;

import com.codecool.marsexploration.data.cell.CellType;
import com.codecool.marsexploration.data.config.Cluster;
import com.codecool.marsexploration.data.config.MapValidationConfiguration;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class ConfigurationJsonParserImpl implements ConfigurationJsonParser {
  private final Scanner configurationScanner;
  private final FileReader fileReader;
  
  public ConfigurationJsonParserImpl(Scanner configurationScanner, FileReader fileReader) {
    this.configurationScanner = configurationScanner;
    this.fileReader = fileReader;
  }
  
  @Override
  public MapValidationConfiguration get() {
    JSONObject jsonObject = readFile();
    double maxFilledTilesRatio = jsonObject.getDouble("maxFilledTilesRatio");
    int minimumMapSize = jsonObject.getInt("minimumMapSize");
    int maximumMapSize = jsonObject.getInt("maximumMapSize");
    double minimumClusterTypeRatio = jsonObject.getDouble("minimumClusterTypeRatio");
    double minimumResourceTypeRatio = jsonObject.getDouble("minimumResourceTypeRatio");
    List<Cluster> clusterTypes = getClusterList(jsonObject);
    return new MapValidationConfiguration(maxFilledTilesRatio,
                                          minimumMapSize,
                                          maximumMapSize,
                                          minimumClusterTypeRatio,
                                          minimumResourceTypeRatio,
                                          clusterTypes);
  }
  
  @NotNull
  private List<Cluster> getClusterList(@NotNull JSONObject jsonObject) {
    JSONArray jsonClusterTypes = jsonObject.getJSONArray("clusterTypes");
    List<Cluster> clusterTypes = new ArrayList<>();
    for (int i = 0; i < jsonClusterTypes.length(); i++) {
      JSONObject jsonCluster = jsonClusterTypes.getJSONObject(i);
      String typeAsString = jsonCluster.getString("clusterType");
      CellType clusterType = CellType.valueOf(typeAsString.toUpperCase());
      Set<CellType> resources = getResourceList(jsonCluster);
      clusterTypes.add(new Cluster(clusterType, resources));
    }
    return clusterTypes;
  }
  
  @NotNull
  private Set<CellType> getResourceList(@NotNull JSONObject jsonCluster) {
    JSONArray jsonResources = jsonCluster.getJSONArray("resourceTypes");
    Set<CellType> resources = new HashSet<>();
    jsonResources.forEach(jsonResource -> {
      String resourceTypeAsString = jsonResource.toString();
      CellType resourceType = CellType.valueOf(resourceTypeAsString.toUpperCase());
      resources.add(resourceType);
    });
    return resources;
  }
  
  private @NotNull JSONObject readFile() {
    String jsonAsString = fileReader.getAsString(configurationScanner);
    return new JSONObject(jsonAsString);
  }
}
