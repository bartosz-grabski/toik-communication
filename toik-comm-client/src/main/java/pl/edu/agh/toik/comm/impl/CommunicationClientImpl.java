package pl.edu.agh.toik.comm.impl;

import java.lang.Exception;
import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import pl.edu.agh.toik.comm.CommunicationService;
import pl.edu.agh.toik.comm.CommunicationService.MessageType;
import pl.edu.agh.toik.comm.CommunicationClient;

public class CommunicationClientImpl implements BundleActivator {

    private static final int SERVICES = 10;

    @SuppressWarnings("rawtypes")
	private ServiceTracker tracker;
	@SuppressWarnings("rawtypes")
	private ServiceRegistration[] registrations = new ServiceRegistration[SERVICES];


    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
	public void start(final BundleContext context) {

        Dictionary<String, String> props = new Hashtable<String, String>();

        props.put("service.exported.interfaces", "*");
        props.put("service.exported.configs", "org.apache.cxf.ws");

        for (int i = 0; i < 10; i++) {
            final int id = i;
            props.put("org.apache.cxf.ws.address", "http://localhost:9091/client"+id);
            registrations[i] = context.registerService(CommunicationClient.class.getName(), new CommunicationClient() {

                @Override
                public void receive(MessageType type, String message) {
                    System.out.println("[RECEIVE "+getId()+"] " + type + ": " + message);
                }

                @Override
                public String getId() {
                    return Integer.toString(id);
                }

            }, props);
        }

    }

    @Override
    public void stop(BundleContext context) throws Exception {
        for(ServiceRegistration reg : registrations) {
            reg.unregister();
        }
    }

}
