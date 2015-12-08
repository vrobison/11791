package rank;

import type.Passage;
import type.Question;

public class OtherRanker extends AbstractRanker {
  private StanfordUtils dep  = new StanfordUtils("tokenize, ssplit, pos, parse");

  /**
   * Returns a score of the given passage associated with the given question.
   * 
   * @param question
   * @param passage
   * @return a score of the passage
   */
  @Override
  public Double score(Question question, Passage passage) {
    String qText = question.getSentence();
    if(question.getSubject()==null){
    question.setSubject(dep.findSubj(qText));
    }
    String pText = passage.getText();
    //boolean score, is question subject present or not?
    if(pText.toLowerCase().contains(question.getSubject())) return 1.0;
    else return 0.0;
  }

}
