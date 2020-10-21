package com.example.setup;

import tinyb.*;

import java.io.BufferedWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Based on the Intel example that was provided
 * <p>
 * @see <a href="https://github.com/intel-iot-devkit/tinyb/tree/master/examples/java">Intel IoT Devkit Java Examples</a>
 */

public class HelloTinyB<toReturn> {
    static boolean running = true;

    static BluetoothDevice getDevice(String address) throws InterruptedException {
        BluetoothManager manager = BluetoothManager.getBluetoothManager();
        BluetoothDevice sensor = null;
        for (int i = 0; (i < 15) && running; ++i) {
            List<BluetoothDevice> list = manager.getDevices();
            if (list == null)
                return null;

            for (BluetoothDevice device : list) {
                System.out.print(device.getName());
                System.out.print(device.getConnected());
                if (device.getAddress().equals(address))
                    sensor = device;
            }

            if (sensor != null) {
                return sensor;
            }
            Thread.sleep(4000);
        }
        return null;
    }

    static BluetoothGattService getService(BluetoothDevice device, String UUID) throws InterruptedException {
        BluetoothGattService tempService = null;
        List<BluetoothGattService> bluetoothServices = null;
        do
        {
            bluetoothServices = device.getServices();
            if (bluetoothServices == null)
                return null;

            for (BluetoothGattService service : bluetoothServices) {
                if (service.getUUID().equals(UUID))
                    tempService = service;
            }
            Thread.sleep(4000);
        } while (bluetoothServices.isEmpty() && running);
        return tempService;
    }

    static BluetoothGattCharacteristic getCharacteristic(BluetoothGattService service, String UUID) {
        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
        if (characteristics == null)
            return null;

        for (BluetoothGattCharacteristic characteristic : characteristics) {
            if (characteristic.getUUID().equals(UUID))
                return characteristic;
        }
        return null;
    }

    /**
     * This lists are there if there is more than 1 Cube. They couldn't be tested that well but should work,
     */
    private static ArrayList<String> cubenameList = new ArrayList<String>();
    private static ArrayList<String> connectionProblemList = new ArrayList<String>();

    /**
     * Sends the Warning that we get from the cube to the RestService.
     * @param id The MAC address of the cube.
     * @param warning The Warning message. It is "Battery" or "Connection"
     */
    private static void writeWarning(String id, String warning, String url){
        String mac = id.replace(':','-');
        Thread t = new Thread(new RestClient(mac, warning,url));
        t.start();
    }

    /**
     * Sends the data that we get to the RestService.
     * @param id The MAC Address of the cube.
     * @param side The side which was on the top
     * @param start The starting time when the side on the top-
     * @param end The end time.when we switches sides.
     */
    private static void writeMessage(String id, int side, String start, String end, String url){
        String mac = id.replace(':','-');
        String content =  (side + "," + start + "," + end);
        Thread t = new Thread(new RestClient(mac, content, url));
        t.start();
    }

