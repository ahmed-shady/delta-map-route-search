/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author aboshady
 *
 */
public class Search {

    private Search() {
    }

    public static List<Integer> bfs(int start, int goal, Map<Integer, Double>[] neighbors) {
        LinkedList<Integer> queue = new LinkedList<>();
        int[] reach = new int[neighbors.length];
        boolean visited[] = new boolean[neighbors.length];
        Arrays.fill(visited, false);
        queue.addLast(start);
        reach[start] = -1;
        visited[start] = true;
        while (!queue.isEmpty()) {
              
            int current = queue.removeFirst();
            if(current == goal) return construct(start, goal, reach);
            for (int e : neighbors[current].keySet()) {
                if (!visited[e]) {
                    visited[e] = true;
                    reach[e] = current;
                    queue.addLast(e);
                }

            }
        }
         
        return null;

    }

    public static List<Integer> astar(int start, int goal, Map<Integer, Double>[] neighbors, double[][] h) {

        ArrayList<Integer> openSet = new ArrayList<>(), closedSet = new ArrayList<>();
        int cameFrom[] = new int[neighbors.length];
        cameFrom[start] = -1;
        double[] gScore = new double[neighbors.length];
        Arrays.fill(gScore, Double.POSITIVE_INFINITY);
        gScore[start] = 0;
        double[] fScore = new double[neighbors.length];
        Arrays.fill(fScore, Double.POSITIVE_INFINITY);
        fScore[start] = h[start][goal];
        openSet.add(start);

        while (!openSet.isEmpty()) {
            int current = openSet.get(0);
            for (int e : openSet) {
                if (fScore[e] < fScore[current]) {
                    current = e;
                }
            }

            if (current == goal) {
                return construct(start, goal, cameFrom);
            }
            openSet.remove((Integer) current);
            closedSet.add(current);
            for (int neighbor : neighbors[current].keySet()) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }
                double tempGScore = gScore[current] + neighbors[current].get(neighbor);
                if (tempGScore >= gScore[neighbor]) {
                    continue;
                }
                cameFrom[neighbor] = current;
                gScore[neighbor] = tempGScore;
                fScore[neighbor] = gScore[neighbor] + h[neighbor][goal];

            }
        }
     
        return null;
    }

    public static List<Integer> dfs(int start, int goal, Map<Integer, Double>[] neighbors) {
        Stack<Integer> stack = new Stack();
        int[] reach = new int[neighbors.length];
        boolean visited[] = new boolean[neighbors.length];
        Arrays.fill(visited, false);
        stack.push(start);
        reach[start] = -1;
        visited[start] = true;
        while (!stack.isEmpty()) {
            int current = stack.pop();
            if(current == goal) return construct(start , goal ,reach);
            for (int e : neighbors[current].keySet()) {
                if (!visited[e]) {
                    visited[e] = true;
                    reach[e] = current;
                    stack.push(e);


                }

            }
        }
        return null;
    }

    public static List<Integer> bestFirst(int start, int goal, Map<Integer, Double>[] neighbors, double[][] h) {

        ArrayList<Integer> openSet = new ArrayList<>(), closedSet = new ArrayList<>();
        int cameFrom[] = new int[neighbors.length];
        cameFrom[start] = -1;
        double[] fScore = new double[neighbors.length];
        Arrays.fill(fScore, Double.POSITIVE_INFINITY);
        fScore[start] = h[start][goal];
        openSet.add(start);

        while (!openSet.isEmpty()) {
            int current = openSet.get(0);
            for (int e : openSet) {
                if (fScore[e] < fScore[current]) {
                    current = e;
                }
            }

            if (current == goal) {
                return construct(start, goal, cameFrom);
            }
            openSet.remove((Integer) current);
            closedSet.add(current);
            for (int neighbor : neighbors[current].keySet()) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }

                cameFrom[neighbor] = current;
                fScore[neighbor] =  h[neighbor][goal];

            }
        }
     
        return null;
    }

    private static List<Integer> construct(int start, int goal, int[] reach) {

        LinkedList<Integer> dir = new LinkedList<>();
        if (reach == null) {
            return null;
        }
        for (int i = goal;;) {
            dir.addFirst(i);
            if (reach[i] == -1) {
                break;
            } else {
                i = reach[i];
            }

        }
        return dir;

    }

}
