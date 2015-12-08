package rank;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import type.Passage;
import type.Question;

public class NgramRanker extends AbstractRanker {
  private StanfordUtils lem  = new StanfordUtils("tokenize, ssplit, pos, lemma");
  /**
   * Returns a score of the given passage associated with the given question.
   * 
   * @param question
   * @param passage
   * @return a score of the passage
   */
  @Override
  public Double score(Question question, Passage passage) {
    String qText = question.getSentence().toLowerCase();
    String pText = passage.getText().toLowerCase();
    
    qText.replaceAll("<.{0,10}>", ""); //crude html markup removal
    //assume more than 10 chars between <> means broken tags, leave them rather than delete extra
    List<String> qL = lem.lemmatize(qText);
    int bi = qL.size()-1; 
    for(int j=0;j<bi;j++){
      qL.add(qL.get(j)+" "+qL.get(j+1));
    }
    Set<String> qToks = new HashSet<String>(qL);
    
    pText.replaceAll("<.{0,10}>", ""); //crude html markup removal
    //assume more than 10 chars between <> means broken tags, leave them rather than delete extra
    List<String> pL = lem.lemmatize(pText);
    bi = pL.size()-1; 
    for(int j=0;j<bi;j++){
      pL.add(pL.get(j)+" "+pL.get(j+1));
    }
    Set<String> pToks = new HashSet<String>(pL);
    double orig = (double)pToks.size();
    pToks.retainAll(qToks);
    double overlap = pToks.size();
    return overlap;
  }

}
