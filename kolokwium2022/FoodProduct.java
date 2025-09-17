// KROK-03: FoodProduct – czyta wiele wektorów (województwa) i liczy średnią
// * Dziedziczy po Product
// * fromCsv(Path): mapuje "WOJEWÓDZTWO" -> wektor cen
// * getPrice(y, m): średnia arytmetyczna po wszystkich województwach
// * getPrice(y, m, provincePrefix): cena w województwie dopasowanym po prefiksie (case-insensitive)

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FoodProduct extends Product {
    private final Map<String, Double[]> provinceSeries; // np. "MAZOWIECKIE" -> [ceny...]
    private final Double[] avgSeries;                   // średnia po województwach

    private FoodProduct(String name, Map<String, Double[]> data) {
        super(name, data.values().iterator().next().length);
        this.provinceSeries = data;
        this.avgSeries = computeAverage();
    }

    public static FoodProduct fromCsv(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);
            if (lines.size() < 3) throw new IllegalArgumentException("Za mało linii w pliku: " + path);

            String name = lines.get(0).trim();    // nazwa produktu
            // lines.get(1) – nagłówek z "Jednostka terytorialna;2010 I;2010 II;..."
            Map<String, Double[]> map = new LinkedHashMap<>();

            for (int i = 2; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(";");
                String province = parts[0].trim(); // nazwa województwa
                Double[] prices = new Double[parts.length - 1];
                for (int j = 1; j < parts.length; j++) {
                    prices[j - 1] = Double.valueOf(parts[j].trim().replace(",", "."));
                }
                map.put(province.toUpperCase(Locale.ROOT), prices);
            }
            return new FoodProduct(name, map);
        } catch (IOException e) {
            throw new RuntimeException("Błąd odczytu pliku: " + path, e);
        }
    }

    private Double[] computeAverage() {
        int n = monthsCount;
        Double[] avg = new Double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0.0;
            int count = 0;
            for (Double[] series : provinceSeries.values()) {
                if (i < series.length) { // asekuracyjnie
                    sum += series[i];
                    count++;
                }
            }
            avg[i] = (count == 0) ? Double.NaN : (sum / count);
        }
        return avg;
    }

    @Override
    public double getPrice(int year, int month) {
        int idx = indexOf(year, month);
        return avgSeries[idx];
    }

    public double getPrice(int year, int month, String provincePrefix) {
        int idx = indexOf(year, month);
        // dopasowanie województwa po prefiksie (case-insensitive)
        String pref = provincePrefix.toUpperCase(Locale.ROOT);
        List<Map.Entry<String, Double[]>> matches = provinceSeries.entrySet().stream()
                .filter(e -> e.getKey().startsWith(pref))
                .collect(Collectors.toList());
        if (matches.isEmpty()) {
            throw new IndexOutOfBoundsException("Brak województwa dla prefiksu: \"" + provincePrefix + "\"");
        }
        if (matches.size() > 1) {
            List<String> names = matches.stream().map(Map.Entry::getKey).collect(Collectors.toList());
            throw new AmbigiousProductException(names);
        }
        return matches.get(0).getValue()[idx];
    }
}
