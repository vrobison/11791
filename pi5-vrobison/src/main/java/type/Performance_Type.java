
/* First created by JCasGen Mon Sep 28 00:00:30 EDT 2015 */
package type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.cas.TOP_Type;

/** 
 * Updated by JCasGen Mon Oct 05 00:38:31 EDT 2015
 * @generated */
public class Performance_Type extends TOP_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Performance_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Performance_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Performance(addr, Performance_Type.this);
  			   Performance_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Performance(addr, Performance_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Performance.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("type.Performance");
 
  /** @generated */
  final Feature casFeat_pAt1;
  /** @generated */
  final int     casFeatCode_pAt1;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public double getPAt1(int addr) {
        if (featOkTst && casFeat_pAt1 == null)
      jcas.throwFeatMissing("pAt1", "type.Performance");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_pAt1);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPAt1(int addr, double v) {
        if (featOkTst && casFeat_pAt1 == null)
      jcas.throwFeatMissing("pAt1", "type.Performance");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_pAt1, v);}
    
  
 
  /** @generated */
  final Feature casFeat_pAt5;
  /** @generated */
  final int     casFeatCode_pAt5;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public double getPAt5(int addr) {
        if (featOkTst && casFeat_pAt5 == null)
      jcas.throwFeatMissing("pAt5", "type.Performance");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_pAt5);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPAt5(int addr, double v) {
        if (featOkTst && casFeat_pAt5 == null)
      jcas.throwFeatMissing("pAt5", "type.Performance");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_pAt5, v);}
    
  
 
  /** @generated */
  final Feature casFeat_rr;
  /** @generated */
  final int     casFeatCode_rr;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public double getRr(int addr) {
        if (featOkTst && casFeat_rr == null)
      jcas.throwFeatMissing("rr", "type.Performance");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_rr);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setRr(int addr, double v) {
        if (featOkTst && casFeat_rr == null)
      jcas.throwFeatMissing("rr", "type.Performance");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_rr, v);}
    
  
 
  /** @generated */
  final Feature casFeat_ap;
  /** @generated */
  final int     casFeatCode_ap;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public double getAp(int addr) {
        if (featOkTst && casFeat_ap == null)
      jcas.throwFeatMissing("ap", "type.Performance");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_ap);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setAp(int addr, double v) {
        if (featOkTst && casFeat_ap == null)
      jcas.throwFeatMissing("ap", "type.Performance");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_ap, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public Performance_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_pAt1 = jcas.getRequiredFeatureDE(casType, "pAt1", "uima.cas.Double", featOkTst);
    casFeatCode_pAt1  = (null == casFeat_pAt1) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_pAt1).getCode();

 
    casFeat_pAt5 = jcas.getRequiredFeatureDE(casType, "pAt5", "uima.cas.Double", featOkTst);
    casFeatCode_pAt5  = (null == casFeat_pAt5) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_pAt5).getCode();

 
    casFeat_rr = jcas.getRequiredFeatureDE(casType, "rr", "uima.cas.Double", featOkTst);
    casFeatCode_rr  = (null == casFeat_rr) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_rr).getCode();

 
    casFeat_ap = jcas.getRequiredFeatureDE(casType, "ap", "uima.cas.Double", featOkTst);
    casFeatCode_ap  = (null == casFeat_ap) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_ap).getCode();

  }
}



    