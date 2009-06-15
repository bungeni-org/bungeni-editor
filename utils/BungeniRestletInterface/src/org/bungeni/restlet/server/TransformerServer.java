package org.bungeni.restlet.server;

import org.bungeni.restlet.resources.OdtResource;
import org.restlet.data.Protocol;
import org.restlet.*;

public class TransformerServer extends Application {

 

  public static void main(String [] args) throws Exception {
    Component component = new Component();
    component.getServers().add(Protocol.HTTP, 8182);

    TransformerServer transServer = new TransformerServer();
    component.getDefaultHost().attach("", transServer);
    component.start();
  }

    public static String getTempFileFolder() {
        return "/home/undesa/tmp";
    }

    @Override
  public Restlet createRoot() {
    Router router = new Router(getContext());
    router.attach("/convert_to_anxml", OdtResource.class);
    return router;
  }
}
