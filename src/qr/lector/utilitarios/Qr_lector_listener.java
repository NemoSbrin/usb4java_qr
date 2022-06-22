package qr.lector.utilitarios;

import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.event.UsbDeviceErrorEvent;
import javax.usb.event.UsbDeviceEvent;
import javax.usb.event.UsbDeviceListener;

public class Qr_lector_listener implements UsbDeviceListener {
	private boolean usbDevice_Detached = true;
	private boolean dataEvent_Detached = true;
	private boolean errorEvent_Detached = true;

	public boolean isDataEvent_Detached() {
		return dataEvent_Detached;
	}

	public boolean isErrorEvent_Detached() {
		return errorEvent_Detached;
	}

	public boolean isUsbDevice_Detached() {
		return usbDevice_Detached;
	}

	@Override
	public void usbDeviceDetached(UsbDeviceEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Se conecto usb: " + arg0);
		System.out.println("              : " + arg0.getSource());
		System.out.println("              : " + arg0.getUsbDevice());
	}

	@Override
	public void errorEventOccurred(UsbDeviceErrorEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Ocurrio un error evento: " + arg0);
		System.out.println("                       : " + arg0.getSource());
	}

	@Override
	public void dataEventOccurred(UsbDeviceDataEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Ocurrio un evento: " + arg0);
		System.out.println("                 : " + arg0.getSource());
	}
}
