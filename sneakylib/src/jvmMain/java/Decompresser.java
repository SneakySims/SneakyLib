// This is a Java port of FreeSO's "Decompresser" class
// Ported by ChatGPT (which, surprisingly, ported it correctly?!)
// https://github.com/riperiperi/FreeSO/blob/master/Other/tools/SimsLib/SimsLib/FAR3/Decompresser.cs
// This is here as a reference because this is a working decompressor
class Decompresser {
    private long compressedSize = 0;
    private long decompressedSize = 0;
    private boolean compressed = false;

    public long getDecompressedSize() {
        return decompressedSize;
    }

    public void setDecompressedSize(long decompressedSize) {
        System.out.println("Setting setDecompressedSize to " + decompressedSize);
        this.decompressedSize = decompressedSize;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public void setCompressedSize(long compressedSize) {
        System.out.println("Setting setCompressedSize to " + compressedSize);
        this.compressedSize = compressedSize;
    }

    private void arrayCopy2(byte[] src, int srcPos, byte[] dest, int destPos, long length) {
        if (dest.length < destPos + length) {
            byte[] destExt = new byte[(int) (destPos + length)];
            System.arraycopy(dest, 0, destExt, 0, dest.length);
            dest = destExt;
        }
        for (int i = 0; i < length; i++) {
            dest[destPos + i] = src[srcPos + i];
        }
    }

    private void offsetCopy(byte[] array, int srcPos, int destPos, long length) {
        srcPos = destPos - srcPos;
        if (array.length < destPos + length) {
            byte[] newArray = new byte[(int) (destPos + length)];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
        for (int i = 0; i < length; i++) {
            array[destPos + i] = array[srcPos + i];
        }
    }

    public byte[] decompress(byte[] data) {
        compressed = false;
        if (data.length > 6) {
            byte[] decompressedData = new byte[(int) decompressedSize];
            System.out.println("decompressedData: " + decompressedSize);
            int dataPos = 0;
            compressed = true;
            int pos = 0;
            long control1 = 0;

            while (control1 != 0xFC && pos < data.length) {
                control1 = data[pos++] & 0xFF;
                if (pos == data.length) break;

                if (control1 <= 127) {
                    long control2 = data[pos++] & 0xFF;
                    long numPlainText = control1 & 0x03;
                    arrayCopy2(data, pos, decompressedData, dataPos, numPlainText);
                    dataPos += numPlainText;
                    pos += numPlainText;
                    if (dataPos == decompressedData.length) break;
                    long offset = (((control1 & 0x60) << 3) + (int) control2 + 1);
                    long numToCopy = ((control1 & 0x1C) >> 2) + 3;
                    offsetCopy(decompressedData, (int) offset, dataPos, numToCopy);
                    dataPos += numToCopy;
                    if (dataPos == decompressedData.length) break;
                } else if (control1 <= 191) {
                    long control2 = data[pos++] & 0xFF;
                    long control3 = data[pos++] & 0xFF;
                    long numPlainText = (control2 >> 6) & 0x03;
                    arrayCopy2(data, pos, decompressedData, dataPos, numPlainText);
                    dataPos += numPlainText;
                    pos += numPlainText;
                    if (dataPos == decompressedData.length) break;
                    int offset = (((int) control2 & 0x3F) << 8) + (int) control3 + 1;
                    long numToCopy = (control1 & 0x3F) + 4;
                    offsetCopy(decompressedData, offset, dataPos, numToCopy);
                    dataPos += numToCopy;
                    if (dataPos == decompressedData.length) break;
                } else if (control1 <= 223) {
                    long numPlainText = control1 & 0x03;
                    long control2 = data[pos++] & 0xFF;
                    long control3 = data[pos++] & 0xFF;
                    long control4 = data[pos++] & 0xFF;
                    arrayCopy2(data, pos, decompressedData, dataPos, numPlainText);
                    dataPos += numPlainText;
                    pos += numPlainText;
                    if (dataPos == decompressedData.length) break;
                    long offset = (((control1 & 0x10) << 12) + ((int) control2 << 8) + (int) control3 + 1);
                    long numToCopy = ((control1 & 0x0C) << 6) + control4 + 5;
                    offsetCopy(decompressedData, (int) offset, dataPos, numToCopy);
                    dataPos += numToCopy;
                    if (dataPos == decompressedData.length) break;
                } else if (control1 <= 251) {
                    long numPlainText = ((control1 & 0x1F) << 2) + 4;
                    arrayCopy2(data, pos, decompressedData, dataPos, numPlainText);
                    dataPos += numPlainText;
                    pos += numPlainText;
                    if (dataPos == decompressedData.length) break;
                } else {
                    long numPlainText = control1 & 0x03;
                    arrayCopy2(data, pos, decompressedData, dataPos, numPlainText);
                    dataPos += numPlainText;
                    pos += numPlainText;
                    if (dataPos == decompressedData.length) break;
                }
            }
            return decompressedData;
        }
        System.out.println("Returning as is");
        return data;
    }
}