
# 📘 Notatka – Kolokwium 1 – Analiza cen produktów (2023)

## ✅ Krok 0: Klasa `NonFoodProduct` *(parsowanie danych z plików nonfood)*

- Zawiera prywatne pola:  
  `String name` – nazwa produktu  
  `Double[] prices` – ceny od 01.2010 do 03.2022  
- Konstruktor prywatny.  
- Metoda `fromCsv(Path path)` tworzy obiekt z pliku `.csv`.

---

## ✅ Krok 1: Abstrakcyjna klasa `Product` *(wspólna baza dla produktów)*

```java
public abstract class Product {
    private String name;
    public String getName() { return name; }
    public abstract double getPrice(int year, int month);
}
```

W `NonFoodProduct` nadpisujemy:

```java
@Override
public double getPrice(int year, int month) {
    if (month < 1 || month > 12 || year < 2010 || (year == 2022 && month > 3) || year > 2022)
        throw new IndexOutOfBoundsException();
    int index = (year - 2010) * 12 + (month - 1);
    return prices[index];
}
```

---

## ✅ Krok 2: Klasa `FoodProduct` *(średnia z województw i ceny regionalne)*

```java
public class FoodProduct extends Product {
    private Map<String, Double[]> provincePrices;

    public static FoodProduct fromCsv(Path path) {
        // parsowanie CSV do mapy: województwo → ceny[]
    }

    public double getPrice(int year, int month, String province) {
        if (!provincePrices.containsKey(province)) throw new IndexOutOfBoundsException();
        int index = (year - 2010) * 12 + (month - 1);
        return provincePrices.get(province)[index];
    }

    @Override
    public double getPrice(int year, int month) {
        return provincePrices.values().stream()
            .mapToDouble(arr -> arr[(year - 2010) * 12 + (month - 1)])
            .average().orElseThrow();
    }
}
```

---

## ✅ Krok 3: Statyczne zarządzanie listą produktów w klasie `Product`

```java
private static List<Product> products = new ArrayList<>();

public static void clearProducts() {
    products.clear();
}

public static void addProducts(Function<Path, Product> factory, Path dir) throws IOException {
    try (Stream<Path> files = Files.list(dir)) {
        files.map(factory).forEach(products::add);
    }
}
```

W `Main::main`:
```java
Product.addProducts(NonFoodProduct::fromCsv, Path.of("data/nonfood"));
Product.addProducts(FoodProduct::fromCsv, Path.of("data/food"));
```

---

## ✅ Krok 4: Wyszukiwanie po nazwie i wyjątek `AmbigiousProductException`

```java
public class AmbigiousProductException extends Exception {
    public AmbigiousProductException(List<String> names) {
        super("Niejednoznaczne: " + names);
    }
}
```

```java
public static Product getProduct(String prefix)
    throws IndexOutOfBoundsException, AmbigiousProductException {

    List<Product> matches = products.stream()
        .filter(p -> p.getName().startsWith(prefix))
        .toList();

    if (matches.size() == 0)
        throw new IndexOutOfBoundsException();
    if (matches.size() > 1)
        throw new AmbigiousProductException(
            matches.stream().map(Product::getName).toList());

    return matches.get(0);
}
```

---

## ✅ Krok 5: Klasa `Cart` *(symulacja koszyka i inflacji)*

```java
public class Cart {
    private Map<Product, Integer> contents = new HashMap<>();

    public void addProduct(Product p, int amount) {
        contents.put(p, amount);
    }

    public double getPrice(int year, int month) {
        return contents.entrySet().stream()
            .mapToDouble(e -> e.getKey().getPrice(year, month) * e.getValue())
            .sum();
    }

    public double getInflation(int y1, int m1, int y2, int m2) {
        double p1 = getPrice(y1, m1);
        double p2 = getPrice(y2, m2);
        int months = (y2 - y1) * 12 + (m2 - m1);
        return (p2 - p1) / p1 * 100 / months * 12;
    }
}
```

---

## 🧪 Przykładowe testy w `Main::main`

- Test `getPrice` na `NonFoodProduct` i `FoodProduct`
- Testy `getProduct("Bu")`, `getProduct("Abc")`, `getProduct("Ja")`
- Tworzenie koszyka, dodanie produktów, test `getPrice` i `getInflation`
