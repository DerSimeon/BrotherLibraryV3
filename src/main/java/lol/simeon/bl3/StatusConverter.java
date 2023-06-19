package lol.simeon.bl3;

import java.util.LinkedHashMap;
import java.util.Map;

public class StatusConverter {

  public static String convertBattery(String battery) {
    return switch (battery) {
      case "00h" -> "Full Battery";
      case "01h" -> "Half Battery";
      case "02h" -> "Low Battery";
      case "03h" -> "Changing required";
      case "04h" -> "AC adapter in use";
      default -> "Unknown";
    };
  }

  public static String convertExtendedError(String error) {
    return switch (error) {
      case "1Dh" -> "High resolution/High- speed printing error";
      case "1Eh" -> "Power switching error";
      case "1Fh" -> "Battery error";
      case "21h" -> "incompatible media";
      default -> "Unknown";
    };
  }

  public static String convertRibbonColor(String color) {
    return switch (color) {
      case "01h" -> "white";
      case "04h" -> "red";
      case "05h" -> "blue";
      case "08h" -> "black";
      case "0Ah" -> "Gold";
      case "62h" -> "Blue";
      case "F0h" -> "Clearning";
      case "F1h" -> "Stenctil";
      case "02h" -> "Other";
      case "FFH" -> "Incompatible";
      default -> "Unknown";
    };
  }

  public static String convertMediaColor(String color) {
    return switch (color) {
      case "01h" -> "White";
      case "02h" -> "Other";
      case "03h" -> "Clear";
      case "04h" -> "Red";
      case "05h" -> "Blue";
      case "06h" -> "Yellow";
      case "07h" -> "Green";
      case "08h" -> "Black";
      case "09h" -> "Clear";
      case "20h" -> "Matte White";
      case "21h" -> "Matte Clear";
      case "22h" -> "Matte Silver";
      case "23h" -> "Satin Gold";
      case "24h" -> "Satin Silver";
      case "30h" -> "Blue";
      case "31h" -> "Red";
      case "40h" -> "Fluorescent Orange";
      case "41h" -> "Fluorescent Yellow";
      case "50h" -> "Berry Pink";
      case "51h" -> "Light Gray";
      case "52h" -> "Lime Green";
      case "60h" -> "Yellow";
      case "61h" -> "Pink";
      case "62h" -> "Blue";
      case "70h" -> "Heat-shrink Tube";
      case "71h" -> "Heat-shrink Tube E";
      case "90h" -> "White (Flex ID)";
      case "91h" -> "Yellow (Flex ID)";
      case "F0h" -> "Clearning";
      case "F1h" -> "Stenctil";
      case "FFH" -> "Cooling finished";
      default -> "Unknown";
    };
  }

  public static String convertNotification(String notification) {
    return switch (notification) {
      case "00h" -> "No notification";
      case "01h" -> "Cover open";
      case "02h" -> "Cover close";
      case "03h" -> "Cooling started";
      case "04h" -> "Cooling finished";
      default -> "Unknown";
    };
  }

  public static String convertStatusType(String statusType) {
    return switch (statusType) {
      case "00h" -> "Reply to status request";
      case "01h", "03h", "04h" -> "Not use";
      case "02h" -> "Error occurred";
      case "05h" -> "Notification";
      case "06h" -> "Phase change";
      default -> "Unknown";
    };
  }

  public static void printStatus(byte[] data) {
    String[] converted = ByteConverter.convert(data);
    Map<String, Object> values = extractValues(converted);
    values.forEach((key, value) -> System.out.println(key + ": " + value));
  }

  private static Map<String, Object> extractValues(String[] byteArray) {
    Map<String, Object> values = new LinkedHashMap<>();
    values.put("Print head mark", byteArray[0].replace("f", ""));
    values.put("Size", byteArray[1]);
    values.put("Brother code", byteArray[2]);
    values.put("Series code", byteArray[3]);
    char modelCode = switch (byteArray[4]) {
      case "6fh" -> 'o';
      case "70h" -> 'p';
      case "71h" -> 'q';
      default -> '?';
    };
    values.put("Model code", modelCode);

    values.put("Country code", byteArray[5]);
    values.put("Battery level information", StatusConverter.convertBattery(
        String.valueOf(byteArray[6])));
    values.put("Extended error",
        StatusConverter.convertExtendedError(String.valueOf(byteArray[7])));
    values.put("Error information 1", byteArray[8]);
    values.put("Error information 2", byteArray[9]);
    values.put("Media width", byteArray[10]);
    values.put("Media type", byteArray[11]);
    values.put("Number of colors", byteArray[12]);
    values.put("Internal font information", byteArray[13]);
    values.put("Internal Japanese font information", byteArray[14]);
    values.put("Mode", byteArray[15]);
    values.put("Density", byteArray[16]);
    values.put("Media length", byteArray[17]);
    values.put("Status type", StatusConverter.convertStatusType(String.valueOf(byteArray[18])));
    values.put("Phase type", byteArray[19]);
    values.put("Phase number (higher order bytes)", byteArray[20]);
    values.put("Phase number (lower order bytes)", byteArray[21]);
    values.put("Notification number", StatusConverter.convertNotification(
        String.valueOf(byteArray[22])));
    values.put("Expansion area (number of bytes)", byteArray[23]);
    values.put("Media color information", StatusConverter.convertMediaColor(
        String.valueOf(byteArray[24])));
    values.put("Ribbon color information", StatusConverter.convertRibbonColor(
        String.valueOf(byteArray[25])));

    return values;
  }

}
