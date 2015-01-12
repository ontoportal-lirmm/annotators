package org.sifrproject.scoring;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Scorer {
    public Map<String, Score> dictScoreConcept  = new HashMap<>();
    public Map<String, Score> dictScoreConcept2 = new HashMap<>();
    public ArrayList <String> conceptTries      = new ArrayList<>();
    public ArrayList <String> conceptTriesF     = new ArrayList<>();
    public ArrayList <String> conceptTriesF2    = new ArrayList<>();
    public Map<String, CvalueTerme> dictCvalue  = new HashMap<>();

    public int classementMax = 0;
    public int classementMaxF = 0;
    public int classementMaxF2 = 0;

    public Scorer(String filePath1, String filePath2){
        scoreWithoutContext(filePath1, filePath2);
    }
    
  // Aggregated scores
  public void calculeScoreWithoutContext(String FilePath){
      //TODO useless?  d.Dictscoreconcept = new HashMap<String, Score>();
      JSONParser parser = new JSONParser();
      try {
          JSONArray a = (JSONArray) parser.parse(new FileReader(FilePath));
          int score = 0;
          JSONObject annotatedClass;
          String id = "";
          for (Object o : a){
              JSONObject annotation = (JSONObject) o;
              
              // Compute direct annotation score
              annotatedClass = (JSONObject) annotation.get("annotatedClass");
              
              // Retrieve the concept id of direct annotation
              id = (String) annotatedClass.get("@id");
              
              // Retrieve direct annotation and its matchType
              JSONArray annotations = (JSONArray) annotation.get("annotations");
              int nbannotation = 0;
              score = 0;
              ArrayList<String> termeAnnote = new ArrayList<>();
              for (Object c : annotations){
                  JSONObject uneannotation = (JSONObject) c;
                  String matchType = (String) uneannotation.get("matchType");
                  String text = (String) uneannotation.get("text");
                  if (!termeAnnote.contains(text))
                      termeAnnote.add(text);

                  if (matchType.equals("PREF"))     score += 10;
                  else if (matchType.equals("SYN")) score += 8;
                  nbannotation++;
              }
              
              // Check if the concept id exist in the dictionary
              if (dictScoreConcept.get(id) == null) {
                  Score nouvellestr = new Score(termeAnnote,
                          score, 0, 0, annotatedClass, false, false, false,
                          false, 0, 0, 0);
                  dictScoreConcept.put(id, nouvellestr);
              } else {
                  Score nouvellestr = dictScoreConcept.get(id);
                  nouvellestr.score += score;
                  for (int i = 0; i < termeAnnote.size(); i++) {
                      if (!nouvellestr.TermesAnnotes.contains(termeAnnote.get(i))) 
                      {
                          nouvellestr.TermesAnnotes.add(termeAnnote.get(i));
                      }
                  }
                  dictScoreConcept.remove(id);
                  dictScoreConcept.put(id, nouvellestr);
              }
              
              // retrieve hierarchical annotation and their distances in the hierarchy
              JSONArray hierarchy = (JSONArray) annotation.get("hierarchy");

              for (Object h : hierarchy) {
                  JSONObject unehierarchie = (JSONObject) h;
                  annotatedClass = (JSONObject) unehierarchie.get("annotatedClass");
                  
                  // Retrieve the concept id of the direct annotation
                  id = (String) annotatedClass.get("@id");
              Long dista = (Long) unehierarchie.get("distance");

                  if (dista > 12) {
                      score = 1 * nbannotation;
                  } else {
                      double res = 1 + 10 * Math.exp(-0.2 * dista);
                      score = (int) res;
                      score = score * nbannotation;
                  }
                  
                  // Check the concept id already exist in the dictionary
                  if (dictScoreConcept.get(id) == null){
                      Score nouvellestr = new Score(termeAnnote,
                              score, 0, 0, annotatedClass, false, false,
                              false, true, 0, 0, 0);
                      dictScoreConcept.put(id, nouvellestr);
                  } 
                  else 
                  {
                      Score nouvellestr = dictScoreConcept.get(id);
                      nouvellestr.score = nouvellestr.score + score;
                      nouvellestr.annotatedClass = nouvellestr.annotatedClass;
                      for (int i = 0; i < termeAnnote.size(); i++) {
                          if (!nouvellestr.TermesAnnotes.contains(termeAnnote.get(i))) 
                          {
                              nouvellestr.TermesAnnotes.add(termeAnnote.get(i));
                          }
                      }
                      dictScoreConcept.remove(id);
                      dictScoreConcept.put(id, nouvellestr);
                  }
              }

          }
          
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      } catch (ParseException e) {
          e.printStackTrace();
      }
  }

  public void scoreWithoutContext(String FilePath, String FilePath2) {
      calculeScoreWithoutContext(FilePath2);
      JSONObject annotationScore = new JSONObject();

      ordonnerDict(dictScoreConcept, "score");
      // Save score in new file
      try {
          FileWriter filer = new FileWriter(FilePath);
          Iterator<String> i = conceptTries.iterator();
          while (i.hasNext()) {
              String K = (String) i.next();
              Score elementdic = dictScoreConcept.get(K);
              annotationScore.put("score", elementdic.score);
              annotationScore.put("@id", K);
              annotationScore.put("Termes", elementdic.TermesAnnotes);
              annotationScore.put("Classement", elementdic.ClassementScore+ "/" + classementMax);
              try {
                  filer.write(annotationScore.toString());
                  filer.flush();

              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          filer.close();

      } catch (IOException e) {
          e.printStackTrace();
      }
  }
 
  public void ordonnerDict(Map<String, Score> dict,    String TypeScore) {
      conceptTries   = new ArrayList<String>();
      conceptTriesF  = new ArrayList<String>();
      conceptTriesF2 = new ArrayList<String>();
      
      int classement = 0;
      int classementegale = 1;
      
      ArrayList<Integer>   listeClassement  = new ArrayList<>();
      ArrayList<Double>    listeClassementF = new ArrayList<>();

      Map<String, Score> dictOrdonne = dict;
      int i = 0;
      while (i < dictOrdonne.size()) {
          String max = maxDict(dictOrdonne, TypeScore);
          Score nouvellestr = dictOrdonne.get(max);
          if (nouvellestr != null) {
              if (TypeScore == "score") {
                  nouvellestr.trie = true;
                  if (!listeClassement.contains(nouvellestr.score)) {
                      classement=classement+classementegale;
                      listeClassement.add(nouvellestr.score);
                      classementegale=1;
                      
                  }else{
                      classementegale++;
                  }
                  nouvellestr.ClassementScore = classement;
                  conceptTries.add(max);
                  
              } else if (TypeScore == "scoreF") {
                  nouvellestr.trieF = true;
                  if (!listeClassementF.contains(nouvellestr.scoreF)) {
                      classement=classement+classementegale;
                      listeClassementF.add(nouvellestr.scoreF);
                      classementegale=1;
                  }
                  else
                  {
                      classementegale++;
                  }
                  nouvellestr.ClassementScoreF = classement;
                  conceptTriesF.add(max);
              } else {
                  nouvellestr.trieF2 = true;
                  if (!listeClassementF.contains(nouvellestr.scoreF2)) {
                      classement=classement+classementegale;
                      listeClassementF.add(nouvellestr.scoreF2);
                      classementegale=1;
                  }
                  else
                  {
                      classementegale++;
                  }
                  nouvellestr.ClassementScoreF2 = classement;
                  conceptTriesF2.add(max);
              }
              dictOrdonne.remove(max);
              dictOrdonne.put(max, nouvellestr);

          }
          i++;
      }
      
      if (TypeScore == "score")       classementMax   = classement;
      else if (TypeScore == "scoreF") classementMaxF  = classement;
      else                            classementMaxF2 = classement;
  }

  public static String maxDict(Map<String, Score> dict, String scoreType) {
      String maxKey = "";
      double maxValue = 0;

      for(String key : dict.keySet()){
          Score score = dict.get(key);
          double value = 0;
          boolean trie = false;
          
          if (scoreType == "score"){
              value = score.score;
              trie = score.trie;
          }else if (scoreType == "scoreF"){
              value = score.scoreF;
              trie = score.trieF;
          }else{
              value = score.scoreF2;
              trie = score.trieF2;
          }
          
          if (trie==false && ((maxKey=="" && maxValue==0) || (value>maxValue))) {
              maxKey = key;
              maxValue = value;
          }
      }
      return maxKey;
  }

  /*
  // Scores cvalue
      public static void CalculeScoreCvaluescore(String PathFile) {
          JSONParser parser = new JSONParser();
          try {
              JSONArray a = (JSONArray) parser.parse(new FileReader(PathFile));
              JSONObject annotatedClass;
              JSONObject annotationscore = new JSONObject();
              for (Object o : a) {
                  JSONObject annotation = (JSONObject) o;
                  // Calcluer le score de l'annotation directe
                  annotatedClass = (JSONObject) annotation.get("annotatedClass");
                  // R�cup�rer l'annotation directe et son matchType
                  JSONArray annotations = (JSONArray) annotation.get("annotations");
                  for (Object c : annotations) {
                      JSONObject uneannotation = (JSONObject) c;
                      String Terme = (String) uneannotation.get("text");
                      Long from = (Long) uneannotation.get("from");
                      Long to = (Long) uneannotation.get("to");
                      // V�rifier c'est le terme existe d�ja dans le dictionnaire
                      // ou pas
                      if (d.DictCvalue.get(Terme) == null) {
                          ArrayList<String> Position = new ArrayList<String>();
                          Position.add(from + "-" + to);
                          CvalueTerme nouvellestr = new CvalueTerme(Position, 1,0);
                          d.DictCvalue.put(Terme, nouvellestr);
                      } else 
                      {
                          CvalueTerme nouvellestr = d.DictCvalue.get(Terme);
                          if (!nouvellestr.Position.contains(from + "-" + to))
                          {
                              nouvellestr.Freq += 1;
                              nouvellestr.Position.add(from + "-" + to);
                              d.DictCvalue.remove(Terme);
                              d.DictCvalue.put(Terme, nouvellestr);
                          }
                      }
                  }

              }
              // Sauvgaregr le score dans un nouveau fichier
              Enumeration<String> key = d.DictCvalue.keys();
              while (key.hasMoreElements()) 
              {
                  String k = key.nextElement();
                  CvalueTerme nouvellestr = d.DictCvalue.get(k);
                  ArrayList<String> ListTermes = calculerTa(k, d.DictCvalue);
                  String[] tab = k.split(" ");
                  int taille = tab.length;
                  double cvalue = 0;
                  if (ListTermes.isEmpty()) 
                  {
                      cvalue = calculelog(taille, 2) * nouvellestr.Freq;
                  } 
                  else 
                  {
                      int sommeFreq = 0;
                      for (String s : ListTermes) 
                      {
                          CvalueTerme nouvellestr2 = d.DictCvalue.get(s);
                          sommeFreq += nouvellestr2.Freq;
                      }
                      cvalue = calculelog(taille, 2)* (nouvellestr.Freq - 1 / ListTermes.size()* sommeFreq);
                  }
                  nouvellestr.cvalue = cvalue;
                  d.DictCvalue.remove(k);
                  d.DictCvalue.put(k, nouvellestr);
              }

          } catch (FileNotFoundException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          } catch (ParseException e) {
              e.printStackTrace();
          } catch (Exception e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
          }
      }
      
  public static void CalculeScoreF() {
      Enumeration<String> key = d.Dictscoreconcept.keys();
      while (key.hasMoreElements()) {
          String K = key.nextElement();
          StructScore elementdic = d.Dictscoreconcept.get(K);
          double cvalue = 0;
          for (Object terme : elementdic.TermesAnnotes) {

              CvalueTerme nouvellestr2 = d.DictCvalue.get(terme);
              if (nouvellestr2 != null) {
                  cvalue += nouvellestr2.cvalue;
              }
          }
          
              if (cvalue == 0) 
              {
                  elementdic.scoreF = calculelog(elementdic.score, 10);
                  elementdic.scoreF2 = calculelog(elementdic.score, 10);
                  } 
              else 
              {
                  elementdic.scoreF = calculelog(elementdic.score, 10)* cvalue;
                      elementdic.scoreF2 = calculelog(elementdic.score, 10)* cvalue;
                      if(elementdic.herarchie)
                      {
                          elementdic.scoreF2 = calculelog(elementdic.score, 10);
                      }
              }
          
          d.Dictscoreconcept.remove(K);
          d.Dictscoreconcept.put(K, elementdic);
          // System.out.println(K+" aaaaaaaaa"+elementdic.score+"ccccccc"+cvalue+"ggg"+calculelog
          // (elementdic.score,10)*cvalue);
      }
  }

  // Score+Cvalue si le param�tres de CalculeScoreF est false on lisse la Fscore des hi�rachie sans les
  public static void ScoreF(String FilePath, String FilePath2) 
  {
      CalculeScorewithoutcontext(FilePath2);
      CalculeScoreCvaluescore(FilePath2);
      CalculeScoreF();
      OrdonnerDict(d.Dictscoreconcept, "scoreF");
      // Sauvgaregr le score dans un nouveau fichier
      JSONObject annotationscore = new JSONObject();
      try {
          FileWriter filer;
          filer = new FileWriter(FilePath);

          Enumeration<String> key = d.Dictscoreconcept.keys();
          Iterator i = d.ConceptTriesF.iterator();
          while (i.hasNext()) {
              String K = (String) i.next();
              StructScore elementdic = d.Dictscoreconcept.get(K);
              annotationscore.put("score", elementdic.score);
              annotationscore.put("scoreF", elementdic.scoreF);
              annotationscore.put("@id", K);
              annotationscore.put("Classement", elementdic.ClassementScoreF   + "/" + ClassementMaxF);
              annotationscore.put("Termes", elementdic.TermesAnnotes);
              System.out.println(K+" aaaaaaaaa"+d.Dictscoreconcept.get(K).TermesAnnotes);
              try {
                  filer.write(annotationscore.toString());
                  filer.flush();

              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          filer.close();
          //System.out.println("Fin scoreF");
      } catch (IOException e) {
          e.printStackTrace();
      }

  }
  // Score+Cvalue si le param�tres de CalculeScoreF est true on lisse la Fscore � 1
  public static void ScoreF2(String FilePath,String FilePath2) {

      CalculeScorewithoutcontext(FilePath2);
      CalculeScoreCvaluescore(FilePath2);
      CalculeScoreF();
      OrdonnerDict(d.Dictscoreconcept, "scoreF2");
      // Sauvgaregr le score dans un nouveau fichier
      JSONObject annotationscore = new JSONObject();
      try {
          FileWriter filer;
          filer = new FileWriter(FilePath);

          Enumeration<String> key = d.Dictscoreconcept.keys();
          Iterator i = d.ConceptTriesF2.iterator();
          while (i.hasNext()) {
              String K = (String) i.next();
              StructScore elementdic = d.Dictscoreconcept.get(K);
              annotationscore.put("score", elementdic.score);
              annotationscore.put("scoreF2", elementdic.scoreF2);
              annotationscore.put("@id", K);
              annotationscore.put("Classement", elementdic.ClassementScoreF2  + "/" + ClassementMaxF2);
              annotationscore.put("Contexte", elementdic.herarchie);
              annotationscore.put("Termes", elementdic.TermesAnnotes);
              // System.out.println(K+" aaaaaaaaa"+d.Dictscoreconcept.get(K).TermesAnnotes);
              try {
                  filer.write(annotationscore.toString());
                  filer.flush();

              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          filer.close();
          System.out.println("Fin score F2");
      } catch (IOException e) {
          e.printStackTrace();
      }

  }

  // Score non aggr�g�s
  public static void CalculeScorewithcontext() {
      JSONParser parser = new JSONParser();
      try {

          JSONArray a = (JSONArray) parser.parse(new FileReader(
                  "F:\\annotation.json"));
          FileWriter filer = new FileWriter("F:\\testwithcontext.json");
          int score = 0;
          JSONObject annotationscore = new JSONObject();
          for (Object o : a) {
              JSONObject annotation = (JSONObject) o;
              JSONObject annotationsav = (JSONObject) o;

              JSONObject annotatedClassdirect = (JSONObject) annotation
                      .get("annotatedClass");
              annotationscore.put("annotatedClass", annotatedClassdirect);
              try {
                  filer.write(annotationscore.toString());
                  filer.flush();

              } catch (IOException e) {
                  e.printStackTrace();
              }
              JSONArray hierarchy = (JSONArray) annotation.get("hierarchy");
              JSONArray listhierarchy = new JSONArray();
              for (Object h : hierarchy) {
                  JSONObject unehierarchie = (JSONObject) h;
                  Long dista = (Long) unehierarchie.get("distance");
                  JSONObject annotatedClass = (JSONObject) unehierarchie
                          .get("annotatedClass");

                  System.out.println(dista + "");
                  if (dista > 12) {
                      score = 1;
                  } else {
                      double res = 1 + 10 * Math.exp(-0.2 * dista);
                      score = (int) res;
                  }

                  JSONObject info = new JSONObject();
                  info.put("score", score);
                  info.put("distance", dista);
                  info.put("annotatedClass", annotatedClass);
                  listhierarchy.add(info);
              }
              annotationscore.put("hierarchy", listhierarchy);
              try {
                  filer.write(annotationscore.toString());
                  filer.flush();

              } catch (IOException e) {
                  e.printStackTrace();
              }
              JSONArray annotations = (JSONArray) annotation
                      .get("annotations");
              JSONObject annotescore = new JSONObject();
              JSONArray listannotattion = new JSONArray();
              for (Object c : annotations) {
                  JSONObject uneannotation = (JSONObject) c;
                  String matchType = (String) uneannotation.get("matchType");
                  String text = (String) uneannotation.get("text");
                  Long from = (Long) uneannotation.get("from");
                  Long to = (Long) uneannotation.get("to");
                  System.out.println(matchType + "");
                  if (matchType.equals("PREF")) {
                      System.out.println(matchType + "ok");
                      score = 10;
                  } else {
                      if (matchType.equals("SYN")) {
                          score = 8;
                      }
                  }
                  JSONObject info = new JSONObject();
                  info.put("from", from);
                  info.put("to", to);
                  info.put("matchType", matchType);
                  info.put("text", text);
                  info.put("score", score);
                  listannotattion.add(info);

              }
              annotationscore = new JSONObject();
              annotationscore.put("annotations", listannotattion);
              try {
                  filer.write(annotescore.toString());
                  filer.flush();

              } catch (IOException e) {
                  e.printStackTrace();
              }

              JSONArray mappings = (JSONArray) annotation.get("mappings");
              annotationscore.put("mappings", mappings);
              try {
                  filer.write(annotationscore.toString());
                  filer.flush();

              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          filer.close();

      } catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      } catch (ParseException e) {
          e.printStackTrace();
      } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
  }

  
  // Calculer le Ta
  public static ArrayList<String> calculerTa(String terme,
          Dictionary<String, CvalueTerme> Dict) {
      ArrayList<String> ListTermes = new ArrayList<String>();
      Enumeration<String> key = Dict.keys();
      while (key.hasMoreElements()) {
          String k = key.nextElement();
          if (k.contains(terme) && !k.equals(terme)) {
              ListTermes.add(k);
          }
      }
      return ListTermes;
  }

  public static double calculelog(int x, int base) {
      return (double) (Math.log(x) / Math.log(base));
  }


  public static void OrdonnerDictsav(Dictionary<String, StructScore> dic, String TypeScore) {
      d.ConceptTries = new ArrayList<String>();
      d.ConceptTriesF = new ArrayList<String>();
      d.ConceptTriesF2 = new ArrayList<String>();
      int classement = 0;
      ArrayList<Integer> Listeclassement = new ArrayList<Integer>();
      Dictionary<String, Integer> DictClasssement = new Hashtable<String, Integer>();
      ArrayList ListeclassementF = new ArrayList();
      // System.out.println("cooooo");
      Dictionary<String, StructScore> Dictordonne = new Hashtable<String, StructScore>();
      Dictordonne = dic;
      int i = 0;
      while (i < Dictordonne.size()) {
          String max = MaxDic(Dictordonne, TypeScore);
          StructScore nouvellestr = Dictordonne.get(max);
          if (nouvellestr != null) {
              if (TypeScore == "score") {
                  nouvellestr.trie = true;
                  if (!Listeclassement.contains(nouvellestr.score)) {
                      classement++;
                      Listeclassement.add(nouvellestr.score);
                  }
                  nouvellestr.ClassementScore = classement;
                  d.ConceptTries.add(max);
              } else {
                  if (TypeScore == "scoreF") {
                      nouvellestr.trieF = true;
                      if (!ListeclassementF.contains(nouvellestr.scoreF)) {
                          classement++;
                          ListeclassementF.add(nouvellestr.scoreF);
                      }
                      nouvellestr.ClassementScoreF = classement;
                      d.ConceptTriesF.add(max);
                  } else {
                      nouvellestr.trieF2 = true;
                      if (!ListeclassementF.contains(nouvellestr.scoreF2)) {
                          classement++;
                          ListeclassementF.add(nouvellestr.scoreF2);
                      }
                      nouvellestr.ClassementScoreF2 = classement;
                  }
                  d.ConceptTriesF2.add(max);
              }
              Dictordonne.remove(max);
              Dictordonne.put(max, nouvellestr);
          //  System.out.println("Max est :"+max);

          }
          i++;
      }
      if (TypeScore == "score") {
          ClassementMax = classement;
      } else {
          if (TypeScore == "scoreF") {
              ClassementMaxF = classement;
          } else {
              ClassementMaxF2 = classement;
          }
      }
       //System.out.println("Fin Classement"+TypeScore);

  }
  */
}