package utils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
/**
 * @author hexushuai@jd.com
 * @version 1.0.0
 * @description json工具类
 * @date 2022/4/17 2:42 下午
 */
public class JacksonUtils {
    private static ObjectMapper defaultMapper = new ObjectMapper();
    private static ObjectMapper formatedMapper;

    public JacksonUtils() {
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("this argument is required; it must not be null");
        } else {
            try {
                return defaultMapper.writeValueAsString(obj);
            } catch (IOException var2) {
                throw new RuntimeException(var2);
            }
        }
    }

    public static <T> T parseJson(String jsonValue, Class<T> valueType) {
        if (jsonValue != null && valueType != null) {
            try {
                return defaultMapper.readValue(jsonValue, valueType);
            } catch (IOException var3) {
                throw new RuntimeException(var3);
            }
        } else {
            throw new IllegalArgumentException("this argument is required; it must not be null");
        }
    }

    public static <T> T parseJson(String jsonValue, Type type) {
        if (jsonValue != null && type != null) {
            try {
                return defaultMapper.readValue(jsonValue, defaultMapper.constructType(type));
            } catch (IOException var3) {
                throw new RuntimeException(var3);
            }
        } else {
            throw new IllegalArgumentException("this argument is required; it must not be null");
        }
    }

    public static <T> T parseJson(String jsonValue, JavaType valueType) {
        if (jsonValue != null && valueType != null) {
            try {
                return defaultMapper.readValue(jsonValue, valueType);
            } catch (IOException var3) {
                throw new RuntimeException(var3);
            }
        } else {
            throw new IllegalArgumentException("this argument is required; it must not be null");
        }
    }

    public static <T> T parseJson(String jsonValue, TypeReference<T> valueTypeRef) {
        if (jsonValue != null && valueTypeRef != null) {
            try {
                return defaultMapper.readValue(jsonValue, valueTypeRef);
            } catch (IOException var3) {
                throw new RuntimeException(var3);
            }
        } else {
            throw new IllegalArgumentException("this argument is required; it must not be null");
        }
    }

    public static String toJsonWithFormat(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("this argument is required; it must not be null");
        } else {
            try {
                return formatedMapper.writeValueAsString(obj);
            } catch (IOException var2) {
                throw new RuntimeException(var2);
            }
        }
    }

    public static String toJsonNoException(Object obj) {
        if (obj == null) {
            return "";
        } else {
            try {
                return formatedMapper.writeValueAsString(obj);
            } catch (Exception var2) {
                return "";
            }
        }
    }

    public static <T> T parseJsonWithFormat(String jsonValue, Class<T> valueType) {
        if (jsonValue != null && valueType != null) {
            try {
                return formatedMapper.readValue(jsonValue, valueType);
            } catch (IOException var3) {
                throw new RuntimeException(var3);
            }
        } else {
            throw new IllegalArgumentException("this argument is required; it must not be null");
        }
    }

    public static <T> T parseJsonWithFormat(String jsonValue, Type type) {
        if (jsonValue != null && type != null) {
            try {
                return formatedMapper.readValue(jsonValue, formatedMapper.constructType(type));
            } catch (IOException var3) {
                throw new RuntimeException(var3);
            }
        } else {
            throw new IllegalArgumentException("this argument is required; it must not be null");
        }
    }

    public static <T> T parseJsonWithFormat(String jsonValue, JavaType valueType) {
        if (jsonValue != null && valueType != null) {
            try {
                return formatedMapper.readValue(jsonValue, valueType);
            } catch (IOException var3) {
                throw new RuntimeException(var3);
            }
        } else {
            throw new IllegalArgumentException("this argument is required; it must not be null");
        }
    }

    public static <T> T parseJsonWithFormat(String jsonValue, TypeReference<T> valueTypeRef) {
        if (jsonValue != null && valueTypeRef != null) {
            try {
                return formatedMapper.readValue(jsonValue, valueTypeRef);
            } catch (IOException var3) {
                throw new RuntimeException(var3);
            }
        } else {
            throw new IllegalArgumentException("this argument is required; it must not be null");
        }
    }

    static {
        defaultMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        defaultMapper.configure(org.codehaus.jackson.map.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        formatedMapper = new ObjectMapper();
        formatedMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        formatedMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        formatedMapper.configure(org.codehaus.jackson.map.SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        formatedMapper.configure(org.codehaus.jackson.map.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    }
}
