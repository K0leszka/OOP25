# Klasa

Klasa to szablon (receptura), który służy do tworzenia obiektów. Zawiera:
- **Pola** (np. moc, kolor, napęd) – dane, które opisują obiekt
- **Metody** (np. start silnika) – co obiekt może zrobić
- **Konstruktor** – specjalna metoda wywoływana podczas tworzenia obiektu

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

Obiekt to instancja klasy utworzona przez `new`. Można tworzyć ich wiele z jednej klasy. Przykład:
```java
RaceCar car1 = new RaceCar("AWD", 650, 300.0f);
RaceCar car2 = new RaceCar("RWD", 700, 350.0f);
```
Słowo `this` wskazuje na aktualny obiekt – używane m.in. w konstruktorach, setterach.

# Metody

Metody definiują zachowania obiektu. Mogą zwracać wartość (`int`, `String`, `boolean` itd.) lub być `void` (czyli nic nie zwracają).

### Rodzaje metod:
- **Instancyjne** – działają na konkretnych obiektach
- **Statyczne** – działają bez obiektu, np. `Math.pow(2, 3)`
- **Gettery** – zwracają wartość prywatnego pola
- **Settery** – ustawiają wartość prywatnego pola

## Przykład getterów i setterów (akcesorów i mutatorów)

```java
public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) {
        if (age > 0) {
            this.age = age;
        }
    }
}
```

# Enkapsulacja

Ukrywanie pól i metod klasy – ograniczanie bezpośredniego dostępu.
- Chroni dane przed nieautoryzowaną zmianą
- Dostęp przez gettery/settery

## Modyfikatory dostępu:
1. `public` – dostępny wszędzie
2. `private` – tylko wewnątrz tej klasy
3. `protected` – klasa + klasy dziedziczące (w tym z innego pakietu)
4. (brak – default) – dostęp tylko w tym samym pakiecie

# Dziedziczenie

Klasa może rozszerzać inną klasę (`extends`). Przejmuje wszystkie publiczne i chronione metody/pola.
- Użycie `super()` wywołuje konstruktor klasy nadrzędnej
- Metody mogą być nadpisywane (`@Override`)

```java
public class Car {
    protected int wheels = 4;
    public void start() {
        System.out.println("Silnik uruchomiony");
    }
}

public class RaceCar extends Car {
    @Override
    public void start() {
        System.out.println("Silnik wyścigówki uruchomiony");
    }
}
```

# Klasy abstrakcyjne

- Nie można tworzyć ich obiektów (`new` nie działa)
- Mogą zawierać metody abstrakcyjne (bez implementacji)
- Podklasy **muszą** zaimplementować te metody

```java
public abstract class Animal {
    public abstract void makeSound();
}

public class Dog extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Hau hau");
    }
}
```

# Klasy wewnętrzne (nested / inner classes)

Używane, gdy chcemy zgrupować klasy logicznie lub ograniczyć ich widoczność.

## Klasa wewnętrzna zwykła (inner)
```java
public class Outer {
    private String data = "Zewnętrzne dane";

    public class Inner {
        public void show() {
            System.out.println(data); // dostęp do prywatnych pól
        }
    }
}
```

## Klasa statyczna wewnętrzna (static nested)
```java
public class Outer {
    public static class Nested {
        public void display() {
            System.out.println("Statyczna klasa wewnętrzna");
        }
    }
}
```

## Klasa lokalna (w metodzie)
```java
public void exampleMethod() {
    class Local {
        void print() {
            System.out.println("Lokalna klasa w metodzie");
        }
    }
    Local local = new Local();
    local.print();
}
```

## Klasa anonimowa (anonymous class)
Szybka implementacja klasy lub interfejsu "w locie".
```java
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("Biegnę jako anonimowa klasa");
    }
};
r.run();
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
