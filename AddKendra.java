import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int customers = sc.nextInt();
        int products = sc.nextInt();

        // Map to count how many customers purchased each product
        Map<Integer, Integer> freqMap = new HashMap<>();

        for (int i = 0; i < customers; i++) {
            Set<Integer> uniqueProducts = new HashSet<>();

            for (int j = 0; j < products; j++) {
                uniqueProducts.add(sc.nextInt());
            }

            // Count product once per customer
            for (int product : uniqueProducts) {
                freqMap.put(product, freqMap.getOrDefault(product, 0) + 1);
            }
        }

        List<Integer> result = new ArrayList<>();

        // Product must appear in all customers
        for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
            if (entry.getValue() == customers) {
                result.add(entry.getKey());
            }
        }

        Collections.sort(result);

        // Print result
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
    }
}
