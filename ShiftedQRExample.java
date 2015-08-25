
public class ShiftedQRExample{
	public static void main(String[] args){
	
		ComplexMatrix A = ComplexMatrix.parseMatrix("[[1,2,1,1][2,7,0,3][1,-1,6,4][-1,2,4,-2]]");
      Eigen eA = new Eigen(A);
		//System.out.println(A.toStringMSWord());
		
		Complex sigma = A.get(4,4);
		
		ComplexMatrix A1 = A.sub(ComplexMatrix.identity(4).mult(sigma));
		//System.out.println(A1.toStringMSWord());
		
		ComplexMatrix[] QR = A1.QRfactor();
		//System.out.println(QR[0].toStringMSWord());
      int x = 5;
      for(int i=1; i<x; i++){
         A = QR[0].transpose().mult(A).mult(QR[0]);
         System.out.println(i);
         System.out.println(sigma);
         System.out.println(A);
         System.out.println(A.toStringMSWord());
         sigma = A.get(4,4);
         A1 = A.sub(ComplexMatrix.identity(4).mult(sigma));
         QR = A1.QRfactor();
      }
      
      A = A.partition(1,1,3,3);
      System.out.println("Submatrix:\n"+A.toStringMSWord());
      sigma = A.get(3,3);
      A1 = A.sub(ComplexMatrix.identity(3).mult(sigma));
      QR = A1.QRfactor();
      int y = 7;
	   for(int i=x; i<y; i++){
         A = QR[0].transpose().mult(A).mult(QR[0]);
         System.out.println(i);
         System.out.println(sigma);
         System.out.println(A);
         System.out.println(A.toStringMSWord());
         sigma = A.get(3,3);
         A1 = A.sub(ComplexMatrix.identity(3).mult(sigma));
         QR = A1.QRfactor();
      }
      
      System.out.println(A.partition(1,1,2,2).toStringMSWord());
      
      
      
      System.out.println("\n\n"+eA);
	}
}
