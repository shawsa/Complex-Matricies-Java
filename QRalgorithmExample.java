

import java.util.Random;
public class QRalgorithmExample{
	public static void main(String[] args){
	
		ComplexMatrix A = ComplexMatrix.parseMatrix("[[1,2,1,1][2,7,0,3][1,-1,6,4][-1,2,4,-2]]");
      ComplexMatrix B = A;
      ComplexMatrix[] QR = A.QRfactor();
      
      System.out.println(QR[0]);
      System.out.println(QR[1]);
      
      int n = 1;
      A = QR[1].mult(QR[0]);
      ComplexMatrix Q = QR[0];
      System.out.println(A);
      
      for(int i=0; i<149; i++){
         QR = A.QRfactor();
         Q = Q.mult(QR[0]);
         A = QR[1].mult(QR[0]);
         n++;
         //System.out.println("n = "+n);
         //System.out.println(A);
      }
      
      for(int i=0; i<2; i++){
         QR = A.QRfactor();
         Q = Q.mult(QR[0]);
         A = QR[1].mult(QR[0]);
         n++;
         System.out.println("n = "+n);
         //System.out.println(A);
         System.out.println(A);
      }
      
      System.out.println(Q);
      System.out.println(Q.mult(A).mult(Q.transpose()));
		
      Eigen eigen = new Eigen(B);
      ComplexMatrix v = ComplexMatrix.parseMatrix("[[-0.0840][0.0331][1][0]]");
      System.out.println(Q.mult(v));
      System.out.println(eigen);
      System.out.println(A.sub(ComplexMatrix.identity(4).mult(-4.193)));
      
	}
}
