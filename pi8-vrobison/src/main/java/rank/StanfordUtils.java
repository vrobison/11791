package rank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;

//***Adapted from http://stackoverflow.com/questions/1578062/lemmatization-java***//


public class StanfordUtils {

    public StanfordCoreNLP pipeline;
    private Pattern word = Pattern.compile("(\\w*)(-\\d+\\))");

    public StanfordUtils(String annotators) {
        // Create StanfordCoreNLP object properties
        Properties props = new Properties();
        props.setProperty("annotators", annotators);
        this.pipeline = new StanfordCoreNLP(props);
    }

    public List<String> lemmatize(String documentText){
        List<String> lemmas = new LinkedList<String>();
        // Create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);
        
        // run all Annotators on this text
        pipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
                lemmas.add(token.get(LemmaAnnotation.class));
            }
        }
        return lemmas;
    }
    
    public List<String> parse(String documentText){
      LexicalizedParser lp = LexicalizedParser.loadModel(
              "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz",
              "-maxLength", "80", "-retainTmpSubcategories");
      TreebankLanguagePack tlp = new PennTreebankLanguagePack();
      // Uncomment the following line to obtain original Stanford Dependencies
      // tlp.setGenerateOriginalDependencies(true);
      GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
      String sent =  documentText;
      Tree parse = lp.parse(sent);
      GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
      Collection<TypedDependency>tdl = gs.typedDependenciesCCprocessed();
     
      Object[] tArr = tdl.toArray();
      List<String> parseAsList = new ArrayList<String>();
      for(int i =0; i< tdl.size();i++){
        parseAsList.add(tArr[i].toString());
      }
      
     return parseAsList;      
  }
  public String findSubj(String text){
    List<String> parse = parse(text);
    String root = "";
    String n ="";
    String subj;
    for(int i=0;i<parse.size();i++){
      if(parse.get(i).contains("ROOT")) root = parse.get(i);
    }
    for(int i=0;i<parse.size();i++){
      if(parse.get(i).startsWith("nsubj")){ 
        n = parse.get(i);
      }
    }
    Matcher m = word.matcher(n);
    String w = "";
    if(m.find()) w = m.group(1);
    return w.toLowerCase();
    
  }
}