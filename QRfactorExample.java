

import java.util.Random;
public class QRfactorExample{
	public static void main(String[] args){
	
		ComplexMatrix A = ComplexMatrix.parseMatrix("[[1,2,1,1][2,7,0,3][1,-1,6,4][-1,2,4,-2]]");
      
      ComplexMatrix x = A.partition(1,1,4,1);
      ComplexMatrix u = x.copy();
      u.set(1,1,u.get(1,1).add(u.norm()));
      u = u.mult(1/u.norm());
      
      ComplexMatrix Q1 = ComplexMatrix.identity(4).sub(u.mult(u.transpose()).mult(2));
      System.out.println(Q1);
      ComplexMatrix A1 = Q1.mult(A);
      System.out.println(A1);
      
      x = A1.partition(2,2,3,1);
      u = x.copy();
      u.set(1,1,u.get(1,1).add(u.norm()));
      u = u.mult(1/u.norm());
      ComplexMatrix Q2 = ComplexMatrix.identity(3).sub(u.mult(u.transpose()).mult(2));
      Q2 = Q2.imbed(4);
      System.out.println(Q2);
      
      ComplexMatrix A2 = Q2.mult(A1);
      System.out.println(A2);
      
      x = A2.partition(3,3,2,1);
      u = x.copy();
      u.set(1,1,u.get(1,1).add(u.norm()));
      u = u.mult(1/u.norm());
      ComplexMatrix Q3 = ComplexMatrix.identity(2).sub(u.mult(u.transpose()).mult(2));
      Q3 = Q3.imbed(4);
      System.out.println(Q3);
      
      ComplexMatrix A3 = Q3.mult(A2);
      System.out.println(A3);
      
      System.out.println(Q1.mult(Q2));
	}
}