    /**
     * This is our main application. Here we iterate through all the cubes and when the day is over we send the data that was accumulated to the main server.
     *
     * @param args You must write the URL of the Server so that the RestService can send it.
     * @throws InterruptedException It could throw this exception
     */
    public static void main(String[] args) throws InterruptedException {

        if(args.length != 1){
            System.out.println("Please instert a IP address");
            System.exit(1);
        }
        String url = args[0];

        cubenameList.add("TimeFlip1");

        /**
         * Here we change the interval when our programm is iterating through every cube.
         * For testing purposes you can change this or the time of the pi.
         */
        Calendar sendTime = Calendar.getInstance();
        sendTime.set(Calendar.HOUR_OF_DAY, 23);
        sendTime.set(Calendar.MINUTE, 58);
        sendTime.set(Calendar.SECOND, 50);
        Calendar sendTimeStop = Calendar.getInstance();
        sendTimeStop.set(Calendar.HOUR_OF_DAY, 23);
        sendTimeStop.set(Calendar.MINUTE, 59);
        sendTimeStop.set(Calendar.SECOND, 59);




        while (true) {
            Calendar cal = Calendar.getInstance();

            if(cal.getTime().after(sendTime.getTime()) && cal.getTime().before(sendTimeStop.getTime())){

                for(String cubeName : cubenameList) {
                    BluetoothManager manager = BluetoothManager.getBluetoothManager();

                    /**
                     * Find the Cube. If you dont find it, send a Warning.
                     */
                    try {
                        manager.startDiscovery();
                    }catch (BluetoothException e) {
                        System.err.println("Warning: Connection"+cubeName);
                        connectionProblemList.add(cubeName);
                        writeWarning(cubeName, "Connection",url);

                        continue;
                    }

                    List<BluetoothDevice> sensors = new ArrayList<BluetoothDevice>();
                    sensors = manager.getDevices();

                    BluetoothDevice sensor = null;
                    for (BluetoothDevice blue : sensors) {
                        System.out.println(blue.getName() + " " + cubeName);
                        if (blue.getName().equals(cubeName) || blue.getName().equals("TimeFlip")) {
                            System.out.println("Connected to Timeflip : " + blue.getName());
                            sensor = blue;
                        }
                    }

                    try {
                        manager.stopDiscovery();
                    } catch (BluetoothException e) {

                    }

                    /**
                     * If you cant connect send also a warning.
                     */
                    if (sensor == null || !sensor.connect()) {
                        System.err.println("Warning: Connection"+cubeName + "" + sensor.getAddress());
                        connectionProblemList.add(cubeName);

                        writeWarning(sensor.getAddress(), "Connection",url);

                        continue;
                    }


                    Lock lock = new ReentrantLock();
                    Condition cv = lock.newCondition();

                    Runtime.getRuntime().addShutdownHook(new Thread() {
                        public void run() {
                            running = false;
                            lock.lock();
                            try {
                                cv.signalAll();
                            } finally {
                                lock.unlock();
                            }

                        }
                    });


                    BluetoothGattService batteryService = getService(sensor, "0000180f-0000-1000-8000-00805f9b34fb");

                    /**
                     * Check the battery. If it is under 20 percent it will send a Warning.
                     */
                    for (BluetoothGattCharacteristic ch : batteryService.getCharacteristics()) {
                        byte[] batteryRaw = ch.readValue();
                        String tempbattery = Arrays.toString(batteryRaw);
                        tempbattery = tempbattery.substring(1, 2);
                        int battery = Integer.parseInt(tempbattery);
                        if (battery <= 20) {
                            System.out.println("Warning: Battery");

                            writeWarning(sensor.getAddress(), "Battery",url);

                        }
                    }
                    BluetoothGattService sideService = getService(sensor, "f1196f50-71a4-11e6-bdf4-0800200c9a66");

                    /**
                     * Write the password to the cube. The password are six 0s.
                     */
                    for (BluetoothGattCharacteristic ch : sideService.getCharacteristics()) {
                        if (ch.getUUID().equals("f1196f57-71a4-11e6-bdf4-0800200c9a66")) {
                            byte[] conf = {0x30, 0x30, 0x30, 0x30, 0x30, 0x30};
                            ch.writeValue(conf);
                        }
                    }
                    Thread.sleep(100);

                    /**
                     * Write the request for the History of the cube.
                     */
                    for (BluetoothGattCharacteristic ch : sideService.getCharacteristics()) {
                        if (ch.getUUID().equals("f1196f54-71a4-11e6-bdf4-0800200c9a66")) {
                            byte[] conf = {0x01};
                            ch.writeValue(conf);
                        }
                    }
                    Thread.sleep(100);
                    boolean loop = true;
                    /**
                     * Get all the History as long as you dont get all zeros.
                     */
                    while (loop) {
                        for (BluetoothGattCharacteristic ch : sideService.getCharacteristics()) {
                            if (ch.getUUID().equals("f1196f53-71a4-11e6-bdf4-0800200c9a66")) {
                                byte[] historyLine = ch.readValue();
                                System.out.print("\nData: \n");
                                String end = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ";
                                String checkend = "";
                                String output = "";
                                for (byte b : historyLine) {
                                    output += String.format("%x ", Byte.toUnsignedInt(b));

                                    checkend = output;
                                }
                                System.out.print(output);
                                int x = 0;
                                int y = 0;
                                String c1 = "", c2 = "", c3 = "";
                                for (char c : output.toCharArray()) {
                                    switch (y) {
                                        case 0:
                                            c1 += c;
                                            break;
                                        case 1:
                                            c2 += c;
                                            break;
                                        case 2:
                                            c3 += c;
                                            break;
                                    }
                                    if (c == ' ') {
                                        y++;
                                        if (y == 3) {
                                            if (c1.length() <= 2) {
                                                c1 = "0" + c1;
                                            }
                                            if (c2.length() <= 2) {
                                                c2 = "0" + c2;
                                            }
                                            if (c3.length() <= 2) {
                                                c3 = "0" + c3;
                                            }
                                            if (!(c2.equals("00 ") && c3.equals("00 "))) {
                                                //addToConvert(c1, c2, c3, id);
                                                if (c1.contains(" ")) {
                                                    c1 = c1.substring(0,c1.indexOf(" "));
                                                }
                                                if (c2.contains(" ")) {
                                                    c2 = c2.substring(0,c2.indexOf(" "));
                                                }
                                                if (c3.contains(" ")) {
                                                    c3 = c3.substring(0,c3.indexOf(" "));
                                                }
                                                /**
                                                 * Because we only get Hex we need to convert it into a Format which we can use. We also send it to the RestService after we are done.
                                                 */
                                                HextoInt(c1,c2,c3,sensor.getAddress(),cubeName, url);
                                            }
                                            y = 0;
                                            c1 = "";
                                            c2 = "";
                                            c3 = "";
                                        }
                                    }
                                }

                                if (end.equals(checkend)) {
                                    loop = false;
                                }
                                System.out.println("");
                            } else {
                                continue;
                            }
                        }
                        Thread.sleep(100);
                    }
                    /**
                     * After we are done we are deleting the history.
                     */
                    for (BluetoothGattCharacteristic ch : sideService.getCharacteristics()) {
                        if (ch.getUUID().equals("f1196f54-71a4-11e6-bdf4-0800200c9a66")) {
                            byte[] conf = {0x02};
                            ch.writeValue(conf);
                        }
                    }
                    sensor.disconnect();
                }
                lastDate = null;
                Thread.sleep(1000);
            }
            Thread.sleep(600000);

        }
    }

