/*
 * Copyright (C) 2014 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information. 
 */

package org.usb4java.javax.examples.adb;

import java.util.ArrayList;
import java.util.List;
import javax.usb.UsbConfiguration;
import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbEndpoint;
import javax.usb.UsbEndpointDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfaceDescriptor;
import javax.usb.UsbServices;
import javax.usb.util.UsbUtil;

/**
 * Some static helper methods for ADB.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class Adb
{
    /** Constant for ADB class. */
    private static final byte ADB_CLASS = (byte) 0xff;

    /** Constant for ADB sub class. */
    private static final byte ADB_SUBCLASS = 0x42;

    /** Constant for ADB protocol. */
    private static final byte ADB_PROTOCOL = 1;
    
    private static UsbServices services = null;
    private static UsbHub hub = null;
    
    public Adb() {
    	 try {
			services = UsbHostManager.getUsbServices();
			hub = services.getRootUsbHub();
			System.out.println("Cargado Correctamente");
		} catch (SecurityException | UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
    }
    
    /**
     * Returns the list of all available ADB devices.
     * 
     * @return The list of available ADB devices.
     * @throws UsbException
     *             When USB communication failed.
     */
    public static List<AdbDevice> findDevices() throws UsbException
    {
        List<AdbDevice> usbDevices = new ArrayList<AdbDevice>();
        findDevices(services.getRootUsbHub(), usbDevices);
        return usbDevices;
    }
    
    /**
     * Recursively scans the specified USB hub for ADB devices and puts them
     * into the list.
     * 
     * @param hub
     *            The USB hub to scan recursively.
     * @param devices
     *            The list where to add found devices.
     */
    @SuppressWarnings("unchecked")
	private static void findDevices(UsbHub hub, List<AdbDevice> devices)
    {
        for (UsbDevice usbDevice: (List<UsbDevice>) hub.getAttachedUsbDevices())
        {
            if (usbDevice.isUsbHub())
            {
                findDevices((UsbHub) usbDevice, devices);
            }
            else
            {
                checkDevice(usbDevice, devices);
            }
        }
    }

    /**
     * Encuentra un dispositivo usb conectado
     * 
     * @param vendorId
     * @param productId
     */
    @SuppressWarnings("unchecked")
	public static UsbDevice findDevice(short vendorId, short productId) throws SecurityException, UsbException {
    	UsbDevice deviceReturn = null;
		//System.out.println("** Se recibe hub" + hub + ", vId: " + vendorId + ", pId: " + productId);
		for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
			UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
			if (desc.idVendor() == vendorId && desc.idProduct() == productId) {
				deviceReturn = device;
				break;
			}
		}
		return deviceReturn;
	}

    /**
     * Checks if the specified USB device is a ADB device and adds it to the
     * list if it is.
     * 
     * @param usbDevice
     *            The USB device to check.
     * @param adbDevices
     *            The list of ADB devices to add the USB device to when it is an
     *            ADB device.
     */
    @SuppressWarnings("unchecked")
	private static void checkDevice(UsbDevice usbDevice,
        List<AdbDevice> adbDevices)
    {
        UsbDeviceDescriptor deviceDesc = usbDevice.getUsbDeviceDescriptor();

        // Ignore devices from Non-ADB vendors
        if (!isAdbVendor(deviceDesc.idVendor())) return;

        // Check interfaces of device
        UsbConfiguration config = usbDevice.getActiveUsbConfiguration();
        for (UsbInterface iface: (List<UsbInterface>) config.getUsbInterfaces())
        {
            List<UsbEndpoint> endpoints = iface.getUsbEndpoints();

            // Ignore interface if it does not have two endpoints
            if (endpoints.size() != 2) continue;

            // Ignore interface if it does not match the ADB specs
            if (!isAdbInterface(iface)) continue;

            UsbEndpointDescriptor ed1 =
                endpoints.get(0).getUsbEndpointDescriptor();
            UsbEndpointDescriptor ed2 =
                endpoints.get(1).getUsbEndpointDescriptor();

            // Ignore interface if endpoints are not bulk endpoints
            if (((ed1.bmAttributes() & UsbConst.ENDPOINT_TYPE_BULK) == 0) ||
                ((ed2.bmAttributes() & UsbConst.ENDPOINT_TYPE_BULK) == 0))
                continue;
            
            // Determine which endpoint is in and which is out. If both
            // endpoints are in or out then ignore the interface
            byte a1 = ed1.bEndpointAddress();
            byte a2 = ed2.bEndpointAddress();
            byte in, out;
            if (((a1 & UsbConst.ENDPOINT_DIRECTION_IN) != 0) &&
                ((a2 & UsbConst.ENDPOINT_DIRECTION_IN) == 0))
            {
                in = a1;
                out = a2;
            }
            else if (((a2 & UsbConst.ENDPOINT_DIRECTION_IN) != 0) &&
                ((a1 & UsbConst.ENDPOINT_DIRECTION_IN) == 0))
            {
                out = a1;
                in = a2;
            }
            else continue;                
            
            // Create ADB device and add it to the list
            AdbDevice adbDevice = new AdbDevice(iface, in, out);
            adbDevices.add(adbDevice);
        }
    }

    /**
     * Checks if the specified vendor is an ADB device vendor.
     * 
     * @param vendorId
     *            The vendor ID to check.
     * @return True if ADB device vendor, false if not.
     */
    private static boolean isAdbVendor(short vendorId)
    {
        for (short adbVendorId: Vendors.VENDOR_IDS)
            if (adbVendorId == vendorId) return true;
        return false;
    }

    /**
     * Checks if the specified USB interface is an ADB interface.
     * 
     * @param iface
     *            The interface to check.
     * @return True if interface is an ADB interface, false if not.
     */
    private static boolean isAdbInterface(UsbInterface iface)
    {
        UsbInterfaceDescriptor desc = iface.getUsbInterfaceDescriptor();
        return desc.bInterfaceClass() == ADB_CLASS &&
            desc.bInterfaceSubClass() == ADB_SUBCLASS &&
            desc.bInterfaceProtocol() == ADB_PROTOCOL;
    }
    
    /**
	 * Show how to communicate using UsbControlIrp objects.
	 * @param usbDevice The UsbDevice to use.
	 */
	public static void showUsbControlIrpCommunication(UsbDevice usbDevice)
	{
		/* UsbControlIrps have 2 parts: a header, or 'setup packet' in
		 * low-level USB terms, and data buffer.  The header tells the
		 * device what control action the communication is requesting.
		 * These particular values will perform a get-device-descriptor request.
		 */
		byte bmRequestType =
			UsbConst.REQUESTTYPE_DIRECTION_IN | UsbConst.REQUESTTYPE_TYPE_STANDARD | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
		byte bRequest = UsbConst.REQUEST_GET_DESCRIPTOR;
		short wValue = UsbConst.DESCRIPTOR_TYPE_DEVICE << 8;
		short wIndex = 0;
		System.out.println(" -- bmRequestType = "+UsbConst.REQUESTTYPE_DIRECTION_IN+" | "+UsbConst.REQUESTTYPE_TYPE_STANDARD+" | "+UsbConst.REQUESTTYPE_RECIPIENT_DEVICE+" : "+bmRequestType);
		System.out.println(" -- bRequest = "+UsbConst.REQUEST_GET_DESCRIPTOR);
		System.out.println(" -- wValue = "+UsbConst.DESCRIPTOR_TYPE_DEVICE+" << "+8+" : "+wValue);
		System.out.println(" -- wIndex = "+0);
		/* For this specific case, where we are getting a device descriptor,
		 * 256 bytes is enough; device descriptors are fixed-length.
		 */
		byte[] buffer = new byte[256];

		/* All communication on the DCP (and all control-type pipes) is
		 * done using UsbControlIrp objects.  There are 3 different ways to
		 * create a UsbControlIrp object:
		 * (1) Call the UsbDevice.createUsbControlIrp() method.
		 * (2) Use the javax.usb.util.DefaultUsbControlIrp class.
		 * (3) Define your own class that implements the UsbControlIrp interface.
		 * The first way is the best (but least flexible) as it produces a
		 * UsbControlIrp that may be optimized to whatever javax.usb implementation
		 * you are currently using; however you are limited to only those
		 * methods provided by the UsbControlIrp interface.
		 * The second way is more flexible, as you can use all the methods
		 * defined by the DefaultUsbControlIrp class, but it may not
		 * be as optimized to the implementation (i.e. the javax.usb
		 * implementation may have to do more work to process it).
		 * The third way is the most flexible as you can put whatever
		 * you want into your class; however this way will very likely
		 * require more processing and/or memory by the javax.usb
		 * implementation.  A combination of the second and third
		 * options is also possible, where you create your own class
		 * that extends the DefaultUsbControlIrp class.
		 * For this example we will use the first option, as it is the easiest.
		 */
		UsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
		System.err.println(" -- usbControlIrp: "+usbControlIrp.getActualLength());
		System.err.println(" -- usbControlIrp: "+usbControlIrp.getData()+" -- "+usbControlIrp.getData().length+" -- ");

		//if (!sendUsbControlIrp(usbDevice, usbControlIrp))
		//	return;

		/* This is the number of bytes actually received from the device.
		 * If the data direction was out (host-to-device), this would
		 * be the number of bytes actually sent to the device.  For the
		 * input (device-to-host) case, this may be less than the length of
		 * the provided buffer (providing the irp is set to accept short packets).
		 * For the output case, this should never be less than the size
		 * of the provided buffer (but you may want to check anyway to be sure!).
		 */
		int length = usbControlIrp.getActualLength();

		/* The device descriptor is binary, as specified by the USB spec.
		 * We're not going to parse it here, but we can print it out.
		 */
		//System.out.println("Got device descriptor (length " + length + ") :");
		System.out.println(UsbUtil.toHexString(" 0x", buffer, length));

		/* Now let's try getting the current configuration number. */
		bmRequestType =
			UsbConst.REQUESTTYPE_DIRECTION_IN | UsbConst.REQUESTTYPE_TYPE_STANDARD | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
		bRequest = UsbConst.REQUEST_GET_CONFIGURATION;
		wValue = 0;
		wIndex = 0;
		/* The current configuration number will be returned in this byte. */
		buffer = new byte[1];

		usbControlIrp = usbDevice.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
		usbControlIrp.setData(buffer);

		//if (!sendUsbControlIrp(usbDevice, usbControlIrp))
		//	return;

		length = usbControlIrp.getActualLength();

		/* If we didn't get 1 byte, something went wrong... */
		if (1 > length)
			System.out.println(" -- Got no data during submission!");
		else
			System.out.println(" -- Got current configuration number : " + UsbUtil.unsignedInt(buffer[0]));
	}
    
}
