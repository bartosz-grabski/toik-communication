package pl.edu.agh.toik.comm.impl;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import pl.edu.agh.toik.comm.CommunicationService;
import pl.edu.agh.toik.comm.CommunicationService.MessageType;
import pl.edu.agh.toik.comm.CommunicationClient;

public class CommunicationClientImpl implements BundleActivator, CommunicationClient {

    @SuppressWarnings("rawtypes")
	private ServiceTracker tracker;
	@SuppressWarnings("rawtypes")
	private ServiceRegistration registration;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
	public void start(final BundleContext context) {
        Dictionary<String, String> props = new Hashtable<String, String>();
        
        props.put("service.exported.interfaces", "*");
        props.put("service.exported.configs", "org.apache.cxf.ws");
        props.put("org.apache.cxf.ws.address", "http://localhost:9090/client");

        registration = context.registerService(CommunicationClient.class.getName(), this, props);
        
        tracker = new ServiceTracker(context, CommunicationService.class.getName(), null) {
			@Override
            public Object addingService(ServiceReference reference) {
                Object service = super.addingService(reference);
                
                if(service instanceof CommunicationService) {
                	useService((CommunicationService) service);
                }
                
                return service;
            }
        };
        tracker.open();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        tracker.close();
        registration.unregister();
    }

    @Override
	public void receive(MessageType type, String content) {
        System.out.println("[RECEIVE] " + type + ": " + content);
	}
    
    protected void useService(final CommunicationService service) {
        new Thread(new Runnable() {
            public void run() {
        		service.send(CommunicationService.MessageType.RAPORT, "Hello World!", registration.getReference());
            }
        }).start();
    }

}
