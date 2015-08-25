

import java.util.Random;
public class SOAR{
	public static void main(String[] args){
	
		ComplexMatrix x = ComplexMatrix.parseMatrix("[[1][2][1][-1]]");
      System.out.println(x.toStringMSWord());
      double norm = x.norm();
      System.out.println(norm);
		
      ComplexMatrix u = x.copy();
      u.set(1,1,u.get(1,1).add(norm));
      System.out.println(u.toStringMSWord());
      u = u.mult(1/u.norm());
      System.out.println(u.toStringMSWord());
      //System.out.println(u.norm());
    
      ComplexMatrix Q = ComplexMatrix.identity(4).sub(u.mult(u.transpose().mult(2)));
      System.out.println(Q.mult(Q));
      System.out.println(Q.toStringMSWord());
      
      System.out.println((Q.transpose().mult(x)).toStringMSWord());
      
      System.out.println(x.reflector());
	}
}
