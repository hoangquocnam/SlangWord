package controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeMap;
import utils.Constant;

public class SlangManager {

  public class Slang {

    private String slang;
    private ArrayList<String> meaning;

    public Slang(String slang, ArrayList<String> meaning) {
      this.slang = slang;
      this.meaning = meaning;
    }

    public String getSlang() {
      return slang;
    }

    public void setSlang(String slang) {
      this.slang = slang;
    }

    public ArrayList<String> getMeaning() {
      return meaning;
    }

    public void setMeaning(ArrayList<String> meaning) {
      this.meaning = meaning;
    }
  }

  static TreeMap<String, ArrayList<String>> slangMap = new TreeMap<String, ArrayList<String>>();
  static TreeMap<String, ArrayList<String>> historyMap = new TreeMap<String, ArrayList<String>>();
  static DatasourceManager datasourceManager = new DatasourceManager();

  public ArrayList<String> getMeaning(String slang) {
    return slangMap.get(slang);
  }

  public void setUserSlang() {
    datasourceManager.copyFile(
      Constant.DATASOURCE,
      Constant.USER_SLANG_DATASOURCE
    );
  }

  public TreeMap<String, ArrayList<String>> getAllSlang() {
    return slangMap;
  }

  public TreeMap<String, ArrayList<String>> getHistorySlang() {
    return historyMap;
  }

  public void setSlangList(ArrayList<String> slangList) {
    for (int i = 0; i < slangList.size(); i++) {
      String[] slang = slangList.get(i).split("`");
      if (slang.length == 2) {
        String slangName = slang[0];
        String[] slangMeaningArray = slang[1].split("\\| ");
        ArrayList<String> slangMeaning = new ArrayList<String>();
        for (int j = 0; j < slangMeaningArray.length; j++) {
          slangMeaning.add(slangMeaningArray[j]);
        }
        slangMap.put(slangName, slangMeaning);
      }
    }
  }

  public void loadHistory() {
    ArrayList<String> historyLog = datasourceManager.getData(
      Constant.SLANG_HISTORY_PATH
    );

    for (String log : historyLog) {
      ArrayList<String> history = new ArrayList<String>();
      String slangHistory = log.split(Constant.LOG_SEPARATOR_TIME_CONTENT)[1];
      String[] slangHistoryArray = slangHistory.split(
        Constant.LOG_SEPARATOR_SLANG_DEFINITION
      );
      if (slangHistoryArray.length == 2) {
        String slang = slangHistoryArray[0];
        String[] slangMeaningArray =
          slangHistoryArray[1].split(
              Constant.LOG_SEPARATOR_DEFINITION_DEFINITION
            );
        for (int j = 0; j < slangMeaningArray.length; j++) {
          history.add(slangMeaningArray[j]);
        }
        historyMap.put(slang, history);
      }
    }
  }

  public void loadSlangs() {
    ArrayList<String> data = new ArrayList<String>();
    if (datasourceManager.checkFileExist(Constant.USER_SLANG_DATASOURCE)) {
      data = datasourceManager.getData(Constant.USER_SLANG_DATASOURCE);
    } else {
      setUserSlang();
      data = datasourceManager.getData(Constant.USER_SLANG_DATASOURCE);
    }

    setSlangList(data);
    loadHistory();
  }

  public void logHistory(Constant.SlangType type, String data) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(
      Constant.TIME_LOG_FORMAT
    );
    LocalDateTime now = LocalDateTime.now();
    String timeLog =
      '[' + dtf.format(now) + ']' + Constant.LOG_SEPARATOR_TIME_CONTENT;

