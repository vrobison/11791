import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceProcessException;

import type.Passage;


/**Template mostly filled in with copied bits from pi3 in turn copied from uimaj-examples
 */
public class PassageRankingWriter extends CasConsumer_ImplBase {
  public static final String PARAM_OUTPUTDIR = "OutputDir";

  @Override
  public void processCas(CAS aCAS) throws ResourceProcessException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }

      String outFileName = "passageRanking.txt";
      File outFile = new File((String)getConfigParameterValue(PARAM_OUTPUTDIR), outFileName);
      
      try {
        //write a txt file
        FileWriter fw = new FileWriter(outFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        //iterate over passages
        FSIndex passIndex = jcas.getAnnotationIndex(Passage.type);
        Iterator passIter = passIndex.iterator(); 
        while (passIter.hasNext()) {
          //get current passage
          Passage p = (Passage)passIter.next();
          //find the location of the label we want to replace
          Matcher l = Pattern.compile(" -?\\d ").matcher(p.getCoveredText());
        //truncate score to 4 decimal places
          double score = (double)((int)(p.getScore()*10000))/10000;
          if(l.find()){
          //write out with score subbed in for label
          bw.write(p.getCoveredText().substring(0, l.start())+" "+score+" "+p.getCoveredText().substring(l.end())+"\n"); 
          }
          else{
            //if line is malformed, print the original line so it doesn't throw off the line numbers of all the others
            bw.write(p.getCoveredText()+"\n");
          }
              
        }
        //close writer
        bw.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    
  }

}
