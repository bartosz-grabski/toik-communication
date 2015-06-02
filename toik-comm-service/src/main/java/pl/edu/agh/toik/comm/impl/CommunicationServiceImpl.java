package pl.edu.agh.toik.comm.impl;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import pl.edu.agh.toik.comm.CommunicationService;
import pl.edu.agh.toik.comm.CommunicationClient;

/**
 * Created by bartosz on 31/05/15.
 */
public class CommunicationServiceImpl implements BundleActivator, CommunicationService {

	@SuppressWarnings("rawtypes")
	private ServiceTracker tracker;
    @SuppressWarnings("rawtypes")
	private ServiceRegistration registration;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(BundleContext context) throws Exception {
        Dictionary<String, String> props = new Hashtable<String, String>();

        props.put("service.exported.interfaces", "*");
        props.put("service.exported.configs", "org.apache.cxf.ws");
        props.put("org.apache.cxf.ws.address", "http://localhost:9090/service");

        registration = context.registerService(CommunicationService.class.getName(), 
        		this, props);
        
        tracker = new ServiceTracker(context, CommunicationClient.class.getName(), null);
        tracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		tracker.close();
        registration.unregister();
	}

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void send(MessageType type, String content, ServiceReference reference) {
        System.out.println("[SEND] " + type + ": " + content);
        
        if(tracker.getService(reference) instanceof CommunicationClient)
        	((CommunicationClient) tracker.getService(reference)).receive(type, content);
    }
}
