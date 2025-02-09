package it.salvatoregargano.weendtray.telephone;

import it.salvatoregargano.weendtray.acl.PhonePlan;

// Message Event (SMS)
public class MessageEvent extends PhoneEvent {
    private final String content;

    public MessageEvent(PhonePlan plan, String sourceNumber, String targetNumber, String content) {
        super(sourceNumber, targetNumber, plan);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String getDescription() {
        return "[" + getUUID() + "][SMS] from " + getSourceNumber() + " to " + getTargetNumber() + " (" + getContent().length() + " characters)";
    }
}
