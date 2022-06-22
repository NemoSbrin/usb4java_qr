package testing;

import org.usb4java.javax.examples.adb.*;

import qr.lector.utilitarios.Qr_lector_control;
import qr.lector.utilitarios.Qr_lector_listener;

import java.util.HexFormat;
import java.util.List;

import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbException;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.event.UsbDeviceErrorEvent;
import javax.usb.event.UsbDeviceEvent;
import javax.usb.event.UsbDeviceListener;


public class testNoUsar implements Runnable {
	
	private static void convertStringToHex(String str) {
        StringBuilder stringBuilder = new StringBuilder();

        char[] charArray = str.toCharArray();

        for (char c : charArray) {
            String charToHex = Integer.toHexString(c);
            stringBuilder.append(charToHex);
        }

        System.out.println("Converted Hex from String: "+stringBuilder.toString());
    }

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		int datosDeEntrada = args.length;
		if ( datosDeEntrada != 2) {
			if ( datosDeEntrada == 1 )
				System.out.println("No ha ingresado PID o VID");
			else
				System.out.println("Ingrese solo VID PID, hay "+datosDeEntrada+" argumentos recibidos");
		}else {
			try {
				String [] aux = {args[0].split("0x")[1],args[1].split("0x")[1]};
				System.out.println(args[1] +" --> "+Short.MAX_VALUE+" "+Short.MIN_VALUE);
				for (String s : aux) {
					int aux1 = Integer.parseInt(s,16);
					short aux2 = (short) aux1;
					System.out.println("\t"+s+"**"+aux1+"**"+aux2);
				}
				
				short qr_vid = (short) 0x0525;
				short qr_pid = (short) 0xa4ac;
				System.out.println("\t --> "+qr_pid+" "+qr_vid);
				//System.out.println(HexFormat.fromHexDigits(args[1]));
//				short qr_vid = Short.decode(args[0]);
//				short qr_pid = Short.decode(args[1]);
//				System.out.println(args[0] +" --> "+ qr_vid);
//				System.out.println(args[1] +" --> "+ qr_pid);
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println("Ocurrio un error: "+e.getMessage());
				e.printStackTrace();
			}
			
		}
		
		/*
		Adb a = new Adb();

		short qr_vid = (short) 0x0525;
		short qr_pid = (short) 0xa4ac;
		UsbDevice qr_lector = null;
		// UsbDeviceDataEvent evento = new UsbDeviceDataEvent(qr_lector, 1);
		UsbDeviceListener qr_lector_listener = new Qr_lector_listener();
		UsbControlIrp qr_lector_control = null;
		Thread hilo1 = new Thread();
		synchronized (hilo1) {
			while (true) {
				try {
					qr_lector = a.findDevice(qr_vid, qr_pid);
					if (qr_lector == null) {
						System.out.println("No se detectó Qr");
					} else {
						// System.out.println(qr_lector);
						// System.out.println("->" + qr_lector.getUsbDeviceDescriptor());
						// System.out.println("-->" + qr_lector.getUsbConfigurations());
						a.showUsbControlIrpCommunication(qr_lector);
						// System.out.println(evento.getData());
						byte bmRequestType = UsbConst.REQUESTTYPE_DIRECTION_IN | UsbConst.REQUESTTYPE_TYPE_STANDARD
								| UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
						byte bRequest = UsbConst.REQUEST_GET_STATUS;
						short wValue = UsbConst.DESCRIPTOR_TYPE_DEVICE << 8;
						short wIndex = 0;
						//qr_lector_control = qr_lector.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
						List aux = qr_lector.getUsbConfigurations();
						
						System.out.println(qr_lector.getUsbConfigurations());
						
						// irp.setData(new byte[1]);
						// device.syncSubmit(irp);
						// System.out.println(irp.getData()[0]);
						
						UsbDeviceDataEvent qr_lector_evento = new UsbDeviceDataEvent(qr_lector, qr_lector_control);
						UsbDeviceErrorEvent qr_lector_error = new UsbDeviceErrorEvent(qr_lector, qr_lector_control);
						
						
						qr_lector.addUsbDeviceListener(qr_lector_listener);
						
						// Runtime runtime = Runtime.getRuntime();
						
						qr_lector_listener.dataEventOccurred(qr_lector_evento);
						qr_lector_listener.usbDeviceDetached(qr_lector_evento);
						qr_lector_listener.errorEventOccurred(qr_lector_error);
					}
					
					hilo1.wait(1000);

				} catch (UsbException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	*/}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
