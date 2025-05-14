# Klasa

Klasa to szablon (receptura), który służy do tworzenia obiektów. Zawiera:
- **Pola** (np. moc, kolor, napęd)
- **Metody** (np. start silnika)
- **Konstruktor** – instrukcja tworząca obiekt

```java
public class RaceCar {
    private String drivetrain;
    private int power;
    private float downforce;

    public RaceCar(String drivetrain, int power, float downforce) {
        this.drivetrain = drivetrain;
        this.power = power;
        this.downforce = downforce;
    }
    // Gettery, Settery, Metody...
}
```

# Obiekty

Obiekt to instancja klasy utworzona przez `new`. W klasie `this` odnosi się do bieżącego obiektu.

# Metody

Metody to zachowania obiektów. Wyróżniamy:
- **Zwykłe** – działają na obiektach
- **Statyczne** – działają na klasie (`Math.random()`)
- **Gettery / Settery** – do enkapsulacji (pola prywatne)

# Enkapsulacja

Ukrywanie pól i metod klasy. Dostęp:
1. `public` – dostęp wszędzie
2. `private` – tylko w klasie
3. `protected` – w klasie + klasach dziedziczących

# Dziedziczenie

Klasa może rozszerzać inną (`extends`). Dziedziczy metody i pola. Można:
- użyć `super()` – wywołanie konstruktora z klasy nadrzędnej
- nadpisać metodę – `@Override`

```java
public class Car { ... }
public class RaceCar extends Car { ... }
```

# Klasy abstrakcyjne

Nie można utworzyć ich obiektów. Zawierają metody abstrakcyjne (bez ciała), które **muszą** być zaimplementowane przez podklasy.

```java
public abstract class Car {
    public abstract String startUp();
}

public class RoadCar extends Car {
    @Override
    public String startUp() { return "Odpalamy..."; }
}
```

# Kolekcje (Collections)

## ArrayList
Uporządkowana, indeksowana, z duplikatami.
```java
List<RaceCar> raceCars = new ArrayList<>();
raceCars.add(new RaceCar("RWD", 700, 350.0f, 80.0f));
```

## Set
Bez duplikatów, brak kolejności i indeksów.
```java
Set<String> brands = new HashSet<>();
brands.add("Ferrari");
```

## Map (HashMap)
Pary klucz–wartość. Klucze muszą być unikalne.
```java
Map<Integer, RaceCar> raceCarNumbers = new HashMap<>();
raceCarNumbers.put(44, new RaceCar("RWD", 900, 420.0f, 100.0f));
```

## Queue (Kolejka)
FIFO – pierwszy wchodzi, pierwszy wychodzi.
```java
Queue<RaceCar> pitStopQueue = new LinkedList<>();
pitStopQueue.add(new RaceCar("RWD", 800, 370.0f, 85.0f));
```

## Stack (Stos)
LIFO – ostatni wchodzi, pierwszy wychodzi.
```java
Stack<RaceCar> stack = new Stack<>();
stack.push(new RaceCar("RWD", 750, 360.0f, 80.0f));
```

# Przykład użycia Mapy

```java
Map<String, Integer> grades = new HashMap<>();
grades.put("Bartek", 5);
grades.put("Dawid", 4);

System.out.println(grades.get("Bartek"));
```

# Gotowe na kolokwium i wrzucenie na GitHuba 😎
