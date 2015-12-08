import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;

import type.Measurement;
import type.Question;

/**
 * This CAS Consumer generates the report file with the method metrics
 */
public class PassageRankingWriter extends CasConsumer_ImplBase {
  final String PARAM_OUTPUTDIR = "OutputDir";

  final String OUTPUT_FILENAME = "ErrorAnalysis.csv";

  File mOutputDir;

  @Override
  public void initialize() throws ResourceInitializationException {
    String mOutputDirStr = (String) getConfigParameterValue(PARAM_OUTPUTDIR);
    if (mOutputDirStr != null) {
      mOutputDir = new File(mOutputDirStr);
      if (!mOutputDir.exists()) {
        mOutputDir.mkdirs();
      }
    }
  }

  @Override
  public void processCas(CAS arg0) throws ResourceProcessException {
    // Import the CAS as a aJCas
    JCas aJCas = null;
    File outputFile = null;
    PrintWriter writer = null;
    try {
      aJCas = arg0.getJCas();
      try {
        outputFile = new File(Paths.get(mOutputDir.getAbsolutePath(), OUTPUT_FILENAME).toString());
        outputFile.getParentFile().mkdirs();
        writer = new PrintWriter(outputFile);
      } catch (FileNotFoundException e) {
        System.out.printf("Output file could not be written: %s\n",
                Paths.get(mOutputDir.getAbsolutePath(), OUTPUT_FILENAME).toString());
        return;
      }

      writer.println("question_id,tp,fn,fp,precision,recall,f1");
      // Retrieve all the questions for printout
      List<Question> allQuestions = new ArrayList<Question>(JCasUtil.select(aJCas, Question.class));
      List<Question> subsetOfQuestions = RandomUtils.getRandomSubset(allQuestions, 10);

      // TODO: Here one needs to sort the questions in ascending order of their question ID
      int totTP = 0;
      int totFP = 0;
      int totFN = 0;
      double microF1 = 0.0;
      for (Question q : subsetOfQuestions) {
        Measurement m = q.getMeasurement();
        
        // TODO: Calculate actual precision, recall and F1
        double precision = (double)m.getTp()/(double)(m.getTp()+m.getFp());
        double recall;
        if(m.getTp()+m.getFn()==0) recall = 0;
        else recall = (double)m.getTp()/(double)(m.getTp()+m.getFn());
        double f1 = 0.0;
        if(precision +recall != 0) f1 = (precision * recall) / (precision+recall);
        microF1 += f1;
        totTP += m.getTp();
        totFP += m.getFp();
        totFN += m.getFn();
        writer.printf("%s,%d,%d,%d,%.3f,%.3f,%.3f\n", q.getId(), m.getTp(), m.getFn(), m.getFp(),
                precision, recall, f1);
        System.out.println(q.getSentence());
        for(int i = 0; i<Math.min(q.getQpassages().size(),5);i++){
          System.out.println(q.getQpassages(i).getLabel()+" "+q.getQpassages(i).getText());
        }
        System.out.println();
      }
      double macP = (double)totTP/(double)(totTP+totFP);
      double macR = (double)totTP/(double)(totTP+totFN);
      microF1/=10;
      System.out.println("Macro-average F1: "+((macP*macR)/(macP+macR)));
      System.out.println("Micro-average F1: "+microF1);
    } catch (CASException e) {
      try {
        throw new CollectionException(e);
      } catch (CollectionException e1) {
        e1.printStackTrace();
      }
    } finally {
      if (writer != null)
        writer.close();
    }
  }
}
