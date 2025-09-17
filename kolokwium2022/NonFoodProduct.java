// KROK-02: NonFoodProduct – czyta pojedynczy wektor cen z CSV (nonfood/*)
// * Dziedziczy po Product
// * Ma statyczną fabrykę fromCsv(Path)
// * Implementuje getPrice(year, month) – zwraca z wektora cen

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class NonFoodProduct extends Product {
    private final Double[] prices; // jeden wektor cen miesięcznych

    private NonFoodProduct(String name, Double[] prices) {
        super(name, prices.length);
        this.prices = prices;
    }

    public static NonFoodProduct fromCsv(Path path) {
        String name;
        Double[] prices;
        try (Scanner scanner = new Scanner(Files.newBufferedReader(path))) {
            // 1) linia z nazwą
            name = scanner.nextLine().trim();
            // 2) nagłówek z miesiącami – pomijamy
            scanner.nextLine();
            // 3) linia z cenami rozdzielonymi ';' i z przecinkami jako separator dziesiętny
            prices = Arrays.stream(scanner.nextLine().split(";"))
                    .map(s -> s.trim().replace(",", "."))
                    .map(Double::valueOf)
                    .toArray(Double[]::new);
        } catch (IOException e) {
            throw new RuntimeException("Błąd odczytu pliku: " + path, e);
        }
        return new NonFoodProduct(name, prices);
    }

    @Override
    public double getPrice(int year, int month) {
        int idx = indexOf(year, month);
        return prices[idx];
    }
}
