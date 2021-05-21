import java.awt.MouseInfo;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Date;

public class Prng {

    private long[] XOR (long[] a, long[] b) {
        long[] res = new long[a.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = a[i] ^ b[i];
        }
        return res;
    }

    private long[] getIV(){
        long x = 0, y = 0;
        long[] iv = new long[2];
        System.out.println("Move your mouse around the screen");
        for (int i = 0; i < 16; i++) {
            x = MouseInfo.getPointerInfo().getLocation().x;
            y = MouseInfo.getPointerInfo().getLocation().y;
            iv[i/8] = (iv[i/8] << 8) + (x ^ y);
            try {
                Thread.sleep(250);
            } catch (Exception e) { }
        }
        System.out.println("IV created");
        return iv;
    }

    private byte[][] longToByte(long D1, long D2){
        byte[][] bytes = new byte[2][8];
        for (int i = 7; i >=0 ; i--){
            bytes[0][i]= (byte)D1;
            bytes[1][i] = (byte)D2;
            D1 >>>= 8;
            D2 >>>= 8;
        }
        return bytes;
    }

    private long[][] getRandomValueAndNewInput(long[] subkeys, long[] v) {
        Date time = new Date();
        Camellia camellia = new Camellia();
        long[] new_r, temp1, temp2, new_v;
        long date = time.getTime();
        temp1 = camellia.crypt(0, date, subkeys);
        temp2 = XOR(v, temp1);
        new_r = camellia.crypt(temp2[0], temp2[1], subkeys);
        temp2 = XOR(new_r, temp1);
        new_v = camellia.crypt(temp2[0], temp2[1], subkeys);
        return new long[][] {new_r,new_v};
    }

    public void generateRandomNumbers(String path, String key, long length) {
        Camellia camellia = new Camellia();
        long[] subkeys = camellia.keySchedule(key);
        long[] temp1, temp2;
        long[][] output = new long[2][2];
        output[1] = getIV();
        byte[][] r_bytes;
    //    long date, mask = allow_negative ? 0xFFFFFFFFFFFFFFFFL : 0xFFFFFFFFL;
        long date;
        long num = length / 16, leftover = length % 16;
        try {
            BufferedOutputStream writer = new  BufferedOutputStream(new FileOutputStream(path));
            for (int i = 0; i < num; i++) {
                output = getRandomValueAndNewInput(subkeys, output[1]);
                r_bytes = longToByte(output[0][0], output[0][1]);
                writer.write(r_bytes[0]);
                writer.write(r_bytes[1]);
            }
            if (leftover > 0) {
                output = getRandomValueAndNewInput(subkeys, output[1]);
                r_bytes = longToByte(output[0][0], output[0][1]);
                if (leftover <= 8) {
                    writer.write(r_bytes[0], 0, (int)leftover);
                } else {
                    writer.write(r_bytes[0]);
                    writer.write(r_bytes[0], 8, 16-(int)leftover);
                }
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error occurred");
            System.out.println(e);
            return;
        }
    }

    public static void main(String[] args) {
        Prng test = new Prng();
        test.generateRandomNumbers("results.txt", "1234567887654321", 10000000);

    }
}
