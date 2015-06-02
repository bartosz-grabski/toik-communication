package pl.edu.agh.toik.comm;

import org.osgi.framework.ServiceReference;

/**
 * Created by bartosz on 31/05/15.
 */
public interface CommunicationService {

    @SuppressWarnings("rawtypes")
	void send(MessageType type, String content, ServiceReference reference);

    public enum MessageType {
        SEND_CONFIG, AGENT_IDS_LIST, AGENTS_LIST, RAPORT;
    }

}
