package zzq.nio.http.server.servlet;

import zzq.nio.http.server.Request;
import zzq.nio.http.server.Response;

/**
 * MappingUrlServlet
 *
 */
public class MappingUrlServlet implements CloudServlet {

    @Override
    public boolean match(Request request) {
        return true;
    }

    @Override
    public void init(Request request, Response response) {

    }

    @Override
    public void doService(Request request, Response response) {

    }
}
