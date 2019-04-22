import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Scanner;

public class DataCom {
    public static void main(String[] args) {
        while (true) {
            System.out.println("input two numbers a and b");
            Scanner in = new Scanner(System.in);
            double a = in.nextDouble();
            double b = in.nextDouble();
            String stra = NumToString(a);
            String strb = NumToString(b);
            int result = stra.compareTo(strb);
            if (result > 0)
                System.out.println("a大于b");
            else if (result == 0)
                System.out.println("a等于b");
            else
                System.out.println("a小于b");
            System.out.println(StringToNum(strb).stripTrailingZeros().toPlainString());
        }

    }

    /**
     * 将数字转换为对应的字符串，先转换为科学计数法，从而截取小数和指数部分并进行逆置和取反，注意数字会溢出
     *
     * @return
     * @parama
     */
    public static String NumToString(double a) {
        DecimalFormat f = new DecimalFormat("0.000000000000E00");
        String stra = f.format(Double.parseDouble(String.valueOf(a)));
        //其中laterstra是两位，prefix是13位
        String Laterstra = FuncSubstring(a, stra).substring(0, 2);
        String prefixa = FuncSubstring(a, stra).substring(2);
        String finalstr;
        if (a >= 1) {
            finalstr = "11" + Laterstra + "E" + prefixa;
        } else if (a >= 0 && a < 1) {
            //小数不要取反，指数要取反
            Laterstra = IndexCover(Laterstra);
            finalstr = "10" + Laterstra + "E" + prefixa;
        } else if (a < 0 && a >= -1) {
            //指数不用取反，小数要进行取反
            prefixa = DecimalCover(prefixa);
            finalstr = "01" + Laterstra + "E" + prefixa;
        } else {
            //对指数进行取反
            Laterstra = IndexCover(Laterstra);
            //对小数进行取反
            prefixa = DecimalCover(prefixa);
            finalstr = "00" + Laterstra + "E" + prefixa;
        }
        System.out.println(finalstr);
        return finalstr;
    }

    /**
     * 此函数对指数部分进行取反
     *
     * @return
     * @paramLaterstra
     */
    public static String IndexCover(String Laterstra) {
        int index = Integer.valueOf(Laterstra);
        int indexlen = String.valueOf(100 - index).length();
        String indexstr[] = {"", "0", "00"};
        Laterstra = indexstr[2 - indexlen] + String.valueOf(100 - index);
        return Laterstra;
    }

    /**
     * 此函数对小数部分进行取反
     *
     * @return
     * @paramprefixa
     */
    public static String DecimalCover(String prefixa) {
        String decstr[] = {"", "0", "00", "000", "0000", "00000", "000000", "0000000", "00000000", "000000000", "0000000000", "00000000000", "000000000000"};
        long decimal = Long.valueOf(prefixa);
        long coverage = Long.valueOf("10000000000000") - decimal;
        int decimallen = String.valueOf(coverage).length();
        prefixa = decstr[13 - decimallen] + String.valueOf(coverage);
        return prefixa;
    }
    /**
     * 此函数截取小数部分和指数部分
     *
     * @return
     * @paramstra
     */
    public static String FuncSubstring(double a, String stra) {
        String Laterstra;
        String prefixa;
        if (a >= 1) {
            Laterstra = stra.substring(15);
            prefixa = stra.charAt(0) + stra.substring(2, 14);
        } else if (a >= 0 && a < 1) {
            Laterstra = stra.substring(16);
            prefixa = stra.charAt(0) + stra.substring(2, 14);
        } else if (a < 0 && a >= -1) {
            Laterstra = stra.substring(17);
            prefixa = stra.charAt(1) + stra.substring(3, 15);
        } else {
            Laterstra = stra.substring(16);
            prefixa = stra.charAt(1) + stra.substring(3, 15);
        }
        return Laterstra + prefixa;
    }

    /**
     * 此函数将字符串转换为数字
     *
     * @return
     * @paramstr
     */
    public static BigDecimal StringToNum(String str) {
        String pre = str.substring(0, 2);
        String strdec = str.substring(5);
        String strindex = str.substring(2, 4);
        BigDecimal bigDec;
        BigDecimal a = new BigDecimal(strdec);
        BigDecimal b = new BigDecimal(Double.toString(Math.pow(10, 12)));
        BigDecimal c = new BigDecimal(Double.toString(Math.pow(10, Integer.valueOf(strindex))));
        BigDecimal d = new BigDecimal(Double.toString(Math.pow(10, 100 - Integer.valueOf(strindex))));
        BigDecimal e = new BigDecimal(Double.toString((-1) * ((Math.pow(10, 13) - Double.valueOf(strdec)))));
        if (pre.equals("11")) {
            bigDec = a.divide(b).multiply(c);
        } else if (pre.equals("10")) {
            //在将数字转换为字符串的过程中采用指数取反，小数不变，下面这个过程刚好相反即可
            bigDec = a.divide(b).divide(d);
        } else if (pre.equals("01")) {
            //在将数字转换为字符串的过程中采用指数不变，小数取反，下面这个过程刚好相反即可
            bigDec = e.divide(b).divide(c);
        } else {
            //在将数字转换为字符串的过程中采用指数取反，小数取反，下面这个过程刚好相反即可
            bigDec = e.divide(b).multiply(d);
        }
        return bigDec;
    }
}
