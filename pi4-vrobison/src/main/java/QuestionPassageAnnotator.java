import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

import type.InputDocument;
import type.Passage;
import type.Question;

//annotate text with questions and passages
public class QuestionPassageAnnotator extends JCasAnnotator_ImplBase{
  //regex for question lines
  private Pattern qPattern = Pattern.compile("(?m)^\\d{4} QUESTION .*?$");
  
  //regex for passage lines
  private Pattern pPattern = Pattern.compile("(?m)^\\d{4} [A-Z]{3}\\d{8}\\.\\d{4}.*?$");
  
  //regex for 4-digit question ids
  private Pattern idPattern = Pattern.compile("^\\d{4}");
  
  //regex for source document
  private Pattern srcPattern = Pattern.compile("[A-Z]{3}\\d{8}\\.\\d{4}");
  
  public void process(JCas aJCas) {
    //get text
    String docText = aJCas.getDocumentText();
    InputDocument indoc = new InputDocument(aJCas);
    
    //annotate questions
    Matcher q = qPattern.matcher(docText);
    while(q.find()){
      Question question = new Question(aJCas);
      question.setBegin(q.start());
      question.setEnd(q.end());
      //find question id for this line
      Matcher id = idPattern.matcher((q.group()));
      if(id.find()){
        //set id
        question.setId(id.group());
        //set sentence--assumes 4 digit id followed by "QUESTION"
        question.setSentence(q.group().substring(id.end()+9));
      }
      question.setComponentId("QuestionPassageAnnotator");
      question.addToIndexes();
    }
    //annotate passages
    Matcher p = pPattern.matcher(docText);
    while(p.find()){
      Passage passage = new Passage(aJCas);
      
      passage.setBegin(p.start());
      passage.setEnd(p.end());
      //find id of corresponding question
      Matcher qId = idPattern.matcher((p.group()));
      if(qId.find()){
        String questionId = qId.group();
        /*find the matching question
        there is surely a more efficient UIMA way to retrieve 
        a obj of a specific type with a specific feature value*/
        FSIndex qIndex = aJCas.getAnnotationIndex(Question.type);
        Iterator qIter = qIndex.iterator(); 
        while(qIter.hasNext()){
          Question temp = (Question)qIter.next();
          if(questionId.equals(temp.getId())){
            passage.setQuestion(temp);
          }
        }
      }
      //find & set the document source
      Matcher srcId = srcPattern.matcher(p.group());
      if(srcId.find()){
        passage.setSourceDocId(srcId.group());
      }
      //find & set label  
      Matcher l = Pattern.compile("-?\\d").matcher(p.group().substring(srcId.end()));
      if(l.find()){
        passage.setLabel(Integer.parseInt(l.group()) > 0); //1 and 2->true, -1->false
      }
      //store passage text so we can clean it
      //use annotation bounds to get original text
      String text = p.group().substring(srcId.end()).substring(l.end()+1);
      text.replace("<.*?>",""); //crude removal of html tags
      passage.setText(text);
      passage.setComponentId("DocumentAnnotator");
      
      passage.addToIndexes();
    }
  }

}