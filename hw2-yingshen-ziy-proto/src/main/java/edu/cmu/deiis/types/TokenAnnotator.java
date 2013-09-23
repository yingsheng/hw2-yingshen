package edu.cmu.deiis.types;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;

public class TokenAnnotator extends JCasAnnotator_ImplBase {
  
  private Pattern tokenPattern=
          Pattern.compile("(?<=\\s)(.+)(?=\\s)");
  
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    
    //get document text from JCas
    String docText=aJCas.getDocumentText();
    FSIterator<org.apache.uima.jcas.tcas.Annotation> QueIterator = aJCas.getAnnotationIndex(Question.type).iterator();
    
    while(QueIterator.hasNext()){
      //Answer annotation = new Answer(aJCas);
      Question QueAnnotation=(Question) QueIterator.next(); 
      int QueBegin=QueAnnotation.getBegin();
      int QueEnd=QueAnnotation.getEnd();
      String QueStr=docText.substring(QueBegin,QueEnd);
      
      //match token pattern
      Matcher matcher=tokenPattern.matcher(QueStr);
      int pos=0;
      
      if (matcher.find(pos)) {
        //add token annotation for answer
        Token annotation = new Token(aJCas);
        annotation.setBegin(matcher.start()+QueBegin-1);
        annotation.setEnd(matcher.end()+QueBegin-1);
        annotation.setConfidence(1.0);
        annotation.setCasProcessorId("TokenAnnotator");
        
        //add the annotation to index
        annotation.addToIndexes();
      }
    }

    
    
    FSIterator<org.apache.uima.jcas.tcas.Annotation> AnsIterator = aJCas.getAnnotationIndex(Answer.type).iterator();
//    Iterator<Annotation> typeIterator=aJCas.getAnnotationIndex(Answer.type);
 
     
    while(AnsIterator.hasNext()){
       //Answer annotation = new Answer(aJCas);
       Answer AnsAnnotation=(Answer) AnsIterator.next(); 
       int AnsBegin=AnsAnnotation.getBegin();
       int AnsEnd=AnsAnnotation.getEnd();
       String AnsStr=docText.substring(AnsBegin,AnsEnd);
       
       //match token pattern
       Matcher matcher=tokenPattern.matcher(AnsStr);
       int pos=0;
       
       if (matcher.find(pos)) {
         //add token annotation for answer
         Token annotation = new Token(aJCas);
         annotation.setBegin(matcher.start()+AnsBegin-1);
         annotation.setEnd(matcher.end()+AnsBegin-1);
         annotation.setConfidence(1.0);
         annotation.setCasProcessorId("TokenAnnotator");
         
         //add the annotation to index
         annotation.addToIndexes();
       }
     }
 
 
    

  }

}
