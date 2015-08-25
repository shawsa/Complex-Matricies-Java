/*
Outline

class
	variables
	constructors
	static constant methods
		zero
		identity
	IO
		toString
		parseMatrix
	Get & Set
		get
		set
	Arithmetic 
		transpose
		addition
		scalar mult
		mult
		subtraction
		inverse
	Comparative Operators
		equals
		isReal
		isSymetric
		isHermitian
	Vector methods
		reflector
		norm
		infinity norm
	Format
		EROs
			rowSwap
			rowMult
			rowSum
		ECOs
			colSwap
			colMult
			colSum
		rref
		unit hessenburg reduction
		LU decomp
		QR factor
			imbed
	Solving Systems
		forwardSub
		backSub
		LU solve
		QR solve
	Eigenproblem methods
		eigen
		wilkinson shift
		QR algorithm Wiklinson shift
		QR rayleigh shift
		Implicit QR algorithm
		Implicit QR double shift algorithm
	Misc
		copy
		partition
		augment
		random
		largestElement
	
	
*/
import java.util.StringTokenizer;
import java.util.Random;
public class ComplexMatrix{
// 	variables
	int rows, col;
	private Complex[][] v;
	
// 	constructors
	public ComplexMatrix(int dim)throws IllegalArgumentException{
		if(dim<1)
			throw new IllegalArgumentException("Dimentions must be at least one by one.");
		rows = dim;
		col = dim;
		v = new Complex[rows][col];
	}
	public ComplexMatrix(int rows, int col)throws IllegalArgumentException{
		if(rows<1)
			throw new IllegalArgumentException("A matrix must have at least one row.");
		if(col<1)
			throw new IllegalArgumentException("A matrix must have at least one column.");
		this.rows = rows;
		this.col = col;
		v = new Complex[rows][col];
	}
	
// 	static constant methods
// 		zero
			public static ComplexMatrix zero(int dim)throws IllegalArgumentException{
				return new ComplexMatrix(dim);
			}
			public static ComplexMatrix zero(int rows, int col)throws IllegalArgumentException{
				return new ComplexMatrix(rows,col);
			}
// 		identity
			public static ComplexMatrix identity(int dim)throws IllegalArgumentException{
				ComplexMatrix ret = new ComplexMatrix(dim);
				for(int i=1; i<=dim; i++){
					ret.set(i,i,Complex.One);
				}
				return ret;
			}
			
// 	IO
// 		toString
			public String toString(){
				String[][] s = new String[rows][col];
				int[] max = new int[col];
				for(int i=0; i<rows; i++){
					for(int j=0; j<col; j++){
						s[i][j] = get(i+1, j+1).toString();
						if(max[j]<s[i][j].length())
							max[j] = s[i][j].length();
					}
				}
				for(int i=0; i<col; i++){
					max[i] += 3;
				}
				String ret = "";
				for(int i=0; i<rows; i++){
					for(int j=0; j<col; j++){
						ret += setLength(s[i][j], max[j]);
					}
					//if(i!=rows-1)
						ret += "\n";
				}
				return ret;
			}
			//set string length
			private static String setLength(String s, int length)throws IllegalArgumentException{
				s.replaceAll("\\s","");
				int l = s.length();
				if(length<l)
					throw new IllegalArgumentException();	
				if(l%8!=0){
					s +="\t";
					l += 8-(l%8);
				}
				while(length-l>0){
					s += "\t";
					l += 8;
				}
				return s;
			}
			//MS word
			public String toStringMSWord(){
				String ret = "";
				for(int i=1; i<=rows; i++){
					for(int j=1; j<col; j++){
						ret += get(i,j).toStringR()+"&";
					}
					if(i!=rows)
						ret += get(i,col).toStringR()+"@";
				}
				ret+= get(rows,col).toStringR();
				return ret;
			}
// 		parseMatrix
			public static ComplexMatrix parseMatrix(String arg)throws NumberFormatException{
				arg = arg.replaceAll("\\s","");
				int length = arg.length();
				int col = 0;
				int rows = -1;
				int rows2 = -1;
				for(int i=0; i<length; i++){
					switch(arg.charAt(i)){
						case '[':rows++; break;
						case ']':rows2++; break;
						case ',':col++; break;
						default:
					}
				}
				if(rows==0)
					throw new NumberFormatException("Matrix must have at least two sets of brackets.");
				if(col%rows!=0)
					throw new NumberFormatException("Matrix dimension mismatch.");
				if(rows!=rows2)
					throw new NumberFormatException("Bracket count off.");
				col = col/rows + 1;
				ComplexMatrix ret = new ComplexMatrix(rows, col);		
				StringTokenizer tokens = new StringTokenizer(arg,"[],",true);
				if(!tokens.nextToken().equals("["))
					throw new NumberFormatException("String must begin with \"[\"");
				for(int i=1; i<=rows; i++){
					if(!tokens.nextToken().equals("["))
						throw new NumberFormatException("Row "+i+" must begin with \"[\".");
					for(int j=1; j<=col-1; j++){
						try{
							ret.set(i,j, Complex.parseComplex(tokens.nextToken()));
						}catch(Exception e){throw new NumberFormatException("Invalid entry at "+i+","+j+".");}
						if(!tokens.nextToken().equals(","))
							throw new NumberFormatException("Row "+i+" has too few columns.");
					}
					//no more commas need last col
					try{
						ret.set(i,col, Complex.parseComplex(tokens.nextToken()));
					}catch(Exception e){throw new NumberFormatException("Invalid entry at "+i+","+col+".");}
					//ret.set(i,col,Complex.parseComplex(tokens.nextToken()));
					if(!tokens.nextToken().equals("]"))
						throw new NumberFormatException("Row "+i+" must end with a \"]\".");
				}
				if(!tokens.nextToken().equals("]"))
					throw new NumberFormatException("Matrix must end with \"]\".");
				if(tokens.hasMoreTokens())
					throw new NumberFormatException("String has extranious characters.");
				return ret;
			}	
// 	Get & Set
// 		get
			public Complex get(int a, int b)throws IllegalArgumentException{
				if(a<1)
					throw new IllegalArgumentException("row argument is less than one");
				if(b<1)
					throw new IllegalArgumentException("col argument is less than one");
				if(a>rows)
					throw new IllegalArgumentException("row argument is too large");
				if(b>col)
					throw new IllegalArgumentException("col argument is too large");
				Complex ret = v[--a][--b];
				if(ret==null)
					return Complex.Zero;
				return ret;
			}
// 		set
			public void set(int a, int b, Complex c)throws IllegalArgumentException{
				if(a<1)
					throw new IllegalArgumentException("row argument is less than one");
				if(b<1)
					throw new IllegalArgumentException("col argument is less than one");
				if(a>rows)
					throw new IllegalArgumentException("row argument is too large");
				if(b>col)
					throw new IllegalArgumentException("col argument is too large");
				v[--a][--b] = c;
			}
			public void set(int a, int b, double c)throws IllegalArgumentException{set(a,b,new Complex(c));}
// 	Arithmetic 
//			transpose
			public ComplexMatrix transpose(){
				ComplexMatrix ret = new ComplexMatrix(col,rows);
				for(int i=1; i<=col; i++){
					for(int j=1; j<=rows; j++){
						ret.set(i,j,get(j,i));
					}
				}
				return ret;
			}
			public ComplexMatrix conjugateTranspose(){
				ComplexMatrix ret = new ComplexMatrix(col,rows);
				for(int i=1; i<=col; i++){
					for(int j=1; j<=rows; j++){
						ret.set(i,j,get(j,i).conjugate());
					}
				}
				return ret;
			}
// 		addition
			public ComplexMatrix add(ComplexMatrix A)throws IllegalArgumentException{
				if(rows!=A.rows || col!=A.col)
					throw new IllegalArgumentException("nonconformable dimentions");
				ComplexMatrix ret = new ComplexMatrix(rows,col);
				for(int i=1; i<=rows; i++){
					for(int j=1; j<=col; j++){
						ret.set(i,j,get(i,j).add(A.get(i,j)));
					}
				}
				return ret;
			}
// 		scalar mult
			public ComplexMatrix mult(double a){
				ComplexMatrix ret = new ComplexMatrix(rows, col);
				for(int i=1; i<=rows; i++){
					for(int j=1; j<=col; j++){
						ret.set(i,j,get(i,j).mult(a));
					}
				}
				return ret;
			}
			public ComplexMatrix mult(Complex a){
				ComplexMatrix ret = new ComplexMatrix(rows, col);
				for(int i=1; i<=rows; i++){
					for(int j=1; j<=col; j++){
						ret.set(i,j,get(i,j).mult(a));
					}
				}
				return ret;
			}
// 		mult
			public ComplexMatrix mult(ComplexMatrix A)throws IllegalArgumentException{
				if(col!=A.rows)
					throw new IllegalArgumentException("nonconformable matrices");
				ComplexMatrix ret = new ComplexMatrix(rows,A.col);
				for(int i=1; i<=rows; i++){
					for(int j=1; j<=A.col; j++){
						Complex x = Complex.Zero;
						for(int k=1; k<=col; k++){
							x = x.add(get(i,k).mult(A.get(k,j)));
						}
						ret.set(i,j,x);
					}
				}
				return ret;
			}
// 		subtraction
			public ComplexMatrix sub(ComplexMatrix A)throws IllegalArgumentException{
				return add(A.mult(-1));
			}
// 		inverse
			public ComplexMatrix inverse()throws IllegalArgumentException, ArithmeticException{
				if(rows!=col)
					throw new IllegalArgumentException("Non-square matrix");
				ComplexMatrix A = copy();
				A = A.augment(identity(rows));
				//Perform rref
				//in a loop that counts rows
				for(int r=1; r<=rows; r++){
					//if zero swap for non zero
					if(A.get(r,r).equals(Complex.Zero)){
						for(int x=r; x<=rows; x++){
							if(!A.get(x,x).equals(Complex.Zero)){
								A.rowSwap(x,r);
								break;
							}
						}
					}
					//if still zero matrix is singular.
					if(A.get(r,r).equals(Complex.Zero)){
						throw new ArithmeticException("Matrix is singular. Inverse doesn't exist.");
					}
					//divide by leading entry
					A.rowMult(r,A.get(r,r).inverse());
					//use row sum to cancel all entries in the column
					for(int x=1; x<r; x++)
						A.rowSum(r,A.get(x,r).mult(-1),x);
					for(int x=r+1; x<=rows; x++)
						A.rowSum(r,A.get(x,r).mult(-1),x);
				}
				//A = A.rref();
				//System.out.println(A);
				return A.partition(1,col+1,rows,col);
			}

// 	Comparative Operators
// 		equals
			public boolean equals(ComplexMatrix A){
				if(rows!=A.rows || col!=A.col)
					return false;
				for(int i=1; i<=rows; i++){
					for(int j=1; j<=col; j++){
						if(!get(i,j).equals(A.get(i,j)))
							return false;
					}
				}
				return true;
			}
// 		isReal
			public boolean isReal(){
				for(int i=1; i<=rows; i++){
					for(int j=1; j<=col; j++){
						if(!get(i,j).isReal())
							return false;
					}
				}
				return true;
			}
// 		isSymetric
			public boolean isSymetric(){
				if(rows!=col)
					return false;
				for(int i=1; i<=rows; i++){
					for(int j=i+1; j<=col; j++){
						if(!get(i,j).equals(get(j,i)))
							return false;
					}
				}
				return true;
			}
// 		isHermitian
			public boolean isHermitian(){
				if(rows!=col)
					return false;
				for(int i=1; i<=rows; i++){
					for(int j=i+1; j<=col; j++){
						if(!get(i,j).equals(get(j,i).conjugate()))						
							return false;
					}
				}
				return true;
			}

// 	Vector methods
// 		reflector - returns the desired reflector matrix
			public ComplexMatrix reflector()throws IllegalArgumentException{
				if(col!=1)
					throw new IllegalArgumentException("Must have only one column to be a vector.");
				double n = norm();
				//The zero vector returns the identity
				if(n==0)
					return identity(rows);
				//Get a vector of the same magnitued in
					//the same direction as the 1,1 entry.
				Complex eitheta = Complex.Zero;
				if(!get(1,1).equals(Complex.Zero))
					eitheta = get(1,1).mult(1/get(1,1).mag());
				Complex sigma = eitheta.mult(n);
				//sigma is now the appropriate magnitued 
					//and in the appropriate direction
				//create u
				ComplexMatrix u = new ComplexMatrix(rows,1);
				u.set(1,1, sigma.add(get(1,1)));
				for(int i=2; i<=rows; i++)
					u.set(i,1,get(i,1));
				double uNorm = u.norm();
				double gamma = 2.0/(uNorm*uNorm);
				//Calculate Q as per Watkin's formula
				ComplexMatrix Q = identity(rows).sub(u.mult(u.conjugateTranspose()).mult(gamma));
				return Q;
			}
// 		norm
			public double norm()throws IllegalArgumentException{
				if(col!=1)
					throw new IllegalArgumentException("Must have only one column to be a vector.");
				double ret = 0;
				for(int i=1; i<=rows; i++){
					double temp = get(i,1).mag();
					ret += temp*temp;
				}
				return Math.sqrt(ret);
			}
// 		infinity norm
			public double infinityNorm()throws NumberFormatException{
				if(col!=1)
					throw new NumberFormatException("Must have only one column to be a vector.");
				double max = 0;
				for(int i=1; i<=rows; i++){
					double temp = get(i,1).mag();
					if(temp>max)
						max = temp;
				}
				return max;
			}
// 	Format
// 		EROs
// 			rowSwap
				public void rowSwap(int a, int b)throws IllegalArgumentException{
					if(a<1)
						throw new IllegalArgumentException("First argument:"+a+" less than one. No such row.");
					if(b<1)
						throw new IllegalArgumentException("Second argument:"+b+" less than one. No such row.");
					if(a>rows)
						throw new IllegalArgumentException("First argument:"+a+" too large. No such row.");
					if(b>rows)
						throw new IllegalArgumentException("Second argument:"+b+" too large. No such row.");
					if(a==b)
						return;
					for(int i=1; i<=col; i++){
						Complex temp = get(a,i);
						set(a,i,get(b,i));
						set(b,i,temp);
					}
				}
// 			rowMult
				public void rowMult(int a, Complex c)throws IllegalArgumentException{
					if(a<1)
						throw new IllegalArgumentException("First argument:"+a+" less than one. No such row.");
					if(a>rows)
						throw new IllegalArgumentException("First argument:"+a+" too large. No such row.");
					if(c.equals(Complex.One))
						return;
					for(int i=1; i<=col; i++){
						set(a,i,get(a,i).mult(c));
					}
				}
// 			rowSum
				public void rowSum(int a, Complex c, int b)throws IllegalArgumentException{
										//add c*row a to row b
					if(a<1)
						throw new IllegalArgumentException("First argument:"+a+" less than one. No such row.");
					if(b<1)
						throw new IllegalArgumentException("Second argument:"+b+" less than one. No such row.");
					if(a>rows)
						throw new IllegalArgumentException("First argument:"+a+" too large. No such row.");
					if(b>rows)
						throw new IllegalArgumentException("Second argument:"+b+" too large. No such row.");
					for(int i=1; i<=col; i++){
						set(b,i,get(b,i).add(c.mult(get(a,i))));
					}
				}
// 		ECOs
// 			colSwap
				public void colSwap(int a, int b)throws IllegalArgumentException{
					if(a<1)
						throw new IllegalArgumentException("First argument:"+a+" less than one. No such column.");
					if(b<1)
						throw new IllegalArgumentException("Second argument:"+b+" less than one. No such column.");
					if(a>col)
						throw new IllegalArgumentException("First argument:"+a+" too large. No such column.");
					if(b>col)
						throw new IllegalArgumentException("Second argument:"+b+" too large. No such column.");
					if(a==b)
						return;
					for(int i=1; i<=rows; i++){
						Complex temp = get(i,a);
						set(i,a,get(i,b));
						set(i,b,temp);
					}
				}
// 			colMult
				public void colMult(int a, Complex c)throws IllegalArgumentException{
					if(a<1)
						throw new IllegalArgumentException("First argument:"+a+" less than one. No such column.");
					if(a>col)
						throw new IllegalArgumentException("First argument:"+a+" too large. No such column.");
					if(c.equals(Complex.One))
						return;
					for(int i=1; i<=rows; i++){
						set(i,a,get(i,a).mult(c));
					}
				}
// 			colSum
				public void colSum(int a, Complex c, int b)throws IllegalArgumentException{
					if(a<1)
						throw new IllegalArgumentException("First argument:"+a+" less than one. No such column.");
					if(b<1)
						throw new IllegalArgumentException("Second argument:"+b+" less than one. No such column.");
					if(a>col)
						throw new IllegalArgumentException("First argument:"+a+" too large. No such column.");
					if(b>col)
						throw new IllegalArgumentException("Second argument:"+b+" too large. No such column.");
					for(int i=1; i<=rows; i++){
						set(i,b,get(i,b).add(c.mult(get(i,a))));
					}
				}
// 		rref
			public ComplexMatrix rref(){
				ComplexMatrix A = copy();
				//Set up counter to track column and row
				int aug = 0; // aug is the number of columns ahead the leading entry is
				//in a loop that counts rows
				for(int r=1; r<=rows; r++){
					//It is possible that all columns have been checked. If so, quit.
					if(r+aug>col)
						break;
					//if zero swap for non zero
					if(A.get(r,r+aug).equals(Complex.Zero)){
						for(int x=r; x<=rows; x++){
							if(!A.get(x,x+aug).equals(Complex.Zero)){
								A.rowSwap(x,r);
								break;
							}
						}
					}
					//if still zero repeat this row on next column
					if(A.get(r,r+aug).equals(Complex.Zero)){
						r--;
						aug++;
						continue;
					}
					//divide by leading entry
					A.rowMult(r,A.get(r,r+aug).inverse());
					//use row sum to cancel all entries in the column
					for(int x=1; x<r; x++)
						A.rowSum(r,A.get(x,r+aug).mult(-1),x);
					for(int x=r+1; x<=rows; x++)
						A.rowSum(r,A.get(x,r+aug).mult(-1),x);
				}
				return A;		
			}
// 		unit hessenburg reduction
			public ComplexMatrix[] unitHessenburgReduction()throws IllegalArgumentException{
				ComplexMatrix A = copy();
				ComplexMatrix Q = identity(rows);
				if(col!=rows)
					throw new IllegalArgumentException("non-square matrix");
				if(rows<3){
					ComplexMatrix[] ret = {this,identity(rows)};
					return ret;
				}
				for(int i=2; i<=rows-1; i++){
					ComplexMatrix Q1 = A.partition(i,i-1,A.rows-i+1,1).reflector().imbed(rows);
					Q = Q.mult(Q1);
					A = Q1.mult(A.mult(Q1));
				}
				for(int i=3; i<=rows; i++)
					for(int j=1; j<i-1; j++)
						A.set(i,j,Complex.Zero);
				ComplexMatrix[] ret = {A,Q};
				return ret;
			}
// 		LU decomp
			public ComplexMatrix[] LUfactor()throws IllegalArgumentException, ArithmeticException{
				if(col!=rows)
					throw new IllegalArgumentException("non-square matrix");
				ComplexMatrix L = identity(rows);
				ComplexMatrix U = new ComplexMatrix(rows);
				//calculate a row of U then a column of L
				for(int k=1; k<=rows; k++){
					//calculate a row of U
					for(int j=k; j<=col; j++){
						//calculate the k,j entry
						Complex temp = Complex.Zero;
						for(int m=1; m<k; m++){
							temp = temp.add(L.get(k,m).mult(U.get(m,j)));
						}
						temp = get(k,j).sub(temp);
						U.set(k,j,temp);
					}
					if(U.get(k,k).equals(Complex.Zero))
						throw new ArithmeticException("Error: A principle leading submatrix is singular.");
					//calculate a column of L
					for(int i=k+1; i<=rows; i++){
						//calculate the i,k entry
						Complex temp = Complex.Zero;
						for(int m=1; m<k; m++){
							temp = temp.add(L.get(i,m).mult(U.get(m,k)));
						}
						temp = get(i,k).sub(temp);
						temp = temp.divideBy(U.get(k,k));
						L.set(i,k,temp);
					}
				}				
				ComplexMatrix [] ret = {L,U};
				return ret;
			}
// 		QR factor
			public ComplexMatrix[] QRfactor()throws IllegalArgumentException{
				if(rows!=col)
					throw new IllegalArgumentException("error: non-square matrix");
				ComplexMatrix Q = identity(rows);
				ComplexMatrix R = copy();
				//Calculate R one column at a time.
				for(int i=1; i<rows; i++){
					//Partition the appropriate column vector
					ComplexMatrix v = R.partition(i,i,rows-i+1,1);
					//Calculate the reflector and imbed it in the identity.
					ComplexMatrix Q1 = v.reflector().imbed(rows);
					//Store the reflector in the Matrix Q
					Q = Q.mult(Q1);
					//Calculate R
					R = Q1.conjugateTranspose().mult(R);
					//set zeros for simplicity
					for(int j=i+1; j<=rows; j++)
						R.set(j,i,Complex.Zero);
				}
				ComplexMatrix[] ret = {Q,R};
				return ret;
			}
//				imbed
				public ComplexMatrix imbed(int dim){
					int index = dim-rows;
					ComplexMatrix ret = identity(dim);
					for(int i=1; i<=rows; i++)
						for(int j=1; j<=col; j++)
							ret.set(index+i,index+j,get(i,j));
					return ret;
				}
				public ComplexMatrix upperImbed(int dim){
					ComplexMatrix ret = identity(dim);
					for(int i=1; i<=rows; i++)
						for(int j=1; j<=col; j++)
							ret.set(i,j,get(i,j));
					return ret;
				}
				public ComplexMatrix imbed(int r, int c)throws IllegalArgumentException{
					if(r<rows || c<col)
						throw new IllegalArgumentException("Atempting to imbed in a smaller size matrix.");
					ComplexMatrix ret = new ComplexMatrix(r,c);
					for(int i=1; i<=rows; i++)
						for(int j=1; j<=col; j++)
							ret.set(i,j,get(i,j));
					return ret;
				}
// 	Solving Systems
// 		forwardSub
			public ComplexMatrix forwardSub()throws IllegalArgumentException, ArithmeticException{
				if(col!=rows+1)
					throw new IllegalArgumentException("Must be a square matrix that has been augmented by a vector.");
				for(int i=1; i<rows; i++)
					for(int j=i+1; j<col; j++)
						if(!get(i,j).equals(Complex.Zero))
							throw new IllegalArgumentException("Coeficient matrix is not lower triangular due to the "+i+","+j+" entry.");
				ComplexMatrix x = new ComplexMatrix(rows,1);
				for(int i=1; i<=rows; i++){
					Complex temp = Complex.Zero;
					for(int k=1; k<i; k++){
						temp = temp.add(x.get(k,1).mult(get(i,k)));
					}
					if(get(i,i).equals(Complex.Zero))
						throw new ArithmeticException("Coeficient matrix is singular. No unique solution.");
					temp = get(i,col).sub(temp);
					x.set(i,1,temp.divideBy(get(i,i)));
				}
				return x;
			}
// 		backSub
			public ComplexMatrix backSub()throws IllegalArgumentException, ArithmeticException{
				if(col!=rows+1)
					throw new IllegalArgumentException("Must be a square matrix that has been augmented by a vector.");
				for(int i=1; i<rows; i++)
					for(int j=i+1; j<col; j++)
						if(!get(j,i).equals(Complex.Zero))
							throw new IllegalArgumentException("Coeficient matrix is not upper triangular due to the "+j+","+i+" entry.");
				ComplexMatrix x = new ComplexMatrix(rows,1);
				for(int i=rows; i>0; i--){
					Complex temp = Complex.Zero;
					for(int j=i+1; j<col; j++){
						temp = temp.add(x.get(j,1).mult(get(i,j)));
					}
					temp = get(i,col).sub(temp);
					if(get(i,i).equals(Complex.Zero))
						throw new ArithmeticException("Coeficient matrix is singular. No unique solution.");
					x.set(i,1,temp.divideBy(get(i,i)));	
				}
				return x;
			}
// 		LU solve
			public ComplexMatrix LUsolve()throws IllegalArgumentException, ArithmeticException{
				if(col!=rows+1)
					throw new IllegalArgumentException("Must be a square matrix that has been augmented by a vector.");
				ComplexMatrix A = partition(1,1,rows,rows);
				ComplexMatrix b = partition(1,col,rows,1);
				ComplexMatrix[] LU;
				try{
					LU = A.LUfactor();
				}catch(ArithmeticException e){throw new ArithmeticException("Coefficient matrix has a singular"+
																		" leading submatrix. LU factorization doesn't exist.");}
				// LUx=b
				ComplexMatrix Ux = LU[0].augment(b).forwardSub();
				return LU[1].augment(Ux).backSub();
			}
// 		QR solve
			public ComplexMatrix QRsolve()throws IllegalArgumentException, ArithmeticException{
				if(col!=rows+1)
					throw new IllegalArgumentException("Must be a square matrix that has been augmented by a vector.");
				ComplexMatrix A = partition(1,1,rows,rows);
				ComplexMatrix b = partition(1,col,rows,1);
				ComplexMatrix[] QR = A.QRfactor();
				b = QR[0].conjugateTranspose().mult(b);
				return QR[1].augment(b).backSub();
			}
// 	Eigenproblem methods
//			eigen
			public Eigen eigen()throws IllegalArgumentException{
				if(col!=rows)
					throw new IllegalArgumentException("error: non-square matrix");
				return new Eigen(this);
			}
// 		wilkinson shift
			public Complex wilkinsonShift()throws IllegalArgumentException{
				if(col!=rows)
					throw new IllegalArgumentException("error: non-square matrix");
				if(rows==1)
					return get(1,1);
				ComplexMatrix A = partition(rows-1,col-1,2,2);
				Complex tau = A.get(1,1).add(A.get(2,2));
				Complex det = A.get(1,1).mult(A.get(2,2)).sub(A.get(1,2).mult(A.get(2,1)));
				Complex desc = tau.mult(tau).sub(det.mult(4));
				Complex ret = tau.add(Complex.sqrt(desc)).mult(.5);
				Complex ret2 = tau.sub(Complex.sqrt(desc)).mult(.5);
				if(ret.sub(A.get(2,2)).mag() < ret2.sub(A.get(2,2)).mag())
					return ret;
				return ret2;
			}
// 		QR algorithm Wiklinson shift
			public Complex[] QRalgorithmWilkinsonShift(int itterations)throws IllegalArgumentException{
				if(col!=rows)
					throw new IllegalArgumentException("error: non-square matrix");
				ComplexMatrix A = copy();
				if(rows==1){
					Complex[] ret = {get(1,1)};
					return ret;
				}
				Complex[] ret = new Complex[rows];
				for(int i = rows; i>1; i--){
					for(int j=0; j<itterations; j++){
						Complex shift = A.wilkinsonShift();
						ComplexMatrix B = A.sub(identity(A.rows).mult(shift));
						ComplexMatrix[] QR = B.QRfactor();
						B = QR[1].mult(QR[0]);
						A = B.add(identity(A.rows).mult(shift));
					}
					//System.out.println(A);
					ret[rows-i] = A.get(A.rows,A.rows);
					A = A.partition(1,1,A.rows-1,A.rows-1);
				}
				ret[rows-1] = A.get(1,1);
				return ret;
			}
// 		QR rayleigh shift
			public Complex[] QRalgorithmRayleighShift(int itterations)throws IllegalArgumentException{
				if(col!=rows)
					throw new IllegalArgumentException("error: non-square matrix");
				ComplexMatrix A = copy();
				if(rows==1 && col==1){
					Complex[] ret = {get(1,1)};
					return ret;
				}
				Complex[] ret = new Complex[rows];
				for(int i = rows; i>1; i--){
					for(int j=0; j<itterations; j++){
						Complex shift = A.get(A.rows,A.col);
						ComplexMatrix B = A.sub(identity(A.rows).mult(shift));
						ComplexMatrix[] QR = B.QRfactor();
						B = QR[1].mult(QR[0]);
						A = B.add(identity(A.rows).mult(shift));
					}
					//System.out.println(A);
					ret[rows-i] = A.get(A.rows,A.rows);
					A = A.partition(1,1,A.rows-1,A.rows-1);
				}
				ret[rows-1] = A.get(1,1);
				return ret;
			}
// 		Implicit QR algorithm
			public Complex[] implicitQRalgorithmRayleigh(int itterations)throws IllegalArgumentException, ArithmeticException{
				if(col!=rows)
					throw new IllegalArgumentException("error: non-square matrix");
				if(rows<3)
					return QRalgorithmWilkinsonShift(itterations);
				Complex[] eigen = new Complex[rows];
				ComplexMatrix A = unitHessenburgReduction()[0];
				for(int i=1; i<=rows-2; i++){
					for(int j=0; j<itterations; j++){
						A = A.implicit();
					}
					eigen[rows-i] = A.get(rows+1-i,rows+1-i);
					A=A.partition(1,1,A.rows-1,A.col-1);
				}
				Complex tau = A.get(1,1).add(A.get(2,2));
				Complex det = A.get(1,1).mult(A.get(2,2)).sub(A.get(2,1).mult(A.get(1,2)));
				Complex desc = tau.mult(tau).sub(det.mult(4));
				eigen[0] = tau.add(desc.sqrt()).mult(.5);
				eigen[1] = tau.sub(desc.sqrt()).mult(.5);
				return eigen;				
			}
				//implicit - forms a single implicit step using the rayliegh quotient
				private ComplexMatrix implicit()throws ArithmeticException{
					Complex shift = get(rows,rows);
					ComplexMatrix v = new ComplexMatrix(rows,1);
					v.set(1,1,get(1,1).sub(shift));
					v.set(2,1,get(2,1));
					double mag = v.norm();
					if(mag==0)
						throw new ArithmeticException("error: hessenburg reduction is improper");
					Complex c = v.get(1,1).divideBy(mag);
					Complex s = v.get(2,1).divideBy(mag);
					ComplexMatrix V = identity(rows);
					V.set(1,1,c);
					V.set(1,2,s.conjugate().mult(-1));
					V.set(2,1,s);
					V.set(2,2,c.conjugate());
					ComplexMatrix A = V.conjugateTranspose().mult(this).mult(V);
					//Chase the bulge
					for(int i=1; i<=col-2; i++){
						ComplexMatrix U = identity(rows);
						Complex a = A.get(i+1,i);
						Complex b = A.get(i+2,i);
						double m = Math.sqrt(a.mag()*a.mag()+b.mag()*b.mag());
						U.set(i+1,i+1,a.divideBy(m));
						U.set(i+1,i+2,b.conjugate().divideBy(-1*m));
						U.set(i+2,i+1,b.divideBy(m));
						U.set(i+2,i+2,a.conjugate().divideBy(m));
						A = U.conjugateTranspose().mult(A).mult(U);
					}
					return A;
				}
// 		Implicit QR double shift algorithm
// 	Misc
// 		copy
			public ComplexMatrix copy(){
				ComplexMatrix ret = new ComplexMatrix(rows, col);
				for(int i=0; i<rows; i++){
					for(int j=0; j<col; j++){
						ret.v[i][j] = v[i][j].copy();
					}
				}
				return ret;
			}
// 		partition
			public ComplexMatrix partition(int a, int b, int r, int c)throws IllegalArgumentException{
				if(a<1 || b<1)
					throw new IllegalArgumentException("Starting index must be at least one.");
				if(r<1 || c<1)
					throw new IllegalArgumentException("You must partition at least one row and column out.");
				if(a+r-1>rows)
					throw new IllegalArgumentException("End of column reached. Row index out of bounds.");
				if(b+c-1>col)
					throw new IllegalArgumentException("End of row reached. Column index out of bounds.");
				ComplexMatrix ret = new ComplexMatrix(r,c);
				for(int i=1; i<=r; i++){
					for(int j=1; j<=c; j++){
						ret.set(i,j,get(i+a-1,j+b-1));
					}
				}
				return ret;
			}
// 		augment
			public ComplexMatrix augment(ComplexMatrix A)throws IllegalArgumentException{
				if(rows!=A.rows)
					throw new IllegalArgumentException("Rows differ.");
				ComplexMatrix ret = new ComplexMatrix(rows,col+A.col);
				for(int i=1; i<=rows; i++){
					for(int j=1; j<=col; j++)
						ret.set(i,j,get(i,j));
					for(int j=1; j<=A.col; j++)
						ret.set(i,j+col,A.get(i,j));
				}
				return ret;
			}
//			Random
			public static ComplexMatrix random(int dim, double mag)throws IllegalArgumentException{
				Random rand = new Random();
				return random(rand,dim,dim,mag);
			}
			public static ComplexMatrix random(int rows, int col, double mag)throws IllegalArgumentException{
				Random rand = new Random();
				return random(rand,rows,col,mag);
			}
			public static ComplexMatrix random(Random rand, int rows, int col, double mag)throws IllegalArgumentException{
				ComplexMatrix ret = new ComplexMatrix(rows, col);
				for(int i=1; i<=rows; i++)
					for(int j=1; j<=col; j++)
						ret.set(i,j,Complex.random(rand,mag));
				return ret;
			}
//			Largest Element
			public Complex largestElement(){
				double length = 0;
				Complex ret = get(1,1);
				for(int i=1; i<=rows; i++){
					for(int j=1; j<=col; j++){
						if(get(i,j).mag()>length){
							length = get(i,j).mag();
							ret = get(i,j);
						}
					}
				}
				return ret;
			}
	

}
