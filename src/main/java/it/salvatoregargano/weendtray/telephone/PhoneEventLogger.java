package it.salvatoregargano.weendtray.telephone;

import it.salvatoregargano.weendtray.logging.CombinedLogger;
import it.salvatoregargano.weendtray.patterns.Observer;

public class PhoneEventLogger implements Observer<PhoneEvent> {
    @Override
    public void update(PhoneEvent event) {
        final var logger = CombinedLogger.getInstance();
        logger.info(event.getDescription());
    }
}
