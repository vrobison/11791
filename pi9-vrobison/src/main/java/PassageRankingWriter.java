import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;

import rank.CompositeRanker;
import rank.IRanker;
import rank.NgramRanker;
import rank.OtherRanker;
import type.Measurement;
import type.Passage;
import type.Question;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * This CAS Consumer generates the report file with the method metrics
 */
public class PassageRankingWriter extends CasConsumer_ImplBase {
  final String PARAM_OUTPUTDIR = "OutputDir";

  final String PARAM_NUMFOLDS = "NumFolds";

  final String OUTPUT_FILENAME = "RankMeasurements.csv";

  File mOutputDir;

  int numFolds;

  IRanker ngramRanker, otherRanker;

  CompositeRanker compositeRanker;

  @Override
  public void initialize() throws ResourceInitializationException {
    String mOutputDirStr = (String) getConfigParameterValue(PARAM_OUTPUTDIR);
    if (mOutputDirStr != null) {
      mOutputDir = new File(mOutputDirStr);
      if (!mOutputDir.exists()) {
        mOutputDir.mkdirs();
      }
    }
    numFolds = (int) getConfigParameterValue(PARAM_NUMFOLDS);

    // Initialize rankers
    compositeRanker = new CompositeRanker();
    ngramRanker = new NgramRanker();
    otherRanker = new OtherRanker();
    compositeRanker.addRanker(ngramRanker);
    compositeRanker.addRanker(otherRanker);
  }

  @Override
  public void processCas(CAS arg0) throws ResourceProcessException {
    System.out.println(">> Passage Ranking Writer Processing");
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

      writer.println("question_id,p_at_1,p_at_5,rr,ap");

      // Retrieve all the questions.
      List<Question> allQuestions = UimaUtils.getAnnotations(aJCas, Question.class);

      // Train a model to get the optimized weights.
      List<Double> weights = train(allQuestions);

      // Use the learned weights for rank aggregation (composition).
      compositeRanker.setWeights(weights);

      // TODO: Here one needs to sort the questions in ascending order of their question ID
      double overallPAt1 = 0.0;
      int numPass = 0;
      double MRR = 0.0;
      double MAP = 0.0;
      int numQs = 0;
      for (Question question : allQuestions) {
        List<Passage> passages = UimaUtils.convertFSListToList(question.getPassages(),
                Passage.class);

        // You could compare the composite ranker with individual rankers her.
        // List<Passage> ngramRankedPassages = ngramRanker.rank(question, passages);
        // List<Passage> otherRankedPassages = otherRanker.rank(question, passages);
        List<Passage> compositeRankedPassages = compositeRanker.rank(question, passages);
        List<Passage> ranked = compositeRankedPassages;//change just this line to test another ranker
        // TODO: Compute the measurement for this question.
        Measurement m = question.getMeasurement();
        
        //calculate & set p at 1
        if(ranked.get(0).getLabel()){
          m.setPrecisionAt1(1.0);
          overallPAt1 += 1;
        }
        else{m.setPrecisionAt1(0.0);}
        numPass++;  
        //calculate & set p at 5
        double numRel = 0;
        for(int i = 0; i<5;i++){
          if(ranked.get(i).getLabel()) numRel++;
         }
         m.setPrecisionAt5(numRel/5.0);
          
        //calculate & set rr
        //calculate & set ap
        double numerator = 0.0;
        numRel = 0;
        boolean found1st = false;
        for(int i=0; i< ranked.size(); i++){
          if(ranked.get(i).getLabel()){
            numRel++;
            numerator += numRel/(i+1); //add P(i+1)
            if(!found1st){ 
              m.setReciprocalRank(1/(i+1));//found first correct answer, set rr
              found1st = true;
            }
          }
        }
        if(numRel != 0) m.setAveragePrecision(numerator/numRel);
        else m.setAveragePrecision(0.0);
        question.setMeasurement(m);  
        MRR += m.getReciprocalRank();
        MAP += m.getAveragePrecision();
        numQs++;
        writer.printf("%s,%.3f,%.3f,%.3f,%.3f\n", question.getId(), m.getPrecisionAt1(),
                m.getPrecisionAt5(), m.getReciprocalRank(), m.getAveragePrecision());
      }
      MRR/=(double)numQs;
      MAP/=(double)numQs;
      System.out.println("MRR: "+MRR);
      System.out.println("MAP: "+MAP);
      System.out.println("MP@1:" +overallPAt1/(double)numPass);
    } catch (CASException e) {
      try {
        throw new CollectionException(e);
      } catch (CollectionException e1) {
        e1.printStackTrace();
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      if (writer != null)
        writer.close();
    }
  }

