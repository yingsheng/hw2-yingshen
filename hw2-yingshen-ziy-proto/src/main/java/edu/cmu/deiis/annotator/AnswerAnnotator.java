package edu.cmu.deiis.annotator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Answer;

public class AnswerAnnotator extends JCasAnnotator_ImplBase {

  // create regular expression pattern for answer score and answer
  private Pattern mIsCorrectPattern = 
          Pattern.compile("(?<=\\b[A]\\s)(\\b[0-1])(?=\\s)");

  private Pattern mAnswerPattern = 
         Pattern.compile("(?<=\\b[A]\\s\\b[0-1]\\s)(.+)[.]");

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {

    //get document text from JCas
    String docText=aJCas.getDocumentText();
    
    //match the question pattern
    Matcher matcherIsCorrect = mIsCorrectPattern.matcher(docText);
    Matcher matcherAnswer = mAnswerPattern.matcher(docText);
    int pos=0;
    while (matcherIsCorrect.find(pos) && matcherAnswer.find(pos)){
      Answer annotation = new Answer(aJCas);
      annotation.setBegin(matcherAnswer.start());
      annotation.setEnd(matcherAnswer.end());
      annotation.setConfidence(1.0);
      annotation.setCasProcessorId("AnswerAnnotator");
      if (docText.charAt(matcherIsCorrect.start())=='1'){
        annotation.setIsCorrect(true);
      }
      else{
        annotation.setIsCorrect(false);
      }
      
      annotation.addToIndexes();
      pos = matcherAnswer.end();
    }
       

  }

}
