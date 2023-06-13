package lol.simeon.bl3;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.usb.UsbException;

public class Test {

  public static void main(String[] args) {
    USBConnector connector = new USBConnector();
    try {
      connector.connect();
    } catch (UsbException e) {
      throw new RuntimeException(e);
    }

    try {
      connector.getOutputPipe()
          .syncSubmit(new CommandBuilder(CommandPrefixes.INITIALIZE).build().getBytes());
      connector.getOutputPipe().syncSubmit(
          new CommandBuilder(CommandPrefixes.SPECIFY_COMMAND_MODE).replaceNextInt("03h").build()
              .getBytes());
      connector.getOutputPipe()
          .syncSubmit(new CommandBuilder(CommandPrefixes.SELECT_OBJECT_BY_NUMBER).replaceNextInt(0).replaceNextInt(0).build().getBytes());
      String text = "Hello World";
      int length = text.length();
      int nh1 = length % 256;
      int nh2 = length / 256;
      String insertCommand = new CommandBuilder(CommandPrefixes.DIRECTLY_INSERT_OBJECT).replaceNextInt(nh1).replaceNextInt(nh2).replaceNextString(text).build();
      System.out.println(insertCommand);
      connector.getOutputPipe()
          .syncSubmit(insertCommand.getBytes());
      connector.getOutputPipe().syncSubmit(CommandPrefixes.START_PRINTING.getPrefix().getBytes());

    } catch (UsbException e) {
      throw new RuntimeException(e);
    }

    /*byte[] result = connector.readData(10, 32);
    checkData(result);
    StatusConverter.printStatus(result);*/
  }

  private static void checkData(byte[] data) {
    int length = data.length;
    int i = 0;
    for (byte b : data) {
      if (b == 0) {
        i++;
      }
    }
    if (i == length) {
      throw new RuntimeException("Invalid Response");
    }
  }



}
