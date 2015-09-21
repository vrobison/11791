import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;

import type.Token;


public class TokenAnnotator extends JCasAnnotator_ImplBase{
  private Pattern basicToken = Pattern.compile("\\b\\S+?\\b");
  
  public void process(JCas aJCas) {
 // get document text
    String docText = aJCas.getDocumentText();
    Matcher matcher = basicToken.matcher(docText);
    while(matcher.find()){
      //found one->create annotation
      Token annotation = new Token(aJCas);
      annotation.setBegin(matcher.start());
      annotation.setEnd(matcher.end());
      annotation.setComponentId("TokenAnnotator");
      annotation.setScore(1);
      annotation.addToIndexes();
    }
  }

}
