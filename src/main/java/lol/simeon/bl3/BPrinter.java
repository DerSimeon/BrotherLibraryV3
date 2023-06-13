package lol.simeon.bl3;

import javax.usb.UsbException;

public class BPrinter {

  private final USBConnector usbConnector;

  public BPrinter() {
    this.usbConnector = new USBConnector();
    try {
      this.usbConnector.connect();
    } catch (UsbException e) {
      throw new RuntimeException(e);
    }
  }

  public void printTemplate(String id) {
    try {
      usbConnector.getOutputPipe()
          .syncSubmit(new CommandBuilder(CommandPrefixes.INITIALIZE).build().getBytes());
      usbConnector.getOutputPipe().syncSubmit(
          new CommandBuilder(CommandPrefixes.SPECIFY_COMMAND_MODE).replaceNextInt("03h").build()
              .getBytes());
      setValue("ENV-QR", id);
      setValue("ENV-ID", id);
      print();
    } catch (UsbException e) {
      throw new RuntimeException(e);
    }

  }

  private void setValue(String name, String value) throws UsbException {
    usbConnector.getOutputPipe()
        .syncSubmit(
            new CommandBuilder(CommandPrefixes.SELECT_OBJECT_BY_NAME).replaceNextString(name)
                .build().getBytes());
    int length = value.length();
    int nh1 = length % 256;
    int nh2 = length / 256;
    String insertCommand = new CommandBuilder(
        CommandPrefixes.DIRECTLY_INSERT_OBJECT).replaceNextInt(nh1).replaceNextInt(nh2)
        .replaceNextString(value).build();
    usbConnector.getOutputPipe().syncSubmit(insertCommand.getBytes());
  }

  private void print() {
    try {
      usbConnector.getOutputPipe()
          .syncSubmit(CommandPrefixes.START_PRINTING.getPrefix().getBytes());
    } catch (UsbException e) {
      throw new RuntimeException(e);
    }
  }

}
