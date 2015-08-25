

import java.util.Random;
public class QRalgorithmExample2{
	public static void main(String[] args){
	
		ComplexMatrix A = ComplexMatrix.parseMatrix("[[2,-2,2,1][2,-1,0,-1][0,-1,4,-1][-1,2,1,-4]]");
      System.out.println(A);
      ComplexMatrix[] QR = A.QRfactor();
      
      System.out.println(QR[0]);
      System.out.println(QR[1]);
      
      int n = 1;
      A = QR[1].mult(QR[0]);
      System.out.println(A);
      
      for(int i=0; i<100; i++){
         QR = A.QRfactor();
         A = QR[1].mult(QR[0]);
         n++;
         System.out.println("n = "+n);
         //System.out.println(A);
      }
      
      for(int i=0; i<50; i++){
         QR = A.QRfactor();
         A = QR[1].mult(QR[0]);
         n++;
         System.out.println("n = "+n);
         //System.out.println(A);
         System.out.println(A);
      }
      
      System.out.println(new Eigen(A));
		
      
	}
}
