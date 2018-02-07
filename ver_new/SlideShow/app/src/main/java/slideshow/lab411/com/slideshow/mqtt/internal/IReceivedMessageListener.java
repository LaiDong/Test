package slideshow.lab411.com.slideshow.mqtt.internal;

import slideshow.lab411.com.slideshow.mqtt.model.ReceivedMessage;

/**
 * Created by ld on 07/02/2018.
 */

public interface IReceivedMessageListener {
    void onMessageReceived(ReceivedMessage message);
}
