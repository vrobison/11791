import java.util.Iterator;

import org.apache.uima.analysis_component.Annotator_ImplBase;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.AbstractCas;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.FSList;

import type.Measurement;
import type.Passage;
import type.Question;

public class ErrorMeasurer extends JCasAnnotator_ImplBase {

  
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    FSIndex qIndex = aJCas.getAnnotationIndex(Question.type);
    Iterator qIter = qIndex.iterator(); 
    while(qIter.hasNext()){
      Measurement m = new Measurement(aJCas);
      //get current passages
      Question q = (Question)qIter.next();
      FSList passages = q.getPassages();
      int i = 0;
      
        
      
      int tp = 0;
      int fp = 0;
      int fn = 0;
      
      while(passages.getNthElement(i) != null){
        Passage  p = (Passage)passages.getNthElement(i);
        boolean label = p.getLabel();
        if(i<5){
          if(label) tp++;
          else fp++;
        }
        else{
          if(label) fn++;
        }
        i++;
      }
      m.setTp(tp);
      m.setFp(fp);
      m.setFn(fn);
      q.setMeasurement(m);
    }//end while loop over questions

  }

}
