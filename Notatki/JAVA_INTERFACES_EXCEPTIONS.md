
# Interfejsy (`interface`)

Interfejsy służą do definiowania zbiorów metod, które muszą być zaimplementowane w klasach. Są sposobem na osiągnięcie polimorfizmu i rozdzielenie deklaracji od implementacji.

## Podstawowy przykład

```java
public interface Vehicle {
    void start();
    void stop();
}
```

```java
public class Bike implements Vehicle {
    @Override
    public void start() {
        System.out.println("Ruszamy!");
    }

    @Override
    public void stop() {
        System.out.println("Zatrzymujemy się.");
    }
}
```

## `default` i `static` w interfejsach (od Javy 8)

```java
public interface MusicPlayer {
    default void play() {
        System.out.println("Odtwarzanie muzyki...");
    }

    static void info() {
        System.out.println("To jest interfejs odtwarzacza.");
    }
}
```

# Wyjątki (`try-catch`, `throw`, `throws`)

Wyjątki to sposób obsługi błędów w czasie działania programu.

## Obsługa wyjątku

```java
try {
    int wynik = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("Błąd: " + e.getMessage());
}
```

## Rzucanie wyjątku

```java
public void divide(int a, int b) throws ArithmeticException {
    if (b == 0) {
        throw new ArithmeticException("Nie dziel przez zero!");
    }
    System.out.println(a / b);
}
```

## Własny wyjątek

```java
public class MyException extends Exception {
    public MyException(String message) {
        super(message);
    }
}
```

## Przykład użycia własnego wyjątku

```java
public class Demo {
    public static void main(String[] args) {
        try {
            checkAge(15);
        } catch (MyException e) {
            System.out.println("Złapano wyjątek: " + e.getMessage());
        }
    }

    static void checkAge(int age) throws MyException {
        if (age < 18) {
            throw new MyException("Musisz mieć co najmniej 18 lat.");
        }
    }
}
```

Gotowe na kolosa i na repo 😎
