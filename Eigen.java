/*
Constructor
get value
set value
get vector
set vector
original matrix
vectors as matrix
diagonalized values
max error
*/

public class Eigen extends ComplexMatrix{
	public ComplexMatrix original;
	public double maxError;
	//Constructor
	public Eigen(ComplexMatrix A)throws IllegalArgumentException,ArithmeticException{
		super(A.rows+1,A.rows);
		if(A.rows!=A.col)
			throw new IllegalArgumentException("error: argument is non-square");
		original = A.copy();
		
		//find eigenvalues and eigenvectors
		//I will use the Wilkinson Shift because it usually converges
		ComplexMatrix Q = identity(col);
		ComplexMatrix T = A.copy();
		for(int i=0; i<A.rows; i++){
			Complex check = T.get(col-i,col-i);
			do{
				Complex shift = T.partition(1,1,col-i,col-i).wilkinsonShift();
				check = T.get(col-i,col-i);
				ComplexMatrix Q1 = imbed2(T.sub(identity(col).mult(shift)).partition(1,1,col-i,col-i).QRfactor()[0],col);
				Q = Q.mult(Q1);
				T = Q1.conjugateTranspose().mult(T).mult(Q1);
			}while(!check.equals(T.get(col-i,col-i)));
		}
		setValue(1,T.get(1,1));
		setVector(1,Q.partition(1,1,col,1));
		for(int i=2; i<rows; i++){
			setValue(i,T.get(i,i));
			ComplexMatrix S = T.partition(1,1,i-1,i-1);
			S = S.sub(identity(S.rows).mult(getValue(i)));
			for(int k=2; k<=S.rows; k++)
				for(int j=1; j<k; j++)
					S.set(k,j,Complex.Zero);
			ComplexMatrix r = T.partition(1,i,i-1,1);
			ComplexMatrix v = S.augment(r.mult(-1)).backSub();
			v = imbedV(v,col);
			setVector(i,Q.mult(v));
		}
		sort();
		maxError = maxError();
	}
		private ComplexMatrix imbed2(ComplexMatrix A,int dim){
			ComplexMatrix ret = identity(dim);
			for(int i=1; i<=A.rows; i++)
				for(int j=1; j<=A.col; j++)
					ret.set(i,j,A.get(i,j));
			return ret;
		}
		private static ComplexMatrix imbedV(ComplexMatrix A, int dim){
			ComplexMatrix ret = new ComplexMatrix(dim,1);
			for(int i=1; i<=A.rows; i++)
				ret.set(i,1,A.get(i,1));
			ret.set(A.rows+1,1,1.0);
			return ret;
		}
		private void sort(){
			int index = 1;
			for(int i=1; i<col; i++){
				index = i;
				for(int j=i+1; j<=col; j++){
					if(getValue(index).mag()<getValue(j).mag())
						index = j;
				}
				if(i!=index)
					colSwap(i,index);
			}
		}
	public Complex getValue(int i)throws IllegalArgumentException{
		if(i<1 || i>rows)
			throw new IllegalArgumentException("index "+i+" is out of bounds");
		return get(1,i);
	}
	private void setValue(int i, Complex A){
		set(1,i,A);
	}
	public ComplexMatrix getVector(int i)throws IllegalArgumentException{
		if(i<1 || i>rows)
			throw new IllegalArgumentException("index "+i+" is out of bounds");
		ComplexMatrix ret = new ComplexMatrix(rows-1,1);
		for(int j=1; j<rows; j++)
			ret.set(j,1,get(j+1,i));
		return ret;
	}
	private void setVector(int i, ComplexMatrix A){
		for(int j=1; j<rows; j++)
			set(j+1,i,A.get(j,1));
	}
	public ComplexMatrix getMatrix(){return original;}
	public ComplexMatrix getVectors(){return this.partition(2,1,col,col);}
	public ComplexMatrix diagonal(){
		ComplexMatrix ret = new ComplexMatrix(col,col);
		for(int i=1; i<rows; i++)
			ret.set(i,i,get(1,i));
		return ret;
	}
	public double maxError(){
		double ret = 0;
		for(int i=1; i<col; i++){
			double current = original.mult(getVector(i)).sub(getVector(i).mult(getValue(i))).norm();
			if(current>ret)
				ret = current;
		}
		return ret;
	}
	public String toString(){
		String ret = "";
		for(int i=1; i<=col; i++){
			ret += getValue(i).toString()+"\n";
			ret += getVector(i).transpose().toString()+"\n";
		}
		ret += "Max error = "+maxError;
		return ret;
	}
}
