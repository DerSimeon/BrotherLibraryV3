package lol.simeon.bl3;

import java.util.ArrayList;
import java.util.List;

public class ByteConverter {

  /*
    Fixed at 80h
    Fixed at 20h
    Fixed at "B" (42h)
    Fixed at "0" (30h)

   */

  public static String[] convert(byte[] data){
    List<String> result = new ArrayList<>();
    for (byte b : data) {
      result.add(convertByte(b));
    }
    return result.toArray(new String[0]);
  }

  private static String convertByte(byte b){
    String hex = Integer.toHexString(b);
    if(hex.length() == 1){
      hex = "0" + hex;
    }
    return hex + "h";
  }


}
