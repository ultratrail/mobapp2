package teamtreehouse.com.iamhere;


import com.google.android.gms.maps.model.LatLng;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

//import com.google.android.gms.maps.model.LatLng;

/**
 * Created by romane on 23/03/17.

 Use : -create message with Message m = new Message(Person p)

 OR

 -create message with Message m = new Message()
 -set fields with the m.setXX() methods

 -use m.makeLoraPayload() to create the byte[] describing the message
 -get the byte[] message with m.loraPayload()
 -notify the receiver that this is a SOS (8'th bit) and that the message contains heartrate (7'th bit) with m.setOptions((byte) 0x3);
 -sos and heartRate can be set through setSOS() and setbluetooth_heartRate() (unecessary if setHeartRate() already used.)


 on receive : create message with Message m = Message()
 - set fields with m.decodeLoraPayload(byte[] received_data)

 OR create message with Message m = Message

 - get fields with m.getXX() methods
 - message can be verifyed with m.checked()
 - message is SOS if m.isSos() returns true
 - message contains heartRate if m.isHeartRate() returns true

 */

public class Message {

    private ByteOrder byteOrder;

    private String nom;
    private short heartRate;
    private byte[] loraPayload;

    private byte options; // id size, heartRate, sos
    private short seq; //sequence number of the message

    LatLng coord;

    private short hour;
    private short min;
    private short sec;

    private boolean checked;


