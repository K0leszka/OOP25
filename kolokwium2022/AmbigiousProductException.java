// KROK-04: AmbigiousProductException – gdy prefiks pasuje do >1 elementu
// * przechowuje listę kandydatów i pokazuje ją w komunikacie

import java.util.List;

public class AmbigiousProductException extends RuntimeException {
    private final List<String> candidates;

    public AmbigiousProductException(List<String> candidates) {
        super("Zbyt wiele dopasowań: " + String.join(", ", candidates));
        this.candidates = candidates;
    }

    public List<String> getCandidates() {
        return candidates;
    }
}
