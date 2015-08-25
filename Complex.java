/*

Outline

Class
	Variables
		r
		i
	Static Variables
		One
		Zero
		I
	Constructors
	Arithmetic
		equals
		isReal
		direction
		angle
		add
		subtract
		multiply
		inverse
		divide
		conjugate
		square root
	ParseComplex
	ToString
	Copy
	Random
	Random(magnitude)

*/

import java.util.StringTokenizer;
import java.util.Random;
public class Complex{
	double r;
	double i;
	
	static Complex One = new Complex(1);
	static Complex Zero = new Complex(0);
	static Complex I = new Complex(0,1);
	
//constructors
	public Complex(int a){r=a;}
	public Complex(int a, int b){r=a; i=b;}
	public Complex(double a){r=a;}
	public Complex(double a, double b){r=a; i=b;}
	
//Arithmetic
	//Equals
	public boolean equals(Complex c){return (r==c.r && i==c.i);}
	//isReal
	public boolean isReal(){return i==0;}
	//Magnitude
	public double mag(){return Math.sqrt(r*r+i*i);}
	public static double mag(Complex c){return c.mag();}
	//Direction
	public double dir(){
		return Math.atan2(i,r);
	}
	public static double dir(Complex c){return c.dir();}
	//Addition
	public Complex add(int a){return new Complex(a+r,i);}
	public Complex add(double a){return new Complex(a+r,i);}
	public Complex add(Complex c){return new Complex(r+c.r,i+c.i);}
	
	//Subtraction
	public Complex sub(int a){return new Complex(r-a,i);}
	public Complex sub(double a){return new Complex(r-a,i);}
	public Complex sub(Complex c){return new Complex(r-c.r,i-c.i);}
	//multiplication
	public Complex mult(int a){return new Complex(r*a,i*a);}
	public Complex mult(double a){return new Complex(r*a,i*a);}
	public Complex mult(Complex c){return new Complex(r*c.r-i*c.i, r*c.i+i*c.r);}
	//inverse
	public Complex inverse()throws ArithmeticException{
		double n = r*r+i*i;
		if(n==0)
			throw new ArithmeticException("Cannot divide by zero.");
		return new Complex(r/n,(-1)*i/n);
	}
	//Divide
	public Complex divideBy(double a)throws ArithmeticException{
		if(a==0)
			throw new ArithmeticException("Cannot divide by zero.");
		return mult(1/a);
	}
	public Complex divideBy(Complex c)throws ArithmeticException{
		return this.mult(c.inverse());
	}
	//Conjugate
	public Complex conjugate(){return new Complex(r,(-1)*i);}
	//square root
	public Complex sqrt(){//gives the principle square root
		double length = Math.sqrt(mag());
		double angle = dir()/2;
		return new Complex(length*Math.cos(angle),length*Math.sin(angle));
	}
	public static Complex sqrt(Complex A){return A.sqrt();}
	public static Complex sqrt(double a){return (new Complex(a)).sqrt();}
	//To String
	public String toString(){
		String ret="";
		if(r==0){
			if(i==0)
				return "0";
			if(i==1)
				return "i";
			return i+"i";
		}
		ret += r;
		if(i==0)
			return ret;
		if(i==1)
			return ret+="+i";
		if(i<0)
			ret+=i+"i";
		else
			ret+="+"+i+"i";
		return ret;
	}
   public String toStringR(){
		String ret="";
		if(r==0){
			if(i==0)
				return "0";
			if(i==1)
				return "i";
			return i+"i";
		}
		ret += (double)(Math.round(10000*r))/10000;
		if(i==0)
			return ret;
		if(i==1)
			return ret+="+i";
		if(i<0)
			ret+=(double)(Math.round(10000*i))/10000+"i";
		else
			ret+="+"+(double)(Math.round(10000*i))/10000+"i";
		return ret;
	}
	//Parse Complex
	public static Complex parseComplex(String arg)throws NumberFormatException{
		
		arg.replaceAll("\\s","");
		StringTokenizer tokens = new StringTokenizer(arg,"i+-",true);
		
		StringStack stack = new StringStack();
		while(tokens.hasMoreTokens()){
			stack.push(tokens.nextToken());
		}
		
		//allocate space
		double imaginary = 0;
		double real = 0;
		
		//evaluate strings
		String current = stack.pop();
		
		//If imaginary component
		if(current.equals("i")){
			imaginary = 1.0;
			current = stack.pop();
			if(current==null)
				return new Complex(0,1);
			if(!(current.equals("-") || current.equals("+"))){
				imaginary *= Double.parseDouble(current);
				current = stack.pop();
			}
			if(current==null)
				return new Complex(0,imaginary);
			if(current.equals("-")){
				imaginary *= (-1);
			}else if(!current.equals("+")){
				throw new NumberFormatException();
			}
			current = stack.pop();
			if(current==null)
				return new Complex(0,imaginary);
		}//Now the imaginary component is handled and the real component is in current.
		real = Double.parseDouble(current);
		current = stack.pop();
		if(current==null)
			return new Complex(real,imaginary);
		if(current.equals("-"))
			real *= (-1);
		else if(!current.equals("+") || current!=null)
			throw new NumberFormatException();
		
		//Instantiate number
		return new Complex(real,imaginary);
	}
	//Copy
	public Complex copy(){
		return new Complex(r,i);
	}
	//Random
	public static Complex random(){
		Random rand = new Random();
		return random(rand);
	}
	public static Complex random(Random rand){
		double dir = rand.nextDouble()*2*Math.PI;
		return new Complex(Math.cos(dir),Math.sin(dir));
	}
	public static Complex random(double mag){
		Random rand = new Random();
		return random(rand, mag);
	}
	public static Complex random(Random rand, double mag){
		return random(rand).mult(mag*rand.nextDouble());
	}

}
