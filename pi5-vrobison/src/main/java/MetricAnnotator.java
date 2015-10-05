import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.apache.uima.analysis_component.Annotator_ImplBase;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.AbstractCas;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;

import type.Passage;
import type.Performance;
import type.Question;

public class MetricAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    double MRR = 0.0;
    double MAP = 0.0;
    int numQs = 0; //count questions as they are processed
    System.out.println(">>Calculating Metric Scores");
    FSIndex qIndex = aJCas.getAnnotationIndex(Question.type);
    Iterator qIter = qIndex.iterator(); 
    while(qIter.hasNext()){
      Question q = (Question)qIter.next();
      FSArray passages = q.getQpassages();
      ArrayList<Passage> arr = new ArrayList<Passage>(passages.size());
      for(int i = 0;i< passages.size();i++){
        arr.add(i,(Passage)passages.get(i));
      }
      Collections.sort(arr, new PassageSort());
      Collections.reverse(arr);
      
      Performance perf = new Performance(aJCas);
      //calculate  p at 1
      if(arr.get(0).getLabel()){perf.setPAt1(1.0);}
      else{perf.setPAt1(0.0);}
      //calculate & set p at 5
      double numRel = 0;
      for(int i = 0; i<5;i++){
        if(arr.get(i).getLabel()) numRel++;
      }
      perf.setPAt5(numRel/5.0);
      
      //calculate & set rr
      //calculate & set ap
      double numerator = 0.0;
      numRel = 0;
      boolean found1st = false;
      for(int i=0; i< arr.size(); i++){
        if(arr.get(i).getLabel()){
           numRel++;
           numerator += numRel/(i+1); //add P(i+1)
           if(!found1st){ 
             perf.setRr(1/(i+1));//found first correct answer, set rr
             found1st = true;
           }
        }
      }
      if(numRel != 0) perf.setAp(numerator/numRel);
      else perf.setAp(0.0);
    q.setPerformance(perf);
    numQs++;
    MRR += perf.getRr();
    MAP += perf.getAp();
    }//end loop over questions
    MRR/=numQs;
    MAP/=numQs;
    System.out.println("MRR: "+MRR);
    System.out.println("MAP: "+MAP);
  }

}