    switch (type) {
      case SLANG:
        String meaning = "";
        if (getMeaning(data) != null) {
          for (String m : getMeaning(data)) {
            if (m == getMeaning(data).get(0)) {
              meaning += m;
            } else {
              meaning += Constant.LOG_SEPARATOR_DEFINITION_DEFINITION + m;
            }
          }
          String historySlang =
            timeLog + data + Constant.LOG_SEPARATOR_SLANG_DEFINITION + meaning;
          datasourceManager.writeData(
            Constant.SLANG_HISTORY_PATH,
            historySlang
          );
        }

        break;
      case MEANING:
        String slang = "";
        for (String key : slangMap.keySet()) {
          if (slangMap.get(key).contains(data)) {
            slang = key;
          }
        }
        String historyMeaning =
          timeLog + slang + Constant.LOG_SEPARATOR_SLANG_DEFINITION + data;
        datasourceManager.writeData(
          Constant.MEANING_HISTORY_PATH,
          historyMeaning
        );
        break;
      case KEYWORD:
        String historyKeyWord = timeLog + data;
        datasourceManager.writeData(
          Constant.KEYWORD_HISTORY_PATH,
          historyKeyWord
        );
        break;
      default:
        break;
    }
  }

  public TreeMap<String, ArrayList<String>> searchBySlang(
    String keyword,
    boolean isPressSearch
  ) {
    TreeMap<String, ArrayList<String>> result = new TreeMap<String, ArrayList<String>>();
    for (String slang : slangMap.keySet()) {
      if (slang.contains(keyword)) {
        result.put(slang, slangMap.get(slang));
      }
    }
    if (isPressSearch) {
      logHistory(Constant.SlangType.SLANG, keyword);
    }
    return result;
  }

  public TreeMap<String, ArrayList<String>> searchByDefinition(
    String keyword,
    boolean isPressSearch
  ) {
    try {
      TreeMap<String, ArrayList<String>> result = new TreeMap<String, ArrayList<String>>();
      for (String slang : slangMap.keySet()) {
        for (String meaning : slangMap.get(slang)) {
          if (meaning.contains(keyword)) {
            result.put(slang, slangMap.get(slang));
          }
        }
      }
      if (isPressSearch) {
        logHistory(Constant.SlangType.MEANING, keyword);
      }
      return result;
    } catch (Exception e) {
      return null;
    }
  }

  public TreeMap<String, ArrayList<String>> searchByAll(
    String keyword,
    boolean isPressSearch
  ) {
    TreeMap<String, ArrayList<String>> result = new TreeMap<String, ArrayList<String>>();
    for (String slang : slangMap.keySet()) {
      if (slang.contains(keyword)) {
        result.put(slang, slangMap.get(slang));
      }
      for (String meaning : slangMap.get(slang)) {
        if (meaning.contains(keyword)) {
          result.put(slang, slangMap.get(slang));
        }
      }
    }
    if (isPressSearch) {
      logHistory(Constant.SlangType.KEYWORD, keyword);
    }
    return result;
  }

  public void showHistory(Constant.SlangType type) {
    if (type == null) {
      return;
    }
    switch (type) {
      case SLANG:
        for (String slang : historyMap.keySet()) {
          System.out.println(slang);
          for (String meaning : historyMap.get(slang)) {
            System.out.println(meaning);
          }
        }
        break;
      case KEYWORD:
        ArrayList<String> keywordLog = datasourceManager.getData(
          Constant.KEYWORD_HISTORY_PATH
        );
        for (String log : keywordLog) {
          System.out.println(log);
        }
        break;
      default:
        break;
    }
  }

  public boolean addDefinition(String slang, String meaning) {
    try {
      ArrayList<String> meaningList = new ArrayList<String>();
      meaningList.add(meaning);
      slangMap.put(slang, meaningList);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean addSlang(
    String slang,
    ArrayList<String> meaning,
    boolean isYes
  ) {
    try {
      if (slangMap.containsKey(slang)) {
        ArrayList<String> oldMeaning = slangMap.get(slang);
        String meaningText = "";

        if (isYes) {
          for (String m : meaning) {
            if (m != meaning.get(meaning.size() - 1)) {
              meaningText += m + "| ";
            } else {
              meaningText += m;
            }
          }
          slangMap.put(slang, meaning);
          datasourceManager.addDefinition(
            Constant.USER_SLANG_DATASOURCE,
            slang,
            meaningText,
            true
          );
        } else {
          for (String m : meaning) {
            oldMeaning.add(m);
            if (m != meaning.get(meaning.size() - 1)) {
              meaningText += m + "| ";
            } else {
              meaningText += m;
            }
          }

          slangMap.put(slang, oldMeaning);
          datasourceManager.addDefinition(
            Constant.USER_SLANG_DATASOURCE,
            slang,
            meaningText,
            false
          );
        }
      } else {
        slangMap.put(slang, meaning);
        String data = slang + "`" + String.join("| ", meaning);
        datasourceManager.writeData(Constant.USER_SLANG_DATASOURCE, data);
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public void removeSlang(String slang) {
    try {
      slangMap.remove(slang);
      datasourceManager.removeLine(Constant.USER_SLANG_DATASOURCE, slang);
    } catch (Exception e) {
      System.out.println("Slang word: " + slang + " does not exist");
    }
  }

  public void resetSlang() {
    try {
      slangMap.clear();
      datasourceManager.clearFile(Constant.USER_SLANG_DATASOURCE);
      setUserSlang();
      ArrayList<String> data = new ArrayList<String>();
      setSlangList(data);
    } catch (Exception e) {
      System.out.println("Error when reset slang");
    }
  }

  public Slang getRandomSlang() {
    Random rand = new Random();
    int randomIndex = rand.nextInt(slangMap.size());
    int i = 0;
    for (String key : slangMap.keySet()) {
      if (i == randomIndex) {
        return new Slang(key, slangMap.get(key));
      }
      i++;
    }
    return null;
  }

  public void quizSlangGame() {
    Slang quizSlang = getRandomSlang();
    System.out.println("What is the meaning of " + quizSlang.getSlang() + "?");
    // generate 4 random definition
    ArrayList<String> randomDefinition = new ArrayList<String>();
    randomDefinition.add(quizSlang.getMeaning().get(0));
    while (randomDefinition.size() < 4) {
      Slang randomSlang = getRandomSlang();
      if (!randomDefinition.contains(randomSlang.getMeaning().get(0))) {
        randomDefinition.add(randomSlang.getMeaning().get(0));
      }
    }
    Collections.shuffle(randomDefinition);
    for (int i = 0; i < randomDefinition.size(); i++) {
      System.out.println((i + 1) + ". " + randomDefinition.get(i));
    }
    System.out.println("Your answer: ");
    String answer = System.console().readLine();
    if (
      randomDefinition
        .get(Integer.parseInt(answer) - 1)
        .equals(quizSlang.getMeaning().get(0))
    ) {
      System.out.println("Correct!");
    } else {
      System.out.println("Wrong!");
    }
  }

  public void quizDefinitionGame() {
    Slang quizSlang = getRandomSlang();
    System.out.println(
      "What is the slang word of " + quizSlang.getMeaning().get(0) + "?"
    );
    ArrayList<String> randomSlang = new ArrayList<String>();
    randomSlang.add(quizSlang.getSlang());
    while (randomSlang.size() < 4) {
      Slang randomSlangObj = getRandomSlang();
      if (!randomSlang.contains(randomSlangObj.getSlang())) {
        randomSlang.add(randomSlangObj.getSlang());
      }
    }

    Collections.shuffle(randomSlang);
    for (int i = 0; i < randomSlang.size(); i++) {
      System.out.println((i + 1) + ". " + randomSlang.get(i));
    }

    System.out.println("Your answer: ");
    String answer = System.console().readLine();
    if (
      randomSlang.get(Integer.parseInt(answer) - 1).equals(quizSlang.getSlang())
    ) {
      System.out.println("Correct!");
    } else {
      System.out.println("Wrong!");
    }
  }

  public boolean editSlang(String oldSlangName, String newSlangName) {
    // change the slang name
    ArrayList<String> meaning = slangMap.get(oldSlangName);
    try {
      slangMap.remove(oldSlangName);
      slangMap.put(newSlangName, meaning);
      Slang newSlang = new Slang(newSlangName, meaning);
      datasourceManager.updateSlang(
        Constant.USER_SLANG_DATASOURCE,
        oldSlangName,
        newSlang
      );
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public boolean editSlang(String slangName, ArrayList<String> newMeaning) {
    // change the slang meaning
    try {
      slangMap.put(slangName, newMeaning);
      Slang newSlang = new Slang(slangName, newMeaning);
      datasourceManager.updateSlang(
        Constant.USER_SLANG_DATASOURCE,
        slangName,
        newSlang
      );
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public boolean isExistSlang(String slang) {
    return slangMap.containsKey(slang);
  }
}
