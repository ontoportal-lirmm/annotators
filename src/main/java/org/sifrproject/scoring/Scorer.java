package org.sifrproject.scoring;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.sifrproject.util.JSON;


public class Scorer {

    protected Map<String,Annotation> annotations;
    public Map<String, Score> scoreDict;

    /*
    public Map<String, Score> dictScoreConcept2 = new HashMap<>();
    public ArrayList <String> conceptTries      = new ArrayList<>();
    public ArrayList <String> conceptTriesF     = new ArrayList<>();
    public ArrayList <String> conceptTriesF2    = new ArrayList<>();
    public Map<String, CvalueTerme> dictCvalue  = new HashMap<>();

    public int classementMax = 0;
    public int classementMaxF = 0;
    public int classementMaxF2 = 0;
     */

    public Scorer(JSONArray annotationArray){
        annotations = new HashMap<>(annotationArray.size());
        for(Object obj : annotationArray){
            Annotation annotation = new Annotation((JSONObject) obj);
            annotations.put(annotation.getId(), annotation);
        }
    }


    public Map<String,Double> computeOldScore(){
        Map<String,Double> scores = new HashMap<>();

        for (Annotation annotation : annotations.values()){
            double score = 0;

            // add score for all annotatedTerms to this annotation
            List<Match> annotatedMatches = annotation.getMatches();
            for (Match match: annotatedMatches){
                switch(match.type){
                case PREF: score += 10;  break;
                case SYN:  score += 8;   break;
                }
            }
            addScore(scores, annotation.getId(), score);


            // add score to hierarchical concepts
            Map<String, Integer> hierarchy = annotation.getHierarchy();
            for (String hid : hierarchy.keySet()){
                Integer distance = hierarchy.get(hid);

                double factor = 1;
                if (distance <= 12) 
                    factor += 10 * Math.exp(-0.2 * distance);

                addScore(scores, hid, factor*annotatedMatches.size());
            }
        }
        return scores;
    }

    private static void addScore(Map<String, Double> scores, String id, double value){
        Double score = scores.get(id);
        if (score==null) score  = value;
        else             score += value;
        scores.put(id, score);
    }

    /**
     * Create a JSON (array) with annotation items sorted by {@code scores}
     * Also add/Override a 'score' entry with respective score value  to each annotation item
     */
    public JSON getSortedAnnotation(Map<String, Double> scores){
        // sort scores
        TreeMap<String, Double> sortedScores = new TreeMap<>();
        sortedScores.putAll(scores);

        // make sorted JSONArray
        JSON sortedAnnotations = new JSON(new JSONArray());
        for(String id : sortedScores.keySet()){
            JSON annotation = new JSON(annotations.get(id).object);
            annotation.put("score", sortedScores.get(id).toString());
            sortedAnnotations.add(annotation.getObject());
        }

        return sortedAnnotations;
    }

    /*
  public void scoreWithoutContext(String FilePath, String FilePath2) {
      //ScoreWithoutContext(FilePath2);
      JSONObject annotationScore = new JSONObject();

      //ordonnerDict(scoreDict, "score");
      // Save score in new file
      try {
          FileWriter filer = new FileWriter(FilePath);
          Iterator<String> i = conceptTries.iterator();
          while (i.hasNext()) {
              String K = (String) i.next();
              Score elementdic = scoreDict.get(K);
              annotationScore.put("score", elementdic.score);
              annotationScore.put("@id", K);
              annotationScore.put("Termes", elementdic.annotatedTerms);
              //annotationScore.put("Classement", elementdic.ClassementScore+ "/" + classementMax);
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

  public void ordonnerDict(Map<String, Score_old> dict,    String TypeScore) {
      conceptTries   = new ArrayList<String>();
      conceptTriesF  = new ArrayList<String>();
      conceptTriesF2 = new ArrayList<String>();

      int classement = 0;
      int classementegale = 1;

      ArrayList<Integer>   listeClassement  = new ArrayList<>();
      ArrayList<Double>    listeClassementF = new ArrayList<>();

      Map<String, Score_old> dictOrdonne = dict;
      int i = 0;
      while (i < dictOrdonne.size()) {
          String max = maxDict(dictOrdonne, TypeScore);
          Score_old nouvellestr = dictOrdonne.get(max);
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

  public static String maxDict(Map<String, Score_old> dict, String scoreType) {
      String maxKey = "";
      double maxValue = 0;

      for(String key : dict.keySet()){
          Score_old score = dict.get(key);
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