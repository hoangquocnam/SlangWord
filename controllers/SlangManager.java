package controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TreeMap;

public class SlangManager {

  static String datasource = "assets/slang.txt";
  static String slangHistory = "log/slang.txt";
  static String keywordHistory = "log/keyword.txt";
  static TreeMap<String, ArrayList<String>> slangMap = new TreeMap<String, ArrayList<String>>();
  static DatasourceManager datasourceManager = new DatasourceManager();

  enum SlangType {
    SLANG,
    MEANING,
    KEYWORD,
  }

  public void addSlang(String slang, ArrayList<String> meaning) {
    slangMap.put(slang, meaning);
  }

  public void removeSlang(String slang) {
    slangMap.remove(slang);
  }

  public void logHistory(SlangType type, String data) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    String timeLog = '[' + dtf.format(now) + ']' + " ----- ";

    switch (type) {
      case SLANG:
        String meaning = "";
        for (String m : getMeaning(data)) {
          meaning += m + ", ";
        }
        String historySlang = timeLog + data + ":" + meaning;
        datasourceManager.writeData(slangHistory, historySlang);
        break;
      case MEANING:
        datasourceManager.writeData(slangHistory, data);
        break;
      case KEYWORD:
        String historyKeyWord = timeLog + data;
        datasourceManager.writeData(keywordHistory, historyKeyWord);
        break;
      default:
        break;
    }
  }

  public ArrayList<String> getMeaning(String slang) {
    return slangMap.get(slang);
  }

  public ArrayList<String> searchBySlang(String keyword) {
    ArrayList<String> result = new ArrayList<String>();
    for (String slang : slangMap.keySet()) {
      if (slang.contains(keyword)) {
        result.add(slang);
      }
    }
    logHistory(SlangType.SLANG, keyword);
    return result;
  }

  public ArrayList<String> searchByKeyword(String keyword) {
    try {
      ArrayList<String> slangList = new ArrayList<String>();
      for (String slang : slangMap.keySet()) {
        ArrayList<String> meaningOfSlang = getMeaning(slang);
        for (String meaning : meaningOfSlang) {
          if (meaning.contains(keyword)) {
            slangList.add(slang);
          }
        }
      }
      logHistory(SlangType.KEYWORD, keyword);
      return slangList;
    } catch (Exception e) {
      return null;
    }
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
        }
        addSlang(slangName, slangMeaning);
      }
    }
  }
}
