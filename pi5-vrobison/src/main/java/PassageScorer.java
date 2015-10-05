import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
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
  public void process(JCas aJCas) {
    System.out.println(">>Passage Scoring");
    //iterate over passages
    FSIndex qIndex = aJCas.getAnnotationIndex(Question.type);
    Iterator qIter = qIndex.iterator(); 
    while(qIter.hasNext()){
      //get current passages
      Question q = (Question)qIter.next();
      FSArray passages = q.getQpassages();
      String qText = q.getSentence();
      //really basic tokenization, named in the style of Chris Dyer
      Set<String> qStupidToks = new HashSet<String>(Arrays.asList(qText.split("\\b")));
      for(int i = 0; i < passages.size();i++){
        Passage p = (Passage)passages.get(i);
        String pText = p.getText();
        Set<String> pStupidToks = new HashSet<String>(Arrays.asList(pText.split("\\b")));
        double totPStupidToks = pStupidToks.size();
        pStupidToks.retainAll(qStupidToks);
        //get number of tokens in intersection
        double overlap = pStupidToks.size();
        //set score as percentage of overlapping tokens in passage
        //TODO in future assignments: better scoring function
        p.setScore(overlap/totPStupidToks);
      }
      //ArrayFS arr = CAS.createArrayFS(3);
      
      
      //loop over FSlist{
      //for(int i =0; i<4;i++){
      //  Passage p = (Passage)list.getNthElement(i);
       // System.err.print(p.getText());
      //}
      
    /*
      //get the passage text
      String pText = p.getText();
     
        //stupidTok = token defined by Java regex word boundary
        //TODO in future assignments: better technique
        //ideas: larger ngrams, stemming of tokens (i.e. so "die" matches "died"), smarter tokenization
        Set<String> pStupidToks = new HashSet<String>(Arrays.asList(pText.split("\\b")));
        //get number of tokens in passage
        double totPStupidToks = pStupidToks.size();
        //throw out tokens not also in question-> pStupidToks now intersection of original sets
        pStupidToks.retainAll(qStupidToks);
        //get number of tokens in intersection
        double overlap = pStupidToks.size();
        //set score as percentage of overlapping tokens in passage
        //TODO in future assignments: better scoring function
        p.setScore(overlap/totPStupidToks);*/
    
      
    }
  }
    

}
