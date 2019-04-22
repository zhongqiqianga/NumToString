import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChineseToNum {
    /*

     *基本数字单位;

     */
    private static final String[] units = {"千", "百", "十", ""};//个位

    /*

     *大数字单位;

     */
    private static final String[] bigUnits = {"万", "亿"};
    //所有单位
    private static final String[] allUnits = {"", "十", "百", "千", "万", "亿"};
    /*

     *中文数字;

     */
    //大写
    private static final char[] numMouneyChars = {'壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
    private static final char[] numChars = {'一', '二', '三', '四', '五', '六', '七', '八', '九'};
    private static char numZero = '零';

    /**
     * 根据传来的内容，选择是否进行句子处理
     *
     * @return
     * @paramsen
     */
    public String preProcess(String str) {
        if (str.indexOf("加") != -1 || str.indexOf("减") != -1 || str.indexOf("乘") != -1 || str.indexOf("除") != -1) {
            str = sentenceSolve(str);

        } else {
            str = (String.valueOf(numberCN2Arab(str)));

        }
        return str;
    }

    /**
     * 将中文数字转换为阿拉伯数字;
     * 想计算30乘以20除以-15
     *
     * @return
     * @paramnumberCN
     */
    public static BigDecimal numberCN2Arab(String numberCN) {
        String temNumberCN = numberCN;
        //如果出现负号，将符号排除除去
        if (numberCN.contains("负")) {
            numberCN = numberCN.substring(1);
        }
//        //先对字符串进行处理，防止出现缩写
        int len = numberCN.length();
        String lastchar = numberCN.substring(len - 1);
        String Units = "十百千万亿";
        if ((!Units.contains(lastchar)) && numberCN.length() > 1 && !numberCN.substring(len - 2, len - 1).equals("零")) {
            int i;
            for (i = 0; i < allUnits.length; i++) {
                if (allUnits[i].equals(numberCN.substring(len - 2, len - 1))) {
                    break;
                }
            }
            numberCN = numberCN + allUnits[i - 1];
        }
        String tempNumberCN = numberCN;
        //异常数据处理;
        if (tempNumberCN == null) {
            return new BigDecimal("0");
        }
        /*
         *nums[0]保存以千单位;nums[1]保存以万单位;nums[2]保存以亿单位;
         */
        String[] nums = new String[bigUnits.length + 1];
        //千位以内,直接处理;
        nums[0] = tempNumberCN;
        /*
         *分割大数字,以千为单位进行运算;
         */
        for (int i = (bigUnits.length - 1); i >= 0; i--) {
            //是否存在大单位(万,亿...);
            int find = tempNumberCN.indexOf(bigUnits[i]);
            if (find != -1) {
                String[] tempStrs = tempNumberCN.split(bigUnits[i]);
                //清空千位内容;
                if (nums[0] != null) {
                    nums[0] = null;
                }
                if (tempStrs[0] != null) {
                    nums[i + 1] = tempStrs[0];
                }
                if (tempStrs.length > 1) {
                    tempNumberCN = tempStrs[1];
                    if (i == 0) {
                        nums[0] = tempStrs[1];
                    }
                } else {
                    tempNumberCN = null;
                    break;
                }
            }
        }
        String tempResultNum = "";
        for (int i = nums.length - 1; i >= 0; i--) {
            if (nums[i] != null) {
                tempResultNum += numberKCN2Arab(nums[i]);

            } else {
                tempResultNum += "0000";
            }

        }
        if (temNumberCN.contains("负")) {

            return new BigDecimal("-" + tempResultNum);
        } else
            return new BigDecimal(tempResultNum);

//        return Integer.parseInt(tempResultNum);


    }


    /**
     * 将一位中文数字转换为一位数字;eg:一返回1;
     *
     * @return
     * @paramonlyCNNumber
     */

    public static int numberCharCN2Arab(char onlyCNNumber) {
        if (numChars[0] == onlyCNNumber || numMouneyChars[0] == onlyCNNumber) {
            return 1;
        } else if (numChars[1] == onlyCNNumber || onlyCNNumber == '两' || numMouneyChars[1] == onlyCNNumber) {//处理中文习惯用法(二,两)
            return 2;
        } else if (numChars[2] == onlyCNNumber || numMouneyChars[2] == onlyCNNumber) {
            return 3;
        } else if (numChars[3] == onlyCNNumber || numMouneyChars[3] == onlyCNNumber) {
            return 4;
        } else if (numChars[4] == onlyCNNumber || numMouneyChars[4] == onlyCNNumber) {
            return 5;
        } else if (numChars[5] == onlyCNNumber || numMouneyChars[5] == onlyCNNumber) {
            return 6;
        } else if (numChars[6] == onlyCNNumber || numMouneyChars[6] == onlyCNNumber) {
            return 7;
        } else if (numChars[7] == onlyCNNumber || numMouneyChars[7] == onlyCNNumber) {
            return 8;
        } else if (numChars[8] == onlyCNNumber || numMouneyChars[8] == onlyCNNumber) {
            return 9;
        }
        return 0;

    }

    /**
     * 将阿拉伯数字转换为中文;
     *
     * @return
     * @paramnum
     */
    public static String toChinese(String string) {
        String[] s1 = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] s2 = {"十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
        String result = "";
        int n = string.length();
        for (int i = 0; i < n; i++) {
            int num = string.charAt(i) - '0';
            if (i != n - 1 && num != 0) {
                result += s1[num] + s2[n - 2 - i];
            } else {
                result += s1[num];
            }
        }
        return result;

    }

    /**
     * 处理千以内中文数字,返回4位数字字符串,位数不够以"0"补齐;
     *
     * @return
     * @paramnumberCN
     */

    private static String numberKCN2Arab(String numberCN) {
        if ("".equals(numberCN)) {
            return "";
        }
        int[] nums = new int[4];
        if (numberCN != null) {
            for (int i = 0; i < units.length; i++) {
                int idx = numberCN.indexOf(units[i]);
                if (idx > 0) {
                    char tempNumChar = numberCN.charAt(idx - 1);
                    int tempNumInt = numberCharCN2Arab(tempNumChar);
                    nums[i] = tempNumInt;
                }
            }
            //处理十位
            char ones = numberCN.charAt(numberCN.length() - 1);
            nums[nums.length - 1] = numberCharCN2Arab(ones);
            //处理个位
            if ((numberCN.length() == 2 || numberCN.length() == 1) && numberCN.charAt(0) == '十') {
                nums[nums.length - 2] = 1;
            }
        }
        //返回结果
        String tempNum = "";
        for (int i = 0; i < nums.length; i++) {
            tempNum += nums[i];
        }
        return (tempNum);
    }

    /**
     * 对含有其他中文的句子进行操作
     *
     * @return
     * @paramsen
     */
    public static String sentenceSolve(String sentence) {
        Pattern patternnum = Pattern.compile("[\\u8d1f\\u4e24\\u4e00\\u4e8c\\u4e09\\u56db\\u4e94\\u516d\\u4e03\\u516b\\u4e5d\\u5341\\u767e\\u5343\\u4e07\\u4ebf]*");
        Matcher resnum = patternnum.matcher(sentence);
        ArrayList arrnum = new ArrayList();
        String strnum;
        String finalstr = "";
        int i;
        //将操作数取出来放到数组中
        while (resnum.find()) {
            // Get the matching num string
            strnum = resnum.group();
            if (!strnum.equals("")) {
                arrnum.add(strnum);
            }
        }
        for (i = 0; i < arrnum.size(); i++) {
            strnum = String.valueOf(numberCN2Arab(arrnum.get(i).toString()));
            if (arrnum.get(i).toString().contains("负")) {
                strnum = "(" + strnum + ")";
            }
            sentence = sentence.replace(arrnum.get(i).toString(), strnum);
        }
        return sentence;
    }

}
