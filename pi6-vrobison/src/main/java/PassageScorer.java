import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.ArrayFS;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.AnnotationBaseImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.NonEmptyFSList;

import type.InputDocument;
import type.Passage;
import type.Question;

//Score each passage by percentage of tokens also found in question
public class PassageScorer extends JCasAnnotator_ImplBase{
  
  //Andrew Wilkinson's function word list from 11-736 Spring 2015
  private Set<String> functionWords = new HashSet<String>(Arrays.asList("you", "i", "to", 
          "the", "a", "and", "that", "it", "of", "me", "what", "is", "in", "this", "know", 
          "i'm", "for", "no", "have", "my", "don't", "just", "not", "do", "be", "on", "your", 
          "was", "we", "it's", "with", "so", "but", "all", "well", "are", "he", "oh", "about"));
  
  public void process(JCas aJCas) {
    System.out.println(">>Passage Scoring");
    //iterate over passages
    FSIndex qIndex = aJCas.getAnnotationIndex(Question.type);
    Iterator qIter = qIndex.iterator(); 
    StanfordUtils lem  = new StanfordUtils();
    while(qIter.hasNext()){
      
      //get current passages
      Question q = (Question)qIter.next();
      FSArray passages = q.getQpassages();
      ArrayList<Passage> arr = new ArrayList<Passage>(passages.size());
      String qText = q.getSentence();
      boolean method1 = false;
      
      Set<String> qToks;
      String qSubj;
      if(method1){
      //really basic tokenization
      qToks = new HashSet<String>(Arrays.asList(qText.split("\\b")));
      
      }
      
      else{
        //use Stanford lemmatizer+tokenizer
        qText.replaceAll("<.*?>", "");
        List<String> qL = lem.lemmatize(qText);
        int bi = qL.size()-1; 
        for(int j=0;j<bi;j++){
          qL.add(qL.get(j)+" "+qL.get(j+1));
        }
        qToks = new HashSet<String>(qL);
        
      //  qSubj = lem.findSubject(qText);
       // System.out.println(qSubj);
        
      }
      for(int i = 0; i < passages.size();i++){
        Passage p = (Passage)passages.get(i);
        String pText = p.getText();
        pText.replaceAll("<.*?>", "");
        Set<String> pToks;
        if(method1){
          pToks = new HashSet<String>(Arrays.asList(pText.split("\\b")));
        }
        else{
          List<String> pL = lem.lemmatize(pText);
          int bi = pL.size()-1; 
          for(int j=0;j<bi;j++){
            pL.add(pL.get(j)+" "+pL.get(j+1));
          }
          pToks = new HashSet<String>(pL);
          pToks.removeAll(functionWords);
        }
        double totPToks = pToks.size();
        pToks.retainAll(qToks);
        //get number of tokens in intersection
        double overlap = pToks.size();
        //set score as percentage of overlapping tokens in passage
        //TODO in future assignments: better scoring function
        p.setScore(overlap);
      }//end for loop to score each passage
      
      //convert to a standard ArrayList so we can use Collections.sort
      for(int i = 0; i< passages.size(); i++){
        arr.add(i,(Passage)passages.get(i));
      }
      //PassageSort is a custom comparator for comparing passages by score
      Collections.sort(arr, new PassageSort());
      //reverse for descending order
      Collections.reverse(arr);
      for(int i = 0;i< passages.size();i++){
        passages.set(i, arr.get(i));
        
      }
     
    }//end while loop iterating over questions
  }
    

}
