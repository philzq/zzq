package zzq.zzqsimpleframeworkhttp.utils;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import zzq.zzqsimpleframeworkcommon.context.ThreadLocalManager;
import zzq.zzqsimpleframeworkcommon.entity.ProjectConstant;
import zzq.zzqsimpleframeworkcommon.enums.BusinessCodeEnum;
import zzq.zzqsimpleframeworkhttp.exception.HttpClientException;
import zzq.zzqsimpleframeworkjson.JacksonUtil;
import zzq.zzqsimpleframeworklog.LogUtilFactory;
import zzq.zzqsimpleframeworklog.entity.RemoteDigestLogEntity;

/**
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-23 17:25
 */
public class HttpUtil {

    /**
     * 发送同步请求
     *
     * @param request
     * @return
     */
    public static Response sendWithResponse(Request request, OkHttpClient okHttpClient) {
        Response response = null;
        boolean success = true;
        try {
            //将上下文信息填充到header中
            Request newRequest = request.newBuilder().header(ProjectConstant.GLOBAL_CONTEXT_HEADER_KEY, JacksonUtil.toJSon(ThreadLocalManager.globalContextThreadLocal.get())).build();
            Call r = okHttpClient.newCall(newRequest);
            response = r.execute();
        } catch (Exception e) {
            success = false;
            if (request != null) {
                RemoteDigestLogEntity logTag = request.tag(RemoteDigestLogEntity.class);
                if (logTag != null) {
                    logTag.setErrorCode(BusinessCodeEnum.REMOTE_DIGEST_EXCEPTION.getBusinessCode());
                    logTag.setErrorDesc(BusinessCodeEnum.REMOTE_DIGEST_EXCEPTION.getMessage() + "\r\n" + ExceptionUtil.getStackTrace(e));
                }
            }
            throw new HttpClientException("【HTTP调用异常】", e);
        } finally {
            if (request != null) {
                RemoteDigestLogEntity logTag = request.tag(RemoteDigestLogEntity.class);
                if (logTag != null) {
                    logTag.setSuccess(success);
                    logTag.setRemoteIp(request.url().host());
                    logTag.setRemoteAppName(request.url().host());
                    LogUtilFactory.REMOTE_DIGEST.info(logTag);
                }
            }
        }
        return response;
    }
}
