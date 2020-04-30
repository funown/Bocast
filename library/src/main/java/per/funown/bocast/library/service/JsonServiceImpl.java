package per.funown.bocast.library.service;

import android.content.Context;
import android.util.Log;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.SerializationService;
import com.google.gson.Gson;
import java.lang.reflect.Type;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Route(path = "/service/json")
public class JsonServiceImpl implements SerializationService {

  private static final String TAG = JsonServiceImpl.class.getSimpleName();
  private final Gson gson = new Gson();

  @Override
  public <T> T json2Object(String input, Class<T> clazz) {
    Log.e(TAG, "parsing json " + clazz.getSimpleName() );
    return gson.fromJson(input, clazz);
  }

  @Override
  public String object2Json(Object instance) {
    Log.e(TAG, "parsing object");
    return gson.toJson(instance);
  }

  @Override
  public <T> T parseObject(String input, Type clazz) {
    Log.e(TAG, "parsing json " + clazz.getTypeName() );
    return gson.fromJson(input, clazz);
  }

  @Override
  public void init(Context context) {

  }
}
