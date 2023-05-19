package zzq.zzqsimpleframeworkhttp.config;

import feign.Client;
import feign.Request;
import feign.Response;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import zzq.zzqsimpleframeworkhttp.exception.HttpClientException;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理feign不支持注解问题
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-05-18 14:08
 */
public class FeignBlockingLoadBalancerFactoryClient implements Client {

    private final Map<String, FeignBlockingLoadBalancerClient> feignBlockingLoadBalancerClientMap = new HashMap<>();
    private final FeignBlockingLoadBalancerClient defaultFeignBlockingLoadBalancerClient;
    private LoadBalancerClient loadBalancerClient;
    private LoadBalancerClientFactory loadBalancerClientFactory;

    public FeignBlockingLoadBalancerFactoryClient(LoadBalancerClient loadBalancerClient, LoadBalancerClientFactory loadBalancerClientFactory) {
        this.loadBalancerClient = loadBalancerClient;
        this.loadBalancerClientFactory = loadBalancerClientFactory;

        OkHttpFeignClient defaultOkHttpFeignClient = new OkHttpFeignClient(500);
        defaultFeignBlockingLoadBalancerClient = new FeignBlockingLoadBalancerClient(defaultOkHttpFeignClient, loadBalancerClient, loadBalancerClientFactory);
    }

    public void addOkHttpFeignClient(String serviceId, OkHttpFeignClient okHttpFeignClient) {
        if (serviceId == null || okHttpFeignClient == null) {
            throw new HttpClientException("FeignBlockingLoadBalancerClient 注册失败，serviceId 和 okHttpFeignClient 不能为空");
        }

        FeignBlockingLoadBalancerClient feignBlockingLoadBalancerClient = new FeignBlockingLoadBalancerClient(okHttpFeignClient, loadBalancerClient, loadBalancerClientFactory);
        feignBlockingLoadBalancerClientMap.put(serviceId, feignBlockingLoadBalancerClient);
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        final URI originalUri = URI.create(request.url());
        String serviceId = originalUri.getHost();
        FeignBlockingLoadBalancerClient feignBlockingLoadBalancerClient = feignBlockingLoadBalancerClientMap.get(serviceId);
        if (feignBlockingLoadBalancerClient == null) {
            return defaultFeignBlockingLoadBalancerClient.execute(request, options);
        } else {
            return feignBlockingLoadBalancerClient.execute(request, options);
        }
    }
}
