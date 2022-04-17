package utils;
import org.springframework.beans.BeanUtils;
import java.util.List;
/**
 * @author hexushuai@jd.com
 * @version 1.0.0
 * @description BeanUtils
 * @date 2022/4/17 2:59 下午
 */
public class BeanUtil {
    public static <T, U> T copyBeanProperties(Class<T> targetClass, U source) {
        if (source == null) {
            return null;
        }

        T target = BeanUtils.instantiate(targetClass);
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <T, U> List<T> copyListProperties(Class<T> targetClass, List<U> sourceList) {
        if (sourceList == null) {
            return null;
        }

        List<T> targetList = Lists.newArrayList();

        for (U source : sourceList) {
            if (source != null) {
                T target = BeanUtils.instantiate(targetClass);
                BeanUtils.copyProperties(source, target);
                targetList.add(target);
            }
        }

        return targetList;
    }
}