    private static byte[] bits = {0x08,0x04,0x02,0x01};

    /**
     * Couldn't found a Converter which converts Hex to an integer. So i wrote one myself.
     * @param toConvert the Hex number we want to conver.
     * @return the integer number.
     */
    public static int HexConverter(char toConvert){
        switch(toConvert){
            case '0' : return 0;
            case '1' : return 1;
            case '2' : return 2;
            case '3' : return 3;
            case '4' : return 4;
            case '5' : return 5;
            case '6' : return 6;
            case '7' : return 7;
            case '8' : return 8;
            case '9' : return 9;
            case 'A' :
            case 'a' : return 10;
            case 'B' :
            case 'b' : return 11;
            case 'C' :
            case 'c' : return 12;
            case 'D' :
            case 'd' : return 13;
            case 'E' :
            case 'e' : return 14;
            case 'F' :
            case 'f' : return 15;
            default : return 0;
        }
    }


    private static Calendar lastDate = null;

    /**
     * Main Function which splits our input to a side and a time variable.
     * @param inp1 The first 2 Hex numbers
     * @param inp2 The second 2 Hex numbers
     * @param inp3 The third 2 Hex numbners
     * @param mac The MAC address of the Cube.
     * @param id The id we get from the Cube.
     */
    public static void HextoInt(String inp1, String inp2, String inp3, String mac, String id, String url){

        String first = CombineTwo(inp1);
        String second = CombineTwo(inp2);
        String third = CombineTwo(inp3);

        int time = ConvertTime(first,second,third);
        int side = ConvertSide(third);

        if(lastDate == null){
            lastDate = Calendar.getInstance();
            lastDate.set(Calendar.SECOND,0 );
            lastDate.set(Calendar.HOUR_OF_DAY, 0);
            lastDate.set(Calendar.MINUTE,0 );
            for(String ids : connectionProblemList){
                if(id.equals(ids)){
                    lastDate.set(Calendar.DAY_OF_MONTH, -1);
                }
            }
        }
            String startString = ""+lastDate.getTime();
            lastDate.add(Calendar.SECOND, time);
            String endString = "" + lastDate.getTime();
            writeMessage(mac,side,startString,endString,url);



    }

    /**
     * Converts the Bits to an actual side.
     * @param third The bits in an String
     * @return an Integer which represents the side which was on the top.
     */
    private static int ConvertSide(String third) {
        String temp = "";
        for(int i = 0; i < 6; i++){
            temp += third.charAt(i);
        }
        return Integer.parseInt(temp, 2);
    }

    /**
     * Take the 3 Hexduples and converts them to the actual time the cube was on one side.
     * @param first The first Hex Dupel
     * @param second The second Hex Dupel
     * @param third The third Hex Dupel
     * @return An integer which says how many seconds the cube was on one side.
     */
    private static int ConvertTime(String first, String second, String third) {
        String temp = "";
        for(int i = 0; i < 2; i++){
            temp += third.charAt(i);
        }
        temp += second;
        temp += first;
        return Integer.parseInt(temp, 2);
    }

    /**
     * Converts An String with Hex Numbers to an String with Bit numbers,
     * @param toConvert The String with Hex numbers.
     * @return The String with bit numbers.
     */
    private static String CombineTwo(String toConvert) {
        String toReturn = "";
        for(char c : toConvert.toCharArray()){
            int temp = HexConverter(c);
            for(byte offset : bits) {
                if ((temp & offset) == offset) {
                    toReturn += "1";
                } else {
                    toReturn += "0";
                }
            }
        }
        return toReturn;
    }
}

