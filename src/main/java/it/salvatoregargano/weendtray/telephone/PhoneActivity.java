package it.salvatoregargano.weendtray.telephone;

import it.salvatoregargano.weendtray.acl.PhonePlan;
import it.salvatoregargano.weendtray.patterns.Observable;

import java.time.Duration;

// Observable Class
public class PhoneActivity extends Observable<PhoneEvent> {
    private final String userPhoneNumber;
    private final PhonePlan plan;

    public PhoneActivity(String userPhoneNumber, PhonePlan plan) {
        this.userPhoneNumber = userPhoneNumber;
        this.plan = plan;
    }

    public void sendMessage(String number, String content) {
        MessageEvent event = new MessageEvent(plan, userPhoneNumber, number, content);
        notifyObservers(event);
    }

    public void makeCall(String number, Duration duration) {
        CallEvent event = new CallEvent(plan, userPhoneNumber, number, duration);
        notifyObservers(event);
    }

    public void useData(int dataSizeBytes) {
        DataUsageEvent event = new DataUsageEvent(plan, userPhoneNumber, dataSizeBytes);
        notifyObservers(event);
    }
}
