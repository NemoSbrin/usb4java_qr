package main;

import org.usb4java.javax.examples.adb.*;

import qr.lector.utilitarios.Qr_lector_control;
import qr.lector.utilitarios.Qr_lector_listener;
import validates.Validaciones;

import javax.usb.UsbConfiguration;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbPipe;
import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.event.UsbDeviceErrorEvent;
import javax.usb.event.UsbDeviceListener;

public abstract class principal implements Runnable {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		/* Validaciones para el ingreso correcto de inputs */
		Validaciones validate = new Validaciones(args, 2);

		if (validate.validateInputs()) {

			Short[] aux = validate.getShorts();

			/* Empieza la deteccion del qr */
			Adb a = new Adb();
			short qr_vid = aux[0];
			short qr_pid = aux[1];

			UsbDevice qr_lector = null;

			UsbDeviceListener qr_lector_listener = new Qr_lector_listener();
			UsbControlIrp qr_lector_control = new Qr_lector_control();

			Thread hilo1 = new Thread();

			synchronized (hilo1) {
				while (true) {
					try {
						qr_lector = a.findDevice(qr_vid, qr_pid);
						if (qr_lector == null) {
							// System.out.println("No se detectó Lector QR ");
						} else {
							a.showUsbControlIrpCommunication(qr_lector);

							//UsbConfiguration configuration = qr_lector.getActiveUsbConfiguration();
							UsbConfiguration configuration = qr_lector.getUsbConfiguration((byte)1);
							System.out.println(" >> configuration: "+configuration);
							UsbInterface ifaceu = configuration.getUsbInterface((byte)0);
							System.out.println(" >> ifaceu.settings : "+ifaceu.getSettings());
							for (Object ue:ifaceu.getSettings()) {
								System.out.println(" >> ifaceu.settings -- ue: "+ue);
							}
							
							System.out.println(" >> ifaceu.endpoints : "+ifaceu.getUsbEndpoints());
							for (Object ue:ifaceu.getUsbEndpoints()) {
								System.out.println(" >> ifaceu.endpoints -- ue: "+ue.toString());
							}
							System.out.println(" >> active : "+qr_lector.getActiveUsbConfigurationNumber());
							
							UsbInterface iface = configuration.getUsbInterface((byte) 0);
							System.out.println(" >> iface: "+iface);
							
							UsbEndpoint endpoint = iface.getUsbEndpoint((byte) 0x83);
							System.out.println(" >> endpoint: "+endpoint);
//							UsbPipe pipe = endpoint.getUsbPipe();
//							pipe.open();
//							try
//							{
//							    byte[] data = new byte[8];
//							    int received = pipe.syncSubmit(data);
//							    System.out.println(received + " bytes received");
//							}
//							finally
//							{
//							    pipe.close();
//							}
							
							/*
							UsbInterface iface = configuration.getUsbInterface((byte) 0);
							iface.claim();  
							try {
								//System.out.println(iface.isClaimed());
								// ... Communicate with the interface or endpoints ...
							} finally {
								iface.release();
							}
							*/

							/**/
							  UsbDeviceDataEvent qr_lector_evento = new UsbDeviceDataEvent(qr_lector,
							  qr_lector_control); UsbDeviceErrorEvent qr_lector_error = new
							  UsbDeviceErrorEvent(qr_lector, qr_lector_control);
							  
							  qr_lector.addUsbDeviceListener(qr_lector_listener);
							  
							  qr_lector_listener.dataEventOccurred(qr_lector_evento);
							  qr_lector_listener.usbDeviceDetached(qr_lector_evento);
							 qr_lector_listener.errorEventOccurred(qr_lector_error);
							 /**/
						}

						hilo1.wait(1000);

					} catch (UsbException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
