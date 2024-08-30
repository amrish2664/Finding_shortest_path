import java.util.*;

class City {
    String name;
    List<Edge> edges;

    City(String name) {
        this.name = name;
        this.edges = new ArrayList<>();
    }
}

class Edge {
    City destination;
    int cost;

    Edge(City destination, int cost) {
        this.destination = destination;
        this.cost = cost;
    }
}

class Dijkstra {
    public static void findShortestPath(City source, City destination) {
        Map<City, Integer> distances = new HashMap<>();
        Map<City, City> previousCities = new HashMap<>();
        PriorityQueue<City> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (City city : getAllCities(source)) {
            if (city.equals(source)) {
                distances.put(city, 0);
            } else {
                distances.put(city, Integer.MAX_VALUE);
            }
            pq.add(city);
        }

        while (!pq.isEmpty()) {
            City currentCity = pq.poll();

            if (currentCity.equals(destination)) {
                break;
            }

            for (Edge edge : currentCity.edges) {
                City neighbor = edge.destination;
                int newDist = distances.get(currentCity) + edge.cost;
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previousCities.put(neighbor, currentCity);
                    pq.add(neighbor);
                }
            }
        }

        printPathAndCost(source, destination, previousCities, distances);
    }

    private static Set<City> getAllCities(City source) {
        Set<City> cities = new HashSet<>();
        Queue<City> queue = new LinkedList<>();
        queue.add(source);
        while (!queue.isEmpty()) {
            City city = queue.poll();
            if (!cities.contains(city)) {
                cities.add(city);
                for (Edge edge : city.edges) {
                    queue.add(edge.destination);
                }
            }
        }
        return cities;
    }

    private static void printPathAndCost(City source, City destination, Map<City, City> previousCities, Map<City, Integer> distances) {
        List<City> path = new ArrayList<>();
        for (City at = destination; at != null; at = previousCities.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        if (path.get(0).equals(source)) {
            System.out.println("Path: " + path.stream().map(c -> c.name).reduce((a, b) -> a + " -> " + b).orElse(""));
            System.out.println("Total cost: " + distances.get(destination));
        } else {
            System.out.println("No path found.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        City cityA = new City("A");
        City cityB = new City("B");
        City cityC = new City("C");
        City cityD = new City("D");

        cityA.edges.add(new Edge(cityB, 5));
        cityA.edges.add(new Edge(cityC, 10));
        cityB.edges.add(new Edge(cityD, 3));
        cityC.edges.add(new Edge(cityD, 7));

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the source city: ");
        String sourceName = scanner.nextLine();
        System.out.print("Enter the destination city: ");
        String destinationName = scanner.nextLine();

        Map<String, City> cityMap = new HashMap<>();
        cityMap.put("A", cityA);
        cityMap.put("B", cityB);
        cityMap.put("C", cityC);
        cityMap.put("D", cityD);

        City source = cityMap.get(sourceName);
        City destination = cityMap.get(destinationName);

        if (source != null && destination != null) {
            Dijkstra.findShortestPath(source, destination);
        } else {
            System.out.println("Invalid city name entered.");
        }

        scanner.close();
    }
}

