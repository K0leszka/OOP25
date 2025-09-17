// KROK-01: Klasa abstrakcyjna Product + mechanika ładowania i wyszukiwania po prefiksie
// Mega-komentarze: tłumaczę każde miejsce kłopotliwe na kolosie

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Interfejs funkcyjny: pozwala przypisać referencję do metody statycznej fromCsv(Path)
// Działa dla: FoodProduct::fromCsv oraz NonFoodProduct::fromCsv
@FunctionalInterface
interface ProductFactory {
    Product create(Path csvPath);
}

public abstract class Product {
    // --- stan wspólny dla każdego produktu ---
    protected final String name;      // nazwa produktu (z 1. linii pliku CSV)
    protected final int monthsCount;  // ile miesięcy danych (np. 2010 I ... 2022 III)

    // --- magazyn wszystkich wczytanych produktów (statyczny) ---
    private static final List<Product> products = new ArrayList<>();

    // Konstruktor chroniony – wywoływany tylko z podklas
    protected Product(String name, int monthsCount) {
        this.name = name;
        this.monthsCount = monthsCount;
    }

    // Getter nazwy – przydatny w drukowaniu i wyszukiwaniu
    public String getName() { return name; }

    // KROK-01A: Indeksowanie miesiąca
    // Konwencja w treści: dane zaczynają się od 2010 I (styczeń) i dalej co miesiąc.
    // Indeks = (rok - 2010) * 12 + (miesiąc - 1). Walidujemy zakres.
    protected int indexOf(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Niepoprawny miesiąc: " + month + " (1..12)");
        }
        int idx = (year - 2010) * 12 + (month - 1);
        if (idx < 0 || idx >= monthsCount) {
            throw new IndexOutOfBoundsException(
                    "Poza zakresem danych: year=" + year + ", month=" + month + ", idx=" + idx + ", monthsCount=" + monthsCount
            );
        }
        return idx;
    }

    // KROK-01B: Abstrakcyjna cena – każda podklasa zwraca własną logikę (średnia / 1 wektor)
    public abstract double getPrice(int year, int month);

    // KROK-03: Statyczne ładowanie z katalogu – addProducts
    public static void addProducts(Path dir, ProductFactory factory) {
        // Używamy Files.list, bierzemy tylko *.csv, mapujemy przez factory, dorzucamy do products
        try (Stream<Path> stream = Files.list(dir)) {
            stream.filter(p -> p.getFileName().toString().toLowerCase().endsWith(".csv"))
                    .map(factory::create) // np. NonFoodProduct::fromCsv albo FoodProduct::fromCsv
                    .forEach(products::add);
        } catch (IOException e) {
            throw new RuntimeException("Błąd odczytu katalogu: " + dir, e);
        }
    }

    public static void clearProducts() {
        products.clear();
    }

    public static List<Product> getProducts() {
        // zwracamy niemodyfikowalny widok, żeby ktoś nie namieszał z zewnątrz
        return Collections.unmodifiableList(products);
    }

    // KROK-04: Wyszukiwanie po prefiksie nazwy
    // Zasady z PDF-u:
    // - 0 dopasowań -> IndexOutOfBoundsException
    // - 1 dopasowanie -> zwracamy ten produkt
    // - >1 dopasowań -> AmbigiousProductException z listą kandydatów (po nazwie)
    public static Product byPrefix(String prefix) {
        String pref = prefix.toLowerCase(Locale.ROOT);
        List<Product> matches = products.stream()
                .filter(p -> p.getName().toLowerCase(Locale.ROOT).startsWith(pref))
                .collect(Collectors.toList());
        if (matches.isEmpty()) {
            throw new IndexOutOfBoundsException("Brak produktu dla prefiksu: \"" + prefix + "\"");
        }
        if (matches.size() == 1) {
            return matches.get(0);
        }
        // więcej niż 1 -> zbieramy nazwy i rzucamy AmbigiousProductException
        List<String> names = matches.stream().map(Product::getName).collect(Collectors.toList());
        throw new AmbigiousProductException(names);
    }

    @Override
    public String toString() {
        return "Product{name='" + name + "'}";
    }
}
