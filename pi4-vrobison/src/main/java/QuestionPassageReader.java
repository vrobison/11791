import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.FileUtils;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

/**Filled in template with copied  bit from uimaj-examples
 * **/
public class QuestionPassageReader extends CollectionReader_ImplBase {
  
  public static final String PARAM_INPUTDIR = "InputDir";

  public static final String PARAM_ENCODING = "Encoding";

  public static final String PARAM_LANGUAGE = "Language";

  public static final String PARAM_SUBDIR = "BrowseSubdirectories";
  
  private ArrayList<File> mFiles;

  private String mEncoding;

  private String mLanguage;
  
  private Boolean mRecursive;

  private int mCurrentIndex;
  
  @Override
  public void initialize() throws ResourceInitializationException {
  //  System.err.println((String) getConfigParameterValue(PARAM_INPUTDIR));
    File directory = new File(((String) getConfigParameterValue(PARAM_INPUTDIR)).trim());
    mEncoding  = (String) getConfigParameterValue(PARAM_ENCODING);
    mLanguage  = (String) getConfigParameterValue(PARAM_LANGUAGE);
    mCurrentIndex = 0;

    // if input directory does not exist or is not a directory, throw exception
    if (!directory.exists() || !directory.isDirectory()) {
      throw new ResourceInitializationException(ResourceConfigurationException.DIRECTORY_NOT_FOUND,
              new Object[] { PARAM_INPUTDIR, this.getMetaData().getName(), directory.getPath() });
    }

    // get list of files in the specified directory, and subdirectories if the
    // parameter PARAM_SUBDIR is set to True
    mFiles = new ArrayList<File>();
    addFilesFromDir(directory);
  }
  
  @Override
  public void getNext(CAS aCAS) throws IOException, CollectionException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new CollectionException(e);
    }

    // open input stream to file
    File file = (File) mFiles.get(mCurrentIndex++);
    String text = FileUtils.file2String(file, mEncoding);
      // put document in CAS
    jcas.setDocumentText(text);

    // set language if it was explicitly specified as a configuration parameter
    if (mLanguage != null) {
      jcas.setDocumentLanguage(mLanguage);
    }
  }
  
  @Override
  public void close() throws IOException {
  }

  @Override
  public Progress[] getProgress() {
    return new Progress[] { new ProgressImpl(1, 1, Progress.BYTES) };
  //  return new Progress[] {new ProgressImpl(1,1,"foo")};
    //int complete, int total, String unit
   // return null;
  }

  @Override
  public boolean hasNext() throws IOException, CollectionException {
    return mCurrentIndex < mFiles.size();
  }
  
  private void addFilesFromDir(File dir) {
    File[] files = dir.listFiles();
    for (int i = 0; i < files.length; i++) {
      if (!files[i].isDirectory()) {
        mFiles.add(files[i]);
      } else if (mRecursive) {
        addFilesFromDir(files[i]);
      }
    }
  }

}
