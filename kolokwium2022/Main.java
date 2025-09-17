// KROK-06: Main – demonstracja wszystkich wymagań z PDF-a
// Uruchomienie: zapewnij katalogi "data/nonfood" i "data/food" obok klasy Main.
// Przykład testów: prefiksy produktów, ładowanie, getPrice, getInflation.

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // 1) Załaduj produkty
        Product.clearProducts();
        Product.addProducts(Paths.get("data/nonfood"), NonFoodProduct::fromCsv);
        Product.addProducts(Paths.get("data/food"), FoodProduct::fromCsv);

        System.out.println("Załadowano: " + Product.getProducts().size() + " produktów.");

        // 2) FoodProduct::getPrice – średnia (2 arg) oraz województwo (3 arg)
        // Wyszukamy produkt po prefiksie nazwy – np. "Chleb"
        try {
            Product pChleb = Product.byPrefix("Chleb");
            if (pChleb instanceof FoodProduct fp) {
                double avg2010_1 = fp.getPrice(2010, 1); // średnia po województwach
                double maz2010_1 = fp.getPrice(2010, 1, "MAZO"); // województwo dopasowane prefiksem
                System.out.println("[Food] " + fp.getName() + " 2010-01 średnia=" + avg2010_1 + ", MAZOWIECKIE≈" + maz2010_1);
            }
        } catch (Exception ex) {
            System.out.println("Błąd testu FoodProduct: " + ex.getMessage());
        }

        // 3) Test byPrefix – 3 przypadki
        // 0 dopasowań → IndexOutOfBoundsException
        try {
            Product.byPrefix("Abc");
            System.out.println("BŁĄD: oczekiwano wyjątku dla prefiksu 'Abc'!");
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("[OK] 0 dopasowań: " + ex.getMessage());
        }

        // 1 dopasowanie – np. "Bu" → "Buraki - za 1 kg"
        try {
            Product one = Product.byPrefix("Bu");
            System.out.println("[OK] 1 dopasowanie: " + one.getName());
        } catch (Exception ex) {
            System.out.println("BŁĄD: dla 'Bu' nie powinno być wyjątku: " + ex.getMessage());
        }

        // >1 dopasowań – np. "Ja" → "Jabłka - za 1 kg", "Jaja kurze świeże - za 1 szt"
        try {
            Product many = Product.byPrefix("Ja");
            System.out.println("BŁĄD: oczekiwano wyjątku dla prefiksu 'Ja', a zwrócono: " + many.getName());
        } catch (AmbigiousProductException ex) {
            System.out.println("[OK] >1 dopasowań: " + ex.getMessage());
        }

        // 4) Koszyk – dodaj kilka produktów i policz cenę + inflację
        try {
            Cart cart = new Cart();

            // przykładowo: weź "Chleb ..." oraz "Mleko", oraz jakiś produkt nonfood jak "Odbiornik telewizyjny ..."
            Product chleb = Product.byPrefix("Chleb");
            Product mleko = Product.byPrefix("Mleko");
            Product tv = Product.byPrefix("Odbiornik telewizyjny");

            cart.add(chleb, 2.0); // 2 bochenków / sztuki
            cart.add(mleko, 1.0); // 1 litr
            cart.add(tv, 0.01);   // 0.01 szt. (przykładowy udział w koszyku)

            double price2010_1 = cart.getPrice(2010, 1);
            double price2022_3 = cart.getPrice(2022, 3);
            double infl = cart.getInflation(2010, 1, 2022, 3);

            System.out.println("Koszyk 2010-01 = " + price2010_1);
            System.out.println("Koszyk 2022-03 = " + price2022_3);
            System.out.println("Inflacja % (2010-01 → 2022-03) = " + infl);
        } catch (Exception ex) {
            System.out.println("Błąd testu koszyka: " + ex.getMessage());
        }
    }
}
