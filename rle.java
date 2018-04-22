import java.util.ArrayList;
import java.util.List;

public class rle {

    private static byte[] toArrayFromList(List<Byte> list) {
        byte[] bytes = new byte[list.size()]; // создаем экземпляр класса и возвращаем ссылку на вновь созданный объект
        for (int i = 0; i < list.size(); i++) {
            bytes[i] = list.get(i);
        }
        return bytes;
    }

    public static byte[] compressed(byte[] buffer) {
        return compressedRLE(buffer);
    }

    public static byte[] deCompressed(byte[] buffer) {
        return deCompressedRLE(buffer);
    }

    private static byte[] compressedRLE(byte[] buffer) {
        List<Byte> compressed = new ArrayList<>();
        List<Byte> unequalSequence = new ArrayList<>();
        int countEqualValues = 1;
        int countUnequalValues = 1;
        int a; // испольуется для изменения старшего бита
        for (int i = 0; i < buffer.length - 1; i++) {
            if (i < buffer.length - 1 && buffer[i] == buffer[i + 1]) {
                countEqualValues++;
                if (countUnequalValues > 1) {
                    countUnequalValues--;
                    a = 0 << 7;
                    a += countUnequalValues;
                    a--;
                    compressed.add((byte)a);
                    compressed.addAll(unequalSequence);
                }
                countUnequalValues = 0;
                if (countEqualValues == 129) {
                    a = 1 << 7;
                    a += countEqualValues;
                    a -= 2;
                    compressed.add((byte)a);
                    compressed.add(buffer[i]);
                    countEqualValues = 1;
                    countUnequalValues = 1;
                }
                unequalSequence.clear();
            } else if (i < buffer.length - 1 && buffer[i] != buffer[i + 1]) {
                countUnequalValues++;
                if (countUnequalValues > 1) {
                    unequalSequence.add(buffer[i]);
                }
                if (countUnequalValues == 128) {
                    a = 0 << 7;
                    a += countUnequalValues;
                    a--;
                    compressed.add((byte)a);
                    compressed.addAll(unequalSequence);
                    unequalSequence.clear();
                    countUnequalValues = 1;
                }
                if (countEqualValues != 1) {
                    a = 1 << 7;
                    a += countEqualValues;
                    a -= 2;
                    compressed.add((byte)a);
                    compressed.add(buffer[i]);
                }
                countEqualValues = 1;
            }
        }
        if (countEqualValues != 1) {
            a = 1 << 7;
            a += countEqualValues;
            a -= 2;
            compressed.add((byte)a);
            compressed.add(buffer[buffer.length-1]);
        }
        if (countUnequalValues > 0) {
            a = 0 << 7;
            a += countUnequalValues;
            a--;
            compressed.add((byte)a);
            compressed.addAll(unequalSequence);
            compressed.add(buffer[buffer.length-1]);
        }
        return toArrayFromList(compressed);
    }

    private static byte[] deCompressedRLE(byte[] buffer) {
        List<Byte> decompressed = new ArrayList<>();
        byte lenght_of_sequence;
        for(int i = 0; i < buffer.length - 1; i++) {
            lenght_of_sequence = buffer[i];
            if(lenght_of_sequence < 0 ) {
                lenght_of_sequence &= 0x7F;
                lenght_of_sequence += (byte) 2;
                while (lenght_of_sequence > 0) {
                    decompressed.add(buffer[i+1]);
                    lenght_of_sequence --;
                }
            i++;
            }
            else{
                lenght_of_sequence++;
                while(lenght_of_sequence > 0) {
                    decompressed.add(buffer[i+1]);
                    i++;
                    lenght_of_sequence--;
                }
            }
        }
        return toArrayFromList(decompressed);
    }
}