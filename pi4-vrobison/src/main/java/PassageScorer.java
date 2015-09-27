import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.impl.AnnotationBaseImpl;
import org.apache.uima.jcas.JCas;

import type.InputDocument;
import type.Passage;
import type.Question;

//Score each passage by percentage of tokens also found in question
public class PassageScorer extends JCasAnnotator_ImplBase{
  public void process(JCas aJCas) {
    //iterate over passages
    FSIndex passIndex = aJCas.getAnnotationIndex(Passage.type);
    Iterator passIter = passIndex.iterator(); 
    while(passIter.hasNext()){
      //get current passage
      Passage p = (Passage)passIter.next();
      //get its corresponding question
      Question q = p.getQuestion();
      //get the question and passage text
      String pText = p.getText();
      String qText = q.getSentence();
      //stupidTok = token defined by Java regex word boundary
      //TODO in future assignments: better technique
      //ideas: larger ngrams, stemming of tokens (i.e. so "die" matches "died"), smarter tokenization
      Set<String> pStupidToks = new HashSet<String>(Arrays.asList(pText.split("\\b")));
      Set<String> qStupidToks = new HashSet<String>(Arrays.asList(qText.split("\\b")));
      //get number of tokens in passage
      double totPStupidToks = pStupidToks.size();
      //throw out tokens not also in question-> pStupidToks now intersection of original sets
      pStupidToks.retainAll(qStupidToks);
      //get number of tokens in intersection
      double overlap = pStupidToks.size();
      //set score as percentage of overlapping tokens in passage
      //TODO in future assignments: better scoring function
      p.setScore(overlap/totPStupidToks);
      
    }
  }
    

}
