/*
 * Copyright (C) 2014 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information. 
 */

package org.usb4java.javax.examples.adb;

import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbPipe;

/**
 * ADB device.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class AdbDevice
{
    /** The claimed USB ADB interface. */
    private final UsbInterface iface;

    /** The in endpoint address. */
    private final byte inEndpoint;

    /** The out endpoint address. */
    @SuppressWarnings("unused")
	private final byte outEndpoint;

    /**
     * Constructs a new ADB interface.
     * 
     * @param iface
     *            The USB interface. Must not be null.
     * @param inEndpoint
     *            The in endpoint address.
     * @param outEndpoint
     *            THe out endpoint address.
     */
    public AdbDevice(UsbInterface iface, byte inEndpoint,
        byte outEndpoint)
    {
        if (iface == null)
            throw new IllegalArgumentException("iface must be set");
        this.iface = iface;
        this.inEndpoint = inEndpoint;
        this.outEndpoint = outEndpoint;
    }

    /**
     * Opens the ADB device. When you are finished communicating with the device
     * then you should call the {@link #close()} method.
     * 
     * @throws UsbException
     *             When device could not be opened.
     */
    public void open() throws UsbException
    {
        this.iface.claim();
    }

    /**
     * Closes the ADB device.
     * 
     * @throws UsbException
     *             When device could not be closed.
     */
    public void close() throws UsbException
    {
        this.iface.release();
    }

    /**
     * Receives an ADB message.
     * 
     * @return The received ADB message.
     * @throws UsbException
     *             When USB communication failed.
     */
    public void receiveMessage() throws UsbException
    {
        UsbEndpoint inEndpoint =
            this.iface.getUsbEndpoint(this.inEndpoint);
        UsbPipe inPipe = inEndpoint.getUsbPipe();
        inPipe.open();
        try
        {
            byte[] headerBytes = new byte[MessageHeader.SIZE];
            int received = inPipe.syncSubmit(headerBytes);
            if (received != MessageHeader.SIZE)
                throw new InvalidMessageException(
                    "Invalid ADB message header size: " + received);
            MessageHeader header = new MessageHeader(headerBytes);
            if (!header.isValid())
                throw new InvalidMessageException(
                    "ADB message header checksum failure");
            byte[] data = new byte[header.getDataLength()];
            received = inPipe.syncSubmit(data);
            if (received != header.getDataLength())
                throw new InvalidMessageException(
                    "ADB message data size mismatch. Should be "
                        + header.getDataLength() + " but is " + received);
            
        }
        finally
        {
            inPipe.close();
        }
    }
}
