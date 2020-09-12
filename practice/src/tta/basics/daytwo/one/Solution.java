package tta.basics.daytwo.one;

import java.io.File;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

//from doSomething method we can make a reasonable assumption 
//the input should be an integer or a character 'q'
public class Solution {

	private class Coordination {
		private int x;
		private int y;

		public Coordination(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return this.x;
		}

		public int getY() {
			return this.y;
		}
	}
    public int[][] diagonalSort(int[][] mat) {
    	if (mat == null || mat.length == 0 || mat[0] == null || mat[0].length == 0) {
    		return null;
    	}
    	int lenR = mat.length, lenC = mat[0].length;
        Queue<Coordination> queue = new LinkedList<> ();
        boolean[][] visited = new boolean[lenR][lenC];
        queue.offer(new Coordination(lenR - 1, 0));
        visited[lenR - 1][0] = true;
        PriorityQueue<Integer> min = new PriorityQueue<> ();
        LinkedList<Coordination> list = new LinkedList<> ();
        while (!queue.isEmpty()) {
        	int size = queue.size();
        	if (!list.isEmpty() && !min.isEmpty()) {
	        	for (Coordination coordination : list) {
	    			int x = coordination.getX(), y = coordination.getY();
	    			mat[x][y] = min.poll();
	    		}
        	}
        	min.clear();
        	list.clear();
        	for (int i = 0; i < size; i++) {
        		Coordination coordination = queue.poll();
        		list.add(coordination);
        		int x = coordination.getX(), y = coordination.getY();
        		if (x - 1 > -1 && !visited[x - 1][y]) {
        			min.offer(mat[x - 1][y]);
        			queue.offer(new Coordination(x - 1, y));
        		}
        		if (y + 1 < lenC && !visited[x][y + 1]) {
        			min.offer(mat[x][y + 1]);
        			queue.offer(new Coordination(x, y + 1));
        		}
        	}
        }
        return mat;
    }
    
    public static void main() {
    	
    }
}