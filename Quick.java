////////////////////////////////////////////////////////////////////////////////////////
/////|| Ket-Meng Cheng ~ October 11, 2016 ~ Advanced Data Structures ~ Dr. Simon ||/////
/////|| ======================================================================== ||/////
/////||                            Program Number 2                              ||/////
/////||     This program utilizes random shuffling, picking random pivots, and   ||/////
/////|| Tukey's Ninther as general parameters. In this program, we also present  ||/////
/////|| various test cases: arrays of size 10^3, 10^4, 10^5, and 10^6, cutoffs   ||/////
/////|| at 10, 20, amd 50, random arrays, and corner cases of ordered arrays,    ||/////
/////|| reverse ordered arrays, keys being the same, and arrays of two keys.     ||/////
/////||                                                                          ||/////
/////||     Major credit due to Sedgewick for the general outline of his QS      ||/////
/////|| Algorithm, Fisher-Yates for the sort method, and Dylan Porter for        ||/////
/////|| allowing me to analyse his code in the case I messed something up.       ||/////
//////////////////////////////////////////////////////////////////////////////////////// 
//
import java.util.*;

public class Quick {

	//Shuffle Method credited to Fisher-Yates. 
	public static void shuffle(int[] ar) {
		Random rand = new Random();

		for(int i = ar.length - 1; i > 0; i--) {
			int index = rand.nextInt(i);
			int temp = ar[index];
			ar[index] = ar[i];
			ar[i] = temp;
		}
	}

	public static void sort(int[] a, int cutOff) {
		//shuffle(a);                             // Shuffling used for randomized array. Turn on if using Shuffled Array. Make sure tukey() and the appropriate swap() methods are commented out in lines 69 and 70.
		sort(a, 0, a.length - 1, cutOff);
	}

	private static void sort(int[] a, int lo, int hi, int cutOff) { 
		if ((hi - lo + 1) <= cutOff) {
			insertionSort(a, lo, hi);
			return;
		}	

		int j = partition(a, lo, hi);
		sort(a, lo, j-1, cutOff);
		sort(a, j+1, hi, cutOff);
	}
	
	//Tukey's ninther, edited from Sedgewick's Algorithms. (c) Princeton University
	private static void tukey(int[] a, int lo, int hi) { 
		int ninth = (hi - lo + 1) / 8;
		int mid = lo + (hi - lo + 1) / 2;
		int n1 = median(a, lo, lo+ninth, lo+ninth+ninth);
		int n2 = median(a, mid-ninth, mid, mid+ninth);
		int n3 = median(a, hi-ninth-ninth, hi-ninth, hi);
		int ninther = median(a, n1, n2, n3);
		swap(a, ninther, lo);
	}
	
	private static int median(int[] a, int x, int y, int z) {
		return ((a[x] < a[y]) ?
		       ((a[y] < a[z]) ? y : (a[x] < a[z]) ? z : x) :
		       ((a[z] < a[y]) ? y : (a[z] < a[x]) ? z : x));
	}	

	private static int partition(int[] a, int lo, int hi) {
		Random rand = new Random();	
		int i = lo;
		int j = hi + 1;
		tukey(a, lo, hi);                          // Used for Tukey's Ninther. Turn off if using Shuffled Arrays.
		//swap(a, lo, ( i + rand.nextInt(j-i)) );  // Used for pick random pivot. Turn off if using Shuffled Arrays.
		int v = a[lo]; 
		while (true) { 
			while (a[++i] < v)
				if (i == hi) break;
			while (v < a[--j])
				if (j == lo) break;    
			if (i >= j) break;
			swap(a, i, j);
		}
		swap(a, lo, j);
		return j;
	}

	//InsertionSort method.
	public static void insertionSort(int[] a, int lo, int hi) {
		for (int i = lo; i <= hi; i++) 
			for (int j = i; j > lo && a[j] < a[j-1]; j--) 
				swap(a, j, j-1); 
	}

	//Swapper for InsertionSort and QuickSort.
	public static void swap(int[] a, int b, int c) {
		int temp = a[b];
		a[b] = a[c];
		a[c] = temp;	
	}

	//Method for checking sort order.
	private static boolean isSorted(int[] a) { 
		for (int i = 0; i < a.length - 1; i++) {
			if(a[i] > a[i+1])
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		int cutOff = 50;
		long start, end, total = 0;	
		Random rand = new Random();

		//Default represents one key.
		int[] powerThree = new int[1000];
		int[] powerFour  = new int[10000];
		int[] powerFive  = new int[100000];
		int[] powerSix   = new int[1000000];

		//Block segment for backwards-ordered arrays. It should be i >= 0, but initialization makes it a zero anyways.
		/*
		for(int i = 999; i > 0; i--) 
			powerThree[i] = i;
		for(int i = 9999; i > 0; i--) 
			powerFour[i] = i;
		for(int i = 99999; i > 0; i--) 
			powerFive[i] = i;
		for(int i = 999999; i > 0; i--) 
			powerSix[i] = i;
		*/

		//Block segment for ordered arrays
		/*	
		for(int i = 0; i < 1000; i++) 
			powerThree[i] = i;
		for(int i = 0; i < 10000; i++) 
			powerFour[i] = i;
		for(int i = 0; i < 100000; i++) 
			powerFive[i] = i;
		for(int i = 0; i < 1000000; i++) 
			powerSix[i] = i;
		*/

		//Two Distinct Keys
		/*	
		for(int i = 0; i < 1000; i++) 
			if( (i%2) == 1)
				powerThree[i] = 1;
		for(int i = 0; i < 10000; i++) 
			if( (i%2) == 1)
				powerFour[i] = 1;
		for(int i = 0; i < 100000; i++) 
			if( (i%2) == 1)
				powerFive[i] = 1;
		for(int i = 0; i < 1000000; i++) 
			if( (i%2) == 1)
				powerSix[i] = 1;
		*/		

		//Randomized Keys	
		/*		
		for(int i = 0; i < 1000; i++) 
			powerThree[i] = rand.nextInt(1000);
		for(int i = 0; i < 10000; i++) 
			powerFour[i] = rand.nextInt(10000);
		for(int i = 0; i < 100000; i++) 
			powerFive[i] = rand.nextInt(100000);
		for(int i = 0; i < 1000000; i++) 
			powerSix[i] = rand.nextInt(1000000);
		*/	
		
		//Calculates time for 10^3
		start = System.nanoTime();
		Quick.sort(powerThree, cutOff);
		end = System.nanoTime();
		total = end - start;
		System.out.println(isSorted(powerThree));
		System.out.println("10^3: " + total);
		//Calculates time for 10^4
		start = System.nanoTime();
		Quick.sort(powerFour, cutOff);
		end = System.nanoTime();	
		total = end - start;
		System.out.println(isSorted(powerFour));
		System.out.println("10^4: " + total);
		//Calculates time for 10^5
		start = System.nanoTime();
		Quick.sort(powerFive, cutOff);
		end = System.nanoTime();
		total = end - start;
		System.out.println(isSorted(powerFive));
		System.out.println("10^5: " + total);
		//Calculates time for 10^6
		start = System.nanoTime();
		Quick.sort(powerSix, cutOff);
		end = System.nanoTime();
		total = end - start;
		System.out.println(isSorted(powerSix));
		System.out.println("10^6: " + total);
	}
}
