public class SVD1{
	public ComplexMatrix original;
	public ComplexMatrix U,V,S;
	
	public SVD1(ComplexMatrix A){
		original = A;
		boolean transpose = false;
		if(A.rows<A.col){
			A = A.transpose();
			transpose = true;
		}
		Eigen eigen = new Eigen(A.mult(A.conjugateTranspose()));
		U = eigen.getVectors();
		S = eigen.diagonal().partition(1,1,A.rows,A.col);
		for(int i=1; i<=A.col; i++)
			S.set(i,i,S.get(i,i).sqrt());
		V = U.conjugateTranspose().mult(A).partition(1,1,A.col,A.col);
		for(int i=1; i<=A.col; i++){
			for(int j=1; j<=A.col; j++){
				V.set(i,j,V.get(i,j).divideBy(S.get(i,i)));
			}
		}
		System.out.println(U);
		System.out.println(S);
		System.out.println(V);
		System.out.println(U.mult(S).mult(V.conjugateTranspose()).sub(A));
		System.out.println(A);
	}
}
