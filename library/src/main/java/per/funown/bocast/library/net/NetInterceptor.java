package per.funown.bocast.library.net;

import android.util.Log;

import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.charset.Charset;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/27
 *     desc   : http拦截器
 *     version: 1.0
 * </pre>
 */
public class NetInterceptor implements Interceptor {

  public static final String TAG = "NetInterceptor";
  private static final Charset UTF8 = Charset.forName("UTF-8");

  @Override
  public Response intercept(Chain chain) throws IOException {

    Request request = chain.request();
    Connection connection = chain.connection();
    Protocol protocol = (connection != null) ? connection.protocol() : Protocol.HTTP_1_1;
    String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
    Log.d(TAG, requestStartMessage);

    Response response =  chain.proceed(request);
    ResponseBody body = response.body();
    long contentLength = body.contentLength();

    if (!bodyEncoded(response.headers())) {
      BufferedSource source = body.source();
      source.request(Long.MAX_VALUE);
      Buffer buffer = source.getBuffer();
      if (contentLength != 0) {
        Log.d(TAG, String.format("response = %s", buffer.clone().readString(UTF8)));
      }
    }
    return response;
  }

  private boolean bodyEncoded(Headers headers) {
    String encoding = headers.get("Content-Encoding");
    return encoding != null && !encoding.equalsIgnoreCase("identity");
  }
}
