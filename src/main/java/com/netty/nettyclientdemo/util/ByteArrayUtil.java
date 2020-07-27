package com.netty.nettyclientdemo.util;


import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

public class ByteArrayUtil {
    /**
     * 将字节数组转换为指定长度的数组,不足补零,长的裁剪
     *
     * @param srcArray
     * @param destLength
     * @return
     */
    public static byte[] transform(byte[] srcArray, int destLength) {
        byte[] _temp = new byte[destLength];
        if (srcArray.length < destLength) {
            System.arraycopy(srcArray, 0, _temp, 0, srcArray.length);
        } else {
            System.arraycopy(srcArray, 0, _temp, 0, _temp.length);
        }
        return _temp;
    }

    /**
     * 将字符串转换为指定长度的数组,不足补零,长的裁剪.
     *
     * @param srcString  源字符串
     * @param destLength 输出数组长度
     * @param charset    字符编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] transform(String srcString, int destLength, String charset) throws UnsupportedEncodingException {
        byte[] _temp = srcString.getBytes(charset);
        return transform(_temp, destLength);
    }

    /**
     * 将数组中的每个字节转换为十进制的有符号数,并组合为一字符串
     *
     * @param data
     * @return
     */
    public static String toDecimalString(byte[] data) {
        if (data == null) {
            return "";
        }
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            buff.append(data[i] & 0xFF);
            buff.append(",");
        }
        return buff.toString().trim();
    }

    /*
     * 字节数组转16进制字符串
     */
    public static String bytesToHexString(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer(bArr.length);
        String sTmp;

        for (int i = 0; i < bArr.length; i++) {
            sTmp = Integer.toHexString(0xFF & bArr[i]);
            if (sTmp.length() < 2)
                sb.append(0);
            sb.append(sTmp);
        }

        return sb.toString();
    }

    /**
     * 将数组中的每个字节转换为16进制的无符号数,并组合为一字符串
     *
     * @param data
     * @return
     */
    public static String toHexString(byte[] data) {
        if (data == null) {
            return "";
        }
        StringBuffer buff = new StringBuffer(20);
        for (int i = 0; i < data.length; i++) {
            buff.append(Integer.toHexString((data[i] >> 4) & 0x0F));
            buff.append(Integer.toHexString(data[i] & 0x0F));
        }
        return buff.toString().trim();
    }

    /**
     * 字符串位运算取反
     *
     * @param string
     * @return
     */
    public static String stringInvert(String string) {
        try {
            byte[] keyBytes = new byte[16];
            byte[] bytes = string.getBytes();
            byte[] byteArr = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                    (byte) 0xFF};
            for (int i = 0; i < 8; i++) {
                keyBytes[i] = bytes[i];
                keyBytes[i + 8] = (byte) (byteArr[i] ^ bytes[i]);
            }
            return ByteArrayUtil.toHexUpCaseString(keyBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 将数组中的每个字节转换为16进制的无符号数,并组合为一字符串(大写)
     *
     * @param data
     * @return
     */
    public static String toHexUpCaseString(byte[] data) {
        return toHexString(data).toUpperCase();
    }

    /**
     * 将数组中的每个字节转换为16进制的无符号数,并组合为一字符串
     *
     * @param data
     * @param isContain0x
     * @return
     */
    public static String toHexString(byte[] data, boolean isContain0x) {
        if (data == null) {
            return "";
        }
        StringBuffer buff = new StringBuffer(20);
        for (int i = 0; i < data.length; i++) {
            if (isContain0x) {
                buff.append("0x");
            }
            buff.append(Integer.toHexString((data[i] >> 4) & 0x0F));
            buff.append(Integer.toHexString(data[i] & 0x0F));
            if (isContain0x && i < data.length - 1) {
                buff.append(",");
            }
        }
        return buff.toString().trim();
    }

    /**
     * 拼接字节到字节数组中
     *
     * @param paramArrayOfByte 原始字节数组
     * @param paramByte        要拼接的字节
     * @return 拼接后的数组
     */
    public static byte[] MergerArrays(byte[] paramArrayOfByte, byte paramByte) {
        byte[] arrayOfByte = new byte[paramArrayOfByte.length + 1];
        System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
        arrayOfByte[paramArrayOfByte.length] = paramByte;
        return arrayOfByte;
    }

    /**
     * int转字节数组
     *
     * @param paramInt
     * @return
     */
    public static byte[] IntToLBytes(int paramInt) {
        return new byte[]{(byte) (paramInt & 0xFF), (byte) (paramInt >> 8 & 0xFF), (byte) (paramInt >> 16 & 0xFF), (byte) (paramInt >> 24 & 0xFF)};
    }

    /**
     * 两个字节数组拼接
     *
     * @param paramArrayOfByte1 字节数组1
     * @param paramArrayOfByte2 字节数组2
     * @return 拼接后的数组
     */
    public static byte[] MergerArray(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
        byte[] arrayOfByte = new byte[paramArrayOfByte1.length + paramArrayOfByte2.length];
        System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, paramArrayOfByte1.length);
        System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, paramArrayOfByte1.length, paramArrayOfByte2.length);
        return arrayOfByte;
    }

    /**
     * 将数组中的每个字节转换为16进制的无符号数,并组合为一字符串
     *
     * @param data
     * @return
     */
    public static String toHexString(byte data) {
        return Integer.toHexString(data & 0xFF);
    }

    /**
     * 将数组中的每个字节转换为16进制的无符号数,并组合为一字符串
     *
     * @param data
     * @param offset 偏移量
     * @param length 长度
     * @return
     */
    public static String toHexString(byte[] data, int offset, int length) {
        if (data.length < offset || offset < 0) {
            offset = 0;
        }
        if (length > data.length - offset) {
            length = 0;
        }
        byte[] subArray = new byte[length];
        System.arraycopy(data, offset, subArray, 0, length);
        return toHexString(subArray);
    }

    /**
     * 将两个ASCII字符合成一个字节；
     * 如："EF"--> 0xEF
     *
     * @param src0 byte
     * @param src1 byte
     * @return byte
     */
    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    /**
     * 将指定字符串src，以每两个字符分割转换为16进制形式
     * 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
     *
     * @param src String
     * @return byte[]
     */
    public static byte[] hexString2Bytes(String src) {
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /**
     * 将给定的对象数组(每个元素为一个字节数组)
     *
     * @param datas
     * @return
     * @throws Exception
     */
    public static byte[] toSingleByteArray(Object[] datas) {
        if (datas == null) {
            datas = new Object[]{};
        }
        int dataLength = 0;
        //计算数据长度///////////////////
        for (int i = 0; i < datas.length; i++) {
            Object obj = datas[i];
            if (obj == null) {
                return null;
            }
            if (obj instanceof byte[]) {
                byte[] _temp = (byte[]) obj;
                dataLength += _temp.length;
            } else {
                System.out.println("Error: 输入对象数组元素必须为字节数组");
                return new byte[]{};
            }
        }

        //装配数据//////////////////////
        byte[] dataBytes = new byte[dataLength];

        int dstPos = 0;//目标数组的偏移值
        for (Object obj : datas) {
            byte[] _temp = (byte[]) obj;
            System.arraycopy(_temp, 0, dataBytes, dstPos, _temp.length);
            dstPos += _temp.length;
        }
        //////////////////////////////

        return dataBytes;
    }

    /**
     * 给定的链表数据转换为单一的字节数组(每个节点的数据必须为字节数组类型)
     *
     * @param datas
     * @return
     * @throws Exception
     */
    public static byte[] toSingleByteArray(LinkedList<byte[]> datas) {
        if (datas == null) {
            return new byte[]{};
        }
        int dataLength = 0;
        //计算数据长度///////////////////
        for (byte[] obj : datas) {
            if (obj == null) {
                return null;
            }
            if (obj instanceof byte[]) {
                byte[] _temp = (byte[]) obj;
                dataLength += _temp.length;
            } else {
                System.out.println("Error: 输入对象数组元素必须为字节数组");
                return new byte[]{};
            }
        }

        //装配数据//////////////////////
        byte[] dataBytes = new byte[dataLength];

        int dstPos = 0;//目标数组的偏移值
        for (Object obj : datas) {
            byte[] _temp = (byte[]) obj;
            System.arraycopy(_temp, 0, dataBytes, dstPos, _temp.length);
            dstPos += _temp.length;
        }
        //////////////////////////////

        return dataBytes;
    }

    /**
     * char数组转byte数组
     */
    public static byte[] charArrayToByte(char[] inputArray) {
        byte[] temp = new byte[inputArray.length];
        for (int i = 0; i < inputArray.length; i++) {
            temp[i] = (byte) inputArray[i];
        }
        return temp;
    }

    /**
     * 获取字节数组中指定部分的数据
     *
     * @param source
     * @param start    起始索引
     * @param position 偏移量
     * @return
     * @throws Exception
     */
    public static byte[] getSubArray(byte[] source, int start, int position) throws Exception {
        if ((source.length - start + 1) < position) {
            throw new Exception("输入数据不足");
        }
        byte[] dest = new byte[position];
        System.arraycopy(source, start, dest, 0, position);
        return dest;
    }

    /**
     * 采用大字节序列(Big-Endian),即网络字节序列
     *
     * @author ZhangYin
     */
    public static class BigEndian {
        /**
         * 将iSource转为长度为iArrayLen的byte数组，字节数组的低位是整型的高字节位
         *
         * @param iSource   待转换的整型
         * @param iArrayLen 需要转换成的字节数组的长度，多余的字节补0
         */
        public static byte[] toByteArray(int iSource, int iArrayLen) {
            byte[] bLocalArr = new byte[iArrayLen];
            for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
                byte temp = (byte) (iSource >> (8 * i) & 0xFF);
                bLocalArr[iArrayLen - i - 1] = temp;
            }
            return bLocalArr;
        }

        /**
         * 将iSource转为长度为iArrayLen的byte数组，字节数组的低位是整型的高字节位
         *
         * @param iSource   待转换的浮点型
         * @param iArrayLen 需要转换成的字节数组的长度，多余的字节补0
         */
        public static byte[] toByteArray(float iSource, int iArrayLen) {
            int _temp = Float.floatToIntBits(iSource);
            return toByteArray(_temp, iArrayLen);
        }

        /**
         * 将byte数组bRefArr转为一个整数,字节数组的低位是整型的高字节位
         */
        public static int toInt(byte[] bRefArr) {
            int iOutcome = 0;
            byte bLoop;
            int byteCount = bRefArr.length;
            //对大数组,只取前四个字节
            if (byteCount > 4) {
                byteCount = 4;
            }
            int j = 0;
            for (int i = byteCount - 1; i > -1; i--) {
                bLoop = bRefArr[i];
                iOutcome += (bLoop & 0xFF) << (8 * j++);

            }
            return iOutcome;
        }

        /**
         * 将字节转换成整型
         *
         * @param byteIn
         * @return
         */
        public static int toInt(byte byteIn) {
            int outcome = byteIn & 0xFF;
            return outcome;
        }

        /**
         * 将给定的字节数组转换成long类型
         *
         * @param bArray
         * @return
         */
        public static long toLong(byte[] bArray) {
            long iOutconme = 0;
            byte bLoop;
            int byteCount = bArray.length;
            //对于大数组,只取前八个字节
            if (byteCount > 8) {
                byteCount = 8;
            }
            int j = 0;
            for (int i = byteCount - 1; i > -1; i--) {
                bLoop = bArray[i];
                iOutconme += (bLoop & 0xFFL) << (8 * j++);
            }
            return iOutconme;
        }

        /**
         * 将给定的字节数组转换成long类型
         *
         * @param bArray
         * @return
         */
        public static float toFloat(byte[] bArray) {
            int _temp = toInt(bArray);
            return Float.intBitsToFloat(_temp);
        }

        /**
         * 将给定的长整型,转换成指定长度的字节数组
         *
         * @param lSource
         * @param iArrayLength
         * @return
         */
        public static byte[] toByteArray(long lSource, int iArrayLength) {
            byte[] bLocalArr = new byte[iArrayLength];
            for (int i = 0; (i < 8) && (i < iArrayLength); i++) {
                byte temp = (byte) (lSource >> (8 * i) & 0xFF);
                bLocalArr[iArrayLength - i - 1] = temp;
            }
            return bLocalArr;
        }


    }

    ////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////

    /**
     * 采用小字节序列(Little-Endian),即x86采用的字节序列
     *
     * @author ZhangYin
     */
    public static class LittleEndian {
        /**
         * 将iSource转为长度为iArrayLen的byte数组，字节数组的低位是整型的低字节位
         *
         * @param iSource   待转换的整型
         * @param iArrayLen 需要转换成的字节数组的长度，多余的字节补0
         */
        public static byte[] toByteArray(int iSource, int iArrayLen) {
            byte[] bLocalArr = new byte[iArrayLen];
            for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
                byte temp = (byte) (iSource >> (8 * i) & 0xFF);
                bLocalArr[i] = temp;
            }
            return bLocalArr;
        }

        /**
         * 将byte数组bRefArr转为一个整数,字节数组的低位是整型的低字节位
         */
        public static int toInt(byte[] bRefArr) {
            int iOutcome = 0;
            byte bLoop;
            int byteCount = bRefArr.length;
            //对大数组,只取前四个字节
            if (byteCount > 4) {
                byteCount = 4;
            }
            for (int i = 0; i < byteCount; i++) {
                bLoop = bRefArr[i];
                iOutcome += (bLoop & 0xFF) << (8 * i);

            }
            return iOutcome;
        }

        /**
         * 将字节转换成整型
         *
         * @param byteIn
         * @return
         */
        public static int toInt(byte byteIn) {
            int outcome = byteIn;
            return outcome;
        }

        /**
         * 将给定的字节数组转换成long类型
         *
         * @param bArray
         * @return
         */
        public static long toLong(byte[] bArray) {
            long iOutconme = 0;
            byte bLoop;
            int byteCount = bArray.length;
            //对于大数组,只去前八个字节
            if (byteCount > 8) {
                byteCount = 8;
            }
            for (int i = 0; i < byteCount; i++) {
                bLoop = bArray[i];
                iOutconme += (bLoop & 0xFFL) << (8 * i);
            }
            return iOutconme;
        }

        /**
         * 将给定的长整型,转换成指定长度的字节数组
         *
         * @param lSource
         * @param iArrayLength
         * @return
         */
        public static byte[] toByteArray(long lSource, int iArrayLength) {
            byte[] bLocalArr = new byte[iArrayLength];
            for (int i = 0; (i < 8) && (i < iArrayLength); i++) {
                byte temp = (byte) (lSource >> (8 * i) & 0xFF);
                bLocalArr[i] = temp;
            }
            return bLocalArr;
        }

    }

    /**
     * 复制指定的数组，截取或用 0 填充（如有必要），以使副本具有指定的长度。对于在原数组和副本中都有效的所有索引，
     * 这两个数组将包含相同的值。对于在副本中有效而在原数组无效的所有索引，副本将包含 (byte)0。
     * 当且仅当指定长度大于原数组的长度时，这些索引存在。
     */
    public static byte[] copyOf(byte[] original, int newLength) {
        byte[] copy = new byte[newLength];
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    /**
     * 将指定数组的指定范围复制到一个新数组。该范围的初始索引 (from) 必须位于 0 和 original.length（包括）之间。
     * original[from] 处的值放入副本的初始元素中（除非 from == original.length 或 from == to）。
     * 原数组中后续元素的值放入副本的后续元素。该范围的最后索引 (to)（必须大于等于 from）可以大于 original.length，
     * 在这种情况下，(short)0 被放入索引大于等于 original.length - from 的副本的所有元素中。返回数组的长度为 to -
     * from。
     */
    public static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    /**
     * 数据分包
     *
     * @param data         原数据
     * @param packetLength 分包字节大小
     * @return byte[][] 分包后数据数组
     * @throws Exception
     */
    public static byte[][] dataPacketed(byte[] data, int packetLength) throws Exception {
        if (data == null || packetLength <= 0) {
            throw new Exception("调用数据分包出错，参数错误");
        }

        int curson = 0;//游标
        //数据长度
        int dataLength = data.length;
        //分包数量
        int lengthRemain = dataLength % packetLength;
        int updateCount = lengthRemain == 0 ? (dataLength / packetLength) : (dataLength / packetLength) + 1;

        byte[][] result = new byte[updateCount][];
        for (int i = 0; i < updateCount; i++) {
            byte[] updataData = null;

            if (i == (updateCount - 1)) {
                //分包数据长度
                int length = dataLength - packetLength * (updateCount - 1);
                //分包数据
                updataData = new byte[length];
                System.arraycopy(data, curson, updataData, 0, length);
            } else {
                updataData = new byte[packetLength];
                System.arraycopy(data, curson, updataData, 0, packetLength);
                curson += packetLength;
            }

            result[i] = updataData;
        }

        return result;
    }

    public static int toInt(byte[] bRefArr) {
        int iOutcome = 0;
        int byteCount = bRefArr.length;

        if (byteCount > 4) {
            byteCount = 4;
        }
        int j = 0;
        for (int i = byteCount - 1; i > -1; --i) {
            byte bLoop = bRefArr[i];
            iOutcome += ((bLoop & 0xFF) << 8 * j++);
        }

        return iOutcome;
    }

    public static void main(String args[]) {

        byte[] str = ByteArrayUtil.hexString2Bytes("07");

        System.out.println(ByteArrayUtil.toHexString(str, true));
        System.out.println(ByteArrayUtil.toHexString(str, false));

        /**************************************/
//		byte [] byte1=new byte []{0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x09};
//		byte [] byte2=new byte []{0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x09};
//		byte [] byte3=new byte []{0x03,0x03,0x03,0x03,0x03,0x03,0x03,0x03,0x03,0x03,0x09};
//		Object [] datas=new Object[]{byte1,byte2,byte3};
//		System.out.println("字节数组转换为16进制:");
//		System.out.println("单字节：");
//		byte [] single=ByteArrayUtil.toSingleByteArray(datas);
//		System.out.println(ByteArrayUtil.toHexString(single));
//		byte [] testData=new byte []{0x01,0x03,0x05,0x04,0x05,0x05,0x05,0x06,0x05,0x07,0x05,0x08,0x05,0x09,0x05
//				,0x0a,0x05,0x0b,0x05,0x0c,0x05,0x0d,0x05,0x0e,0x05,0x0f,0x05,0x03,0x05,0x03,0x05,0x03,0x05,0x03,0x05};
//
//		System.out.println("---:"+ByteArrayUtil.toHexString(testData));
//		try {
//			byte[][] packetData = ByteArrayUtil.dataPacketed(testData, 6);
//			System.out.print("---:");
//			for(int i=0; i<packetData.length; i++){
//				System.out.print(ByteArrayUtil.toHexString(packetData[i]));
//			}
//
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


//		String hexStr = ByteArrayUtil.toHexString(testData);
//		System.out.println(hexStr);
//		String hexStrSub = ByteArrayUtil.toHexString(testData,-1,testData.length);
//		System.out.println("SubHexStr:\n"+hexStrSub);
//		byte [] bytes = ByteArrayUtil.hexString2Bytes(hexStr);
//		System.out.println(ByteArrayUtil.toHexString(bytes));
//
//		hexStr ="0100";
//		System.out.println(hexStr);
//		bytes = ByteArrayUtil.hexString2Bytes(hexStr);
//		System.out.println(ByteArrayUtil.toHexString(bytes));
//		System.out.println(ByteArrayUtil.BigEndian.toInt(bytes));
//		/**************************************/
//
//		/**************************************/
//		byte [] test="SJY15-E-WT-2008-01".getBytes();
//		byte [] tranTest=ByteArrayUtil.transform(test, 32);
//		System.out.println("Old Length:"+test.length+"    New Length:"+tranTest.length);
//		System.out.println("source:\n"+ByteArrayUtil.toHexString(test));
//		System.out.println("dest:\n"+ByteArrayUtil.toHexString(tranTest));
//		/**************************************/
//
//		BigEndian.testBigEndian();
//		System.out.println();
//		System.out.println();
//		LittleEndian.testLittleEndian();
//		System.out.println(ByteArrayUtil.LittleEndian.toInt(ByteArrayUtil.hexString2Bytes("C8")));
//
//		/****************************************/
//		float floatTmp = 9.2666666666666f;
//		byte [] floatBytes = ByteArrayUtil.BigEndian.toByteArray(floatTmp, 4);
//		float floatTmp2 = ByteArrayUtil.BigEndian.toFloat(floatBytes);
//		System.out.println("9.2666666666666f float bytes:"+ByteArrayUtil.toHexString(floatBytes));
//		System.out.println("bytes to float:"+floatTmp2);

    }
}
