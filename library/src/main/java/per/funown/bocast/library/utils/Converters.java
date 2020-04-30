package per.funown.bocast.library.utils;

import androidx.room.TypeConverter;
import java.util.Date;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Converters {
  @TypeConverter
  public static Date fromTimestamp(Long value) {
    return value == null ? null : new Date(value);
  }

  @TypeConverter
  public static Long dateToTimestamp(Date date) {
    return date == null ? null : date.getTime();
  }
}