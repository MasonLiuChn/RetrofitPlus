package retrofit2.plus;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by liumeng on 1/18/16.
 */
public class RetrofitUtil {
    public static HttpUrl convertHttpsUrl(HttpUrl httpUrl) {
        if (!httpUrl.isHttps()) {
            return httpUrl.newBuilder().scheme("https").port(443).build();
        }
        return httpUrl;
    }

    public static Annotation[][] getParameterAnnotationsWithCallbackArg(Method method) {
        Annotation[][] raw = method.getParameterAnnotations();
        //最后一个参数是 callback 的情况
        if (raw.length > 0 && raw[raw.length - 1].length == 0) {
            Annotation[][] now = new Annotation[raw.length - 1][];
            for (int i = 0; i < now.length; i++) {
                now[i] = new Annotation[raw[i].length];
                for (int j = 0; j < now[i].length; j++) {
                    now[i][j] = raw[i][j];
                }
            }
            return now;
        }
        return raw;
    }

    public static boolean isDirectCall(Method method, Object[] args) {
        Type returnType = method.getGenericReturnType();
        if (returnType == void.class && args.length > 0 && args[args.length - 1] instanceof Callback) {
            return true;
        }
        return false;
    }

    public static Type getReturnTypeIfWithCallbackArg(Method method) {
        Type returnType = method.getGenericReturnType();
        if (returnType == void.class) {
            Type[] parameterTypes = method.getGenericParameterTypes();
            if (parameterTypes.length > 0 && parameterTypes[parameterTypes.length - 1] instanceof ParameterizedType) {
                final ParameterizedType callbackType = (ParameterizedType) parameterTypes[parameterTypes.length - 1];
                returnType = new ParameterizedType() {
                    @Override
                    public Type[] getActualTypeArguments() {
                        return callbackType.getActualTypeArguments();
                    }

                    @Override
                    public Type getOwnerType() {
                        return callbackType.getOwnerType();
                    }

                    @Override
                    public Type getRawType() {
                        return Call.class;
                    }
                };
            } else {
                String message = "If Service methods return void, the last parameter of method must be a Callback" + "\n    for method "
                        + method.getDeclaringClass().getSimpleName()
                        + "."
                        + method.getName();
                throw new IllegalArgumentException(message, null);
            }
        }
        return returnType;
    }
}
