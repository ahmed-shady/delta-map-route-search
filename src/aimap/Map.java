package aimap;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import search.Search;

/**
 *
 * @author aboshady
 */
class Map {

    private int numberOfCities, numberOfRoutes;
    private City[] cities;
    private double[][] h;
    private HashMap<Integer, Double>[] neighbors; //Tree or Hash?!
    private String answer[];

    public Map(File file) throws IOException {

        Scanner input = new Scanner(file);
        numberOfCities = input.nextInt();
        input.nextLine();
        cities = new City[numberOfCities];
        h = new double[numberOfCities][numberOfCities];
        neighbors = (HashMap<Integer, Double>[]) new HashMap[numberOfCities];
        for (int i = 0; i < numberOfCities; i++) {
            String[] line = input.nextLine().split(" ");
            String name = "";
            for (int j = 0; j < line.length - 4; j++) {
                name += line[j] + ((j == line.length - 5) ? "" : " ");
            }
            cities[i] = new City(i, name,
                    Double.parseDouble(line[line.length - 4]), Double.parseDouble(line[line.length - 3]),
                    Double.parseDouble(line[line.length - 2]), Double.parseDouble(line[line.length - 1]));

            neighbors[i] = new HashMap<>();

        }
        numberOfRoutes = input.nextInt();
        for (int i = 0; i < numberOfRoutes; i++) {
            int cityU = input.nextInt(), cityV = input.nextInt();
            double distance = input.nextDouble();
            neighbors[cityU].put(cityV, distance);
            neighbors[cityV].put(cityU, distance);
        }
        fillHFunction();

    }

    private void fillHFunction() {
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {

                double deltaX = (cities[i].mapX - cities[j].mapX) * 111.2;
                double deltaY = (cities[i].mapY - cities[j].mapY) * 96.3;
                h[i][j] = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            }
        }

    }

    public String[] getCitiesNames() {
        String[] names = new String[numberOfCities];
        for (int i = 0; i < numberOfCities; i++) {
            names[i] = cities[i].name;
        }
        return names;

    }

    public String[] getAnswer() {
        return answer;
    }

    public double[] search(int start, int goal, Type type) {
        switch (type) {
            case BFS:
                return routePoints(Search.bfs(start, goal, neighbors));
            case DFS:
                return routePoints(Search.dfs(start, goal, neighbors));
            case A_STAR:
                return routePoints(Search.astar(start, goal, neighbors, h));
            case BEST_FIRST_SEARCH:
            return routePoints(Search.bestFirst(start, goal , neighbors, h));
        }
        return null;
    }

    private double[] routePoints(List<Integer> routeCities) {
        if (routeCities == null) {
            return null;
        }
        double[] points = new double[routeCities.size() * 2];
        answer = new String[routeCities.size()];
        for (int i = 0; i < routeCities.size(); i++) {
            points[2 * i] = cities[routeCities.get(i)].imgX;
            points[2 * i + 1] = cities[routeCities.get(i)].imgY;
            answer[i] = cities[routeCities.get(i)].name;
        }
        return points;
    }

    public enum Type {
        BFS, DFS, A_STAR, BEST_FIRST_SEARCH;
    }

    class City {

        private int id;
        private String name;
        public double mapX, mapY, imgX, imgY;

        private City(int id, String name, double mapX, double mapY, double imgX, double imgY) {
            this.id = id;
            this.name = name;
            this.mapX = mapX;
            this.mapY = mapY;
            this.imgX = imgX;
            this.imgY = imgY;
        }

        public int getId() {
            return id;
        }
    }

}
