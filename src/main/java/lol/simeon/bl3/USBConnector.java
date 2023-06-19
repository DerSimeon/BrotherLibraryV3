package lol.simeon.bl3;

import java.io.IOError;
import java.io.IOException;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbPipe;
import javax.usb.UsbServices;

public class USBConnector {

  private UsbPipe inputPipe;
  private UsbPipe outputPipe;

  public USBConnector() {
    System.out.println("USBConnector created");
  }

  public void connect() throws UsbException, IOException {
    UsbDevice device = discover();
    if (device == null) {
      throw new IOException("No printer found");
    }

    UsbInterface usbInterface = device.getActiveUsbConfiguration().getUsbInterface((byte) 0);
    if (!usbInterface.isClaimed()) {
      usbInterface.claim();
    } else {
      throw new IOException("Interface already claimed");
    }

    UsbEndpoint inputEndpoint = usbInterface.getUsbEndpoint((byte) 0x81);
    UsbEndpoint outputEndpoint = usbInterface.getUsbEndpoint((byte) 0x02);

    this.inputPipe = inputEndpoint.getUsbPipe();
    this.outputPipe = outputEndpoint.getUsbPipe();

    inputPipe.open();
    outputPipe.open();
    System.out.println("USBConnector connected");
  }

  private UsbDevice discover() throws UsbException {
    UsbServices services = UsbHostManager.getUsbServices();
    UsbHub device = services.getRootUsbHub();

    for (Object attachedUsbDevice : device.getAttachedUsbDevices()) {
      UsbDevice usbDevice = (UsbDevice) attachedUsbDevice;
      UsbDeviceDescriptor usbDeviceDescriptor = usbDevice.getUsbDeviceDescriptor();
      if (usbDeviceDescriptor.idVendor() == 0x04F9 && usbDeviceDescriptor.idProduct() == 0x2042) {
        return usbDevice;
      }
      if (usbDevice.isUsbHub()) {
        UsbHub secondLevelHub = (UsbHub) usbDevice;
        for (Object secondLevelUsbDevice : secondLevelHub.getAttachedUsbDevices()) {
          UsbDevice usbDevice1 = (UsbDevice) secondLevelUsbDevice;
          UsbDeviceDescriptor usbDeviceDescriptor1 = usbDevice1.getUsbDeviceDescriptor();
          if (usbDeviceDescriptor1.idVendor() == 0x04f9 && usbDeviceDescriptor1.idProduct() == 0x2085) {
            return usbDevice1;
          }
        }
      }
    }
    return null;
  }

  public UsbPipe getInputPipe() {
    return inputPipe;
  }

  public UsbPipe getOutputPipe() {
    return outputPipe;
  }

  public byte[] readData(int timeoutInSeconds, int length) {
    long start = System.currentTimeMillis();
    byte[] data = new byte[length];
    while (System.currentTimeMillis() - start < timeoutInSeconds * 1000L) {
      try {
        int bytesRead = inputPipe.syncSubmit(data);
        if (bytesRead == length) {
          return data;
        }
      } catch (UsbException e) {
        e.printStackTrace();
      }
    }
    return data;
  }

  public void shutdown() {
    try {
      inputPipe.close();
      outputPipe.close();
    } catch (UsbException e) {
      e.printStackTrace();
    }
  }
}
