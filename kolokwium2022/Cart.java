// KROK-05: Cart – koszyk produktów z ilościami
// * add(Product p, double amount)
// * getPrice(year, month)
// * getInflation(y1, m1, y2, m2) – % zmiany wartości koszyka

import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
    private final Map<Product, Double> items = new LinkedHashMap<>(); // zachowaj kolejność dodawania

    // Dodaje ilość (szt/kg/l) danego produktu do koszyka
    public void add(Product p, double amount) {
        items.merge(p, amount, Double::sum);
    }

    // Wartość koszyka w danym (rok, miesiąc)
    public double getPrice(int year, int month) {
        double sum = 0.0;
        for (Map.Entry<Product, Double> e : items.entrySet()) {
            Product p = e.getKey();
            double qty = e.getValue();
            sum += qty * p.getPrice(year, month);
        }
        return sum;
    }

    // Inflacja (%) między (y1,m1) a (y2,m2) dla zawartości koszyka
    public double getInflation(int y1, int m1, int y2, int m2) {
        double p1 = getPrice(y1, m1);
        double p2 = getPrice(y2, m2);
        if (p1 == 0.0) throw new ArithmeticException("Wartość koszyka w (y1,m1) wynosi 0 – nie można liczyć procentu.");
        return (p2 - p1) / p1 * 100.0;
    }
}
