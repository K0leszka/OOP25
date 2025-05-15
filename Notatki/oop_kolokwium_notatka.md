
# 📘 Notatka – Programowanie Obiektowe – Kolokwium 1 (26.04.2024)

## ✅ Krok 1: Abstrakcyjna klasa `Clock`
```java
public abstract class Clock {
    protected LocalTime time;

    public void setCurrentTime() {
        this.time = LocalTime.now();
    }

    public void setTime(int hour, int minute, int second) {
        if (hour < 0 || hour > 23) throw new IllegalArgumentException("Godzina spoza zakresu (0-23)");
        if (minute < 0 || minute > 59) throw new IllegalArgumentException("Minuty spoza zakresu (0-59)");
        if (second < 0 || second > 59) throw new IllegalArgumentException("Sekundy spoza zakresu (0-59)");
        this.time = LocalTime.of(hour, minute, second);
    }

    @Override
    public String toString() {
        return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
```

## ✅ Krok 2: Klasa `DigitalClock` i tryby 12h/24h
```java
public class DigitalClock extends Clock {
    public enum Mode {
        TWELVE_HOUR, TWENTY_FOUR_HOUR
    }

    private Mode mode;

    public DigitalClock(Mode mode) {
        this.mode = mode;
        setCurrentTime();
    }

    @Override
    public String toString() {
        if (mode == Mode.TWENTY_FOUR_HOUR) {
            return super.toString();
        } else {
            int hour = time.getHour();
            String period = (hour < 12) ? "AM" : "PM";
            hour = hour % 12;
            if (hour == 0) hour = 12;
            return String.format("%d:%02d:%02d %s", hour, time.getMinute(), time.getSecond(), period);
        }
    }
}
```

## ✅ Krok 3: Klasa `City` + wczytywanie pliku `.csv`
```java
public class City {
    private String name;
    private int timezoneOffset;
    private double longitude;

    private static City parseLine(String line) {
        String[] parts = line.split(",");
        String name = parts[0];
        int offset = Integer.parseInt(parts[1]);
        double lon = Double.parseDouble(parts[2]);
        City c = new City();
        c.name = name;
        c.timezoneOffset = offset;
        c.longitude = lon;
        return c;
    }

    public static Map<String, City> parseFile(String path) throws IOException {
        Map<String, City> map = new HashMap<>();
        Files.lines(Paths.get(path))
             .skip(1)
             .forEach(line -> {
                 City city = parseLine(line);
                 map.put(city.name, city);
             });
        return map;
    }
}
```

## ✅ Krok 4: Pole `City` w `Clock`
```java
private City city;

public Clock(City city) {
    this.city = city;
    setCurrentTime();
}

public void setCity(City city) {
    int oldOffset = this.city.getTimezoneOffset();
    int newOffset = city.getTimezoneOffset();
    this.time = this.time.plusHours(newOffset - oldOffset);
    this.city = city;
}
```

## ✅ Krok 5: Metoda `localMeanTime`
```java
public LocalTime localMeanTime(LocalTime zoneTime) {
    double offset = (longitude / 180.0) * 12.0;
    int seconds = (int)(offset * 3600);
    return zoneTime.minusSeconds(timezoneOffset * 3600).plusSeconds(seconds);
}
```

## ✅ Krok 6: `worstTimezoneFit` – komparator miast
```java
public static Comparator<City> worstTimezoneFit(LocalTime zoneTime) {
    return (c1, c2) -> {
        Duration d1 = Duration.between(c1.localMeanTime(zoneTime), zoneTime).abs();
        Duration d2 = Duration.between(c2.localMeanTime(zoneTime), zoneTime).abs();
        return d2.compareTo(d1);
    };
}
```

## ✅ Krok 7: `AnalogClock` i metoda `toSvg`
```java
public class AnalogClock extends Clock {
    public AnalogClock(City city) {
        super(city);
    }

    public void toSvg(String filePath) throws IOException {
        String svg = "<svg width='200' height='200'>
"
                   + "<circle cx='100' cy='100' r='90' stroke='black' fill='none'/>
"
                   + "</svg>";
        Files.writeString(Paths.get(filePath), svg);
    }
}
```

## ✅ Krok 8: Klasa abstrakcyjna `ClockHand`
```java
public abstract class ClockHand {
    public abstract void setTime(LocalTime time);
    public abstract String toSvg();
}
```

## ✅ Krok 9: `SecondHand` – wskazówka sekundowa
```java
public class SecondHand extends ClockHand {
    private int angle;

    @Override
    public void setTime(LocalTime time) {
        this.angle = time.getSecond() * 6;
    }

    @Override
    public String toSvg() {
        return String.format("<line x1='100' y1='100' x2='100' y2='20' "
             + "stroke='red' stroke-width='2' transform='rotate(%d 100 100)'/>", angle);
    }
}
```

## ✅ Krok 10: `MinuteHand` i `HourHand`
```java
public class MinuteHand extends ClockHand {
    private double angle;

    @Override
    public void setTime(LocalTime time) {
        angle = time.getMinute() * 6 + time.getSecond() * 0.1;
    }

    @Override
    public String toSvg() {
        return String.format("<line x1='100' y1='100' x2='100' y2='30' "
             + "stroke='blue' stroke-width='3' transform='rotate(%.2f 100 100)'/>", angle);
    }
}

public class HourHand extends ClockHand {
    private double angle;

    @Override
    public void setTime(LocalTime time) {
        angle = (time.getHour() % 12) * 30 + time.getMinute() * 0.5;
    }

    @Override
    public String toSvg() {
        return String.format("<line x1='100' y1='100' x2='100' y2='50' "
             + "stroke='black' stroke-width='4' transform='rotate(%.2f 100 100)'/>", angle);
    }
}
```

## ✅ Krok 11: Polimorficzna lista wskazówek w `AnalogClock`
```java
private final List<ClockHand> hands = List.of(new HourHand(), new MinuteHand(), new SecondHand());

@Override
public void setTime(int hour, int minute, int second) {
    super.setTime(hour, minute, second);
    updateHands();
}

@Override
public void setCurrentTime() {
    super.setCurrentTime();
    updateHands();
}

private void updateHands() {
    for (ClockHand hand : hands) {
        hand.setTime(time);
    }
}

@Override
public void toSvg(String filePath) throws IOException {
    StringBuilder svg = new StringBuilder("<svg width='200' height='200'>
");
    svg.append("<circle cx='100' cy='100' r='90' stroke='black' fill='none'/>
");
    for (ClockHand hand : hands) {
        svg.append(hand.toSvg()).append("
");
    }
    svg.append("</svg>");
    Files.writeString(Paths.get(filePath), svg.toString());
}
```

## ✅ Krok 12: `generateAnalogClocksSvg` – SVG dla wielu miast
```java
public static void generateAnalogClocksSvg(List<City> cities, AnalogClock clock) throws IOException {
    String dirName = clock.toString().replace(":", "-");
    Files.createDirectories(Paths.get(dirName));
    for (City city : cities) {
        clock.setCity(city);
        String fileName = dirName + "/" + city.getName() + ".svg";
        clock.toSvg(fileName);
    }
}
```
