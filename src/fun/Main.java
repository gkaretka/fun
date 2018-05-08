package fun;

import fun.path.Dijkstra;
import visualize.VisualizePath;

public class Main {

	private static void printArray(int[] array) {	
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + " ");
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
//		int[] a = {15, 16, 32, 19, 255, 568, 9874455};
//		printArray(Sort.SelectSort(a));
		
//		Dijkstra p = new Dijkstra();
//		p.setupGraph();
//		p.run();
		
		VisualizePath vp = new VisualizePath();

	}

}
