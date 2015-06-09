package pl.edu.agh.toik.comm.impl;

import java.util.*;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import pl.edu.agh.toik.comm.CommunicationClient;
import pl.edu.agh.toik.comm.CommunicationService;

/**
 * Created by bartosz on 31/05/15.
 */
public class CommunicationServiceImpl implements BundleActivator, CommunicationService {

	@SuppressWarnings("rawtypes")
	private ServiceTracker tracker;
    @SuppressWarnings("rawtypes")
	private ServiceRegistration registration;

    private Map<String,CommunicationClient> clients = new HashMap<String, CommunicationClient>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(BundleContext context) throws Exception {

        tracker = new ServiceTracker(context, CommunicationClient.class.getName(), null) {
            public Object addingService(ServiceReference reference) {
                Object svc = super.addingService(reference);
                if (svc instanceof CommunicationClient) {
                    CommunicationClient d = (CommunicationClient) svc;
                    System.out.println("Adding client: " + d.getId() + " (" + d + ")");
                    clients.put(d.getId(), d);
                    new ClientThread(d).start();
                }

                return svc;
            }

            public void removedService(ServiceReference reference, Object service) {
                if (service instanceof CommunicationClient) {
                    CommunicationClient clientToRemove = (CommunicationClient) service;
                    String clientToRemoveId = clientToRemove.getId();
                    clients.remove(clientToRemoveId);
                    System.out.println("Removed client: " + clientToRemoveId);
                }

                super.removedService(reference, service);
            }
        };
        tracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void send(MessageType type, String content, ServiceReference reference) {
        System.out.println("[SEND] " + type + ": " + content);


    }

    private class ClientThread extends Thread {

        CommunicationClient client;

        protected ClientThread(CommunicationClient client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                while(true) {
                    Thread.sleep(2000);
                    client.receive(MessageType.AGENT_IDS_LIST,new Date().toString());
                }
            } catch (Exception e) {

            }
        }
    }
}
