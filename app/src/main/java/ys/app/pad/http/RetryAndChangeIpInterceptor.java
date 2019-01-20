package ys.app.pad.http;


import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ys.app.pad.utils.Logger;

/**
 * <p>the interceptor which to retry the data api.</p>
 * Created by dennis on 2016/12/19 11:08
 * for ebrun
 * you can contact me at : pengjianjun@ebrun.com
 * or see link for more details
 *
 * @version 1.0.0
 */
public class RetryAndChangeIpInterceptor implements Interceptor {
    private int retryCount;
    private String firstIP;
    private List<String> servers;

    public RetryAndChangeIpInterceptor(String firstIP, List<String> servers) {
        this(firstIP, servers, 3);
    }

    public RetryAndChangeIpInterceptor(String firstIP, List<String> servers, int retryCount) {
        this.firstIP = firstIP;
        this.servers = servers;
        this.retryCount = retryCount;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        // try the request
        Response response = doRequest(chain, request);
        int tryCount = 0;
        String url = request.url().toString();
        while (response == null && tryCount <= retryCount) {
            url = switchServer(url);
            Request newRequest = request.newBuilder().url(url).build();
            Logger.i("Request is not successful - " + tryCount);
            tryCount++;
            // retry the request
            response = doRequest(chain, newRequest);
        }
        if (response == null) {
            throw new IOException();
        }
        return response;
    }

    private Response doRequest(Chain chain, Request request) {
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
        }
        return response;
    }

    private String switchServer(String url) {
        String newUrlString = url;
        if (servers != null && !servers.isEmpty()) {
            if (url.contains(firstIP)) {
                for (String server : servers) {
                    if (!firstIP.equals(server)) {
                        newUrlString = url.replace(firstIP, server);
                        break;
                    }
                }
            } else {
                for (String server : servers) {
                    if (url.contains(server)) {
                        newUrlString = url.replace(server, firstIP);
                        break;
                    }
                }
            }
        }
        return newUrlString;
    }


}
