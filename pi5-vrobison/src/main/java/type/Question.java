

/* First created by JCasGen Mon Sep 28 00:00:30 EDT 2015 */
package type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;


import org.apache.uima.jcas.cas.FSList;


/** Stores the information about a question.
 * Updated by JCasGen Mon Oct 05 00:38:31 EDT 2015
 * XML source: /home/vivian/git/11791/pi5-andrewid/src/main/resources/descriptors/typeSystem.xml
 * @generated */
public class Question extends ComponentAnnotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Question.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Question() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Question(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Question(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Question(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: id

  /** getter for id - gets The identifier for the question.
   * @generated
   * @return value of the feature 
   */
  public String getId() {
    if (Question_Type.featOkTst && ((Question_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "type.Question");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Question_Type)jcasType).casFeatCode_id);}
    
  /** setter for id - sets The identifier for the question. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setId(String v) {
    if (Question_Type.featOkTst && ((Question_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "type.Question");
    jcasType.ll_cas.ll_setStringValue(addr, ((Question_Type)jcasType).casFeatCode_id, v);}    
   
    
  //*--------------*
  //* Feature: sentence

  /** getter for sentence - gets The text of the question.
   * @generated
   * @return value of the feature 
   */
  public String getSentence() {
    if (Question_Type.featOkTst && ((Question_Type)jcasType).casFeat_sentence == null)
      jcasType.jcas.throwFeatMissing("sentence", "type.Question");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Question_Type)jcasType).casFeatCode_sentence);}
    
  /** setter for sentence - sets The text of the question. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setSentence(String v) {
    if (Question_Type.featOkTst && ((Question_Type)jcasType).casFeat_sentence == null)
      jcasType.jcas.throwFeatMissing("sentence", "type.Question");
    jcasType.ll_cas.ll_setStringValue(addr, ((Question_Type)jcasType).casFeatCode_sentence, v);}    
   
    
  //*--------------*
  //* Feature: qpassages

  /** getter for qpassages - gets Passages associated with this question.
   * @generated
   * @return value of the feature 
   */
  public FSArray getQpassages() {
    if (Question_Type.featOkTst && ((Question_Type)jcasType).casFeat_qpassages == null)
      jcasType.jcas.throwFeatMissing("qpassages", "type.Question");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Question_Type)jcasType).casFeatCode_qpassages)));}
    
  /** setter for qpassages - sets Passages associated with this question. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setQpassages(FSArray v) {
    if (Question_Type.featOkTst && ((Question_Type)jcasType).casFeat_qpassages == null)
      jcasType.jcas.throwFeatMissing("qpassages", "type.Question");
    jcasType.ll_cas.ll_setRefValue(addr, ((Question_Type)jcasType).casFeatCode_qpassages, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for qpassages - gets an indexed value - Passages associated with this question.
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public Passage getQpassages(int i) {
    if (Question_Type.featOkTst && ((Question_Type)jcasType).casFeat_qpassages == null)
      jcasType.jcas.throwFeatMissing("qpassages", "type.Question");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Question_Type)jcasType).casFeatCode_qpassages), i);
    return (Passage)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Question_Type)jcasType).casFeatCode_qpassages), i)));}

  /** indexed setter for qpassages - sets an indexed value - Passages associated with this question.
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setQpassages(int i, Passage v) { 
    if (Question_Type.featOkTst && ((Question_Type)jcasType).casFeat_qpassages == null)
      jcasType.jcas.throwFeatMissing("qpassages", "type.Question");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Question_Type)jcasType).casFeatCode_qpassages), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Question_Type)jcasType).casFeatCode_qpassages), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: performance

  /** getter for performance - gets This stores all the measurements related to the question.
   * @generated
   * @return value of the feature 
   */
  public Performance getPerformance() {
    if (Question_Type.featOkTst && ((Question_Type)jcasType).casFeat_performance == null)
      jcasType.jcas.throwFeatMissing("performance", "type.Question");
    return (Performance)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Question_Type)jcasType).casFeatCode_performance)));}
    
  /** setter for performance - sets This stores all the measurements related to the question. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setPerformance(Performance v) {
    if (Question_Type.featOkTst && ((Question_Type)jcasType).casFeat_performance == null)
      jcasType.jcas.throwFeatMissing("performance", "type.Question");
    jcasType.ll_cas.ll_setRefValue(addr, ((Question_Type)jcasType).casFeatCode_performance, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    