  /**
   * Trains a logistic regression model on the given question data through cross validation.
   * Optimizes the weights on individual rankers with respect to P@1.
   * 
   * @param allQuestions
   * @return the optimized weights
   */
  public List<Double> train(List<Question> questions) throws Exception{
    // TODO: Complete the implementation of this method.
    System.out.println(String.format(
            ">> Training a logistic regression model with %d-fold cross validation", numFolds));

    // weights[0] is for the n-gram ranker, and weights[1] is for the other ranker.
    List<Double> weights = new ArrayList<Double>();
    weights.add(1.0);
    weights.add(1.0);
    // 1. Split the data into k folds for cross validation.
    // folds[0] is the 1st fold, and folds[1] is the 2nd one, etc.
    List<List<Question>> folds = new ArrayList<List<Question>>();
    for(int i=0;i<numFolds;i++){
      folds.add(new ArrayList<Question>());
    }
    //make sure to randomize split
    Collections.shuffle(questions);
    for(int i=0;i<questions.size();i++){
      folds.get(i%numFolds).add(questions.get(i));
    }
    
    Double maxPAt1 = 0.0; 
    Double bestHyp = 0.0;
    for (int i=0; i<folds.size(); i++) {
      // 2. For each fold, do the following:

      // 2-1. Get the training and validation datasets for this fold.
      //randomize again before splitting
      Collections.shuffle(folds.get(i));
      Instances trainingData = convertQuestionsToInstances(folds.get(i).subList(0, folds.get(i).size()*4/5));
      List<Question> validationData = new ArrayList<Question>();
      validationData = folds.get(i).subList( folds.get(i).size()*4/5, folds.get(i).size()-1);
      
      // Reinitialize the logistic regression model by creating a new instance.
      Classifier classifier = new Logistic();
      trainingData.setClassIndex(trainingData.numAttributes() - 1);
   // 2-2. Train the model on the training dataset while tuning hyperparameters.
      classifier.buildClassifier(trainingData);
      
      Evaluation eTest = new Evaluation(trainingData);
      eTest.evaluateModel(classifier, trainingData);
      String strSummary = eTest.toSummaryString();
      
      //extract ridge and weights--there must be a better way, but the documentation is lacking
      String s = classifier.toString();
      String[] S = s.split("\\n");
      String[] s5 = S[5].split("\\s+");
      String[] s6 = S[6].split("\\s+");
      String[] s0 = S[0].split("\\s+");
      weights.set(0,Double.parseDouble(s5[s5.length-1]));
      weights.set(1,Double.parseDouble(s6[s6.length-1]));
      double hyp = Double.parseDouble(s0[s0.length-1]);
 //     System.out.println(weights);
      // 2-3. Compute P@1 of the trained composite model on the validation dataset.
      compositeRanker.setWeights(weights);
      double avPAt1 = 0.0;
      int numPs = 0;
      for(int j=0; j<validationData.size();j++){
        Question q = validationData.get(j);
        List<Passage> passages = UimaUtils.convertFSListToList(questions.get(j).getPassages(), Passage.class);
        List<Passage> ranked = compositeRanker.rank(q, passages);
        double pAt1 = 0.0;
        if (ranked.get(0).getLabel()) pAt1 = 1.0;
        avPAt1 += pAt1;
        numPs++;
      }
   // 3. Compute the average of P@1 over the validation datasets.
      avPAt1 /= (double)numPs;
      System.out.println("P@1"+avPAt1);
      // 4. Get the best hyperparameters that give you the best average of P@1.
      if (avPAt1 > maxPAt1){
        maxPAt1 = avPAt1;
        bestHyp = hyp;
        
      }
    }
    
    // 5. Train the model on the entire dataset with the best hyperparameters you get in step 4.
    Instances allData = convertQuestionsToInstances(questions);
    Logistic classifier = new Logistic();
    classifier.setRidge(bestHyp);
    allData.setClassIndex(allData.numAttributes() - 1);
    classifier.buildClassifier(allData);
    // 6. Return the learned weights you get in step 5.
    String s = classifier.toString();
    String[] S = s.split("\\n");
    String[] s5 = S[5].split("\\s+");
    String[] s6 = S[6].split("\\s+");
    weights.set(0,Double.parseDouble(s5[s5.length-1]));
    weights.set(1,Double.parseDouble(s6[s6.length-1]));
    return weights;
    
  }

  /**
   * Converts the given questions to Weka instances.
   * 
   * @param questions
   * @return Weka instances
   */
  public Instances convertQuestionsToInstances(List<Question> questions) {
    
    ArrayList attInfo = new ArrayList(3);
    Attribute ngram = new Attribute("ngramScore"); //ngram ranker score
    Attribute other = new Attribute("otherScore"); //subj finder ranker score
    ArrayList fvClassVal = new ArrayList(2); 
    fvClassVal.add("1");
    fvClassVal.add("0");
    Attribute gold = new Attribute("theClass", fvClassVal); //class
    
    attInfo.add(ngram);
    attInfo.add(other);
    attInfo.add(gold);
    int capacity = 0;
    //find needed number of instances
    
    for(int i=0;i<questions.size();i++){
      List<Passage> passages = UimaUtils.convertFSListToList(questions.get(i).getPassages(), Passage.class);
      capacity+=passages.size();
    }
    //declare Instances
    Instances insts = new Instances("QAPairs",attInfo,capacity);

    //set Instances 
    for(int i=0; i< questions.size();i++){
      Question question = questions.get(i);
      List<Passage> passages = UimaUtils.convertFSListToList(question.getPassages(), Passage.class);
      for(int j=0;j<passages.size();j++){
        Passage passage = passages.get(j);
        double n = ngramRanker.score(question, passage);
        double o = otherRanker.score(question, passage);
        Instance inst = new DenseInstance(3);
        inst.setValue(ngram,n);
        inst.setValue(other,o);
        String label = passages.get(j).getLabel() ? "1" : "0";
        inst.setValue(gold,label);
        insts.add(inst);
      }
    }

    return insts;
  }

}
