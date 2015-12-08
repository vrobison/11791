import java.util.Comparator;


import type.Passage;

//custom comparator for comparing Passages by score
public class PassageSort implements Comparator<Passage>{
  //returns 1 if first passage has a higher score,  
  //-1 if second has higher score, 
  //and 0 if equal score
  public int compare(Passage p1, Passage p2){
  if(p1.getScore() > p2.getScore()) return 1;
  if(p1.getScore() < p2.getScore()) return -1;
  return 0;
  }
}
