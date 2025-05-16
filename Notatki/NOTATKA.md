# Notatka do kolokwium — Inflacja i produkty (food & nonfood)

## 📁 Struktura danych
- **nonfood** – Pliki .csv z cenami produktów niespożywczych.
  - Format: `Produkt; 01.2010; 02.2010; ...`
- **food** – Pliki .csv z cenami produktów spożywczych w województwach.
  - Format: `Województwo; 01.2010; 02.2010; ...`

## 🧩 Krok po kroku — co zrobić

---

## 🧱 Krok 1: Klasa bazowa `Product`

**Zadanie:**
- Utworzenie klasy abstrakcyjnej `Product`.
- Zawiera `String name` oraz metodę abstrakcyjną:
  ```java
  public abstract double getPrice(int year, int month);
  ```

**Uwagi:**
- `name` to wspólna nazwa dla każdego produktu (spożywczego lub nie).
- Będzie dziedziczona przez inne klasy.

---

## 🧱 Krok 2: `NonFoodProduct` i `FoodProduct`

### ✅ `NonFoodProduct`
- Posiada pole `Double[] prices`.
- Implementuje metodę `getPrice(int year, int month)` poprzez wyliczenie indeksu:
  ```java
  int index = (year - 2010) * 12 + (month - 1);
  ```

### ✅ `FoodProduct`
- Ma mapę `Map<String, Double[]> provincePrices`.
- Implementuje:
  - `double getPrice(int y, int m, String province)` – zwraca dla konkretnego województwa.
  - `double getPrice(int y, int m)` – zwraca średnią ze wszystkich województw.

**Walidacja daty:**
```java
if (year < 2010 || year > 2022 || month < 1 || month > 12 || (year == 2022 && month > 3))
    throw new IndexOutOfBoundsException();
```

---

## 🧱 Krok 3: Ładowanie danych do `Product`

W klasie `Product`:
- Dodajemy statyczną listę `List<Product> products`.
- Tworzymy metody:
  ```java
  public static void clearProducts(); // czyści listę
  public static void addProducts(Function<Path, Product> factory, Path dir);
  ```

**Użycie:**
```java
Product.addProducts(NonFoodProduct::fromCsv, Path.of("data/nonfood"));
Product.addProducts(FoodProduct::fromCsv, Path.of("data/food"));
```

---

## 🧱 Krok 4: Wyszukiwanie produktów

Tworzymy wyjątek:
```java
public class AmbigiousProductException extends Exception
```

W `Product` dodajemy:
```java
public static Product getProduct(String prefix)
```

**Zachowanie:**
- 0 pasujących → `IndexOutOfBoundsException`
- 1 pasujący → zwraca
- wiele → `AmbigiousProductException`

---

## 🧱 Krok 5: Koszyk i inflacja

Klasa `Cart`:
```java
void addProduct(Product p, int amount)
double getPrice(int y, int m)
double getInflation(int y1, m1, y2, m2)
```

**Wzór na inflację:**
```
inflacja = ((cena2 - cena1) / cena1) * 100 / liczba_miesięcy * 12
```

---

## 🧪 `Main.java`: Testy i przykłady

```java
Product.getProduct("Bur") // jednoznaczny
Product.getProduct("Ja")  // wiele, rzuca wyjątek
Product.getProduct("XYZ") // brak, rzuca wyjątek
```

```java
Cart c = new Cart();
c.addProduct(Product.getProduct("Bur"), 2);
System.out.println(c.getPrice(2020, 1));
System.out.println(c.getInflation(2011, 1, 2020, 1));
```

---

## 📌 Uwagi końcowe

- Wszystko bazuje na plikach `.csv` zaczynających się od 01.2010.
- Brak walidacji pliku – zakładamy poprawność danych.
- `getPrice()` musi zgłaszać wyjątek przy złych danych (rok, miesiąc, województwo).
- Działa tylko dla 01.2010 – 03.2022.

---

🧠 Good luck, SIGMA student! 🐺🗿