package pl.edu.agh.toik.comm;

/**
 * Created by bartosz on 31/05/15.
 */
public interface CommunicationService {


    void send(MessageType type, String content);
    void receive(MessageType type, String content);

    public enum MessageType {

        SEND_CONFIG, AGENT_IDS_LIST, AGENTS_LIST, RAPORT;

    }


}
