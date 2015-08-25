
public class SVD{

	public ComplexMatrix U,S,V, original;
	
	public SVD(ComplexMatrix A){
		original = A;
		boolean transpose = false;
		if(A.col>A.rows){
			A = A.conjugateTranspose();
			transpose = true;
		}
		S = new ComplexMatrix(A.rows,A.col);
		ComplexMatrix[] UBV = bidiagonalize(A);
		ComplexMatrix B = UBV[1].partition(1,1,A.col,A.col);
		ComplexMatrix[] UDV = implicitStep(B);
		B = UDV[1];
		ComplexMatrix U = UDV[0];
		ComplexMatrix V = UDV[2];
		while(B.get(B.rows-1,B.rows).mag()>1E-16){
				UDV = implicitStep(B);
				B = UDV[1];
				U = U.mult(UDV[0]);
				V = V.mult(UDV[2]);
		}
		S.set(B.rows,B.rows,B.get(B.rows,B.rows));
		//
		//System.out.println(S.get(B.rows,B.rows));
		//
		UBV[0] = UBV[0].mult(U.upperImbed(A.rows));
		UBV[2] = UBV[2].mult(V);
		B = B.partition(1,1,B.rows-1,B.col-1);
		U = ComplexMatrix.identity(B.rows);
		V = ComplexMatrix.identity(B.rows);
		for(int i=2; i<A.col; i++){
			while(B.get(B.rows-1,B.rows).mag()>1E-16){
				UDV = implicitStep(B);
				B = UDV[1];
				U = U.mult(UDV[0]);
				V = V.mult(UDV[2]);
			}
			S.set(B.rows,B.rows,B.get(B.rows,B.rows).r);
			//
			//System.out.println(S.get(B.rows,B.rows));
			//
			UBV[0] = UBV[0].mult(U.upperImbed(A.rows));
			UBV[2] = UBV[2].mult(V.upperImbed(A.col));
			B = B.partition(1,1,B.rows-1,B.col-1);
			U = ComplexMatrix.identity(B.rows);
			V = ComplexMatrix.identity(B.rows);
		}
		S.set(1,1,B.get(1,1).r);
		this.U = UBV[0];
		this.V = UBV[2];
		//adjust for non-semipositive singular values
		for(int i=1; i<=S.col; i++){
			if(S.get(i,i).dir()!=0){
				Complex rotator = S.get(i,i);
				rotator = rotator.divideBy(rotator.mag()).inverse();
				this.S.set(i,i,S.get(i,i).mult(rotator).r);
				this.U.colMult(i,rotator.conjugate());
			}
		}
		if(transpose){
			ComplexMatrix temp = this.U;
			this.S = this.S.conjugateTranspose();
			this.U = this.V;
			this.V = temp;
		}
		sort();
	}
	
