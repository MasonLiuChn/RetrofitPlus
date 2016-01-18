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
            Type lastArgType = null;
            Class<?> lastArgClass = null;
            Type[] parameterTypes = method.getGenericParameterTypes();
            if (parameterTypes.length > 0) {
                Type typeToCheck = parameterTypes[parameterTypes.length - 1];
                lastArgType = typeToCheck;
                if (typeToCheck instanceof ParameterizedType) {
                    typeToCheck = ((ParameterizedType) typeToCheck).getRawType();
                }
                if (typeToCheck instanceof Class) {
                    lastArgClass = (Class<?>) typeToCheck;
                }
            }
            boolean hasCallback = lastArgClass != null && Callback.class.isAssignableFrom(lastArgClass);
            if(hasCallback){
                final ParameterizedType lastArgTypeTmp = (ParameterizedType)Types.getSupertype(lastArgType, Types.getRawType(lastArgType), Callback.class);
                returnType = new ParameterizedType() {
                    @Override
                    public Type[] getActualTypeArguments() {
                        return lastArgTypeTmp.getActualTypeArguments();
                    }

                    @Override
                    public Type getOwnerType() {
                        return lastArgTypeTmp.getOwnerType();
                    }

                    @Override
                    public Type getRawType() {
                        return Call.class;
                    }
                };
            }else {
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
