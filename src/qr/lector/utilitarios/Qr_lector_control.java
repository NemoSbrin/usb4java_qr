package qr.lector.utilitarios;

import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbException;

public class Qr_lector_control implements UsbControlIrp {

	@Override
	public void complete() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getAcceptShortPacket() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getActualLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getData() {
		
		return null;
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOffset() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UsbException getUsbException() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUsbException() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAcceptShortPacket(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setActualLength(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setComplete(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setData(byte[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setData(byte[] arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLength(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOffset(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUsbException(UsbException arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void waitUntilComplete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void waitUntilComplete(long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte bRequest() {
		return UsbConst.REQUEST_GET_STATUS;
	}

	@Override
	public byte bmRequestType() {
		return UsbConst.REQUESTTYPE_DIRECTION_IN | UsbConst.REQUESTTYPE_TYPE_STANDARD
				| UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
	}

	@Override
	public short wIndex() {
		return 0;
	}

	@Override
	public short wValue() {
		return UsbConst.DESCRIPTOR_TYPE_DEVICE << 8;
	}

}
