import java.util.Random;
public class EigenTester{
	public static void main(String[] args){
	
		//parameters	
		int itterations = 30;
		int size = 24;
		double magnitude = 1000;
		//end parameters
		
		long start, end;
		long[] times = new long[itterations];
		double[] errors = new double[itterations];
		Random rand = new Random();
		for(int i=0; i<itterations; i++){
			ComplexMatrix A = ComplexMatrix.random(rand,size,size,magnitude);
			Eigen eigenA;
			start = System.currentTimeMillis();
			eigenA = A.eigen();
			end = System.currentTimeMillis();
			times[i] = end-start;
			errors[i] = eigenA.maxError;
		}
		
		//statistics
		double aveError =0;
		double aveTime =0;
		double sdError =0;
		double sdTime =0;
		double maxError = 0;
		double maxTime = 0;
		for(int i=0; i<itterations; i++){
			aveError += errors[i];
			aveTime += times[i];
			if(errors[i]>maxError)
				maxError = errors[i];
			if(times[i]>maxTime)
				maxTime = times[i];
		}
		aveError = aveError/((double)itterations);
		aveTime = aveTime/((double)itterations);
		for(int i=0; i<itterations; i++){
			sdError += (errors[i]-aveError)*(errors[i]-aveError);
			sdTime += (times[i]-aveTime)*(times[i]-aveTime);
		}
		sdError = sdError/(itterations-1);
		sdTime = sdTime/(itterations-1);
		sdError = Math.sqrt(sdError);
		sdTime = Math.sqrt(sdTime);
		
		//output
		System.out.println("Size: "+size+"x"+size);
		System.out.println("Magnitude "+magnitude);
		System.out.println("Sample size "+itterations+"\n");
		System.out.println("Average time "+aveTime+"\nTime standard deviation "+sdTime);
		System.out.println("Max time "+maxTime);
		System.out.println("Average error "+aveError+"\nError standard deviation "+sdError);
		System.out.println("Max error "+maxError);
	}
}
