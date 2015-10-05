import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.ArrayFS;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;

import type.Passage;
import type.Performance;
import type.Question;

public class QuestionAnnotator extends JCasAnnotator_ImplBase {

  private Pattern mQuestionPattern = 
          Pattern.compile("(\\d{4}) QUESTION (.*)");

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    //create map of arraylists of passages indexed by questionId
    Map<String, ArrayList<Passage>> map = new HashMap<String, ArrayList<Passage>>();
    FSIndex pIndex = aJCas.getAnnotationIndex(Passage.type);
    Iterator pIter = pIndex.iterator(); 
    while(pIter.hasNext()){
      Passage temp = (Passage)pIter.next();
      String key = temp.getQuestionId();
      if(map.get(key)==null) map.put(key,new ArrayList<Passage>());
      map.get(key).add(temp);
    }
      

    System.out.println(">> Question Annotator Processing");
    // get document text from the CAS
    String docText = aJCas.getDocumentText();
    
    // search for all the questions in the text
    Matcher matcher = mQuestionPattern.matcher(docText);
    int pos = 0;    
    while (matcher.find(pos)) {
      // found one - create annotation
      Question annotation = new Question(aJCas);
      annotation.setBegin(matcher.start());
      annotation.setEnd(matcher.end());
      annotation.setId(matcher.group(1));
      String questionId = matcher.group(1);
      annotation.setSentence(matcher.group(2));
      //Add empty performance
      annotation.setPerformance(new Performance(aJCas));
      annotation.addToIndexes();
      pos = matcher.end();
      System.out.printf("Added Q: %s - %s\n", matcher.group(1), matcher.group(2));
      int numPass = map.get(questionId).size();
      FSArray arr = new FSArray(aJCas, numPass);
      for(int i =0; i< numPass; i++){
        arr.set(i, map.get(questionId).get(i));
      }
      annotation.setQpassages(arr);
     
      
    }
  }

}