    public Message() {
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)) {
            byteOrder = ByteOrder.BIG_ENDIAN;
        } else {
            byteOrder = ByteOrder.LITTLE_ENDIAN;
        }

        options = 0;
    }

    public Message(byte[] message) {
        decodeLoraPayload(message);
    }

    public Message(String nom , LatLng coord) {
        this.nom=nom;
        this.coord=coord;
        options = 0;
    }


    private byte checksum1(byte[] payload) {
        byte res = 0;
        for (int i = 0; i < 43; i++) {
            res += payload[i];
        }


        return res;
    }

    private byte checksum2(byte[] payload) {
        byte res = 0;
        for (int i = 0; i < 43; i++) {
            res += i * payload[i];
        }

        return res;
    }

    private byte checksum3(byte[] payload) {
        byte res = 0;
        for (int i = 0; i < 43; i++) {
            res += (43 - i) * payload[i];
        }

        return res;
    }


    private boolean verifyChecksums(byte[] payloadReceived) {
        byte check1 = checksum1(payloadReceived);
        byte check2 = checksum2(payloadReceived);
        byte check3 = checksum3(payloadReceived);

        boolean res = check1 == payloadReceived[43];
        res = res && (check2 == payloadReceived[44]);
        res = res && (check3 == payloadReceived[45]);

        return res;
    }


    public boolean checked() {
        return checked;
    }

    public void setSOS() {
        options = (byte) (options | 0x1);
    }

    public void setBlueTooth_HeartRate() {
        options = (byte) (options | 0x2);
    }

    @SuppressWarnings("deprecation")
    public void setDate(Date date) {
        this.hour = (byte) date.getHours();
        this.min = (byte) date.getMinutes();
        this.sec = (byte) date.getSeconds();
    }

    public Date getDate() {
        Date date = new Date();
        date.setHours(hour);
        date.setMinutes(min);
        date.setSeconds(sec);
        return date;
    }

    public short getSeq() {
        return seq;
    }

    public boolean isHeartRate() {
        if ((options & 0x2) == 2)
            return true;
        else return false;
    }


    public boolean isSOS() {
        if ((options & 0x1) == 1)
            return true;
        else return false;
    }

    public short getHeartRate() {
        // TODO Auto-generated method stub
        return heartRate;
    }


    public void setHeartRate(short heartRate) {
        // TODO Auto-generated method stub
        this.heartRate = heartRate;
    }


    public LatLng getPos() {
        // TODO Auto-generated method stub
        return coord;
    }

    private void setNameLength(int l) {
        options = (byte) (options | (l << 2));
    }

    public byte getOptions() {
        // TODO Auto-generated method stub
        return options;
    }


    public byte[] loraPayload() {

        if (loraPayload==null){makeLoraPayload();}
        return loraPayload;
    }


    public void setOptions(Byte byte1) {
        options = byte1;
    }


    public void setSeq(short s) {
        // TODO Auto-generated method stub
        seq = s;
    }


    public Message(Personne p) {
        this.nom = p.getNom();
        this.coord = p.getPosition();
        setDate(new Date());
        this.options = 0;

        setNameLength(this.nom.length());
    }


    public Message(String s){
//        String[] tab=s.split(":");
        nom = s;
        options = 0;
        //       coord= new Point(Integer.valueOf(tab[1]),Integer.valueOf(tab[2]));
    }


    private int getNameLength() {
        return (loraPayload[0]>>2);
    }

    public void setNom (String nom){
        this.nom=nom;
    }

    public void setPos(LatLng pos) {
        this.coord = pos;
    }

    public String getNom (){
        return nom;
    }

    @Override
    public String toString (){
        return "Nom :" + nom + "\nPos" + coord.toString() + "\nheart rate" + heartRate + "\nsos" + isSOS() + "\nbluetooth->heartRate" + isHeartRate() + "\nseq : " + getSeq() + "\ntime :" + getDate()+"\nchecked: "+checked();
    }

    public void makeLoraPayload() {
        loraPayload = new byte[51];
        loraPayload[0] = options;

        byte[] idBytes = nom.getBytes();

        for (int i = 0; i < idBytes.length; i++) {
            loraPayload[1 + i] = idBytes[i];
        }

        byte[] posBytes = getBytesFromLatLng();
        for (int i = 0; i < 16; i++) {
            loraPayload[i + 21] = posBytes[i];
        }

        byte heartRateByte;
        byte[] hhmmss = new byte[3];
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            heartRateByte = (byte) (heartRate >> 8);
            hhmmss[0] = (byte) (hour >> 8);
            hhmmss[1] = (byte) (min >> 8);
            hhmmss[2] = (byte) (sec >> 8);
        } else {
            heartRateByte = (byte) (heartRate);

            hhmmss[0] = (byte) (hour);
            hhmmss[1] = (byte) (min);
            hhmmss[2] = (byte) (sec);
        }

        loraPayload[37] = heartRateByte;

        byte[] seqByte = new byte[2];

        seqByte[0] = (byte) (seq);
        seqByte[1] = (byte) ((seq >> 8) & 0xff);

        loraPayload[38] = seqByte[0];
        loraPayload[39] = seqByte[1];


        //We define the time
        for (int i = 0; i < 3; i++) {
            loraPayload[i + 40] = hhmmss[i];
        }


        byte[] checksums = {checksum1(loraPayload), checksum2(loraPayload), checksum3(loraPayload)};

        for (int i = 0; i < 3; i++) {
            loraPayload[i + 43] = checksums[i];
        }

    }

    private void decodeLoraPayload(byte[] bytesReceived) {
        loraPayload = bytesReceived;

        options = bytesReceived[0];


        int nameLength = getNameLength();

        byte[] idBytes = new byte[nameLength];

        for (int i = 0; i < nameLength; i++) {
            idBytes[i] = bytesReceived[i + 1];
        }
        nom = new String(idBytes);


        coord = getLatLngFromBytes(bytesReceived, 21);


        byte heartRateByte = bytesReceived[37];
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            heartRate = (short) ((heartRateByte & 0xFF));
            hour = (short) ((bytesReceived[40] & 0xFF));
            min = (short) ((bytesReceived[41] & 0xFF));
            sec = (short) ((bytesReceived[42] & 0xFF));
        } else {
            heartRate = (short) ((heartRateByte) + (0x00));
            hour = (short) ((bytesReceived[40] + 0x00));
            min = (short) ((bytesReceived[41] + 0x00));
            sec = (short) ((bytesReceived[42] + 0x00));
        }

        if (heartRate < 0)
            heartRate += 256;

        byte[] seqBytes = {bytesReceived[38], bytesReceived[39]};


        seq = (short) ((seqBytes[1] << 8) + (seqBytes[0] & 0xFF));

        checked = verifyChecksums(bytesReceived);

    }


    /**
     * makes coordinates from byte array
     *
     * @param payload
     * @param index_data index of the first byte representing the coordinates
     */
    public LatLng getLatLngFromBytes(byte[] payload, int index_data) {
        byte[] xBytes = new byte[8];
        byte[] yBytes = new byte[8];

        for (int i = 0; i < 8; i++) {
            xBytes[i] = payload[i + index_data];
            yBytes[i] = payload[i + index_data + 8];
        }

        double x = bytesToDouble(xBytes);
        double y = bytesToDouble(yBytes);
        return new LatLng(x, y);
    }


    public byte[] toByteArray(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    public double bytesToDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    public byte[] getBytesFromLatLng() {
        byte[] res = new byte[16];
        byte[] xBytes = toByteArray(coord.latitude);
        byte[] yBytes = toByteArray(coord.longitude);

        for (int i = 0; i < 8; i++) {
            res[i] = xBytes[i];
            res[i + 8] = yBytes[i];
        }

        return res;
    }
}