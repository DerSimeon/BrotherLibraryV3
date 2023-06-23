package lol.simeon.bl3;

import java.io.IOException;
import java.util.Arrays;
import javax.usb.UsbException;

public class BPrinter {

  private static final int ID_ID = 0;
  private static final int STATUS_ID = 3;
  private static final int TYPE_ID = 4;
  private static final int ROOM_ID = 5;
  private static final int QR_ID = 6;
  private final USBConnector usbConnector;

  public BPrinter() {
    this.usbConnector = new USBConnector();
    try {
      this.usbConnector.connect();
    } catch (UsbException | IOException e) {
      e.printStackTrace();
    }
  }

  public void printTemplate(String FurnitureType, String room, String id, String state) {
    try {
      initializePrinter();
      setDelimiter(",");
      for (int i = 0; i <= 7; i++) {
        if (i == ID_ID) {
          sendValueWithDelimiter(id);
        } else if (i == STATUS_ID) {
          sendValueWithDelimiter(state);
        } else if (i == TYPE_ID) {
          sendValueWithDelimiter(FurnitureType);
        } else if (i == ROOM_ID) {
          sendValueWithDelimiter(room);
        } else if (i == QR_ID) {
          sendValueWithDelimiter(id);
        } else {
          usbConnector.getOutputPipe().syncSubmit(",".getBytes());
        }
      }
      print();
    } catch (UsbException e) {
      e.printStackTrace();
    }
  }

  public boolean isAlive() {
    try {
      usbConnector.getOutputPipe().syncSubmit("^SR".getBytes());
    } catch (UsbException e) {
      throw new RuntimeException(e);
    }
    byte[] bytes = usbConnector.readData(5, 32);
    String[] result = ByteConverter.convert(bytes);
    return !Arrays.stream(result).allMatch(s -> s.equals("00h"));
  }

  private void initializePrinter() throws UsbException {
    usbConnector.getOutputPipe().syncSubmit("^II".getBytes());
    usbConnector.getOutputPipe().syncSubmit("^ID".getBytes());
  }

  private void sendValueWithDelimiter(String value) throws UsbException {
    usbConnector.getOutputPipe().syncSubmit((value + ",").getBytes());
  }

  private void setDelimiter(String delimiter) throws UsbException {
    usbConnector.getOutputPipe().syncSubmit(("^SS01" + delimiter).getBytes());
  }

  private void print() throws UsbException {
    usbConnector.getOutputPipe().syncSubmit("^FF".getBytes());
  }

  public boolean hasConnected(){
    return usbConnector.hasConnected();
  }

  public void shutdown(){
    usbConnector.shutdown();
  }

}
