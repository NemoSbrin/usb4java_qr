package main;

import org.usb4java.javax.examples.adb.*;
import qr.lector.utilitarios.Qr_lector_listener;
import validates.Validaciones;

import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbException;
import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.event.UsbDeviceErrorEvent;
import javax.usb.event.UsbDeviceListener;

public abstract class principal implements Runnable {
	

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Adb a = new Adb();
		Validaciones validate = new Validaciones(args);
		System.out.println();
		
		short qr_vid = (short) 0x0525;
		short qr_pid = (short) 0xa4ac;
		UsbDevice qr_lector = null;
		
		UsbDeviceListener qr_lector_listener = new Qr_lector_listener();
		UsbControlIrp qr_lector_control = null;
		
		Thread hilo1 = new Thread();
		
		synchronized (hilo1) {
			while (true) {
				try {
					qr_lector = a.findDevice(qr_vid, qr_pid);
					if (qr_lector == null) {
						System.out.println("No se detectó Qr "+qr_pid);
						
					} else {
						a.showUsbControlIrpCommunication(qr_lector);
						
						System.out.println(qr_lector.getUsbConfigurations());
						
						UsbDeviceDataEvent qr_lector_evento = new UsbDeviceDataEvent(qr_lector, qr_lector_control);
						UsbDeviceErrorEvent qr_lector_error = new UsbDeviceErrorEvent(qr_lector, qr_lector_control);
						
						
						qr_lector.addUsbDeviceListener(qr_lector_listener);
						
						qr_lector_listener.dataEventOccurred(qr_lector_evento);
						qr_lector_listener.usbDeviceDetached(qr_lector_evento);
						qr_lector_listener.errorEventOccurred(qr_lector_error);
					}
					
					hilo1.wait(1000);

				} catch (UsbException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
