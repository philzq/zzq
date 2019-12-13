package zzq.nio.http.server.server;

import zzq.nio.http.server.Request;
import zzq.nio.http.server.Response;
import zzq.nio.http.server.servlet.CloudServlet;
import zzq.nio.http.server.servlet.MappingUrlServlet;
import zzq.nio.http.server.servlet.StaticViewServlet;

import java.util.ArrayList;
import java.util.List;

/**
 * CloudService
 *
 */
public class CloudService {

    private List<CloudServlet> cloudServlets = new ArrayList<>();

    public CloudService() {
        cloudServlets.add(new StaticViewServlet());
        cloudServlets.add(new MappingUrlServlet());
    }

    public void doService(Request request, Response response) {
        CloudServlet servlet = doSelect(request);
        servlet.init(request, response);
        servlet.doService(request, response);
    }

    private CloudServlet doSelect(Request request) {
        for (CloudServlet cloudServlet : cloudServlets) {
            if (cloudServlet.match(request)) {
                return cloudServlet;
            }
        }
        return new MappingUrlServlet();
    }
}
