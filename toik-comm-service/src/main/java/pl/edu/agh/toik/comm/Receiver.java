package pl.edu.agh.toik.comm;

/**
 * Created by bartosz on 31/05/15.
 */
public interface Receiver {

    void receive(CommunicationService.MessageType type, String message);
}
