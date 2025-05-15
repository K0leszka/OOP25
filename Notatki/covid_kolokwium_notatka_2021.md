
# 📘 Notatka – Kolokwium 1 – Analiza danych COVID-19 (2021)

## ✅ Krok 1: Klasy `Country`, `CountryWithoutProvinces`, `CountryWithProvinces`

- `Country` – klasa abstrakcyjna z polem `name` i metodą `getName()`
- `CountryWithoutProvinces` – bez dodatkowych pól
- `CountryWithProvinces` – posiada tablicę `Country[] provinces`

---

## ✅ Krok 2: Wczytywanie plików CSV

```java
private static Path confirmedPath;
private static Path deathsPath;

public static void setFiles(Path confirmed, Path deaths) throws FileNotFoundException {
    if (!Files.exists(confirmed)) throw new FileNotFoundException(confirmed.toString());
    if (!Files.exists(deaths)) throw new FileNotFoundException(deaths.toString());
    confirmedPath = confirmed;
    deathsPath = deaths;
}
```

Metoda `fromCsv(String countryName)` – tworzy obiekt `Country`, otwiera oba pliki, sprawdza strukturę.

---

## ✅ Krok 3: Obsługa braku państwa

```java
public class CountryNotFoundException extends Exception {
    public CountryNotFoundException(String name) {
        super(name);
    }
}
```

```java
private static class CountryColumns {
    public final int firstColumnIndex;
    public final int columnCount;

    public CountryColumns(int first, int count) {
        this.firstColumnIndex = first;
        this.columnCount = count;
    }
}
```

```java
private static CountryColumns getCountryColumns(String headerLine, String country) throws CountryNotFoundException {
    // analizuje linię nagłówkową i znajduje kolumny odpowiadające danemu państwu
}
```

---

## ✅ Krok 4: Zapis dziennych danych w `CountryWithoutProvinces`

```java
private Map<LocalDate, int[]> data = new HashMap<>();

public void addDailyStatistic(LocalDate date, int confirmed, int deaths) {
    data.put(date, new int[] { confirmed, deaths });
}
```

---

## ✅ Krok 5: Parsowanie danych dla kraju i prowincji

W `fromCsv`:

- Tworzy `CountryWithoutProvinces` jeśli brak prowincji
- Tworzy `CountryWithProvinces` i dla każdej prowincji wywołuje `addDailyStatistic`

---

## ✅ Krok 6: Parsowanie wielu krajów

```java
public static Country[] fromCsv(String[] names) {
    List<Country> result = new ArrayList<>();
    for (String name : names) {
        try {
            result.add(fromCsv(name));
        } catch (CountryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    return result.toArray(new Country[0]);
}
```

---

## ✅ Krok 7: Metody `getConfirmedCases` i `getDeaths`

```java
public abstract int getConfirmedCases(LocalDate date);
public abstract int getDeaths(LocalDate date);
```

- `CountryWithoutProvinces` – pobiera dane z mapy
- `CountryWithProvinces` – sumuje dane ze wszystkich prowincji

---

## ✅ Krok 8: Sortowanie krajów wg liczby zgonów

```java
public static void sortByDeaths(List<Country> countries, LocalDate start, LocalDate end) {
    countries.sort((a, b) -> {
        int sumA = 0, sumB = 0;
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            sumA += a.getDeaths(d);
            sumB += b.getDeaths(d);
        }
        return Integer.compare(sumB, sumA);
    });
}
```

---

## ✅ Krok 9: Eksport danych do pliku `.txt`

```java
public void saveToDataFile(Path outputFile) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
        for (LocalDate date : data.keySet().stream().sorted().toList()) {
            int[] stats = data.get(date);
            writer.write(String.format("%d.%02d.%02d	%d	%d
",
                date.getDayOfMonth(), date.getMonthValue(), date.getYear() % 100,
                stats[0], stats[1]));
        }
    }
}
```

---

## 🧪 Wizualizacja w Gnuplot

```
set terminal svg size 800,300 enhanced fname 'arial' fsize 10 butt solid
set output 'out.svg'
set key left
set xdata time
set timefmt "%d.%m.%y"
set format x "%m.%y"
plot "data.txt" using 1:2 title 'Confirmed Cases' with lines,      "data.txt" using 1:3 title 'Deaths' with lines
```
