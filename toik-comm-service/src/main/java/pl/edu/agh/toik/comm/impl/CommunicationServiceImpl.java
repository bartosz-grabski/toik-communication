package pl.edu.agh.toik.comm.impl;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import pl.edu.agh.toik.comm.CommunicationService;

/**
 * Created by bartosz on 31/05/15.
 */
@Component(propagation=true)
@Provides
public class CommunicationServiceImpl implements CommunicationService {

    @Override
    public void send(MessageType type, String content) {
        System.out.println("dupa "+type+" "+content);
    }

    @Override
    public void receive(MessageType type, String content) {
        System.out.println("kupa "+type+" "+content);
    }
}
