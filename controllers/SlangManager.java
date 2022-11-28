package controllers;

import java.util.ArrayList;
import java.util.HashMap;

public class SlangManager {

  static String datasource = "assets/slang.txt";
  static HashMap<String, ArrayList<String>> slangMap = new HashMap<String, ArrayList<String>>();
  static DatasourceManager datasourceManager = new DatasourceManager();

  public void addSlang(String slang, ArrayList<String> meaning) {
    slangMap.put(slang, meaning);
  }

  public void removeSlang(String slang) {
    slangMap.remove(slang);
  }

  public ArrayList<String> getMeaning(String slang) {
    return slangMap.get(slang);
  }

  public ArrayList<String> getSlangByKeyword(String keyword) {
    ArrayList<String> slangList = new ArrayList<String>();
    for (String slang : slangMap.keySet()) {
      ArrayList<String> meaningOfSlang = getMeaning(slang);
      for (String meaning : meaningOfSlang) {
        if (meaning.contains(keyword)) {
          slangList.add(slang);
        }
      }
    }
    for (String slang : slangList) {
      System.out.println(slang);
    }
    return slangList;
  }

  public void loadSlangs() {
    ArrayList<String> data = datasourceManager.getData(datasource);

    for (int i = 0; i < data.size(); i++) {
      String[] slang = data.get(i).split("`");
      if (slang.length == 2) {
        String slangName = slang[0];
        String[] slangMeaningArray = slang[1].split("\\| ");
        ArrayList<String> slangMeaning = new ArrayList<String>();
        for (int j = 0; j < slangMeaningArray.length; j++) {
          slangMeaning.add(slangMeaningArray[j]);
          // System.out.println(slangMeaningArray[j]);
        }
        addSlang(slangName, slangMeaning);
      }
    }
  }
}
