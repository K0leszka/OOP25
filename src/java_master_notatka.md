
# 💻 Java — Kompletna Notatka Master Level (dla kolokwiów, egzaminów i życia)

---

## 🔹 1. Klasy w Javie

### ✅ Klasa zwykła
```java
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

### ✅ Klasa abstrakcyjna
```java
public abstract class Shape {
    public abstract double area();
}
```

### ✅ Klasa finalna (nie można jej dziedziczyć)
```java
public final class Utility {
    public static void log(String msg) {
        System.out.println(msg);
    }
}
```

### ✅ Klasa zagnieżdżona
```java
public class Outer {
    private int value = 10;

    public class Inner {
        public int getOuterValue() {
            return value;
        }
    }
}
```

---

## 🔹 2. Dziedziczenie i polimorfizm

```java
public class Animal {
    public void speak() {
        System.out.println("Some sound");
    }
}

public class Dog extends Animal {
    @Override
    public void speak() {
        System.out.println("Woof");
    }
}
```

Polimorfizm:
```java
Animal a = new Dog(); // działa jak Dog, typ referencyjny: Animal
a.speak(); // "Woof"
```

---

## 🔹 3. Interfejsy i domyślne metody

```java
public interface Drawable {
    void draw();

    default void info() {
        System.out.println("I am drawable.");
    }
}

public class Circle implements Drawable {
    public void draw() {
        System.out.println("Drawing circle");
    }
}
```

---

## 🔹 4. Wyjątki (exception handling)

### ✅ Typy wyjątków

- **Checked** – muszą być obsłużone (`IOException`, `SQLException`)
- **Unchecked** – nie muszą być obsłużone (`NullPointerException`, `IllegalArgumentException`)

### ✅ Obsługa wyjątków
```java
try {
    int x = 5 / 0;
} catch (ArithmeticException e) {
    System.out.println("Division by zero");
} finally {
    System.out.println("Cleanup");
}
```

### ✅ Własny wyjątek
```java
public class CustomException extends Exception {
    public CustomException(String msg) {
        super(msg);
    }
}
```

---

## 🔹 5. Metody statyczne vs instancyjne

```java
public class MathUtils {
    public static int square(int x) {
        return x * x;
    }

    public int multiply(int a, int b) {
        return a * b;
    }
}
```

---

## 🔹 6. Parsowanie CSV

```java
public class DataParser {
    public static List<String[]> parseCSV(Path path) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line.split(";"));
            }
        }
        return data;
    }
}
```

---

## 🔹 7. Kolekcje, Streamy, Lambdy

```java
List<String> names = List.of("Anna", "Bartek", "Celina");

names.stream()
     .filter(name -> name.startsWith("A"))
     .sorted()
     .forEach(System.out::println);
```

Sortowanie obiektów:
```java
list.sort(Comparator.comparing(Product::getPrice).reversed());
```

---

## 🔹 8. Wzorce projektowe (podstawowe)

### ✅ Singleton
```java
public class Singleton {
    private static final Singleton instance = new Singleton();
    private Singleton() {}

    public static Singleton getInstance() {
        return instance;
    }
}
```

### ✅ Fabryka
```java
public class AnimalFactory {
    public static Animal create(String type) {
        return switch (type) {
            case "dog" -> new Dog();
            case "cat" -> new Cat();
            default -> throw new IllegalArgumentException();
        };
    }
}
```

---

## 🔹 9. Typowe pułapki

- Brak `@Override` i cicha pomyłka w nazwie metody
- Nadpisywanie `equals()` bez `hashCode()`
- `==` vs `.equals()` dla obiektów
- Przeciążenie (overload) a nadpisanie (override)
- Domyślny konstruktor znika po dodaniu własnego
- Static context cannot reference non-static field

---

## 🔹 10. Klasy anonimowe i lambda jako interfejsy funkcyjne

```java
Runnable r = new Runnable() {
    public void run() {
        System.out.println("Hello");
    }
};

Runnable r2 = () -> System.out.println("Hello from lambda");
```

---

## 🔹 11. Serializacja

```java
public class Person implements Serializable {
    private String name;
    private int age;
}
```

---

## 🔹 12. Praca z plikami

```java
Path path = Paths.get("data.csv");
Files.lines(path).forEach(System.out::println);
```

---

## 🔹 13. Klasy pomocnicze i narzędziowe

```java
public class StringUtil {
    public static boolean isEmpty(String s) {
        return s == null || s.isBlank();
    }
}
```

---

## 🔹 14. Metody pomocnicze i testy w `main`

```java
public static void main(String[] args) {
    try {
        List<String[]> data = DataParser.parseCSV(Path.of("file.csv"));
        data.forEach(row -> System.out.println(Arrays.toString(row)));
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

---

## 🔹 15. Enumy i typy wyliczeniowe

```java
public enum Direction {
    NORTH, EAST, SOUTH, WEST
}
```

---

## 🔹 16. Rekordy (od Java 14+)

```java
public record Point(int x, int y) {}
```

---

## 🔹 17. Interfejsy funkcyjne

```java
@FunctionalInterface
public interface Calculator {
    int compute(int a, int b);
}
```

---

## 🔹 18. Wyrażenia lambda i `Function`, `Predicate`

```java
Function<Integer, Integer> square = x -> x * x;
Predicate<String> startsWithA = s -> s.startsWith("A");
```

---

## 🔹 19. Obsługa daty i czasu (`java.time`)

```java
LocalDate date = LocalDate.now();
LocalDate parsed = LocalDate.parse("2024-05-16");
Period diff = Period.between(LocalDate.of(2020,1,1), date);
```

---

## 🔹 20. Mapowanie danych CSV do obiektów

```java
public class City {
    private String name;
    private int timezoneOffset;

    public static City fromCsvLine(String line) {
        String[] parts = line.split(";");
        City c = new City();
        c.name = parts[0];
        c.timezoneOffset = Integer.parseInt(parts[1]);
        return c;
    }
}
```

---

# ✅ To koniec — Jesteś uzbrojony w Javę 🧠💣

Przeczytaj to, przetrenuj, a żadne kolokwium Cię nie złamie. 🐺