	public static ComplexMatrix[] bidiagonalize(ComplexMatrix A){
		//Assume nxm where n is larger than m
		ComplexMatrix U,B,V;
		B = A;
		U = ComplexMatrix.identity(A.rows);
		V = ComplexMatrix.identity(A.col);
		if(A.rows<3){
			ComplexMatrix[] ret = {U,B,V};
			return ret;
		}
		ComplexMatrix Q = A.partition(1,1,A.rows,1).reflector();
		U = Q;
		B = Q.mult(B);
		if(A.col==1){
			ComplexMatrix[] ret = {U,B,V};
			return ret;
		}
		for(int i=2; i<=A.col; i++){
			//
			//System.out.println(B);
			B = B.conjugateTranspose();
			Q = B.partition(i,i-1,A.col-i+1,1).reflector().imbed(A.col);
			B = Q.mult(B).conjugateTranspose();
			//System.out.println(Q);
			//
			//System.out.println(B);
			V = V.mult(Q);
			Q = B.partition(i,i,A.rows-i+1,1).reflector().imbed(A.rows);
			//System.out.println(Q);
			B = Q.mult(B);
			U = U.mult(Q);
		}
		for(int i=1; i<=A.col; i++){
			for(int j=i+1; j<=A.rows; j++){
				B.set(j,i,Complex.Zero);
			}
		}
		for(int i=1; i<=A.col-2; i++){
			for(int j=i+2; j<=A.col; j++){
				B.set(i,j,Complex.Zero);
			}
		}
		ComplexMatrix[] ret = {U,B,V};
		return ret;
	}
	public static ComplexMatrix[] implicitStep(ComplexMatrix B){
		//B is assumed to be square and bydiagonal.
		ComplexMatrix V,U,V1,U1;
		V = ComplexMatrix.identity(B.rows);
		U = ComplexMatrix.identity(B.rows);
		//Calculate the shift	
		
		Complex a = B.get(B.rows-1,B.rows-1);
		a = a.mult(a.conjugate());
		Complex b = B.get(B.rows, B.rows);
		b = b.mult(b.conjugate());
		Complex c = B.get(B.rows-1,B.rows);
		c = c.mult(c.conjugate());
		Complex t = a.add(b).add(c);
		Complex det = a.mult(b);
		Complex desc = Complex.sqrt(t.mult(t).sub(det.mult(4)));
		Complex shift = t.add(desc).divideBy(2);
		Complex shift2 = t.sub(desc).divideBy(2);
		if(shift.sub(b).mag()>shift2.sub(b).mag())
			shift = shift2;
		//Complex shift = B.get(B.rows,B.rows);
		
		
		
		//First Rotator V
		Complex x = (B.get(1,1).mult(B.get(1,1).conjugate())).sub(shift);
		Complex y = B.get(1,1).mult(B.get(1,2));
		Complex magnitude = x.mult(x.conjugate()).add(y.mult(y.conjugate()));
		double mag = Math.sqrt(magnitude.r);
		Complex cos = x.divideBy(mag);
		Complex sin = y.divideBy(mag);
		V1 = ComplexMatrix.identity(B.rows);
		V1.set(1,1,cos);
		V1.set(1,2,sin.conjugate().mult(-1));
		V1.set(2,1,sin);
		V1.set(2,2,cos.conjugate());
		B = B.mult(V1);
		V = V.mult(V1);
		//Second Rotator
		x = B.get(1,1);
		y = B.get(2,1);
		magnitude = x.mult(x.conjugate()).add(y.mult(y.conjugate()));
		mag = Math.sqrt(magnitude.r);
		cos = x.divideBy(mag);
		sin = y.divideBy(mag);
		U1 = ComplexMatrix.identity(B.rows);
		U1.set(1,1,cos);
		U1.set(1,2,sin.conjugate().mult(-1));
		U1.set(2,1,sin);
		U1.set(2,2,cos.conjugate());
		B = U1.conjugateTranspose().mult(B);
		U = U.mult(U1);
		B.set(2,1,Complex.Zero);
		//The rest of them
		for(int i=2; i<B.rows; i++){
			//Do a V
			x = B.get(i-1,i).conjugate();
			y = B.get(i-1,i+1).conjugate();
			magnitude = x.mult(x.conjugate()).add(y.mult(y.conjugate()));
			mag = Math.sqrt(magnitude.r);
			if(mag==0){
				cos = new Complex(1,0);
				sin = Complex.Zero;
			}
			else{
				cos = x.divideBy(mag);
				sin = y.divideBy(mag);
			}
			V1 = ComplexMatrix.identity(B.col);
			V1.set(i,i,cos);
			V1.set(i,i+1,sin.conjugate().mult(-1));
			V1.set(i+1,i,sin);
			V1.set(i+1,i+1,cos.conjugate());
			B = B.mult(V1);
			V = V.mult(V1);
			B.set(i-1,i+1,Complex.Zero);
			//Do a U
			x = B.get(i,i);
			y = B.get(i+1,i);
			magnitude = x.mult(x.conjugate()).add(y.mult(y.conjugate()));
			mag = Math.sqrt(magnitude.r);
			if(mag==0){
				cos = Complex.One;
				sin = Complex.Zero;
			}else{
				cos = x.divideBy(mag);
				sin = y.divideBy(mag);
			}
			U1 = ComplexMatrix.identity(B.col);
			U1.set(i,i,cos);
			U1.set(i,i+1,sin.conjugate().mult(-1));
			U1.set(i+1,i,sin);
			U1.set(i+1,i+1,cos.conjugate());
			B = U1.conjugateTranspose().mult(B);
			U = U.mult(U1);
			B.set(i+1,i,Complex.Zero);
		}
		ComplexMatrix[] ret = {U,B,V};
		return ret;
	}
	//Sort
	public void sort(){
		int dim = S.col;
		if(S.rows<dim)
			dim = S.rows;
		for(int i=1; i<=dim; i++){
			int index = i;
			double mag1 = S.get(i,i).mag();
			for(int j=i+1; j<=dim; j++){
				if(S.get(j,j).mag()>mag1){
					index = j;
					mag1 = S.get(j,j).mag();
				}
			}
			if(index!=i){
				Complex temp = S.get(i,i);
				this.S.set(i,i,S.get(index,index));
				this.S.set(index,index,temp);
				this.U.colSwap(i,index);
				this.V.colSwap(i,index);
			}
		}
	}
	
}
