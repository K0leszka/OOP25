
# 📘 Notatka – Zgony w Polsce 2019 – Kolokwium 2024

## ✅ Zadanie 1: Klasa `DeathCauseStatistic` *(parsowanie linii CSV i przechowywanie danych)*

```java
public class DeathCauseStatistic {
    private String icdCode;
    private int[] deathsByAge;

    public static DeathCauseStatistic fromCsvLine(String line) {
        String[] parts = line.split("	");
        String code = parts[0].trim();
        String[] numbers = parts[1].split(";");
        int[] deaths = new int[numbers.length - 1];
        for (int i = 1; i < numbers.length; i++) {
            deaths[i - 1] = Integer.parseInt(numbers[i]);
        }
        return new DeathCauseStatistic(code, deaths);
    }

    public DeathCauseStatistic(String icdCode, int[] deathsByAge) {
        this.icdCode = icdCode;
        this.deathsByAge = deathsByAge;
    }

    public String getIcdCode() {
        return icdCode;
    }
}
```

---

## ✅ Zadanie 2: Klasa wewnętrzna `AgeBracketDeaths` *(grupa wiekowa dla danego wieku)*

```java
public static class AgeBracketDeaths {
    public final int young;
    public final int old;
    public final int deathCount;

    public AgeBracketDeaths(int young, int old, int deathCount) {
        this.young = young;
        this.old = old;
        this.deathCount = deathCount;
    }
}

public AgeBracketDeaths getBracketForAge(int age) {
    int bracketSize = deathsByAge.length;
    int step = 5;
    int index = age / step;
    if (index >= bracketSize) index = bracketSize - 1;
    int deathCount = deathsByAge[index];
    return new AgeBracketDeaths(index * step, index * step + step - 1, deathCount);
}
```

---

## ✅ Zadanie 3: Klasa `DeathCauseStatisticList` *(lista statystyk i analiza danych)*

```java
public class DeathCauseStatisticList {
    private List<DeathCauseStatistic> statistics = new ArrayList<>();

    public void repopulate(String path) throws IOException {
        statistics.clear();
        List<String> lines = Files.readAllLines(Paths.get(path));
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                statistics.add(DeathCauseStatistic.fromCsvLine(line));
            }
        }
    }

    public List<DeathCauseStatistic> mostDeadlyDiseases(int age, int n) {
        return statistics.stream()
            .sorted((a, b) -> {
                int da = a.getBracketForAge(age).deathCount;
                int db = b.getBracketForAge(age).deathCount;
                return Integer.compare(db, da);
            })
            .limit(n)
            .collect(Collectors.toList());
    }
}
```

---

## ✅ Zadanie 4: Interfejs `ICDCodeTabular` i implementacje *(opisy kodów chorób)*

```java
public interface ICDCodeTabular {
    String getDescription(String code) throws IndexOutOfBoundsException;
}
```

---

### 🔹 `ICDCodeTabularOptimizedForTime` *(szybkie – cała mapa w RAM)*

```java
public class ICDCodeTabularOptimizedForTime implements ICDCodeTabular {
    private Map<String, String> codeMap = new HashMap<>();

    public ICDCodeTabularOptimizedForTime(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (String line : lines.subList(87, lines.size())) {
            if (line.matches("^[A-Z]\d{2}.*")) {
                String[] parts = line.split(" ", 2);
                if (parts.length == 2) {
                    codeMap.put(parts[0], parts[1]);
                }
            }
        }
    }

    public String getDescription(String code) {
        if (!codeMap.containsKey(code)) throw new IndexOutOfBoundsException("Nie znaleziono kodu: " + code);
        return codeMap.get(code);
    }
}
```

---

### 🔹 `ICDCodeTabularOptimizedForMemory` *(pamięciooszczędne – szuka za każdym razem)*

```java
public class ICDCodeTabularOptimizedForMemory implements ICDCodeTabular {
    private String filePath;

    public ICDCodeTabularOptimizedForMemory(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription(String code) throws IndexOutOfBoundsException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            int lineNum = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (lineNum < 88) continue;
                if (line.startsWith(code + " ")) {
                    return line.substring(code.length()).trim();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Błąd odczytu pliku", e);
        }
        throw new IndexOutOfBoundsException("Kod nie został odnaleziony: " + code);
    }
}
```
