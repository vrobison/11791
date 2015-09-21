import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.EmptyFSList;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.NonEmptyFSList;

import type.Answer;
import type.InputDocument;
import type.Question;

public class TestElementAnnotator extends JCasAnnotator_ImplBase{
  
  private Pattern aLine = Pattern.compile("(?m)^A.*?$");
  private Pattern ansId = Pattern.compile("^A\\d*");
  private Pattern qLine = Pattern.compile("(?m)^Q.*?$");
  
  public void process(JCas aJCas) {
    String docText = aJCas.getDocumentText();
    InputDocument indoc = new InputDocument(aJCas);
    
    Question question = new Question(aJCas);
    Matcher q = qLine.matcher(docText);
    if(q.find()){
    indoc.setBegin(q.start());
    question.setBegin(q.start());
    question.setEnd(q.end());
    question.setSentence(q.group().substring(2));
    question.setId("Q");//TODO: number from filename?
    question.setComponentId("TestElementAnnotator");
    question.addToIndexes();
    }
    indoc.setQuestion(question);
    Matcher a = aLine.matcher(docText);
    
    if(a==null){indoc.setAnswers(new EmptyFSList(aJCas));}
    else{
      NonEmptyFSList head = new NonEmptyFSList(aJCas);
      NonEmptyFSList list = head;
      while(a.find()){
        Answer answer = new Answer(aJCas);
        answer.setBegin(a.start());
        answer.setEnd(a.end());
        Matcher id = ansId.matcher((a.group()));
        if(id.find()){
          answer.setId(id.group());
          String label = a.group().substring(id.end()+1, id.end()+2);
          if(label.equals("1")){answer.setLabel(true);}
          else{answer.setLabel(false);}
        //  answer.setSentence(a.group().substring(id.end()+1, id.end()+2));
          answer.setSentence(a.group().substring(id.end()+2));
        }
        answer.setComponentId("TestElementAnnotator");
        answer.addToIndexes();
        head.setHead(answer);
        if(a.end()!=docText.length()){
          head.setTail(new NonEmptyFSList(aJCas));
          head = (NonEmptyFSList)head.getTail();
        }
        else{
          head.setTail(new EmptyFSList(aJCas));
        }
      indoc.setEnd(a.end());
      }
    indoc.setAnswers(list);  
    }
    indoc.setComponentId("TestElementAnnotator");
    indoc.addToIndexes();
  }
}
